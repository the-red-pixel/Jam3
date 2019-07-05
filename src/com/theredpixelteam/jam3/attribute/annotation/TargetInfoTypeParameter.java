package com.theredpixelteam.jam3.attribute.annotation;

import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class TargetInfoTypeParameter extends TargetInfo {
    public TargetInfoTypeParameter(@Nonnull Type type,
                                   @Nonnegative int typeParameterIndex)
    {
        super(type);

        setTypeParameterIndex(typeParameterIndex);
    }

    public static @Nonnull TargetInfoTypeParameter from(@Nonnull Type type,
                                                        @Nonnull ByteBuffer byteBuffer)
    {
        int typeParameterIndex = byteBuffer.get() & 0xFF;

        return new TargetInfoTypeParameter(type, typeParameterIndex);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[1];

        BigEndian.pushU1(byts, typeParameterIndex, 0);

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

    private int typeParameterIndex;
}
