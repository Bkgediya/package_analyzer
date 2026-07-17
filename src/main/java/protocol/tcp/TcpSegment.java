package protocol.tcp;

import protocol.common.ProtocolUnit;

public class TcpSegment implements ProtocolUnit {
    private final int sourcePort;
    private final int destinationPort;
    private final long sequenceNumber;
    private final long acknowledgmentNumber;
    private final int flags;
    private final byte[] payload;

    public TcpSegment(int sourcePort, int destinationPort, long sequenceNumber,
                      long acknowledgmentNumber, int flags, byte[] payload) {
        if (payload == null) {
            throw new IllegalArgumentException("Payload cannot be null.");
        }
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
        this.sequenceNumber = sequenceNumber;
        this.acknowledgmentNumber = acknowledgmentNumber;
        this.flags = flags;
        this.payload = payload.clone();
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public long getAcknowledgmentNumber() {
        return acknowledgmentNumber;
    }

    public int getFlags() {
        return flags;
    }

    @Override
    public byte[] getPayload() {
        return payload.clone();
    }

    @Override
    public String getProtocolName() {
        return "TCP";
    }

    @Override
    public String toString() {
        return "TcpSegment{" +
                "sourcePort=" + sourcePort +
                ", destinationPort=" + destinationPort +
                ", sequenceNumber=" + sequenceNumber +
                ", acknowledgmentNumber=" + acknowledgmentNumber +
                ", flags=0x" + Integer.toHexString(flags).toUpperCase() +
                ", payloadLength=" + payload.length +
                '}';
    }
}
