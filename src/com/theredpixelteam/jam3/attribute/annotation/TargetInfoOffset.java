package com.theredpixelteam.jam3.attribute.annotation;

import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class TargetInfoOffset extends TargetInfo {
    public TargetInfoOffset(@Nonnull Type type,
                            @Nonnegative int offset)
    {
        super(type);

        setOffset(offset);
    }

    public static @Nonnull TargetInfoOffset from(@Nonnull Type type,
                                                 @Nonnull ByteBuffer byteBuffer)
    {
        int offset = byteBuffer.getShort() & 0xFFFF;

        return new TargetInfoOffset(type, offset);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[2];

        BigEndian.pushU2(byts, offset, 0);

        return byts;
    }

    public @Nonnegative int getOffset()
    {
        return offset;
    }

    public void setOffset(@Nonnegative int offset)
    {
        this.offset = offset;
    }

    private int offset;
}
