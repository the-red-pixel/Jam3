package com.theredpixelteam.jam3;

import com.theredpixelteam.jam3.attribute.AttributePool;
import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.constant.ConstantTag;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;
import java.util.Objects;

public class AccessibleObjectInfoBase {
    public AccessibleObjectInfoBase(@Nonnull ConstantPool constantPool,
                                    @Nonnegative int modifiers,
                                    @Nonnull ConstantPool.TagRef nameTagRef,
                                    @Nonnull ConstantPool.TagRef descriptorTagRef,
                                    @Nonnull AttributePool attributes)
    {
        this.constantPool = Objects.requireNonNull(constantPool);

        setModifiers(modifiers);
        setNameTagRef(nameTagRef);
        setDescriptorTagRef(descriptorTagRef);
        setAttributes(attributes);
    }

    public static void skip(@Nonnull ByteBuffer byteBuffer)
    {
        byteBuffer.position(byteBuffer.position() + 6);

        int attributeCount = byteBuffer.getShort() & 0xFFFF;

        AttributePool.skip(attributeCount, byteBuffer);
    }

    static @Nonnull <T extends AccessibleObjectInfoBase> T from(@Nonnull ConstantPool constantPool,
                                                                @Nonnull ByteBuffer byteBuffer,
                                                                boolean skipAttributes,
                                                                @Nonnull Constructor<T> constructor)
    {
        int modifier = byteBuffer.getShort() & 0xFFFF;

        ConstantPool.TagRef nameTagRef
                = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

        ConstantPool.TagRef descriptorTagRef
                = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

        int attributeCount = byteBuffer.getShort() & 0xFFFF;

        AttributePool attributes = new AttributePool();

        if (!skipAttributes)
            AttributePool.from(attributes, attributeCount, constantPool, byteBuffer);
        else
            AttributePool.skip(attributeCount, byteBuffer);

        return constructor.construct(constantPool, modifier, nameTagRef, descriptorTagRef, attributes);
    }

    public @Nonnull byte[] toBytes()
    {
        byte[] attrByts = attributes.toBytes();
        byte[] byts = new byte[attrByts.length + 8];

        BigEndian.pushU2(byts, modifiers, 0);

        BigEndian.pushU2(byts, nameTagRef.getRefIndex(), 2);

        BigEndian.pushU2(byts, descriptorTagRef.getRefIndex(), 4);

        BigEndian.pushU2(byts, attributes.size(), 6);

        System.arraycopy(attrByts, 0, byts, 8, attrByts.length);

        return byts;
    }

    public @Nonnegative int getModifiers()
    {
        return modifiers;
    }

    public void setModifiers(@Nonnegative int modifiers)
    {
        this.modifiers = modifiers;
    }

    public @Nonnull ConstantPool.TagRef getNameTagRef()
    {
        return nameTagRef;
    }

    public @Nonnull ConstantPool.TagRef getDescriptorTagRef()
    {
        return descriptorTagRef;
    }

    public void setNameTagRef(@Nonnull ConstantPool.TagRef nameTagRef)
    {
        this.nameTagRef = checkRef(Objects.requireNonNull(nameTagRef));
    }

    public void setDescriptorTagRef(@Nonnull ConstantPool.TagRef descriptorTagRef)
    {
        this.descriptorTagRef = checkRef(Objects.requireNonNull(descriptorTagRef));
    }

    public @Nonnull AttributePool getAttributes()
    {
        return attributes;
    }

    public void setAttributes(@Nonnull AttributePool attributes)
    {
        this.attributes = Objects.requireNonNull(attributes);
    }

    public @Nonnull ConstantPool getConstantPool()
    {
        return constantPool;
    }

    ConstantPool.TagRef checkRef(ConstantPool.TagRef tagRef)
    {
        return ConstantTag.checkRef(constantPool, tagRef);
    }

    private final ConstantPool constantPool;

    private int modifiers;

    private ConstantPool.TagRef nameTagRef;

    private ConstantPool.TagRef descriptorTagRef;

    private AttributePool attributes;

    static interface Constructor<T extends AccessibleObjectInfoBase>
    {
        T construct(@Nonnull ConstantPool constantPool,
                    @Nonnegative int modifier,
                    @Nonnull ConstantPool.TagRef nameTagRef,
                    @Nonnull ConstantPool.TagRef descriptorTagRef,
                    @Nonnull AttributePool attributes);
    }
}
