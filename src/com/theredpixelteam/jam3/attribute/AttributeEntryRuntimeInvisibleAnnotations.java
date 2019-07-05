package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.attribute.annotation.AnnotationInfo;
import com.theredpixelteam.jam3.constant.ConstantPool;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class AttributeEntryRuntimeInvisibleAnnotations extends AttributeEntryAnnotationsBase {
    public AttributeEntryRuntimeInvisibleAnnotations(@Nonnull AttributePool owner,
                                                     @Nonnegative int index,
                                                     @Nonnull ConstantPool constantPool,
                                                     @Nonnull ConstantPool.TagRef nameTagRef,
                                                     @Nonnull AnnotationInfo[] annotations)
    {
        super(NAME, owner, index, constantPool, nameTagRef, annotations);
    }

    public static @Nonnull AttributeEntryRuntimeInvisibleAnnotations from(@Nonnull AttributePool attributePool,
                                                                          @Nonnull ConstantPool constantPool,
                                                                          @Nonnegative int length,
                                                                          @Nonnull ConstantPool.TagRef nameTagRef,
                                                                          @Nonnull ByteBuffer byteBuffer)
    {
        return from(attributePool, constantPool, length,
                nameTagRef, byteBuffer, AttributeEntryRuntimeInvisibleAnnotations::new);
    }

    public static final String NAME = "RuntimeInvisibleAnnotations";
}
