package analyzer;

import model.ParsedPacket;

public interface ParsedPacketObserver {
    void onPacketParsed(ParsedPacket packet);
}
