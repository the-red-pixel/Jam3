package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.constant.ConstantTag;
import com.theredpixelteam.jam3.constant.ConstantTagUTF8;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;
import java.util.Objects;

public class AttributeEntryUnknown extends AttributeEntry {
    public AttributeEntryUnknown(@Nonnull String name,
                                 @Nonnull AttributePool owner,
                                 @Nonnegative int index,
                                 @Nonnull ConstantPool constantPool,
                                 @Nonnull ConstantPool.TagRef nameTagRef,
                                 @Nonnull byte[] bytes)
    {
        super(name, owner, index, constantPool, nameTagRef);

        setBytes(bytes);
    }

    public static @Nonnull AttributeEntryUnknown from(@Nonnull AttributePool attributePool,
                                                      @Nonnull ConstantPool constantPool,
                                                      @Nonnegative int length,
                                                      @Nonnull ConstantPool.TagRef nameTagRef,
                                                      @Nonnull ByteBuffer byteBuffer)
    {
        ConstantTagUTF8 nameTag = nameTagRef.get(ConstantTag.Type.UTF8, ConstantTagUTF8.class);

        if (nameTag == null)
        {
            if (nameTagRef.getRefIndex() == 0)
                throw new ClassFormatError("Null name tag reference of attribute");

            throw new IllegalStateException("Attribute name tag not available");
        }

        String name = nameTag.getString();

        byte[] bytes = new byte[length];

        byteBuffer.get(bytes);

        return attributePool.addEntry(
                new AttributeEntryUnknown(name, attributePool, attributePool.nextIndex(), constantPool,
                        nameTagRef,
                        bytes));
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        return bytes;
    }

    public void setBytes(@Nonnull byte[] bytes)
    {
        this.bytes = Objects.requireNonNull(bytes);
    }

    public @Nonnull byte[] getBytes()
    {
        return bytes;
    }

    private byte[] bytes;
}
