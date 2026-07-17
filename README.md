# DIP PCAP Packet Analyzer

A highly structured, immutable, and performant packet analysis tool written in Java. This project parses raw Packet Capture (PCAP) files and extracts detailed layer-by-layer protocol headers and payloads (Ethernet, IPv4, TCP, UDP, DNS, and HTTP).

The project is designed in strict compliance with the **SOLID Principles**, **Low-Level Design (LLD)** best practices, and clean code standards.

---

## 🏛️ Architecture & Design Patterns

### 1. SOLID Principles
*   **Single Responsibility Principle (SRP)**: Each class has a distinct responsibility. For example, `MagicNumberDetector` only detects PCAP endianness, `PcapReader` manages file reading, `StatisticsCollector` gathers counts and metrics, and individual parsers (e.g., `TcpParser`) only parse their respective headers.
*   **Open/Closed Principle (OCP)**: Adding new network, transport, or application protocols does not require modifying the existing parsing logic. New protocol parsers simply implement the generic `ProtocolParser<T>` interface and register themselves with the respective registry.
*   **Liskov Substitution Principle (LSP)**: All layer-specific parsed objects (like `IPv4Packet`, `TcpSegment`, `HttpMessage`) implement the `ProtocolUnit` interface and can be treated polymorphically throughout the pipeline.
*   **Interface Segregation Principle (ISP)**: Interfaces are kept highly focused and cohesive (e.g., `ProtocolParser`, `ProtocolUnit`, `ParsedPacketObserver`).
*   **Dependency Inversion Principle (DIP)**: High-level orchestrators like the `PacketAnalyzer` depend on abstractions (like the `ProtocolPipeline` interface and `PacketProcessor` abstraction) rather than concrete implementations.

### 2. Design Patterns
*   **Pipeline / Chain of Responsibility Pattern**: Implemented in [ProtocolPipeline.java](src/main/java/pipeline/ProtocolPipeline.java). When a packet record is analyzed, it flows sequentially down the protocol stack (Ethernet ➡️ IPv4 ➡️ TCP/UDP ➡️ HTTP/DNS), with each layer extracting its headers and passing the raw payload to the next parser in the pipeline.
*   **Registry / Dispatcher Pattern**: Implemented under the [registry/](src/main/java/registry/) package. Rather than hardcoding parsing paths via complex nested `if-else` blocks, the registries (`NetworkProtocolRegistry`, `TransportProtocolRegistry`, `ApplicationProtocolRegistry`) map protocol identifiers (such as EtherTypes, IP protocol numbers, and Ports) to their corresponding `ProtocolParser` implementations.
*   **Observer Pattern**: Implemented via [ParsedPacketObserver.java](src/main/java/analyzer/ParsedPacketObserver.java). High-level observers (like [StatisticsCollector.java](src/main/java/statistics/StatisticsCollector.java)) register with [PacketAnalyzer.java](src/main/java/analyzer/PacketAnalyzer.java) and are asynchronously notified of successfully parsed packets, keeping statistical analysis decoupled from parsing logic.

---

## 📂 Project Structure

