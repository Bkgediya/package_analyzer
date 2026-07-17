package model;

import java.util.Arrays;

public class PacketRecord {
    private final long timestampSeconds;
    private final long timestampMicroseconds;
    private final int capturedLength;
    private final int originalLength;
    private final byte[] packetData;

    public PacketRecord(
            long timestampSeconds,
            long timestampMicroseconds,
            int capturedLength,
            int originalLength,
            byte[] packetData) {

        if (packetData == null) {
            throw new IllegalArgumentException("Packet data cannot be null.");
        }
        this.timestampSeconds = timestampSeconds;
        this.timestampMicroseconds = timestampMicroseconds;
        this.capturedLength = capturedLength;
        this.originalLength = originalLength;
        this.packetData = packetData.clone();
    }

    public long getTimestampSeconds() {
        return timestampSeconds;
    }

    public long getTimestampMicroseconds() {
        return timestampMicroseconds;
    }

    public int getCapturedLength() {
        return capturedLength;
    }

    public int getOriginalLength() {
        return originalLength;
    }

    public byte[] getPacketData() {
        return packetData.clone();
    }

    @Override
    public String toString() {
        return "PacketRecord{" +
                "timestampSeconds=" + timestampSeconds +
                ", timestampMicroseconds=" + timestampMicroseconds +
                ", capturedLength=" + capturedLength +
                ", originalLength=" + originalLength +
                ", packetData=" + Arrays.toString(packetData) +
                '}';
    }
}