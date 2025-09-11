package tech.qiantong.qdata.spark.etl.transition;

import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * <P>
 * 用途:转换组件注册
 * </p>
 *
 * @author: FXB
 * @create: 2025-06-20 09:10
 **/
public class TransitionRegistry {

    private final Map<String, Transition> transitionMap = new HashMap<>();


    public TransitionRegistry() {
        this.transitionMap.put(TaskComponentTypeEnum.SPARK_CLEAN.getCode(), new CleanTransition());
        this.transitionMap.put(TaskComponentTypeEnum.SORT_RECORD.getCode(), new SortTransition());
        this.transitionMap.put(TaskComponentTypeEnum.FIELD_DERIVATION.getCode(), new FieldDerivationTransition());


    }

    public Transition getTransition(String code) {
        return this.transitionMap.get(code);
    }
}
