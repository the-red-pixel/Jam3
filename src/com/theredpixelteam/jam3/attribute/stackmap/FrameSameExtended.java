package com.theredpixelteam.jam3.attribute.stackmap;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class FrameSameExtended extends Frame {
    public FrameSameExtended(@Nonnegative int tag,
                             @Nonnegative int offsetDelta)
    {
        super(tag, Type.SAME_EXTENDED);

        setOffsetDelta(offsetDelta);
    }

    public static @Nonnull FrameSameExtended from(@Nonnull ConstantPool constantPool,
                                                  @Nonnegative int typeTag,
                                                  @Nonnull ByteBuffer byteBuffer)
    {
        int offsetDelta = byteBuffer.getShort() & 0xFFFF;

        return new FrameSameExtended(typeTag, offsetDelta);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[3];

        BigEndian.pushU1(byts, getTypeTag(), 0);

        BigEndian.pushU2(byts, offsetDelta, 1);

        return byts;
    }

    @Override
    public @Nonnegative int getOffsetDelta()
    {
        return offsetDelta;
    }

    @Override
    public void setOffsetDelta(@Nonnegative int offsetDelta)
    {
        this.offsetDelta = offsetDelta;
    }

    private int offsetDelta;
}
