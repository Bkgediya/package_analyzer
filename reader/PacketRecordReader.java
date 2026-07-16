public class PacketRecordReader {
    public PacketRecord read(ByteReader reader) {
        long timestampSeconds =
                        Integer.toUnsignedLong(reader.readInt());

        long timestampMicroseconds =
                Integer.toUnsignedLong(reader.readInt());

        int capturedLength =
                reader.readInt();

        int originalLength =
                reader.readInt();

        /*
         * Read the packet bytes
         */

        if(reader.remaining() < capturedLength){
             throw new PacketParsingException(
                "Packet is corrupted. Expected "
                + capturedLength
                + " bytes but only "
                + reader.remaining()
                + " bytes remain.");
        }

        byte[] packetData =
                reader.readBytes(capturedLength);

        return new PacketRecord(
                timestampSeconds,
                timestampMicroseconds,
                capturedLength,
                originalLength,
                packetData
        );
    }

}