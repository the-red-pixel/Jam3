package com.theredpixelteam.jam3.attribute.annotation;

import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class TargetInfoEmpty extends TargetInfo {
    public TargetInfoEmpty(@Nonnull Type type)
    {
        super(type);
    }

    public static @Nonnull TargetInfoEmpty from(@Nonnull Type type,
                                                @Nonnull ByteBuffer byteBuffer)
    {
        return new TargetInfoEmpty(type);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        return new byte[0];
    }
}
