package reader;

import java.util.List;
import java.nio.file.Path;
import model.PacketRecord;

public interface PacketReader {
    List<PacketRecord> read(Path path);
}