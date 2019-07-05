package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.attribute.annotation.TypeAnnotationInfo;
import com.theredpixelteam.jam3.constant.ConstantPool;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class AttributeEntryRuntimeVisibleTypeAnnotations extends AttributeEntryTypeAnnotationsBase {
    public AttributeEntryRuntimeVisibleTypeAnnotations(@Nonnull AttributePool owner,
                                                       @Nonnegative int index,
                                                       @Nonnull ConstantPool constantPool,
                                                       @Nonnull ConstantPool.TagRef nameTagRef,
                                                       @Nonnull TypeAnnotationInfo[] annotations)
    {
        super(NAME, owner, index, constantPool, nameTagRef, annotations);
    }

    public static @Nonnull AttributeEntryRuntimeVisibleTypeAnnotations from(@Nonnull AttributePool attributePool,
                                                                            @Nonnull ConstantPool constantPool,
                                                                            @Nonnegative int length,
                                                                            @Nonnull ConstantPool.TagRef nameTagRef,
                                                                            @Nonnull ByteBuffer byteBuffer)
    {
        return from(attributePool, constantPool, length,
                nameTagRef, byteBuffer, AttributeEntryRuntimeVisibleTypeAnnotations::new);
    }

    public static final String NAME = "RuntimeVisibleTypeAnnotations";
}
