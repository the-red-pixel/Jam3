package com.theredpixelteam.jam3.util;

public final class BigEndian {
    private BigEndian()
    {
    }

    public static void pushU1(byte[] byts, int u1value, int off)
    {
        byts[off] = (byte)(u1value & 0xFF);
    }

    public static void pushU2(byte[] byts, int u2value, int off)
    {
        byts[off] =     (byte)((u2value & 0xFF00) >>> 8);
        byts[off + 1] = (byte)( u2value & 0x00FF);
    }

    public static void pushU4(byte[] byts, int u4value, int off)
    {
        byts[off]     = (byte)((u4value & 0xFF000000) >>> 24);
        byts[off + 1] = (byte)((u4value & 0x00FF0000) >>> 16);
        byts[off + 2] = (byte)((u4value & 0x0000FF00) >>> 8);
        byts[off + 3] = (byte)( u4value & 0x000000FF);
    }

    public static void pushU8(byte[] byts, long u8value, int off)
    {
        byts[off]     = (byte)((u8value & 0xFF00000000000000L) >>> 56);
        byts[off + 1] = (byte)((u8value & 0x00FF000000000000L) >>> 48);
        byts[off + 2] = (byte)((u8value & 0x0000FF0000000000L) >>> 40);
        byts[off + 3] = (byte)((u8value & 0x000000FF00000000L) >>> 32);
        byts[off + 4] = (byte)((u8value & 0x00000000FF000000L) >>> 24);
        byts[off + 5] = (byte)((u8value & 0x0000000000FF0000L) >>> 16);
        byts[off + 6] = (byte)((u8value & 0x000000000000FF00L) >>> 8);
        byts[off + 7] = (byte)( u8value & 0x00000000000000FFL);
    }
}
