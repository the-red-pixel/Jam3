package com.theredpixelteam.jam3.util;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public final class Misc {
    private Misc()
    {
    }

    public static void checkMagicvalue(@Nonnull ByteBuffer byteBuffer)
    {
        if (byteBuffer.getInt() != 0xCAFEBABE)
            throw new ClassFormatError("Not a class file (Corrupt header)");
    }

    public static @Nonnull String classConstantAtPos(@Nonnull ByteBuffer byteBuffer,
                                                     @Nonnull int[] pos,
                                                     @Nonnegative int index)
    {
        if (index == 0)
            return "java/lang/Object";

        int oldPos = byteBuffer.position();

        byteBuffer.position(pos[index - 1]);

        if ((byteBuffer.get() & 0xFF) != 7)
            throw new ClassFormatError("Corrupt constant pool");

        int utf8TagIndex = byteBuffer.getShort() & 0xFFFF;

        String str = stringConstantAtPos(byteBuffer, pos, utf8TagIndex);

        byteBuffer.position(oldPos);

        return str;
    }

    public static @Nonnull String stringConstantAtPos(@Nonnull ByteBuffer byteBuffer,
                                                      @Nonnull int[] pos,
                                                      @Nonnegative int index)
    {
        int oldPos = byteBuffer.position();

        byteBuffer.position(pos[index - 1]);

        if ((byteBuffer.get() & 0xFF) != 1)
            throw new ClassFormatError("Corrupt constant pool");

        int length = byteBuffer.getShort() & 0xFFFF;
        byte[] byts = new byte[length];

        byteBuffer.get(byts);

        byteBuffer.position(oldPos);

        return MUTF8.toString(byts);
    }

    public static int[] quickPosConstants(@Nonnull ByteBuffer byteBuffer)
    {
        int constantTagCount = (byteBuffer.getShort() & 0xFFFF) - 1;

        int[] pos = new int[constantTagCount];

        for (int i = 0; i < constantTagCount; i++)
        {
            pos[i] = byteBuffer.position();

            int tag;
            switch (tag = (byteBuffer.get() & 0xFF))
            {
                case 1:
                    byteBuffer.position((byteBuffer.getShort() & 0xFFFF) + byteBuffer.position());
                    break;

                case 3:
                case 4:
                case 9:
                case 10:
                case 11:
                case 12:
                case 18:
                    byteBuffer.position(byteBuffer.position() + 4);
                    break;

                case 5:
                case 6:
                    byteBuffer.position(byteBuffer.position() + 8);
                    i++;
                    break;

                case 7:
                case 8:
                case 16:
                    byteBuffer.position(byteBuffer.position() + 2);
                    break;

                case 15:
                    byteBuffer.position(byteBuffer.position() + 3);
                    break;

                default:
                    throw new ClassFormatError("Unknwon tag: " + tag);
            }
        }

        return pos;
    }
}
