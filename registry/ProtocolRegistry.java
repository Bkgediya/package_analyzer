package registry;

import parser.ProtocolParser;

import java.util.HashMap;
import java.util.Map;

public class ProtocolRegistry {
    private final Map<Integer, ProtocolParser<?>> parsers = new HashMap<>();
    public ProtocolRegistry() {
        this.protocolParsers = new HashMap<>();
    }

    public void register(int protocolId, ProtocolParser<?> parser) {
        parsers.put(protocolId, parser);
    }

    public ProtocolParser<?> getParser(int protocolId) {
        return parsers.get(protocolId);
    }

}