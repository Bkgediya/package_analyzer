public interface PacketReader {
    List<PacketRecord> read(Path path);
}