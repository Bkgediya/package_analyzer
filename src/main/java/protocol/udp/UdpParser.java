package protocol.udp;

import exception.UdpParsingException;
import protocol.common.ProtocolParser;
import io.ByteReader;

public class UdpParser implements ProtocolParser<UdpDatagram> {

    @Override
    public UdpDatagram parse(ByteReader reader) throws UdpParsingException {
        if (reader.remaining() < 8) {
            throw new UdpParsingException("Insufficient bytes for UDP header: " + reader.remaining());
        }

        int srcPort = Short.toUnsignedInt(reader.readShort());
        int destPort = Short.toUnsignedInt(reader.readShort());
        int length = Short.toUnsignedInt(reader.readShort());
        int checksum = Short.toUnsignedInt(reader.readShort());

        int payloadLength = length - 8;
        if (payloadLength < 0) {
            payloadLength = reader.remaining();
        }
        if (reader.remaining() < payloadLength) {
            payloadLength = reader.remaining();
        }

        byte[] payload = reader.readBytes(payloadLength);

        return new UdpDatagram(
                srcPort,
                destPort,
                payload
        );
    }
}
