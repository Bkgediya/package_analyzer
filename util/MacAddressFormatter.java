package util;
public final class MacAddressFormatter {

    private MacAddressFormatter() {
    }

    public static String format(byte[] macAddress) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < macAddress.length; i++) {
            builder.append(
                    String.format("%02X", macAddress[i]));
            if (i < macAddress.length - 1) {
                builder.append(":");
            }
        }

        return builder.toString();
    }
}