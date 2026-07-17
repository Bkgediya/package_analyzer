package protocol.ipv4;

import protocol.common.ProtocolUnit;

public class IPv4Packet implements ProtocolUnit {
    private final byte[] sourceIP;
    private final byte[] destinationIP;
    private final int protocol;
    private final byte[] payload;

    public IPv4Packet(byte[] sourceIP, byte[] destinationIP, int protocol, byte[] payload) {
        if (sourceIP == null || sourceIP.length != 4) {
            throw new IllegalArgumentException("Source IP must be 4 bytes.");
        }
        if (destinationIP == null || destinationIP.length != 4) {
            throw new IllegalArgumentException("Destination IP must be 4 bytes.");
        }
        if (payload == null) {
            throw new IllegalArgumentException("Payload cannot be null.");
        }
        this.sourceIP = sourceIP.clone();
        this.destinationIP = destinationIP.clone();
        this.protocol = protocol;
        this.payload = payload.clone();
    }

    public String getSourceIpString() {
        return formatIp(sourceIP);
    }

    public String getDestinationIpString() {
        return formatIp(destinationIP);
    }

    public byte[] getSourceIP() {
        return sourceIP.clone();
    }

    public byte[] getDestinationIP() {
        return destinationIP.clone();
    }

    public int getProtocol() {
        return protocol;
    }

    @Override
    public byte[] getPayload() {
        return payload.clone();
    }

    @Override
    public String getProtocolName() {
        return "IPv4";
    }

    private String formatIp(byte[] ip) {
        return (ip[0] & 0xFF) + "." + (ip[1] & 0xFF) + "." + (ip[2] & 0xFF) + "." + (ip[3] & 0xFF);
    }

    @Override
    public String toString() {
        return "IPv4Packet{" +
                "sourceIP=" + getSourceIpString() +
                ", destinationIP=" + getDestinationIpString() +
                ", protocol=" + protocol +
                ", payloadLength=" + payload.length +
                '}';
    }
}