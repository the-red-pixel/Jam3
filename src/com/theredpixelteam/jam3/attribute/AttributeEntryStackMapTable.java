package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.constant.ConstantPool;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class AttributeEntryStackMapTable extends AttributeEntry {
    public AttributeEntryStackMapTable(@Nonnull AttributePool owner,
                                       @Nonnegative int index,
                                       @Nonnull ConstantPool constantPool,
                                       @Nonnull ConstantPool.TagRef nameTagRef)
    {
        super(NAME, owner, index, constantPool, nameTagRef);
    }

    // TODO

    @Override
    public @Nonnull byte[] toBytes()
    {
        return new byte[0];
    }

    public static final String NAME = "StackMapTable";
}
