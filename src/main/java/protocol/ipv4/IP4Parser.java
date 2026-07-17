package protocol.ipv4;

import exception.IPv4ParsingException;
import protocol.common.ProtocolParser;
import io.ByteReader;

public class IP4Parser implements ProtocolParser<IPv4Packet> {

    @Override
    public IPv4Packet parse(ByteReader reader) throws IPv4ParsingException {
        if (reader.remaining() < 20) {
            throw new IPv4ParsingException("Insufficient bytes for IPv4 header: " + reader.remaining());
        }

        byte verAndIhl = reader.readByte();
        int version = (verAndIhl >> 4) & 0x0F;
        int ihl = verAndIhl & 0x0F;

        if (version != 4) {
            throw new IPv4ParsingException("Unsupported IP version: " + version);
        }
        if (ihl < 5) {
            throw new IPv4ParsingException("Invalid IHL: " + ihl);
        }

        byte tos = reader.readByte();
        int totalLength = Short.toUnsignedInt(reader.readShort());
        int identification = Short.toUnsignedInt(reader.readShort());
        int flagsAndOffset = Short.toUnsignedInt(reader.readShort());
        int ttl = Byte.toUnsignedInt(reader.readByte());
        int protocol = Byte.toUnsignedInt(reader.readByte());
        int headerChecksum = Short.toUnsignedInt(reader.readShort());

        byte[] srcIpBytes = reader.readBytes(4);
        byte[] destIpBytes = reader.readBytes(4);

        int headerBytesRead = ihl * 4;
        int optionsLength = headerBytesRead - 20;
        if (optionsLength > 0) {
            if (reader.remaining() < optionsLength) {
                throw new IPv4ParsingException("Insufficient bytes for IPv4 options: " + reader.remaining());
            }
            reader.skip(optionsLength);
        }

        int payloadLength = totalLength - headerBytesRead;
        if (payloadLength < 0) {
            payloadLength = reader.remaining();
        }
        if (reader.remaining() < payloadLength) {
            payloadLength = reader.remaining();
        }

        byte[] payload = reader.readBytes(payloadLength);

        return new IPv4Packet(
                srcIpBytes,
                destIpBytes,
                protocol,
                payload
        );
    }
}
