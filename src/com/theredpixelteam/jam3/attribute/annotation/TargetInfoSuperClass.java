package com.theredpixelteam.jam3.attribute.annotation;

import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class TargetInfoSuperClass extends TargetInfo {
    public TargetInfoSuperClass(@Nonnull Type type,
                                @Nonnegative int superClassIndex)
    {
        super(type);

        setSuperClassIndex(superClassIndex);
    }

    public static @Nonnull TargetInfoSuperClass from(@Nonnull Type type,
                                                     @Nonnull ByteBuffer byteBuffer)
    {
        int superClassIndex = byteBuffer.getShort() & 0xFFFF;

        return new TargetInfoSuperClass(type, superClassIndex);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[2];

        BigEndian.pushU2(byts, superClassIndex, 0);

        return byts;
    }

    public @Nonnegative int getSuperClassIndex()
    {
        return superClassIndex;
    }

    public void setSuperClassIndex(@Nonnegative int superClassIndex)
    {
        this.superClassIndex = superClassIndex;
    }

    private int superClassIndex;
}
