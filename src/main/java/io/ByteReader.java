package io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteReader {
    private final ByteBuffer buffer;

    public ByteReader(byte[] data) {
        this.buffer = ByteBuffer.wrap(data);
    }

    public void setByteOrder(ByteOrder order) {
        buffer.order(order);
    }

    public byte readByte() {
        return buffer.get();
    }

    public short readShort() {
        return buffer.getShort();
    }

    public int readInt() {
        return buffer.getInt();
    }

    public long readLong() {
        return buffer.getLong();
    }

    public byte[] readBytes(int length) {
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return bytes;
    }

    /**
     * Reads raw bytes without interpreting them.
     */
    public byte[] readRawBytes(int length) {
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return bytes;
    }

    public void skip(int bytes) {
        buffer.position(buffer.position() + bytes);
    }

    public int remaining() {
        return buffer.remaining();
    }

    public int position() {
        return buffer.position();
    }

}