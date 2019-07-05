package com.theredpixelteam.jam3.attribute.annotation;

import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class TargetInfoTypeParameterBound extends TargetInfo {
    public TargetInfoTypeParameterBound(@Nonnull Type type,
                                        @Nonnegative int typeParameterIndex,
                                        @Nonnegative int boundIndex)
    {
        super(type);

        setTypeParameterIndex(typeParameterIndex);
        setBoundIndex(boundIndex);
    }

    public static @Nonnull TargetInfoTypeParameterBound from(@Nonnull Type type,
                                                             @Nonnull ByteBuffer byteBuffer)
    {
        int typeParameterIndex = byteBuffer.get() & 0xFF;

        int boundIndex = byteBuffer.get() & 0xFF;

        return new TargetInfoTypeParameterBound(type, typeParameterIndex, boundIndex);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[2];

        BigEndian.pushU1(byts, typeParameterIndex, 0);
        BigEndian.pushU1(byts, boundIndex, 1);

        return byts;
    }

    public @Nonnegative int getTypeParameterIndex()
    {
        return typeParameterIndex;
    }

    public void setTypeParameterIndex(@Nonnegative int typeParameterIndex)
    {
        this.typeParameterIndex = typeParameterIndex;
    }

    public @Nonnegative int getBoundIndex()
    {
        return boundIndex;
    }

    public void setBoundIndex(@Nonnegative int boundIndex)
    {
        this.boundIndex = boundIndex;
    }

    private int typeParameterIndex;

    private int boundIndex;
}
