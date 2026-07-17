package model;

import protocol.common.ProtocolUnit;
import protocol.ethernet.EthernetFrame;

public class ParsedPacket {
    private final PacketRecord rawRecord;
    private final EthernetFrame ethernetFrame;
    private final ProtocolUnit networkPacket;
    private final ProtocolUnit transportSegment;
    private final ProtocolUnit applicationMessage;

    public ParsedPacket(PacketRecord rawRecord, EthernetFrame ethernetFrame,
                        ProtocolUnit networkPacket, ProtocolUnit transportSegment,
                        ProtocolUnit applicationMessage) {
        this.rawRecord = rawRecord;
        this.ethernetFrame = ethernetFrame;
        this.networkPacket = networkPacket;
        this.transportSegment = transportSegment;
        this.applicationMessage = applicationMessage;
    }

    public PacketRecord getRawRecord() {
        return rawRecord;
    }

    public EthernetFrame getEthernetFrame() {
        return ethernetFrame;
    }

    public ProtocolUnit getNetworkPacket() {
        return networkPacket;
    }

    public ProtocolUnit getTransportSegment() {
        return transportSegment;
    }

    public ProtocolUnit getApplicationMessage() {
        return applicationMessage;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[Packet Time: ").append(rawRecord.getTimestampSeconds()).append("s] ");
        sb.append(ethernetFrame.getProtocolName());
        if (networkPacket != null) {
            sb.append(" -> ").append(networkPacket.getProtocolName());
        }
        if (transportSegment != null) {
            sb.append(" -> ").append(transportSegment.getProtocolName());
        }
        if (applicationMessage != null) {
            sb.append(" -> ").append(applicationMessage.getProtocolName());
        }
        return sb.toString();
    }
}
