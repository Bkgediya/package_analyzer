package model;
public class PcapGlobalHeader {
    private final int magicNumber;
    private final short versionMajor;
    private final short versionMinor;
    private final int thisZone;
    private final int sigFigs;
    private final int snapLength;
    private final int network; // represent network layer type 

    public PcapGlobalHeader(
        int magicNumber,
        short versionMajor,
        short versionMinor,
        int thisZone,
        int sigFigs,
        int snapLength,
        int network) {

        this.magicNumber = magicNumber;
        this.versionMajor = versionMajor;
        this.versionMinor = versionMinor;
        this.thisZone = thisZone;
        this.sigFigs = sigFigs;
        this.snapLength = snapLength;
        this.network = network;
    }

    public int getMagicNumber() {
        return magicNumber;
    }

    public short getVersionMajor() {
        return versionMajor;
    }

    public short getVersionMinor() {
        return versionMinor;
    }

    public int getSnapLength() {
        return snapLength;
    }

    public int getNetwork() {
        return network;
    }

    @Override
    public String toString() {
    return "PcapGlobalHeader{" +
        "magicNumber=" + magicNumber +
        ", versionMajor=" + versionMajor +
        ", versionMinor=" + versionMinor +
        ", thisZone=" + thisZone +
        ", sigFigs=" + sigFigs +
        ", snapLength=" + snapLength +
        ", network=" + network +
        '}';
    }
}