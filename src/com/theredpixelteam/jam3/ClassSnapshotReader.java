package com.theredpixelteam.jam3;

import com.theredpixelteam.jam3.util.Misc;

import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public final class ClassSnapshotReader {
    private ClassSnapshotReader()
    {
    }

    public static @Nonnull ClassSnapshot from(@Nonnull ByteBuffer byteBuffer)
    {
        return from(byteBuffer, 0);
    }

    public static @Nonnull ClassSnapshot from(@Nonnull ByteBuffer byteBuffer,
                                              int options)
    {
        Misc.checkMagicvalue(byteBuffer);

        int minorVersion = byteBuffer.getShort() & 0xFFFF;
        int majorVersion = byteBuffer.getShort() & 0xFFFF;

        int[] pos = Misc.quickPosConstants(byteBuffer);

        int modifiers = byteBuffer.getShort() & 0xFFFF;

        String className = Misc.classConstantAtPos(byteBuffer, pos, byteBuffer.getShort() & 0xFFFF);
        String superClassName = Misc.classConstantAtPos(byteBuffer, pos, byteBuffer.getShort() & 0xFFFF);

        int interfaceCount = byteBuffer.getShort() & 0xFFFF;

        String[] interfaceNames;

        if (is(options, SKIP_INTERFACE_NAMES))
            interfaceNames = new String[0];
        else
        {
            interfaceNames = new String[interfaceCount];
            for (int i = 0; i < interfaceCount; i++)
                interfaceNames[i] = Misc.classConstantAtPos(byteBuffer, pos, byteBuffer.getShort() & 0xFFFF);
        }

        return new ClassSnapshot(minorVersion, majorVersion, modifiers, className, superClassName, interfaceNames);
    }

    private static boolean is(int options, int bit)
    {
        return (options & bit) != 0;
    }

    public static final int SKIP_INTERFACE_NAMES        = 0b00000001;
}
