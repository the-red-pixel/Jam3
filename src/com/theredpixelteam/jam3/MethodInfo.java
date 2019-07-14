package com.theredpixelteam.jam3;

import com.theredpixelteam.jam3.attribute.AttributePool;
import com.theredpixelteam.jam3.constant.ConstantPool;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class MethodInfo extends AccessibleObjectInfoBase {
    public MethodInfo(@Nonnull ConstantPool constantPool,
                      @Nonnegative int modifier,
                      @Nonnull ConstantPool.TagRef nameTagRef,
                      @Nonnull ConstantPool.TagRef descriptorTagRef,
                      @Nonnull AttributePool attributes)
    {
        super(constantPool, modifier, nameTagRef, descriptorTagRef, attributes);
    }

    public static @Nonnull MethodInfo from(@Nonnull ConstantPool constantPool,
                                           @Nonnull ByteBuffer byteBuffer)
    {
        return from(constantPool, byteBuffer);
    }

    public static @Nonnull MethodInfo from(@Nonnull ConstantPool constantPool,
                                           @Nonnull ByteBuffer byteBuffer,
                                           boolean skipAttributes)
    {
        return from(constantPool, byteBuffer, skipAttributes, MethodInfo::new);
    }
}
