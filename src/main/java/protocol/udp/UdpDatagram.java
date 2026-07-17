package protocol.udp;

import protocol.common.ProtocolUnit;

public class UdpDatagram implements ProtocolUnit {
    private final int sourcePort;
    private final int destinationPort;
    private final byte[] payload;

    public UdpDatagram(int sourcePort, int destinationPort, byte[] payload) {
        if (payload == null) {
            throw new IllegalArgumentException("Payload cannot be null.");
        }
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
        this.payload = payload.clone();
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    @Override
    public byte[] getPayload() {
        return payload.clone();
    }

    @Override
    public String getProtocolName() {
        return "UDP";
    }

    @Override
    public String toString() {
        return "UdpDatagram{" +
                "sourcePort=" + sourcePort +
                ", destinationPort=" + destinationPort +
                ", payloadLength=" + payload.length +
                '}';
    }
}
