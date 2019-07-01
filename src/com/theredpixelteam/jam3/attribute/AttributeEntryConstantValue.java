package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;
import java.util.Objects;

public class AttributeEntryConstantValue extends AttributeEntry {
    public AttributeEntryConstantValue(@Nonnull AttributePool owner,
                                       @Nonnegative int index,
                                       @Nonnull ConstantPool constantPool,
                                       @Nonnull ConstantPool.TagRef nameTagRef,
                                       @Nonnull ConstantPool.TagRef valueTagRef)
    {
        super(NAME, owner, index, constantPool, nameTagRef);

        setValueTagRef(valueTagRef);
    }

    public static @Nonnull AttributeEntryConstantValue from(@Nonnull AttributePool attributePool,
                                                            @Nonnull ConstantPool constantPool,
                                                            @Nonnegative int length,
                                                            @Nonnull ConstantPool.TagRef nameTagRef,
                                                            @Nonnull ByteBuffer byteBuffer)
    {
        checkLen(length, 2);

        int valueTagRef = byteBuffer.getShort() & 0xFFFF;

        return attributePool.addEntry(
                new AttributeEntryConstantValue(attributePool, attributePool.nextIndex(), constantPool,
                        nameTagRef,
                        constantPool.new TagRef(valueTagRef)));
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[2];

        BigEndian.pushU2(byts, valueTagRef.getRefIndex(), 0);

        return byts;
    }

    public @Nonnull ConstantPool.TagRef getValueTagRef()
    {
        return valueTagRef;
    }

    public void setValueTagRef(@Nonnull ConstantPool.TagRef valueTagRef)
    {
        this.valueTagRef = checkRef(Objects.requireNonNull(valueTagRef));
    }

    private ConstantPool.TagRef valueTagRef;

    public static final String NAME = "ConstantValue";
}
