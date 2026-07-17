package io;

import exception.PacketParsingException;
import model.PacketRecord;
import io.ByteReader;

public class PacketRecordReader {
        public PacketRecord read(ByteReader reader) throws PacketParsingException {
                long timestampSeconds = Integer.toUnsignedLong(reader.readInt());
                long timestampMicroseconds = Integer.toUnsignedLong(reader.readInt());
                int capturedLength = reader.readInt();
                int originalLength = reader.readInt();

                /*
                 * Read the packet bytes
                 */

                if (reader.remaining() < capturedLength) {
                        throw new PacketParsingException(
                                        "Packet is corrupted. Expected "
                                                        + capturedLength
                                                        + " bytes but only "
                                                        + reader.remaining()
                                                        + " bytes remain.");
                }

                byte[] packetData = reader.readBytes(capturedLength);

                return new PacketRecord(
                                timestampSeconds,
                                timestampMicroseconds,
                                capturedLength,
                                originalLength,
                                packetData);
        }

}