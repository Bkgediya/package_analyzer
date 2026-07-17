package protocol.dns;

import exception.DnsParsingException;
import protocol.common.ProtocolParser;
import io.ByteReader;
import java.util.ArrayList;
import java.util.List;

public class DnsParser implements ProtocolParser<DnsMessage> {

    @Override
    public DnsMessage parse(ByteReader reader) throws DnsParsingException {
        if (reader.remaining() < 12) {
            throw new DnsParsingException("Insufficient bytes for DNS header: " + reader.remaining());
        }

        int transactionId = Short.toUnsignedInt(reader.readShort());
        int flags = Short.toUnsignedInt(reader.readShort());
        int questionsCount = Short.toUnsignedInt(reader.readShort());
        int answersCount = Short.toUnsignedInt(reader.readShort());
        int authorityCount = Short.toUnsignedInt(reader.readShort());
        int additionalCount = Short.toUnsignedInt(reader.readShort());

        List<String> queries = new ArrayList<>();
        try {
            for (int q = 0; q < questionsCount; q++) {
                String domain = parseDomainName(reader);
                int qType = Short.toUnsignedInt(reader.readShort());
                int qClass = Short.toUnsignedInt(reader.readShort());
                queries.add(domain);
            }
        } catch (Exception e) {
            throw new DnsParsingException("Failed to parse DNS query fields", e);
        }

        byte[] payload = reader.readBytes(reader.remaining());
        return new DnsMessage(
                transactionId,
                flags,
                queries,
                answersCount,
                payload
        );
    }

    private String parseDomainName(ByteReader reader) {
        StringBuilder sb = new StringBuilder();
        int len;
        while (reader.remaining() > 0 && (len = Byte.toUnsignedInt(reader.readByte())) != 0) {
            if ((len & 0xC0) == 0xC0) {
                // Compression pointer
                if (reader.remaining() > 0) {
                    reader.readByte(); // read offset byte
                }
                sb.append("[Compressed]");
                break;
            }
            if (reader.remaining() < len) {
                break;
            }
            byte[] labelBytes = reader.readBytes(len);
            sb.append(new String(labelBytes)).append(".");
        }
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '.') {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
}
