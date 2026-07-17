package protocol.ethernet;

import protocol.common.ProtocolUnit;

public class EthernetFrame implements ProtocolUnit {
    private final MacAddress destinationMac;
    private final MacAddress sourceMac;
    private final int etherType;
    private final byte[] payload;

    public EthernetFrame(
        MacAddress destinationMac,
        MacAddress sourceMac,
        int etherType,
        byte[] payload) {

        if (destinationMac == null || sourceMac == null) {
            throw new IllegalArgumentException("MAC address cannot be null.");
        }

        if (payload == null) {
            throw new IllegalArgumentException("Payload cannot be null.");
        }
        this.destinationMac = destinationMac;
        this.sourceMac = sourceMac;
        this.etherType = etherType;
        this.payload = payload.clone();
    }

    public MacAddress getDestinationMac() {
        return destinationMac;
    }

    public MacAddress getSourceMac() {
        return sourceMac;
    }

    public int getEtherType() {
        return etherType;
    }

    @Override
    public byte[] getPayload() {
        return payload.clone();
    }

    @Override
    public String getProtocolName() {
        return "Ethernet";
    }

    @Override
    public String toString() {
        return "EthernetFrame{" +
                "destinationMac='" + destinationMac + '\'' +
                ", sourceMac='" + sourceMac + '\'' +
                ", etherType=0x" + Integer.toHexString(etherType).toUpperCase() +
                ", payloadLength=" + payload.length +
                '}';
    }
}