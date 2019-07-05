package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.attribute.annotation.AnnotationInfo;
import com.theredpixelteam.jam3.constant.ConstantPool;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class AttributeEntryRuntimeInvisibleParameterAnnotations extends AttributeEntryParameterAnnotationsBase {
    public AttributeEntryRuntimeInvisibleParameterAnnotations(@Nonnull AttributePool owner,
                                                              @Nonnegative int index,
                                                              @Nonnull ConstantPool constantPool,
                                                              @Nonnull ConstantPool.TagRef nameTagRef,
                                                              @Nonnull AnnotationInfo[][] parameterAnnotations)
    {
        super(NAME, owner, index, constantPool, nameTagRef, parameterAnnotations);
    }

    public static @Nonnull AttributeEntryRuntimeInvisibleParameterAnnotations from(@Nonnull AttributePool attributePool,
                                                                                   @Nonnull ConstantPool constantPool,
                                                                                   @Nonnegative int length,
                                                                                   @Nonnull ConstantPool.TagRef nameTagRef,
                                                                                   @Nonnull ByteBuffer byteBuffer)
    {
        return from(attributePool, constantPool, length,
                nameTagRef, byteBuffer, AttributeEntryRuntimeInvisibleParameterAnnotations::new);
    }

    public static final String NAME = "RuntimeInvisibleParameterAnnotations";
}
