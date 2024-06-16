/******************************************************************************
 *
 *  (C) 2024 thmoon. All rights reserved.
 *  Any part of this source code can not be copied with any method without
 *  prior written permission from the author or authorized person.
 *
 ******************************************************************************/

package com.moais.todo.snowflake;

import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Enumeration;

public class SnowflakeFactory {
    final long nodeId;
    final long customEpoch;
    volatile long lastTimestamp;
    volatile long sequence;

    public SnowflakeFactory(long nodeId, long customEpoch) {
        this.lastTimestamp = -1L;
        this.sequence = 0L;
        if (nodeId >= 0L && nodeId <= 1023L) {
            this.nodeId = nodeId;
            this.customEpoch = customEpoch;
        } else {
            throw new IllegalArgumentException(String.format("NodeId must be between %d and %d", 0, 1023L));
        }
    }

    public SnowflakeFactory(long nodeId) {
        this(nodeId, 863128800L);
    }

    public SnowflakeFactory() {
        this.lastTimestamp = -1L;
        this.sequence = 0L;
        this.nodeId = this.createNodeId();
        this.customEpoch = 863128800L;
    }

    public static long create(){
        SnowflakeFactory snowflake = new SnowflakeFactory();
        return snowflake.nextId();
    }

    public synchronized long nextId() {
        long currentTimestamp = this.timestamp();
        if (currentTimestamp < this.lastTimestamp) {
            throw new IllegalStateException("Invalid System Clock!");
        } else {
            if (currentTimestamp == this.lastTimestamp) {
                this.sequence = this.sequence + 1L & 4095L;
                if (this.sequence == 0L) {
                    currentTimestamp = this.waitNextMillis(currentTimestamp);
                }
            } else {
                this.sequence = 0L;
            }

            this.lastTimestamp = currentTimestamp;
            return currentTimestamp << 22 | this.nodeId << 12 | this.sequence;
        }
    }

    private long timestamp() {
        return Instant.now().toEpochMilli() - this.customEpoch;
    }

    private long waitNextMillis(long currentTimestamp) {
        while(currentTimestamp == this.lastTimestamp) {
            currentTimestamp = this.timestamp();
        }

        return currentTimestamp;
    }

    private long createNodeId() {
        long myNodeId = 0;
        try {
            StringBuilder sb = new StringBuilder();
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            boolean explode = false;
            while(true) {
                byte[] mac = {};
                do {
                    if (!networkInterfaces.hasMoreElements()) {
                        myNodeId = sb.toString().hashCode();
                        explode = true;
                        break;
                    }

                    NetworkInterface networkInterface = networkInterfaces.nextElement();
                    mac = networkInterface.getHardwareAddress();
                } while(mac == null);

                if(explode) break;

                byte[] var7 = mac;
                int var8 = mac.length;

                for(int var9 = 0; var9 < var8; ++var9) {
                    byte macPort = var7[var9];
                    sb.append(String.format("%02X", macPort));
                }
            }
        } catch (Exception var11) {
            myNodeId = (new SecureRandom()).nextInt();
        }

        myNodeId &= 1023L;
        return myNodeId;
    }

    public long[] parse(long id) {
        long maskNodeId = 4190208L;
        long maskSequence = 4095L;
        long timestamp = (id >> 22) + this.customEpoch;
        long myNodeId = (id & maskNodeId) >> 12;
        long mySequence = id & maskSequence;
        return new long[]{timestamp, myNodeId, mySequence};
    }

    public String toString() {
        return "Snowflake Settings [EPOCH_BITS=41, NODE_ID_BITS=10, SEQUENCE_BITS=12, CUSTOM_EPOCH=" + this.customEpoch + ", NodeId=" + this.nodeId + "]";
    }
}
