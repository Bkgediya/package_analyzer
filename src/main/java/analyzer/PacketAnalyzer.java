package analyzer;

import exception.PacketParsingException;
import model.PacketRecord;
import model.ParsedPacket;
import pipeline.ProtocolPipeline;
import java.util.ArrayList;
import java.util.List;

public class PacketAnalyzer implements PacketProcessor {
    private final ProtocolPipeline pipeline;
    private final List<ParsedPacketObserver> observers = new ArrayList<>();

    public PacketAnalyzer(ProtocolPipeline pipeline) {
        this.pipeline = pipeline;
    }

    public void addObserver(ParsedPacketObserver observer) {
        observers.add(observer);
    }

    @Override
    public void process(PacketRecord packet) {
        try {
            ParsedPacket parsedPacket = pipeline.analyze(packet);
            System.out.println("Parsed Packet: " + parsedPacket);
            for (ParsedPacketObserver observer : observers) {
                observer.onPacketParsed(parsedPacket);
            }
        } catch (PacketParsingException e) {
            System.err.println("Error parsing packet: " + e.getMessage());
        }
    }
}
