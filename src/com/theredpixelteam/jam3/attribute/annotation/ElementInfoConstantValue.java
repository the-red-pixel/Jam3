package com.theredpixelteam.jam3.attribute.annotation;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnull;
import java.nio.ByteBuffer;
import java.util.Objects;

public class ElementInfoConstantValue extends ElementInfo {
    public ElementInfoConstantValue(@Nonnull ConstantPool constantPool,
                                    @Nonnull ConstantPool.TagRef nameTagRef,
                                    @Nonnull Type type,
                                    @Nonnull ConstantPool.TagRef constantValueTagRef)
    {
        super(constantPool, nameTagRef, type);

        setConstantValueTagRef(constantValueTagRef);
    }

    public static @Nonnull ElementInfoConstantValue from(@Nonnull ConstantPool constantPool,
                                                         @Nonnull ConstantPool.TagRef nameTagRef,
                                                         @Nonnull Type type,
                                                         @Nonnull ByteBuffer byteBuffer)
    {
        ConstantPool.TagRef constantValueTagRef
                = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

        return new ElementInfoConstantValue(constantPool, nameTagRef, type, constantValueTagRef);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[2];

        BigEndian.pushU2(byts, constantValueTagRef.getRefIndex(), 0);

        return byts;
    }

    public @Nonnull ConstantPool.TagRef getConstantValueTagRef()
    {
        return constantValueTagRef;
    }

    public void setConstantValueTagRef(@Nonnull ConstantPool.TagRef constantValueTagRef)
    {
        this.constantValueTagRef = checkRef(Objects.requireNonNull(constantValueTagRef));
    }

    private ConstantPool.TagRef constantValueTagRef;
}
