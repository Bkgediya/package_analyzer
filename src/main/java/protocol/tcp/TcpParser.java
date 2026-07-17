package protocol.tcp;

import exception.TcpParsingException;
import protocol.common.ProtocolParser;
import io.ByteReader;

public class TcpParser implements ProtocolParser<TcpSegment> {

    @Override
    public TcpSegment parse(ByteReader reader) throws TcpParsingException {
        if (reader.remaining() < 20) {
            throw new TcpParsingException("Insufficient bytes for TCP header: " + reader.remaining());
        }

        int srcPort = Short.toUnsignedInt(reader.readShort());
        int destPort = Short.toUnsignedInt(reader.readShort());
        long seqNum = Integer.toUnsignedLong(reader.readInt());
        long ackNum = Integer.toUnsignedLong(reader.readInt());

        int dataOffsetAndReserved = Short.toUnsignedInt(reader.readShort());
        int dataOffset = (dataOffsetAndReserved >> 12) & 0x0F;
        int flags = dataOffsetAndReserved & 0x01FF;

        int windowSize = Short.toUnsignedInt(reader.readShort());
        int checksum = Short.toUnsignedInt(reader.readShort());
        int urgentPointer = Short.toUnsignedInt(reader.readShort());

        int headerBytesRead = dataOffset * 4;
        if (headerBytesRead < 20) {
            throw new TcpParsingException("Invalid TCP data offset: " + dataOffset);
        }

        int optionsLength = headerBytesRead - 20;
        if (optionsLength > 0) {
            if (reader.remaining() < optionsLength) {
                throw new TcpParsingException("Insufficient bytes for TCP options: " + reader.remaining());
            }
            reader.skip(optionsLength);
        }

        byte[] payload = reader.readBytes(reader.remaining());

        return new TcpSegment(
                srcPort,
                destPort,
                seqNum,
                ackNum,
                flags,
                payload
        );
    }
}