```
dip_analyzer/
├── src/
│   └── main/
│       └── java/
│           ├── Application.java               # Main entry point for PCAP file execution
│           ├── TestHarness.java               # Comprehensive automated test suite
│           ├── analyzer/
│           │   ├── PacketAnalyzer.java        # Core analyzer running pipeline and notifying observers
│           │   ├── PacketProcessor.java       # Interface for raw packet processing
│           │   └── ParsedPacketObserver.java  # Observer callback interface
│           ├── constant/
│           │   ├── PcapConstants.java         # Global PCAP file metrics (headers sizes)
│           │   └── Ports.java                 # Well-known application port constants
│           ├── exception/
│           │   ├── ParserException.java       # Checked exception base class
│           │   ├── InvalidPcapFileException.java
│           │   ├── PacketParsingException.java
│           │   └── ... (Protocol exceptions)  # Ethernet, IPv4, Tcp, Udp, Dns exceptions
│           ├── io/
│           │   ├── ByteReader.java            # Sequential byte reader with endian control
│           │   ├── GlobalHeaderReader.java    # Reader for 24-byte PCAP global header
│           │   ├── PacketRecordReader.java    # Reader for individual PCAP packet records
│           │   └── PcapReader.java            # File reader orchestrating global/packet inputs
│           ├── model/
│           │   ├── PacketRecord.java          # Raw packet wrapper
│           │   ├── ParsedPacket.java          # Wrapper holding the chain of decoded ProtocolUnits
│           │   └── PcapGlobalHeader.java      # Model representing global PCAP file parameters
│           ├── pipeline/
│           │   └── ProtocolPipeline.java      # Pipeline orchestrating layered parsing
│           ├── protocol/
│           │   ├── common/
│           │   │   ├── ProtocolParser.java    # Generic protocol parser interface
│           │   │   └── ProtocolUnit.java      # Common parsed protocol unit interface
│           │   ├── ethernet/
│           │   │   ├── EthernetFrame.java
│           │   │   ├── EthernetParser.java
│           │   │   ├── EtherTypes.java
│           │   │   ├── EthernetHeaderConstants.java
│           │   │   └── MacAddress.java
│           │   ├── ipv4/
│           │   │   ├── IPv4Packet.java
│           │   │   ├── IP4Parser.java
│           │   │   └── IpProtocols.java
│           │   ├── tcp/
│           │   │   ├── TcpSegment.java
│           │   │   └── TcpParser.java
│           │   ├── udp/
│           │   │   ├── UdpDatagram.java
│           │   │   └── UdpParser.java
│           │   ├── http/
│           │   │   ├── HttpMessage.java
│           │   │   └── HttpParser.java
│           │   └── dns/
│           │       ├── DnsMessage.java
│           │       └── DnsParser.java
│           ├── registry/
│           │   ├── ProtocolDispatcher.java    # Generic dispatcher helper
│           │   ├── NetworkProtocolRegistry.java
│           │   ├── TransportProtocolRegistry.java
│           │   └── ApplicationProtocolRegistry.java
│           ├── report/
│           │   └── ReportGenerator.java       # Formats analysis statistics into printable reports
│           ├── statistics/
│           │   └── StatisticsCollector.java   # Observer tracking metrics & top IPs
│           └── util/
│               ├── EndianUtils.java           # Endian conversions
│               └── MacAddressFormatter.java   # MAC formatter helper
```

---

## 🚀 Building & Running

### 1. Compile the Project
Open a terminal in the project root directory (e.g. `dip_analyzer`), then compile all source files into a target binary directory:
```powershell
$files = Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName }; javac -d bin $files
```

### 2. Run the Verification Tests
To run the mock packet suite and confirm that all decoders parse correctly:
```powershell
java -cp bin TestHarness
```
*Expected Output:*
```
=== STARTING PACKET ANALYZER TEST SUITE ===
Testing DNS Packet Parsing...
DNS Packet Parsing Test Passed.
Testing HTTP Packet Parsing...
HTTP Packet Parsing Test Passed.
=== ALL TESTS PASSED SUCCESSFULLY! ===
```

### 3. Run Analysis on a PCAP File
To analyze a local `.pcap` capture file and output statistics/metrics:
```powershell
java -cp bin Application path/to/your/capture.pcap
```

---

## 🛠️ Supported Protocols Details

1.  **Ethernet (Link Layer)**: Extracts MAC source, MAC destination, EtherType, and payload bytes.
2.  **IPv4 (Internet Layer)**: Reads version (expects 4), IHL (Internet Header Length), total length, IP protocol ID, source/destination IP addresses, handles optional IP header options, and extracts transport payload.
3.  **TCP (Transport Layer)**: Extracts source/destination ports, sequence numbers, acknowledgment numbers, data offsets, TCP control flags, options, and payloads.
4.  **UDP (Transport Layer)**: Extracts source/destination ports, length, and payload.
5.  **HTTP (Application Layer)**: Parses text payloads on TCP port 80/8080. Extracts request method/path or response status, along with key-value headers.
6.  **DNS (Application Layer)**: Decodes binary payloads on UDP port 53. Extracts query transaction ID, header flags, query count, answer count, and parses query labels (domains) dynamically, supporting compression pointers.
