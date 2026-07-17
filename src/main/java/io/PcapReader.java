package io;

import analyzer.PacketProcessor;
import model.PcapGlobalHeader;
import model.PacketRecord;
import io.ByteReader;

import constant.PcapConstants;
import exception.InvalidPcapFileException;
import exception.PacketParsingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PcapReader {
    private final GlobalHeaderReader globalHeaderReader;
    private final PacketRecordReader packetRecordReader;

    public PcapReader() {
        this.globalHeaderReader = new GlobalHeaderReader();
        this.packetRecordReader = new PacketRecordReader();
    }

    public void read(Path file, PacketProcessor processor)
            throws IOException, InvalidPcapFileException, PacketParsingException {
        byte[] fileBytes = Files.readAllBytes(file);
        ByteReader reader = new ByteReader(fileBytes);

        // Read the global header
        PcapGlobalHeader globalHeader = globalHeaderReader.read(reader);
        System.out.println(globalHeader);

        // Read packet records
        while (reader.remaining() >= PcapConstants.PACKET_HEADER_SIZE) {
            PacketRecord packetRecord = packetRecordReader.read(reader);
            processor.process(packetRecord);
        }
    }
}