package util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class EndianUtils {

    private EndianUtils() {
    }

    public static int toBigEndianInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes)
        .order(ByteOrder.BIG_ENDIAN)
        .getInt();
    }

    public static int toLittleEndianInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes)
        .order(ByteOrder.LITTLE_ENDIAN)
        .getInt();
    }

}