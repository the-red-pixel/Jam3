package com.theredpixelteam.jam3;

import com.theredpixelteam.jam3.util.Misc;

import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public final class ClassNameReader {
    private ClassNameReader()
    {
    }

    public static @Nonnull String from(ByteBuffer byteBuffer)
    {
        Misc.checkMagicvalue(byteBuffer);

        byteBuffer.position(byteBuffer.position() + 4);

        int[] pos = Misc.quickPosConstants(byteBuffer);

        byteBuffer.position(byteBuffer.position() + 2);

        int thisClassTagRef = byteBuffer.getShort() & 0xFFFF;

        return Misc.classConstantAtPos(byteBuffer, pos, thisClassTagRef);
    }


}
