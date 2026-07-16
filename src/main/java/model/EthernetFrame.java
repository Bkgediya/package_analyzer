package model;
import java.util.Arrays;
public class EthernetFrame implements ProtocolUnit {
    private final String destinationMac;
    private final String sourceMac;
    private final int etherType;
    private final byte[] payload;

    public EthernetFrame(
        String destinationMac,
        String sourceMac,
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

    public String getDestinationMac() {
        return destinationMac;
    }

    public String getSourceMac() {
        return sourceMac;
    }

    public int getEtherType() {
        return etherType;
    }

    public byte[] getPayload() {
        return payload.clone();
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