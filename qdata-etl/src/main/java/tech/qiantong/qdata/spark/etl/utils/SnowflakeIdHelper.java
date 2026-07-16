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

package tech.qiantong.qdata.spark.etl.utils;

public class SnowflakeIdHelper {



    private final long twepoch = 1420041600000L;

    private final long workerIdBits = 5L;

    private final long datacenterIdBits = 5L;

    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    private final long sequenceBits = 12L;

    private final long workerIdShift = sequenceBits;

    private final long datacenterIdShift = sequenceBits + workerIdBits;

    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    private long workerId = 0L;

    private long datacenterId = 0L;

    private long sequence = 0L;

    private long lastTimestamp = -1L;

    private static SnowflakeIdHelper instance;

    public static SnowflakeIdHelper getInstance(long workerId, long datacenterId){
        if(instance==null){
            instance=new SnowflakeIdHelper(workerId,datacenterId);
        }
        return instance;
    }



    //==============================Constructors=====================================
    public SnowflakeIdHelper(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    // ==============================Methods==========================================
    public synchronized long nextId() {
        long timestamp = timeGen();

        //If the current time is less than the timestamp of the last ID generation, it means that the system clock has rolled back past this time and an exception should be thrown.
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //If they are generated at the same time, perform the sequence within milliseconds.
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //Sequence overflow in milliseconds
            if (sequence == 0) {
                //Block until the next millisecond and get a new timestamp
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //Timestamp changes, sequence resets within milliseconds
        else {
            sequence = 0L;
        }

        //The last time the ID was generated
        lastTimestamp = timestamp;

        //Shift and OR operations together to form a 64-bit ID
        return ((timestamp - twepoch) << timestampLeftShift) //
                | (workerId << workerIdShift) //
                | sequence;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

    //==============================Test=============================================
//    public static void main(String[] args) {
//        SnowflakeIdHelper idWorker = new SnowflakeIdHelper(0, 1);
//        long cTime = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            long id = idWorker.nextId();
//            System.out.println(id);
//
//        }
//        long eTime = System.currentTimeMillis();
//
//        System.out.println(eTime-cTime);
//    }
}
