package protocol.ethernet;

public final class MacAddress {
    private final byte[] value;

    public MacAddress(byte[] value) {
        if (value == null || value.length != 6) {
            throw new IllegalArgumentException("MAC address must be exactly 6 bytes.");
        }
        this.value = value.clone();
    }

    public byte[] getValue() {
        return value.clone();
    }

    @Override
    public String toString() {
        return util.MacAddressFormatter.format(value);
    }
}
