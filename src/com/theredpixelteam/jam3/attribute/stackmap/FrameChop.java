package com.theredpixelteam.jam3.attribute.stackmap;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class FrameChop extends Frame {
    public FrameChop(@Nonnegative int tag,
                     @Nonnegative int offsetDelta)
    {
        super(tag, Type.CHOP);

        setOffsetDelta(offsetDelta);
    }

    public static @Nonnull FrameChop from(@Nonnull ConstantPool constantPool,
                                          @Nonnegative int typeTag,
                                          @Nonnull ByteBuffer byteBuffer)
    {
        int offsetDelta = byteBuffer.getShort() & 0xFFFF;

        return new FrameChop(typeTag, offsetDelta);
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

    public @Nonnegative int getLastAbsentLocal()
    {
        return 251 - getTypeTag();
    }

    public void setLastAbsentLocal(@Nonnegative int offset)
    {
        if (offset < 1 || offset > 3)
            throw new IllegalArgumentException("The offset of last absent local must be [1, 3]");

        setTypeTag(251 - offset);
    }

    private int offsetDelta;
}
