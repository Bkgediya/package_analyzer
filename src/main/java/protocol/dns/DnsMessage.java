package protocol.dns;

import protocol.common.ProtocolUnit;
import java.util.ArrayList;
import java.util.List;

public class DnsMessage implements ProtocolUnit {
    private final int transactionId;
    private final int flags;
    private final List<String> queries;
    private final int answersCount;
    private final byte[] payload;

    public DnsMessage(int transactionId, int flags, List<String> queries, int answersCount, byte[] payload) {
        this.transactionId = transactionId;
        this.flags = flags;
        this.queries = new ArrayList<>(queries);
        this.answersCount = answersCount;
        this.payload = payload.clone();
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getFlags() {
        return flags;
    }

    public List<String> getQueries() {
        return new ArrayList<>(queries);
    }

    public int getAnswersCount() {
        return answersCount;
    }

    @Override
    public byte[] getPayload() {
        return payload.clone();
    }

    @Override
    public String getProtocolName() {
        return "DNS";
    }

    @Override
    public String toString() {
        return "DnsMessage{" +
                "transactionId=0x" + Integer.toHexString(transactionId).toUpperCase() +
                ", flags=0x" + Integer.toHexString(flags).toUpperCase() +
                ", queries=" + queries +
                ", answersCount=" + answersCount +
                '}';
    }
}
