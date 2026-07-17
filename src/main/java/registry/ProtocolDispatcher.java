package registry;

import protocol.common.ProtocolParser;
import protocol.common.ProtocolUnit;
import java.util.HashMap;
import java.util.Map;

public class ProtocolDispatcher {
    private final Map<Integer, ProtocolParser<? extends ProtocolUnit>> parsers = new HashMap<>();

    public void register(int protocolId, ProtocolParser<? extends ProtocolUnit> parser) {
        parsers.put(protocolId, parser);
    }

    public ProtocolParser<? extends ProtocolUnit> getParser(int protocolId) {
        return parsers.get(protocolId);
    }
}