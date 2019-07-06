package com.theredpixelteam.jam3.attribute.stackmap;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.constant.ConstantTag;
import com.theredpixelteam.jam3.constant.ConstantTagClass;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Objects;

public class VerificationTypeInfoObject extends VerificationTypeInfo {
    public VerificationTypeInfoObject(@Nonnull Type type,
                                      @Nonnull ConstantPool.TagRef typeTagRef)
    {
        super(type);

        setTypeTagRef(typeTagRef);
    }

    public static @Nonnull VerificationTypeInfoObject from(@Nonnull ConstantPool constantPool,
                                                           @Nonnull Type type,
                                                           @Nonnull ByteBuffer byteBuffer)
    {
        ConstantPool.TagRef typeTagRef
                = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

        return new VerificationTypeInfoObject(type, typeTagRef);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[3];

        BigEndian.pushU1(byts, getType().getTag(), 0);
        BigEndian.pushU2(byts, typeTagRef.getRefIndex(), 1);

        return byts;
    }

    public @Nonnull ConstantPool.TagRef getTypeTagRef()
    {
        return typeTagRef;
    }

    public @Nullable ConstantTagClass getTypeTag()
    {
        return typeTagRef.get(ConstantTag.Type.CLASS, ConstantTagClass.class);
    }

    public void setTypeTagRef(@Nonnull ConstantPool.TagRef typeTagRef)
    {
        this.typeTagRef = Objects.requireNonNull(typeTagRef);
    }

    private ConstantPool.TagRef typeTagRef;
}
