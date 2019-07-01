package com.theredpixelteam.jam3.util;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public final class MUTF8 {
    private MUTF8()
    {
    }

    public static @Nonnull byte[] toBytes(@Nonnull String string)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int strlen = string.length();
        for (int i = 0; i < strlen; i++)
        {
            char c = string.charAt(i);

            if ((c >= 0x0001) && (c <= 0x007F))
                baos.write(c & 0xFF);
            else if (c > 0x07FF)
            {
                baos.write(((c >> 12) & 0x0F) | 0xE0);
                baos.write(((c >>  6) & 0x3F) | 0x80);
                baos.write(((c      ) & 0x3F) | 0x80);
            }
            else
            {
                baos.write(((c >> 6) & 0x1F) | 0xC0);
                baos.write(((c     ) & 0x3F) | 0x80);
            }
        }

        return baos.toByteArray();
    }

    public static @Nonnull String toString(@Nonnull byte[] mutf8byts)
    {
        return toString(mutf8byts, mutf8byts.length);
    }

    public static @Nonnull String toString(@Nonnull byte[] mutf8byts,
                                           @Nonnegative int length)
    {
        return from(ByteBuffer.wrap(mutf8byts), length);
    }

    public static @Nonnull String from(@Nonnull ByteBuffer byteBuffer,
                                       @Nonnegative int length)
    {
        if (length > byteBuffer.remaining())
            throw new MUTF8MalformationException("Corrupt data length");

        if (length == 0 || !byteBuffer.hasRemaining())
            return "";

        char[] chars = new char[length];
        int charlen = 0;
        int rmnlen = length;

        while (rmnlen > 0)
        {
            byte x = byteBuffer.get();
            byte y;
            byte z;

            if ((x & 0x80) == 0)
            {
                chars[charlen++] = (char)(x & 0x7F);
                rmnlen--;

                continue;
            }

            if ((x & 0xE0) == 0xC0)
            {
                if (rmnlen < 2 || byteBuffer.remaining() < 2)
                    throw new MUTF8MalformationException("Unexpected data end");

                y = byteBuffer.get();

                if ((y & 0xC0) != 0x80)
                    throw new MUTF8MalformationException("Corrupt data around: " + (length - rmnlen));

                chars[charlen++] = (char)(((x & 0x1F) << 6) | (y & 0x3F));
                rmnlen -= 2;

                continue;
            }

            if ((x & 0xF0) == 0xE0)
            {
                if (rmnlen < 3 || byteBuffer.remaining() < 3)
                    throw new MUTF8MalformationException("Unexpected data end");

                y = byteBuffer.get();
                z = byteBuffer.get();

                if ((y & 0xC0) != 0x80 || (z & 0xC0) != 0x80)
                    throw new MUTF8MalformationException("Corrupt data around: " + (length - rmnlen));

                chars[charlen++] = (char)(((x & 0x0F) << 12) | ((y & 0x3F) << 6) | (z & 0x3F));
                rmnlen -= 3;

                continue;
            }

            throw new MUTF8MalformationException("Corrupt data round: " + (length - rmnlen)
                    + ", may be unsupported transformation format.");
        }

        return new String(chars, 0, charlen);
    }
}
