package io;

import model.PcapGlobalHeader;
import exception.InvalidPcapFileException;
import io.ByteReader;
import reader.MagicNumberDetector;

import java.nio.ByteOrder;

public class GlobalHeaderReader {
    private final MagicNumberDetector detector;

    public GlobalHeaderReader() {
        this.detector = new MagicNumberDetector();
    }

    public PcapGlobalHeader read(ByteReader reader) throws InvalidPcapFileException {
        // detect the byte order of the file
        ByteOrder byteOrder = detector.detect(reader);
        // set the byte order for the reader
        reader.setByteOrder(byteOrder);

        short versionMajor = reader.readShort();
        short versionMinor = reader.readShort();

        int thisZone = reader.readInt();
        int sigFigs = reader.readInt();
        int snapLength = reader.readInt();
        int network = reader.readInt();
        int magicNumber = (byteOrder == ByteOrder.BIG_ENDIAN)
                ? 0xA1B2C3D4
                : 0xD4C3B2A1;

        return new PcapGlobalHeader(
                magicNumber,
                versionMajor,
                versionMinor,
                thisZone,
                sigFigs,
                snapLength,
                network);
    }

}