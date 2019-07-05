package com.theredpixelteam.jam3.attribute.annotation;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.constant.ConstantTag;
import com.theredpixelteam.jam3.constant.ConstantTagUTF8;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Objects;

public class ElementInfoEnumConstant extends ElementInfo {
    public ElementInfoEnumConstant(@Nonnull ConstantPool constantPool,
                                   @Nonnull ConstantPool.TagRef nameTagRef,
                                   @Nonnull ConstantPool.TagRef descriptorTagRef,
                                   @Nonnull ConstantPool.TagRef constantNameTagRef)
    {
        super(constantPool, nameTagRef, Type.ENUM);

        setDescriptorTagRef(descriptorTagRef);
        setConstantNameTagRef(constantNameTagRef);
    }

    public static @Nonnull ElementInfoEnumConstant from(@Nonnull ConstantPool constantPool,
                                                        @Nonnull ConstantPool.TagRef nameTagRef,
                                                        @Nonnull Type type,
                                                        @Nonnull ByteBuffer byteBuffer)
    {
        ConstantPool.TagRef descriptorTagRef
                = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

        ConstantPool.TagRef constantNameTagRef
                = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

        return new ElementInfoEnumConstant(constantPool, nameTagRef, descriptorTagRef, constantNameTagRef);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[4];

        BigEndian.pushU2(byts, descriptorTagRef.getRefIndex(), 0);
        BigEndian.pushU2(byts, constantNameTagRef.getRefIndex(), 2);

        return byts;
    }

    public @Nonnull ConstantPool.TagRef getDescriptorTagRef()
    {
        return descriptorTagRef;
    }

    public @Nullable ConstantTagUTF8 getDescriptorTag()
    {
        return descriptorTagRef.get(ConstantTag.Type.UTF8, ConstantTagUTF8.class);
    }

    public void setDescriptorTagRef(@Nonnull ConstantPool.TagRef descriptorTagRef)
    {
        this.descriptorTagRef = checkRef(Objects.requireNonNull(descriptorTagRef));
    }

    public @Nonnull ConstantPool.TagRef getConstantNameTagRef()
    {
        return constantNameTagRef;
    }

    public @Nullable ConstantTagUTF8 getConstantNameTag()
    {
        return constantNameTagRef.get(ConstantTag.Type.UTF8, ConstantTagUTF8.class);
    }

    public void setConstantNameTagRef(@Nonnull ConstantPool.TagRef constantNameTagRef)
    {
        this.constantNameTagRef = checkRef(Objects.requireNonNull(constantNameTagRef));
    }

    private ConstantPool.TagRef descriptorTagRef;

    private ConstantPool.TagRef constantNameTagRef;
}
