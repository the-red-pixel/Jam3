package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.constant.ConstantPool;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class AttributeEntrySynthetic extends AttributeEntry {
    public AttributeEntrySynthetic(@Nonnull AttributePool owner,
                                   @Nonnegative int index,
                                   @Nonnull ConstantPool constantPool,
                                   @Nonnull ConstantPool.TagRef nameTagRef)
    {
        super(NAME, owner, index, constantPool, nameTagRef);
    }

    public static @Nonnull AttributeEntrySynthetic from(@Nonnull AttributePool attributePool,
                                                        @Nonnull ConstantPool constantPool,
                                                        @Nonnegative int length,
                                                        @Nonnull ConstantPool.TagRef nameTagRef,
                                                        @Nonnull ByteBuffer byteBuffer)
    {
        checkLen(length, 0);

        return attributePool.addEntry(
                new AttributeEntrySynthetic(attributePool, attributePool.nextIndex(), constantPool,
                        nameTagRef));
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        return new byte[0];
    }

    public static final String NAME = "Synthetic";
}
