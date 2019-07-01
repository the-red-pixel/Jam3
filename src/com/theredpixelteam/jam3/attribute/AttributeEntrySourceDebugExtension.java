package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.constant.ConstantPool;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;
import java.util.Objects;

public class AttributeEntrySourceDebugExtension extends AttributeEntry {
    public AttributeEntrySourceDebugExtension(@Nonnull AttributePool owner,
                                              @Nonnegative int index,
                                              @Nonnull ConstantPool constantPool,
                                              @Nonnull ConstantPool.TagRef nameTagRef,
                                              @Nonnull byte[] byts)
    {
        super(NAME, owner, index, constantPool, nameTagRef);

        setBytes(byts);
    }

    public static @Nonnull AttributeEntrySourceDebugExtension from(@Nonnull AttributePool attributePool,
                                                                   @Nonnull ConstantPool constantPool,
                                                                   @Nonnegative int length,
                                                                   @Nonnull ConstantPool.TagRef nameTagRef,
                                                                   @Nonnull ByteBuffer byteBuffer)
    {
        byte[] byts = new byte[length];

        byteBuffer.get(byts);

        return attributePool.addEntry(
                new AttributeEntrySourceDebugExtension(attributePool, attributePool.nextIndex(), constantPool,
                        nameTagRef,
                        byts));
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        return byts;
    }

    public @Nonnull byte[] getBytes()
    {
        return byts;
    }

    public void setBytes(@Nonnull byte[] byts)
    {
        this.byts = Objects.requireNonNull(byts);
    }

    private byte[] byts;

    public static final String NAME = "SourceDebugExtension";
}
