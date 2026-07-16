package model;

public interface ProtocolUnit {
    /**
     * Returns the payload that should be passed to the
     * next protocol parser.
     */
    byte[] getPayload();
}