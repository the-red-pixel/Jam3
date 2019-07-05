package com.theredpixelteam.jam3.attribute.annotation;

import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class TargetInfoCatch extends TargetInfo {
    public TargetInfoCatch(@Nonnull Type type,
                           @Nonnegative int exceptionTableIndex)
    {
        super(type);

        setExceptionTableIndex(exceptionTableIndex);
    }

    public static @Nonnull TargetInfoCatch from(@Nonnull Type type,
                                                @Nonnull ByteBuffer byteBuffer)
    {
        int exceptionTableIndex = byteBuffer.getShort() & 0xFFFF;

        return new TargetInfoCatch(type, exceptionTableIndex);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[2];

        BigEndian.pushU2(byts, exceptionTableIndex, 0);

        return byts;
    }

    public @Nonnegative int getExceptionTableIndex()
    {
        return exceptionTableIndex;
    }

    public void setExceptionTableIndex(@Nonnegative int exceptionTableIndex)
    {
        this.exceptionTableIndex = exceptionTableIndex;
    }

    private int exceptionTableIndex;
}
