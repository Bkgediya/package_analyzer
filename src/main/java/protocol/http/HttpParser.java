package protocol.http;

import protocol.common.ProtocolParser;
import io.ByteReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpParser implements ProtocolParser<HttpMessage> {

    @Override
    public HttpMessage parse(ByteReader reader) {
        byte[] bytes = reader.readBytes(reader.remaining());
        String text = new String(bytes, StandardCharsets.UTF_8);

        String[] lines = text.split("\r\n");
        if (lines.length == 0 || lines[0].isEmpty()) {
            return new HttpMessage("UNKNOWN", "UNKNOWN", new HashMap<>(), bytes);
        }

        String startLine = lines[0];
        String[] startParts = startLine.split(" ");
        String type = "UNKNOWN";
        String info = startLine;

        if (startParts.length >= 3) {
            if (startParts[0].startsWith("HTTP/")) {
                type = "RESPONSE";
                info = startParts[1] + " " + startParts[2];
            } else {
                type = "REQUEST";
                info = startParts[0] + " " + startParts[1];
            }
        }

        Map<String, String> headers = new HashMap<>();
        for (int i = 1; i < lines.length; i++) {
            if (lines[i].isEmpty()) {
                break;
            }
            int colonIdx = lines[i].indexOf(':');
            if (colonIdx > 0) {
                String key = lines[i].substring(0, colonIdx).trim();
                String val = lines[i].substring(colonIdx + 1).trim();
                headers.put(key, val);
            }
        }

        return new HttpMessage(type, info, headers, bytes);
    }
}
