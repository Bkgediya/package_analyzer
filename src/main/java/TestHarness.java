import analyzer.PacketAnalyzer;
import model.PacketRecord;
import model.ParsedPacket;
import protocol.common.ProtocolUnit;
import protocol.ethernet.EtherTypes;
import protocol.ethernet.EthernetFrame;
import protocol.ethernet.EthernetParser;
import protocol.ipv4.IPv4Packet;
import protocol.ipv4.IP4Parser;
import protocol.ipv4.IpProtocols;
import protocol.tcp.TcpSegment;
import protocol.tcp.TcpParser;
import protocol.udp.UdpDatagram;
import protocol.udp.UdpParser;
import protocol.dns.DnsMessage;
import protocol.dns.DnsParser;
import protocol.http.HttpMessage;
import protocol.http.HttpParser;
import registry.NetworkProtocolRegistry;
import registry.TransportProtocolRegistry;
import registry.ApplicationProtocolRegistry;
import pipeline.ProtocolPipeline;
import statistics.StatisticsCollector;
import constant.Ports;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TestHarness {

    public static void main(String[] args) {
        System.out.println("=== STARTING PACKET ANALYZER TEST SUITE ===");

        try {
            testDnsParsing();
            testHttpParsing();
            System.out.println("=== ALL TESTS PASSED SUCCESSFULLY! ===");
        } catch (Exception e) {
            System.err.println("!!! TEST FAILURE !!!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void testDnsParsing() throws Exception {
        System.out.println("Testing DNS Packet Parsing...");

        // Build mock DNS query payload: google.com
        ByteArrayOutputStream dns = new ByteArrayOutputStream();
        dns.write(new byte[]{0x12, 0x34}); // Transaction ID
        dns.write(new byte[]{0x01, 0x00}); // Flags
        dns.write(new byte[]{0x00, 0x01}); // Questions
        dns.write(new byte[]{0x00, 0x00}); // Answers
        dns.write(new byte[]{0x00, 0x00}); // Authority RRs
        dns.write(new byte[]{0x00, 0x00}); // Additional RRs
        // Question name: "google.com"
        dns.write(6);
        dns.write("google".getBytes(StandardCharsets.US_ASCII));
        dns.write(3);
        dns.write("com".getBytes(StandardCharsets.US_ASCII));
        dns.write(0); // terminate domain
        dns.write(new byte[]{0x00, 0x01}); // Type A
        dns.write(new byte[]{0x00, 0x01}); // Class IN
        byte[] dnsBytes = dns.toByteArray();

        // Build UDP header
        ByteArrayOutputStream udp = new ByteArrayOutputStream();
        udp.write(new byte[]{0x1F, (byte) 0x90}); // Src Port: 8080
        udp.write(new byte[]{0x00, 0x35});        // Dest Port: 53 (DNS)
        int udpLen = 8 + dnsBytes.length;
        udp.write((udpLen >> 8) & 0xFF);
        udp.write(udpLen & 0xFF);
        udp.write(new byte[]{0x00, 0x00});        // Checksum
        udp.write(dnsBytes);
        byte[] udpBytes = udp.toByteArray();

        // Build IP header
        ByteArrayOutputStream ip = new ByteArrayOutputStream();
        ip.write(0x45); // Version & IHL
        ip.write(0x00); // TOS
        int ipLen = 20 + udpBytes.length;
        ip.write((ipLen >> 8) & 0xFF);
        ip.write(ipLen & 0xFF);
        ip.write(new byte[]{0x12, 0x34}); // ID
        ip.write(new byte[]{0x40, 0x00}); // Flags & Offset
        ip.write(0x40); // TTL
        ip.write(17);   // Protocol: UDP (17)
        ip.write(new byte[]{0x00, 0x00}); // Checksum
        ip.write(new byte[]{10, 0, 0, 1}); // Src IP
        ip.write(new byte[]{10, 0, 0, 2}); // Dest IP
        ip.write(udpBytes);
        byte[] ipBytes = ip.toByteArray();

        // Build Ethernet header
        ByteArrayOutputStream eth = new ByteArrayOutputStream();
        eth.write(new byte[]{0x00, 0x11, 0x22, 0x33, 0x44, 0x55}); // Dest MAC
        eth.write(new byte[]{0x66, 0x77, (byte) 0x88, (byte) 0x99, (byte) 0xAA, (byte) 0xBB}); // Src MAC
        eth.write(new byte[]{0x08, 0x00}); // EtherType: IPv4
        eth.write(ipBytes);
        byte[] ethBytes = eth.toByteArray();

        // Parse using pipeline
        ProtocolPipeline pipeline = createPipeline();
        PacketRecord rawRecord = new PacketRecord(1710000000L, 123456L, ethBytes.length, ethBytes.length, ethBytes);
        ParsedPacket packet = pipeline.analyze(rawRecord);

        // Assert Ethernet Frame
        EthernetFrame frame = packet.getEthernetFrame();
        assertEquals("00:11:22:33:44:55", frame.getDestinationMac().toString());
        assertEquals("66:77:88:99:AA:BB", frame.getSourceMac().toString());
        assertEquals(EtherTypes.IPV4, frame.getEtherType());

        // Assert IPv4 Packet
        IPv4Packet ipPacket = (IPv4Packet) packet.getNetworkPacket();
        assertNotNull(ipPacket);
        assertEquals("10.0.0.1", ipPacket.getSourceIpString());
        assertEquals("10.0.0.2", ipPacket.getDestinationIpString());
        assertEquals(IpProtocols.UDP, ipPacket.getProtocol());

        // Assert UDP Datagram
        UdpDatagram udpDatagram = (UdpDatagram) packet.getTransportSegment();
        assertNotNull(udpDatagram);
        assertEquals(8080, udpDatagram.getSourcePort());
        assertEquals(53, udpDatagram.getDestinationPort());

        // Assert DNS Message
        DnsMessage dnsMessage = (DnsMessage) packet.getApplicationMessage();
        assertNotNull(dnsMessage);
        assertEquals(0x1234, dnsMessage.getTransactionId());
        assertEquals(0x0100, dnsMessage.getFlags());
        assertEquals(1, dnsMessage.getQueries().size());
        assertEquals("google.com", dnsMessage.getQueries().get(0));

        System.out.println("DNS Packet Parsing Test Passed.");
    }

    private static void testHttpParsing() throws Exception {
        System.out.println("Testing HTTP Packet Parsing...");

        // Build HTTP Request Payload
        String httpPayload = "GET /index.html HTTP/1.1\r\nHost: example.com\r\nUser-Agent: TestAgent\r\n\r\n";
        byte[] httpBytes = httpPayload.getBytes(StandardCharsets.UTF_8);

        // Build TCP header
        ByteArrayOutputStream tcp = new ByteArrayOutputStream();
        tcp.write(new byte[]{0x1F, (byte) 0x90}); // Src Port: 8080
        tcp.write(new byte[]{0x00, 0x50});        // Dest Port: 80 (HTTP)
        tcp.write(new byte[]{0x00, 0x00, 0x00, 0x01}); // Seq Num
        tcp.write(new byte[]{0x00, 0x00, 0x00, 0x02}); // Ack Num
        tcp.write(0x50); // Data Offset: 5 (20 bytes), Reserved: 0
        tcp.write(0x10); // Flags: ACK
        tcp.write(new byte[]{0x10, 0x00}); // Window size
        tcp.write(new byte[]{0x00, 0x00}); // Checksum
        tcp.write(new byte[]{0x00, 0x00}); // Urgent Pointer
        tcp.write(httpBytes);
        byte[] tcpBytes = tcp.toByteArray();

        // Build IP header
        ByteArrayOutputStream ip = new ByteArrayOutputStream();
        ip.write(0x45); // Version & IHL
        ip.write(0x00); // TOS
        int ipLen = 20 + tcpBytes.length;
        ip.write((ipLen >> 8) & 0xFF);
        ip.write(ipLen & 0xFF);
        ip.write(new byte[]{0x12, 0x34}); // ID
        ip.write(new byte[]{0x40, 0x00}); // Flags & Offset
        ip.write(0x40); // TTL
        ip.write(6);    // Protocol: TCP (6)
        ip.write(new byte[]{0x00, 0x00}); // Checksum
        ip.write(new byte[]{10, 0, 0, 1}); // Src IP
        ip.write(new byte[]{10, 0, 0, 2}); // Dest IP
        ip.write(tcpBytes);
        byte[] ipBytes = ip.toByteArray();

        // Build Ethernet header
        ByteArrayOutputStream eth = new ByteArrayOutputStream();
        eth.write(new byte[]{0x00, 0x11, 0x22, 0x33, 0x44, 0x55}); // Dest MAC
        eth.write(new byte[]{0x66, 0x77, (byte) 0x88, (byte) 0x99, (byte) 0xAA, (byte) 0xBB}); // Src MAC
        eth.write(new byte[]{0x08, 0x00}); // EtherType: IPv4
        eth.write(ipBytes);
        byte[] ethBytes = eth.toByteArray();

        // Parse using pipeline
        ProtocolPipeline pipeline = createPipeline();
        PacketRecord rawRecord = new PacketRecord(1710000000L, 123456L, ethBytes.length, ethBytes.length, ethBytes);
        ParsedPacket packet = pipeline.analyze(rawRecord);

        // Assert Ethernet Frame
        EthernetFrame frame = packet.getEthernetFrame();
        assertEquals(EtherTypes.IPV4, frame.getEtherType());

        // Assert IPv4 Packet
        IPv4Packet ipPacket = (IPv4Packet) packet.getNetworkPacket();
        assertNotNull(ipPacket);
        assertEquals(IpProtocols.TCP, ipPacket.getProtocol());

        // Assert TCP Segment
        TcpSegment tcpSegment = (TcpSegment) packet.getTransportSegment();
        assertNotNull(tcpSegment);
        assertEquals(8080, tcpSegment.getSourcePort());
        assertEquals(80, tcpSegment.getDestinationPort());

        // Assert HTTP Message
        HttpMessage httpMessage = (HttpMessage) packet.getApplicationMessage();
        assertNotNull(httpMessage);
        assertEquals("REQUEST", httpMessage.getType());
        assertEquals("GET /index.html", httpMessage.getInfo());
        assertEquals("example.com", httpMessage.getHeaders().get("Host"));
        assertEquals("TestAgent", httpMessage.getHeaders().get("User-Agent"));

        // Verify stats collector with Observer pattern
        StatisticsCollector collector = new StatisticsCollector();
        collector.onPacketParsed(packet);
        assertEquals(1, collector.getTotalPackets());
        assertEquals(ethBytes.length, collector.getTotalBytes());
        assertEquals(1, (int) collector.getProtocolCounts().get("Ethernet"));
        assertEquals(1, (int) collector.getProtocolCounts().get("IPv4"));
        assertEquals(1, (int) collector.getProtocolCounts().get("TCP"));
        assertEquals(1, (int) collector.getProtocolCounts().get("HTTP"));

        System.out.println("HTTP Packet Parsing Test Passed.");
    }

    private static ProtocolPipeline createPipeline() {
        EthernetParser ethernetParser = new EthernetParser();
        IP4Parser ip4Parser = new IP4Parser();
        TcpParser tcpParser = new TcpParser();
        UdpParser udpParser = new UdpParser();
        HttpParser httpParser = new HttpParser();
        DnsParser dnsParser = new DnsParser();

        NetworkProtocolRegistry networkRegistry = new NetworkProtocolRegistry();
        networkRegistry.register(EtherTypes.IPV4, ip4Parser);

        TransportProtocolRegistry transportRegistry = new TransportProtocolRegistry();
        transportRegistry.register(IpProtocols.TCP, tcpParser);
        transportRegistry.register(IpProtocols.UDP, udpParser);

        ApplicationProtocolRegistry applicationRegistry = new ApplicationProtocolRegistry();
        applicationRegistry.register(Ports.HTTP, httpParser);
        applicationRegistry.register(Ports.DNS, dnsParser);

        return new ProtocolPipeline(
                ethernetParser,
                networkRegistry,
                transportRegistry,
                applicationRegistry
        );
    }

    private static void assertEquals(Object expected, Object actual) {
        if (expected instanceof Number && actual instanceof Number) {
            if (((Number) expected).longValue() == ((Number) actual).longValue()) {
                return;
            }
        }
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected: " + expected + ", but got: " + actual);
        }
    }

    private static void assertNotNull(Object obj) {
        if (obj == null) {
            throw new AssertionError("Expected object to be not null");
        }
    }
}
