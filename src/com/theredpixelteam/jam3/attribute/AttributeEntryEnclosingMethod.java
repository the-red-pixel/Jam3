package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.constant.ConstantTag;
import com.theredpixelteam.jam3.constant.ConstantTagClass;
import com.theredpixelteam.jam3.constant.ConstantTagNameNType;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Objects;

public class AttributeEntryEnclosingMethod extends AttributeEntry {
    public AttributeEntryEnclosingMethod(@Nonnull AttributePool owner,
                                         @Nonnegative int index,
                                         @Nonnull ConstantPool constantPool,
                                         @Nonnull ConstantPool.TagRef nameTagRef,
                                         @Nonnull ConstantPool.TagRef classTagRef,
                                         @Nonnull ConstantPool.TagRef methodNameNTypeTagRef)
    {
        super(NAME, owner, index, constantPool, nameTagRef);

        setClassTagRef(classTagRef);
        setMethodNameNTypeTagRef(methodNameNTypeTagRef);
    }

    public static @Nonnull AttributeEntryEnclosingMethod from(@Nonnull AttributePool attributePool,
                                                              @Nonnull ConstantPool constantPool,
                                                              @Nonnegative int length,
                                                              @Nonnull ConstantPool.TagRef nameTagRef,
                                                              @Nonnull ByteBuffer byteBuffer)
    {
        checkLen(length, 4);

        ConstantPool.TagRef classTagRef
                = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

        ConstantPool.TagRef methodNameNTypeTagRef
                = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

        return attributePool.addEntry(
                new AttributeEntryEnclosingMethod(attributePool, attributePool.nextIndex(), constantPool,
                        nameTagRef,
                        classTagRef,
                        methodNameNTypeTagRef));
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[4];

        BigEndian.pushU2(byts, classTagRef.getRefIndex(), 0);
        BigEndian.pushU2(byts, methodNameNTypeTagRef.getRefIndex(), 2);

        return byts;
    }

    public @Nonnull ConstantPool.TagRef getClassTagRef()
    {
        return classTagRef;
    }

    public @Nullable ConstantTagClass getClassTag()
    {
        return classTagRef.get(ConstantTag.Type.CLASS, ConstantTagClass.class);
    }

    public void setClassTagRef(@Nonnull ConstantPool.TagRef classTagRef)
    {
        this.classTagRef = checkRef(Objects.requireNonNull(classTagRef));
    }

    public @Nonnull ConstantPool.TagRef getMethodNameNTypeTagRef()
    {
        return methodNameNTypeTagRef;
    }

    public @Nullable ConstantTagNameNType getMethodNameNTypeTag()
    {
        return methodNameNTypeTagRef.get(ConstantTag.Type.NAME_N_TYPE, ConstantTagNameNType.class);
    }

    public void setMethodNameNTypeTagRef(@Nonnull ConstantPool.TagRef methodNameNTypeTagRef)
    {
        this.methodNameNTypeTagRef = checkRef(Objects.requireNonNull(methodNameNTypeTagRef));
    }

    private ConstantPool.TagRef classTagRef;

    private ConstantPool.TagRef methodNameNTypeTagRef;

    public static final String NAME = "EnclosingMethod";
}
