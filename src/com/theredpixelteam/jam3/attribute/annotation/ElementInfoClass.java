package com.theredpixelteam.jam3.attribute.annotation;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.constant.ConstantTag;
import com.theredpixelteam.jam3.constant.ConstantTagUTF8;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Objects;

public class ElementInfoClass extends ElementInfo {
    public ElementInfoClass(@Nonnull ConstantPool constantPool,
                            @Nonnull ConstantPool.TagRef nameTagRef,
                            @Nonnull ConstantPool.TagRef descriptorTagRef)
    {
        super(constantPool, nameTagRef, Type.CLASS);

        setDescriptorTagRef(descriptorTagRef);
    }

    public static @Nonnull ElementInfoClass from(@Nonnull ConstantPool constantPool,
                                                 @Nonnull ConstantPool.TagRef nameTagRef,
                                                 @Nonnull ElementInfo.Type type,
                                                 @Nonnull ByteBuffer byteBuffer)
    {
        ConstantPool.TagRef descriptorTagRef
                = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

        return new ElementInfoClass(constantPool, nameTagRef, descriptorTagRef);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[2];

        BigEndian.pushU2(byts, descriptorTagRef.getRefIndex(), 0);

        return byts;
    }

    public @Nonnull ConstantPool.TagRef getDescriptorTagRef()
    {
        return descriptorTagRef;
    }

    public void setDescriptorTagRef(@Nonnull ConstantPool.TagRef descriptorTagRef)
    {
        this.descriptorTagRef = checkRef(Objects.requireNonNull(descriptorTagRef));
    }

    public @Nullable ConstantTagUTF8 getDescriptorTag()
    {
        return descriptorTagRef.get(ConstantTag.Type.UTF8, ConstantTagUTF8.class);
    }

    private ConstantPool.TagRef descriptorTagRef;
}
