import analyzer.PacketAnalyzer;
import protocol.ethernet.EtherTypes;
import protocol.ipv4.IpProtocols;
import constant.Ports;
import protocol.ipv4.IP4Parser;
import protocol.tcp.TcpParser;
import protocol.udp.UdpParser;
import protocol.http.HttpParser;
import protocol.dns.DnsParser;
import protocol.ethernet.EthernetParser;
import registry.NetworkProtocolRegistry;
import registry.TransportProtocolRegistry;
import registry.ApplicationProtocolRegistry;
import pipeline.ProtocolPipeline;
import io.PcapReader;
import statistics.StatisticsCollector;
import report.ReportGenerator;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Application {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Application <path-to-pcap-file>");
            return;
        }

        Path pcapPath = Paths.get(args[0]);

        // 1. Initialize Parsers
        EthernetParser ethernetParser = new EthernetParser();
        IP4Parser ip4Parser = new IP4Parser();
        TcpParser tcpParser = new TcpParser();
        UdpParser udpParser = new UdpParser();
        HttpParser httpParser = new HttpParser();
        DnsParser dnsParser = new DnsParser();

        // 2. Initialize Registries
        NetworkProtocolRegistry networkRegistry = new NetworkProtocolRegistry();
        networkRegistry.register(EtherTypes.IPV4, ip4Parser);

        TransportProtocolRegistry transportRegistry = new TransportProtocolRegistry();
        transportRegistry.register(IpProtocols.TCP, tcpParser);
        transportRegistry.register(IpProtocols.UDP, udpParser);

        ApplicationProtocolRegistry applicationRegistry = new ApplicationProtocolRegistry();
        applicationRegistry.register(Ports.HTTP, httpParser);
        applicationRegistry.register(Ports.HTTP_ALT, httpParser);
        applicationRegistry.register(Ports.DNS, dnsParser);

        // 3. Initialize Pipeline and Analyzer
        ProtocolPipeline pipeline = new ProtocolPipeline(
                ethernetParser,
                networkRegistry,
                transportRegistry,
                applicationRegistry);

        PacketAnalyzer analyzer = new PacketAnalyzer(pipeline);

        // 4. Attach Statistics Collector
        StatisticsCollector statsCollector = new StatisticsCollector();
        analyzer.addObserver(statsCollector);

        // 5. Read and Parse the PCAP File
        PcapReader pcapReader = new PcapReader();
        try {
            System.out.println("Reading PCAP file: " + pcapPath.toAbsolutePath());
            pcapReader.read(pcapPath, analyzer);

            // 6. Generate and Print Report
            ReportGenerator reportGenerator = new ReportGenerator();
            String report = reportGenerator.generateReport(statsCollector);
            System.out.println(report);

        } catch (IOException e) {
            System.err.println("IO Error reading PCAP file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Fatal Error during analysis: " + e.getMessage());
            e.printStackTrace();
        }
    }
}