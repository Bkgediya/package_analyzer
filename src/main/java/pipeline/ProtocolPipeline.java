package pipeline;

import exception.PacketParsingException;
import model.ParsedPacket;
import model.PacketRecord;
import protocol.common.ProtocolParser;
import protocol.common.ProtocolUnit;
import protocol.ethernet.EthernetFrame;
import protocol.ethernet.EthernetParser;
import registry.NetworkProtocolRegistry;
import registry.TransportProtocolRegistry;
import registry.ApplicationProtocolRegistry;
import io.ByteReader;

public class ProtocolPipeline {
    private final EthernetParser ethernetParser;
    private final NetworkProtocolRegistry networkRegistry;
    private final TransportProtocolRegistry transportRegistry;
    private final ApplicationProtocolRegistry applicationRegistry;

    public ProtocolPipeline(EthernetParser ethernetParser,
                            NetworkProtocolRegistry networkRegistry,
                            TransportProtocolRegistry transportRegistry,
                            ApplicationProtocolRegistry applicationRegistry) {
        this.ethernetParser = ethernetParser;
        this.networkRegistry = networkRegistry;
        this.transportRegistry = transportRegistry;
        this.applicationRegistry = applicationRegistry;
    }

    public ParsedPacket analyze(PacketRecord rawRecord) throws PacketParsingException {
        ByteReader reader = new ByteReader(rawRecord.getPacketData());
        EthernetFrame ethernetFrame = ethernetParser.parse(reader);

        ProtocolUnit networkPacket = null;
        ProtocolUnit transportSegment = null;
        ProtocolUnit applicationMessage = null;

        int etherType = ethernetFrame.getEtherType();
        ProtocolParser<? extends ProtocolUnit> networkParser = networkRegistry.getParser(etherType);
        if (networkParser != null) {
            ByteReader netReader = new ByteReader(ethernetFrame.getPayload());
            networkPacket = networkParser.parse(netReader);

            if (networkPacket instanceof protocol.ipv4.IPv4Packet) {
                protocol.ipv4.IPv4Packet ipv4 = (protocol.ipv4.IPv4Packet) networkPacket;
                int ipProtocol = ipv4.getProtocol();
                ProtocolParser<? extends ProtocolUnit> transportParser = transportRegistry.getParser(ipProtocol);
                if (transportParser != null) {
                    ByteReader transReader = new ByteReader(ipv4.getPayload());
                    transportSegment = transportParser.parse(transReader);

                    int port = getPort(transportSegment);
                    if (port != -1) {
                        ProtocolParser<? extends ProtocolUnit> appParser = applicationRegistry.getParser(port);
                        if (appParser != null) {
                            ByteReader appReader = new ByteReader(transportSegment.getPayload());
                            applicationMessage = appParser.parse(appReader);
                        }
                    }
                }
            }
        }

        return new ParsedPacket(
                rawRecord,
                ethernetFrame,
                networkPacket,
                transportSegment,
                applicationMessage
        );
    }

    private int getPort(ProtocolUnit transportSegment) {
        int srcPort = -1;
        int destPort = -1;
        if (transportSegment instanceof protocol.tcp.TcpSegment) {
            protocol.tcp.TcpSegment tcp = (protocol.tcp.TcpSegment) transportSegment;
            srcPort = tcp.getSourcePort();
            destPort = tcp.getDestinationPort();
        } else if (transportSegment instanceof protocol.udp.UdpDatagram) {
            protocol.udp.UdpDatagram udp = (protocol.udp.UdpDatagram) transportSegment;
            srcPort = udp.getSourcePort();
            destPort = udp.getDestinationPort();
        }

        if (applicationRegistry.getParser(destPort) != null) {
            return destPort;
        } else if (applicationRegistry.getParser(srcPort) != null) {
            return srcPort;
        }
        return -1;
    }
}
