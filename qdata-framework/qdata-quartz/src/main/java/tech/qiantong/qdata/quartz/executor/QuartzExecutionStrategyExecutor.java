/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.quartz.executor;

import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.data.redis.core.RedisTemplate;
import tech.qiantong.qdata.common.core.redis.RedisCache;
import tech.qiantong.qdata.common.utils.spring.SpringUtils;
import tech.qiantong.qdata.quartz.domain.QuartzJob;
import tech.qiantong.qdata.quartz.domain.SysJob;
import tech.qiantong.qdata.quartz.enums.ScheduleExecutionTypeEnum;
import tech.qiantong.qdata.quartz.util.JobInvokeUtil;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Controls Quartz execution strategies.
 */
public class QuartzExecutionStrategyExecutor {

    private static final long LOCK_TTL_SECONDS = 12 * 60 * 60;
    private static final String LOCK_PREFIX = "qdata:quartz:running:";
    private static final String PENDING_PREFIX = "qdata:quartz:pending:";

    private final LockStore lockStore;

    public QuartzExecutionStrategyExecutor() {
        this(new RedisLockStore());
    }

    QuartzExecutionStrategyExecutor(LockStore lockStore) {
        this.lockStore = lockStore;
    }

    public ExecutionOutcome execute(JobExecutionContext context, SysJob sysJob) throws Exception {
        String executionTypeValue = sysJob instanceof QuartzJob
                ? ((QuartzJob) sysJob).getExecutionType() : null;
        ScheduleExecutionTypeEnum executionType = ScheduleExecutionTypeEnum.resolve(
                executionTypeValue, sysJob.getConcurrent());
        String key = buildKey(context, sysJob);
        return executeWithPolicy(executionType, key, () -> JobInvokeUtil.invokeMethod(sysJob),
                () -> triggerAgain(context));
    }

    ExecutionOutcome executeWithPolicy(ScheduleExecutionTypeEnum executionType, String key,
                                       CheckedRunnable jobInvoker, CheckedRunnable followUpTrigger) throws Exception {
        if (executionType == ScheduleExecutionTypeEnum.PARALLEL || executionType == ScheduleExecutionTypeEnum.SERIAL_WAIT) {
            jobInvoker.run();
            return ExecutionOutcome.EXECUTED;
        }

        String token = UUID.randomUUID().toString();
        if (!lockStore.tryLock(lockKey(key), token, LOCK_TTL_SECONDS)) {
            if (executionType == ScheduleExecutionTypeEnum.SERIAL_PRIORITY) {
                lockStore.markPending(pendingKey(key), LOCK_TTL_SECONDS);
                return ExecutionOutcome.PENDING;
            }
            return ExecutionOutcome.DISCARDED;
        }

        Exception failure = null;
        try {
            jobInvoker.run();
            return ExecutionOutcome.EXECUTED;
        } catch (Exception e) {
            failure = e;
            throw e;
        } finally {
            lockStore.unlock(lockKey(key), token);
            if (executionType == ScheduleExecutionTypeEnum.SERIAL_PRIORITY
                    && lockStore.consumePending(pendingKey(key))) {
                try {
                    followUpTrigger.run();
                } catch (Exception triggerException) {
                    if (failure != null) {
                        failure.addSuppressed(triggerException);
                    } else {
                        throw triggerException;
                    }
                }
            }
        }
    }

    private String buildKey(JobExecutionContext context, SysJob sysJob) {
        if (context != null && context.getJobDetail() != null
                && context.getJobDetail().getKey() != null) {
            return context.getJobDetail().getKey().toString();
        }
        if (sysJob.getJobId() != null) {
            return sysJob.getJobGroup() + ":" + sysJob.getJobId();
        }
        return sysJob.getJobGroup() + ":" + sysJob.getJobName();
    }

    private String lockKey(String key) {
        return LOCK_PREFIX + key;
    }

    private String pendingKey(String key) {
        return PENDING_PREFIX + key;
    }

    private void triggerAgain(JobExecutionContext context) throws SchedulerException {
        context.getScheduler().triggerJob(context.getJobDetail().getKey());
    }

    public enum ExecutionOutcome {
        EXECUTED,
        DISCARDED,
        PENDING
    }

    public interface CheckedRunnable {
        void run() throws Exception;
    }

    interface LockStore {
        boolean tryLock(String key, String token, long ttlSeconds);

        void unlock(String key, String token);

        void markPending(String key, long ttlSeconds);

        boolean consumePending(String key);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static class RedisLockStore implements LockStore {
        @Override
        public boolean tryLock(String key, String token, long ttlSeconds) {
            RedisTemplate redisTemplate = redisTemplate();
            Boolean locked = redisTemplate.opsForValue().setIfAbsent(key, token, ttlSeconds, TimeUnit.SECONDS);
            return Boolean.TRUE.equals(locked);
        }

        @Override
        public void unlock(String key, String token) {
            RedisTemplate redisTemplate = redisTemplate();
            Object currentToken = redisTemplate.opsForValue().get(key);
            if (token.equals(currentToken)) {
                redisTemplate.delete(key);
            }
        }

        @Override
        public void markPending(String key, long ttlSeconds) {
            redisTemplate().opsForValue().set(key, "1", ttlSeconds, TimeUnit.SECONDS);
        }

        @Override
        public boolean consumePending(String key) {
            RedisTemplate redisTemplate = redisTemplate();
            Boolean exists = redisTemplate.hasKey(key);
            if (exists) {
                redisTemplate.delete(key);
                return true;
            }
            return false;
        }

        private RedisTemplate redisTemplate() {
            return SpringUtils.getBean(RedisCache.class).redisTemplate;
        }
    }
}
