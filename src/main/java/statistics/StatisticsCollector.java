package statistics;

import model.ParsedPacket;
import protocol.common.ProtocolUnit;
import protocol.ipv4.IPv4Packet;
import analyzer.ParsedPacketObserver;
import java.util.HashMap;
import java.util.Map;

public class StatisticsCollector implements ParsedPacketObserver {
    private int totalPackets = 0;
    private long totalBytes = 0;
    private final Map<String, Integer> protocolCounts = new HashMap<>();
    private final Map<String, Integer> ipSourceCounts = new HashMap<>();
    private final Map<String, Integer> ipDestCounts = new HashMap<>();

    @Override
    public void onPacketParsed(ParsedPacket packet) {
        totalPackets++;
        totalBytes += packet.getRawRecord().getCapturedLength();

        incrementProtocolCount("Ethernet");

        ProtocolUnit net = packet.getNetworkPacket();
        if (net != null) {
            incrementProtocolCount(net.getProtocolName());
            if (net instanceof IPv4Packet) {
                IPv4Packet ipv4 = (IPv4Packet) net;
                incrementIpCount(ipSourceCounts, ipv4.getSourceIpString());
                incrementIpCount(ipDestCounts, ipv4.getDestinationIpString());
            }
        }

        ProtocolUnit trans = packet.getTransportSegment();
        if (trans != null) {
            incrementProtocolCount(trans.getProtocolName());
        }

        ProtocolUnit app = packet.getApplicationMessage();
        if (app != null) {
            incrementProtocolCount(app.getProtocolName());
        }
    }

    private void incrementProtocolCount(String protocol) {
        protocolCounts.put(protocol, protocolCounts.getOrDefault(protocol, 0) + 1);
    }

    private void incrementIpCount(Map<String, Integer> counts, String ip) {
        counts.put(ip, counts.getOrDefault(ip, 0) + 1);
    }

    public int getTotalPackets() {
        return totalPackets;
    }

    public long getTotalBytes() {
        return totalBytes;
    }

    public Map<String, Integer> getProtocolCounts() {
        return new HashMap<>(protocolCounts);
    }

    public Map<String, Integer> getIpSourceCounts() {
        return new HashMap<>(ipSourceCounts);
    }

    public Map<String, Integer> getIpDestCounts() {
        return new HashMap<>(ipDestCounts);
    }
}
