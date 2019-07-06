package com.theredpixelteam.jam3.attribute.stackmap;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class VerificationTypeInfoUninitialized extends VerificationTypeInfo {
    public VerificationTypeInfoUninitialized(@Nonnull Type type,
                                             @Nonnegative int offset)
    {
        super(type);

        setOffset(offset);
    }

    public static @Nonnull VerificationTypeInfoUninitialized from(@Nonnull ConstantPool constantPool,
                                                                  @Nonnull Type type,
                                                                  @Nonnull ByteBuffer byteBuffer)
    {
        int offset = byteBuffer.getShort() & 0xFFFF;

        return new VerificationTypeInfoUninitialized(type, offset);
    }

    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[3];

        BigEndian.pushU1(byts, getType().getTag(), 0);
        BigEndian.pushU2(byts, offset, 1);

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
