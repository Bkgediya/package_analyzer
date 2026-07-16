package analyzer;

import model.PacketRecord;
public interface PacketProcessor {
    void process(PacketRecord packet);
}