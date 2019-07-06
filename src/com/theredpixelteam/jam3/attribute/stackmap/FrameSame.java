package com.theredpixelteam.jam3.attribute.stackmap;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class FrameSame extends Frame {
    public FrameSame(@Nonnegative int tag)
    {
        super(tag, Type.SAME);
    }

    public static @Nonnull FrameSame from(@Nonnull ConstantPool constantPool,
                                          @Nonnegative int typeTag,
                                          @Nonnull ByteBuffer byteBuffer)
    {
        return new FrameSame(typeTag);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[1];

        BigEndian.pushU1(byts, getTypeTag(), 0);

        return byts;
    }

    @Override
    public int getOffsetDelta()
    {
        return getTypeTag();
    }

    @Override
    public void setOffsetDelta(@Nonnegative int offsetDelta)
    {
        checkOffsetDeltaRange(offsetDelta, Type.SAME);

        setTypeTag(offsetDelta);
    }
}
