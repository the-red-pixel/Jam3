package com.theredpixelteam.jam3.attribute.annotation;

import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class TargetInfoFormalParameter extends TargetInfo {
    public TargetInfoFormalParameter(@Nonnull Type type,
                                     @Nonnegative int formalParameterIndex)
    {
        super(type);

        setFormalParameterIndex(formalParameterIndex);
    }

    public static @Nonnull TargetInfoFormalParameter from(@Nonnull Type type,
                                                          @Nonnull ByteBuffer byteBuffer)
    {
        int formalParameterIndex = byteBuffer.get() & 0xFF;

        return new TargetInfoFormalParameter(type, formalParameterIndex);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[1];

        BigEndian.pushU1(byts, formalParameterIndex, 0);

        return byts;
    }

    public @Nonnegative int getFormalParameterIndex()
    {
        return formalParameterIndex;
    }

    public void setFormalParameterIndex(@Nonnegative int formalParameterIndex)
    {
        this.formalParameterIndex = formalParameterIndex;
    }

    private int formalParameterIndex;
}
