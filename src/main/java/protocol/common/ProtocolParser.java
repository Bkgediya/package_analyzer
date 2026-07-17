package protocol.common;

import exception.PacketParsingException;
import io.ByteReader;

public interface ProtocolParser<T extends ProtocolUnit> {
    T parse(ByteReader reader) throws PacketParsingException;
}