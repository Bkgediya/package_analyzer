package report;

import statistics.StatisticsCollector;
import java.util.Map;

public class ReportGenerator {
    public String generateReport(StatisticsCollector stats) {
        StringBuilder sb = new StringBuilder();
        sb.append("=========================================\n");
        sb.append("         PACKET ANALYSIS REPORT          \n");
        sb.append("=========================================\n");
        sb.append(String.format("Total Packets Analyzed : %d\n", stats.getTotalPackets()));
        sb.append(String.format("Total Bytes Analyzed   : %d bytes\n", stats.getTotalBytes()));
        if (stats.getTotalPackets() > 0) {
            sb.append(String.format("Average Packet Size    : %.2f bytes\n", 
                (double) stats.getTotalBytes() / stats.getTotalPackets()));
        }
        sb.append("\n--- Protocol Distribution ---\n");
        for (Map.Entry<String, Integer> entry : stats.getProtocolCounts().entrySet()) {
            sb.append(String.format("  %-15s : %d packets\n", entry.getKey(), entry.getValue()));
        }

        sb.append("\n--- Top Source IP Addresses ---\n");
        stats.getIpSourceCounts().entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(5)
            .forEach(entry -> sb.append(String.format("  %-15s : %d packets\n", entry.getKey(), entry.getValue())));

        sb.append("\n--- Top Destination IP Addresses ---\n");
        stats.getIpDestCounts().entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(5)
            .forEach(entry -> sb.append(String.format("  %-15s : %d packets\n", entry.getKey(), entry.getValue())));

        sb.append("=========================================\n");
        return sb.toString();
    }
}
