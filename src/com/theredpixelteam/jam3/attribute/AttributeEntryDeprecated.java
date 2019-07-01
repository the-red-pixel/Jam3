package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.constant.ConstantPool;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class AttributeEntryDeprecated extends AttributeEntry {
    public AttributeEntryDeprecated(@Nonnull AttributePool owner,
                                    @Nonnegative int index,
                                    @Nonnull ConstantPool constantPool,
                                    @Nonnull ConstantPool.TagRef nameTagRef)
    {
        super(NAME, owner, index, constantPool, nameTagRef);
    }

    public static @Nonnull AttributeEntryDeprecated from(@Nonnull AttributePool attributePool,
                                                         @Nonnull ConstantPool constantPool,
                                                         @Nonnegative int length,
                                                         @Nonnull ConstantPool.TagRef nameTagRef,
                                                         @Nonnull ByteBuffer byteBuffer)
    {
        checkLen(length, 0);

        return attributePool.addEntry(
                new AttributeEntryDeprecated(attributePool, attributePool.nextIndex(), constantPool,
                        nameTagRef));
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        return new byte[0];
    }

    public static final String NAME = "Deprecated";
}
