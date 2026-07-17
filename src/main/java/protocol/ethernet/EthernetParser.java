package protocol.ethernet;

import exception.EthernetParsingException;
import protocol.common.ProtocolParser;
import io.ByteReader;

public class EthernetParser implements ProtocolParser<EthernetFrame> {

    @Override
    public EthernetFrame parse(ByteReader reader) throws EthernetParsingException {
        if (reader.remaining() < EthernetHeaderConstants.HEADER_LENGTH) {
            throw new EthernetParsingException(
                    "Insufficient bytes for Ethernet header. Expected "
                            + EthernetHeaderConstants.HEADER_LENGTH
                            + " but only " + reader.remaining() + " bytes remain.");
        }

        byte[] destinationBytes = reader.readBytes(EthernetHeaderConstants.MAC_ADDRESS_LENGTH);
        byte[] sourceBytes = reader.readBytes(EthernetHeaderConstants.MAC_ADDRESS_LENGTH);
        int etherType = Short.toUnsignedInt(reader.readShort());
        byte[] payload = reader.readBytes(reader.remaining());

        return new EthernetFrame(
                new MacAddress(destinationBytes),
                new MacAddress(sourceBytes),
                etherType,
                payload);
    }
}