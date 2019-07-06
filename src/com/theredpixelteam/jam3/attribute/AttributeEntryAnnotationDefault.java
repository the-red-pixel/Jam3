package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.attribute.annotation.ElementInfo;
import com.theredpixelteam.jam3.constant.ConstantPool;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;
import java.util.Objects;

public class AttributeEntryAnnotationDefault extends AttributeEntry {
    public AttributeEntryAnnotationDefault(@Nonnull AttributePool owner,
                                           @Nonnegative int index,
                                           @Nonnull ConstantPool constantPool,
                                           @Nonnull ConstantPool.TagRef nameTagRef,
                                           @Nonnull ElementInfo element)
    {
        super(NAME, owner, index, constantPool, nameTagRef);

        setElement(element);
    }

    public static @Nonnull AttributeEntryAnnotationDefault from(@Nonnull AttributePool attributePool,
                                                                @Nonnull ConstantPool constantPool,
                                                                @Nonnegative int length,
                                                                @Nonnull ConstantPool.TagRef nameTagRef,
                                                                @Nonnull ByteBuffer byteBuffer)
    {
        ElementInfo element = ElementInfo.from(constantPool, byteBuffer);

        return attributePool.addEntry(
                new AttributeEntryAnnotationDefault(attributePool, attributePool.nextIndex(), constantPool,
                        nameTagRef,
                        element));
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        return element.toBytes();
    }

    public @Nonnull ElementInfo getElement()
    {
        return element;
    }

    public void setElement(@Nonnull ElementInfo element)
    {
        this.element = Objects.requireNonNull(element);
    }

    private ElementInfo element;

    public static final String NAME = "AnnotationDefault";
}
