package reader;

import exception.InvalidPcapFileException;
import io.ByteReader;
import util.EndianUtils;

import java.nio.ByteOrder;

public class MagicNumberDetector {
    private static final int MAGIC_BIG_ENDIAN = 0xA1B2C3D4;

    public ByteOrder detect(ByteReader reader) throws InvalidPcapFileException {

        // Read the first 4 bytes (magic number)
        byte[] magicBytes = reader.readRawBytes(4);

        // Try interpreting the bytes as Big Endian
        int bigEndianMagic = EndianUtils.toBigEndianInt(magicBytes);

        // Try interpreting the same bytes as Little Endian
        int littleEndianMagic = EndianUtils.toLittleEndianInt(magicBytes);

        // Determine the correct byte order
        if (bigEndianMagic == MAGIC_BIG_ENDIAN) {
            return ByteOrder.BIG_ENDIAN;
        } else if (littleEndianMagic == MAGIC_BIG_ENDIAN) {
            return ByteOrder.LITTLE_ENDIAN;
        }

        throw new InvalidPcapFileException(
                "Invalid PCAP file. Unknown magic number.");
    }

}