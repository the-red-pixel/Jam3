package com.theredpixelteam.jam3.attribute.annotation;

import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class TargetInfoThrows extends TargetInfo {
    public TargetInfoThrows(@Nonnull Type type,
                            @Nonnegative int throwsTypeIndex)
    {
        super(type);

        setThrowsTypeIndex(throwsTypeIndex);
    }

    public static @Nonnull TargetInfoThrows from(@Nonnull Type type,
                                                 @Nonnull ByteBuffer byteBuffer)
    {
        int throwsTypeIndex = byteBuffer.getShort() & 0xFFFF;

        return new TargetInfoThrows(type, throwsTypeIndex);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[2];

        BigEndian.pushU2(byts, throwsTypeIndex, 0);

        return byts;
    }

    public @Nonnegative int getThrowsTypeIndex()
    {
        return throwsTypeIndex;
    }

    public void setThrowsTypeIndex(@Nonnegative int throwsTypeIndex)
    {
        this.throwsTypeIndex = throwsTypeIndex;
    }

    private int throwsTypeIndex;
}
