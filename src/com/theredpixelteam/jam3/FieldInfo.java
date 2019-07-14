package com.theredpixelteam.jam3;

import com.theredpixelteam.jam3.attribute.AttributePool;
import com.theredpixelteam.jam3.constant.ConstantPool;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class FieldInfo extends AccessibleObjectInfoBase {
    public FieldInfo(@Nonnull ConstantPool constantPool,
                     @Nonnegative int modifier,
                     @Nonnull ConstantPool.TagRef nameTagRef,
                     @Nonnull ConstantPool.TagRef descriptorTagRef,
                     @Nonnull AttributePool attributes)
    {
        super(constantPool, modifier, nameTagRef, descriptorTagRef, attributes);
    }

    public static @Nonnull FieldInfo from(@Nonnull ConstantPool constantPool,
                                          @Nonnull ByteBuffer byteBuffer,
                                          boolean skipAttributes)
    {
        return from(constantPool, byteBuffer, skipAttributes, FieldInfo::new);
    }
}
