package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.attribute.annotation.TypeAnnotationInfo;
import com.theredpixelteam.jam3.constant.ConstantPool;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class AttributeEntryRuntimeInvisibleTypeAnnotations extends AttributeEntryTypeAnnotationsBase {
    public AttributeEntryRuntimeInvisibleTypeAnnotations(@Nonnull AttributePool owner,
                                                         @Nonnegative int index,
                                                         @Nonnull ConstantPool constantPool,
                                                         @Nonnull ConstantPool.TagRef nameTagRef,
                                                         @Nonnull TypeAnnotationInfo[] annotations)
    {
        super(NAME, owner, index, constantPool, nameTagRef, annotations);
    }

    public static @Nonnull AttributeEntryRuntimeInvisibleTypeAnnotations from(@Nonnull AttributePool attributePool,
                                                                              @Nonnull ConstantPool constantPool,
                                                                              @Nonnegative int length,
                                                                              @Nonnull ConstantPool.TagRef nameTagRef,
                                                                              @Nonnull ByteBuffer byteBuffer)
    {
        return from(attributePool, constantPool, length,
                nameTagRef, byteBuffer, AttributeEntryRuntimeInvisibleTypeAnnotations::new);
    }

    public static final String NAME = "RuntimeInvisibleTypeAnnotations";
}
