package com.apec.mqcenter.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
* Created by wjw on 2016/11/15.
 * @author xxx
 */
@Component
public class SnowFlakeKeyGen {

    private final long workerIdBits = 6L;

    private final long maxWorkerId =
            -1L ^ (-1L << workerIdBits);

    private final long sequenceBits = 6L;

    private final long workerIdShift = sequenceBits;

    private final long timestampLeftShift = sequenceBits + workerIdBits;

    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    @Value("${workerId}")
    private long workerId;

    private long sequence = 0L;

    private long lastTimestamp = -1L;

    private final static long twepoch = 1452866247339L;

    public SnowFlakeKeyGen()
    {
        if(workerId > maxWorkerId || workerId < 0)
        {
            throw new IllegalArgumentException(
                    String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
    }

    public SnowFlakeKeyGen(long workerId)
    {
        if(workerId > maxWorkerId || workerId < 0)
        {
            throw new IllegalArgumentException(
                    String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        this.workerId=workerId;
    }

    public synchronized long nextId()
    {
        long timestamp = timeGen();
        if(timestamp < lastTimestamp)
        {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                            lastTimestamp - timestamp));
        }
        if(lastTimestamp == timestamp)
        {
            sequence = (sequence + 1) & sequenceMask;
            if(sequence == 0)
            {
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        else
        {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return (timestamp - twepoch << timestampLeftShift) | (workerId << workerIdShift) | sequence;
    }

    protected long tilNextMillis(long lastTimestamp)
    {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp)
        {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen()
    {
        return System.currentTimeMillis();
    }

}
