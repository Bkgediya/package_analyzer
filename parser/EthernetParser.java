package parser;

import constant.EthernetConstants;
import model.EthernetFrame;
import util.ByteReader;
import util.MacAddressFormatter;

public class EthernetParser implements ProtocolParser<EthernetFrame> {

    @Override
    public EthernetFrame parse(byte[] packetData) {
        ByteReader reader = new ByteReader(packetData);
        byte[] destinationBytes = reader.readBytes(EthernetConstants.DESTINATION_MAC_LENGTH);
        byte[] sourceBytes = reader.readBytes(EthernetConstants.SOURCE_MAC_LENGTH);
        int etherType = Short.toUnsignedInt(reader.readShort());
        byte[] payload = reader.readBytes(reader.remaining());

        return new EthernetFrame(MacAddressFormatter.format(destinationBytes),
            MacAddressFormatter.format(sourceBytes),
            etherType,
            payload
        );
    }
}   