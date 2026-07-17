package registry;

import protocol.common.ProtocolParser;
import protocol.common.ProtocolUnit;
import java.util.HashMap;
import java.util.Map;

public class TransportProtocolRegistry {
    private final Map<Integer, ProtocolParser<? extends ProtocolUnit>> parsers = new HashMap<>();

    public void register(int ipProtocol, ProtocolParser<? extends ProtocolUnit> parser) {
        parsers.put(ipProtocol, parser);
    }

    public ProtocolParser<? extends ProtocolUnit> getParser(int ipProtocol) {
        return parsers.get(ipProtocol);
    }
}
