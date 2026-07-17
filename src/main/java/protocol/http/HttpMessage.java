package protocol.http;

import protocol.common.ProtocolUnit;
import java.util.Map;
import java.util.HashMap;

public class HttpMessage implements ProtocolUnit {
    private final String type; // REQUEST or RESPONSE
    private final String info; // e.g. "GET /index.html" or "200 OK"
    private final Map<String, String> headers;
    private final byte[] rawPayload;

    public HttpMessage(String type, String info, Map<String, String> headers, byte[] rawPayload) {
        this.type = type;
        this.info = info;
        this.headers = new HashMap<>(headers);
        this.rawPayload = rawPayload.clone();
    }

    public String getType() {
        return type;
    }

    public String getInfo() {
        return info;
    }

    public Map<String, String> getHeaders() {
        return new HashMap<>(headers);
    }

    @Override
    public byte[] getPayload() {
        return rawPayload.clone();
    }

    @Override
    public String getProtocolName() {
        return "HTTP";
    }

    @Override
    public String toString() {
        return "HttpMessage{" +
                "type='" + type + '\'' +
                ", info='" + info + '\'' +
                ", headersCount=" + headers.size() +
                '}';
    }
}
