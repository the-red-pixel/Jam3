package com.theredpixelteam.jam3.attribute.annotation;

import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class TargetInfoTypeArgument extends TargetInfo {
    public TargetInfoTypeArgument(@Nonnull Type type,
                                  @Nonnegative int offset,
                                  @Nonnegative int typeArgumentIndex)
    {
        super(type);

        setOffset(offset);
        setTypeArgumentIndex(typeArgumentIndex);
    }

    public static @Nonnull TargetInfoTypeArgument from(@Nonnull Type type,
                                                       @Nonnull ByteBuffer byteBuffer)
    {
        int offset = byteBuffer.getShort() & 0xFFFF;

        int typeArgumentIndex = byteBuffer.get() & 0xFF;

        return new TargetInfoTypeArgument(type, offset, typeArgumentIndex);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[3];

        BigEndian.pushU2(byts, offset, 0);
        BigEndian.pushU1(byts, typeArgumentIndex, 2);

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

    public @Nonnegative int getTypeArgumentIndex()
    {
        return typeArgumentIndex;
    }

    public void setTypeArgumentIndex(@Nonnegative int typeArgumentIndex)
    {
        this.typeArgumentIndex = typeArgumentIndex;
    }

    private int offset;

    private int typeArgumentIndex;
}
