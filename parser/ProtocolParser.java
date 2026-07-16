package parser;

import util.ByteReader;


public interface ProtocolParser<T> {
    T parse(byte[] data);
}