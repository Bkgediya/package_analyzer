package registry;

import protocol.common.ProtocolParser;
import protocol.common.ProtocolUnit;
import java.util.HashMap;
import java.util.Map;

public class ApplicationProtocolRegistry {
    private final Map<Integer, ProtocolParser<? extends ProtocolUnit>> parsers = new HashMap<>();

    public void register(int port, ProtocolParser<? extends ProtocolUnit> parser) {
        parsers.put(port, parser);
    }

    public ProtocolParser<? extends ProtocolUnit> getParser(int port) {
        return parsers.get(port);
    }
}
