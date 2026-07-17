package registry;

import protocol.common.ProtocolParser;
import protocol.common.ProtocolUnit;
import java.util.HashMap;
import java.util.Map;

public class NetworkProtocolRegistry {
    private final Map<Integer, ProtocolParser<? extends ProtocolUnit>> parsers = new HashMap<>();

    public void register(int etherType, ProtocolParser<? extends ProtocolUnit> parser) {
        parsers.put(etherType, parser);
    }

    public ProtocolParser<? extends ProtocolUnit> getParser(int etherType) {
        return parsers.get(etherType);
    }
}
