package com.theredpixelteam.jam3.constant;

import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Objects;

public abstract class ConstantTagAccessibleObjectRefBase extends ConstantTag {
    ConstantTagAccessibleObjectRefBase(@Nonnull Type tagType,
                                       @Nonnull ConstantPool pool,
                                       @Nonnegative int index)
    {
        super(tagType, pool, index);
    }

    ConstantTagAccessibleObjectRefBase(@Nonnull Type tagType,
                                       @Nonnull ConstantPool pool,
                                       @Nonnegative int index,
                                       @Nonnull ConstantPool.TagRef classTagRef,
                                       @Nonnull ConstantPool.TagRef nameNTypeTagRef)
    {
        this(tagType, pool, index);

        setClassTagRef(classTagRef);
        setNameNTypeTagRef(nameNTypeTagRef);
    }

    public static void skip(@Nonnull ByteBuffer byteBuffer)
    {
        byteBuffer.position(byteBuffer.position() + 4);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[4];

        BigEndian.pushU2(byts, classTagRef.getRefIndex(), 0);
        BigEndian.pushU2(byts, nameNTypeTagRef.getRefIndex(), 2);

        return byts;
    }

    public @Nullable ConstantPool.TagRef getClassTagRef()
    {
        return classTagRef;
    }

    public @Nullable ConstantTagClass getClassTag()
    {
        return classTagRef.get(Type.CLASS, ConstantTagClass.class);
    }

    public void setClassTagRef(@Nonnull ConstantPool.TagRef classTagRef)
    {
        this.classTagRef = checkRef(Objects.requireNonNull(classTagRef));
    }

    public @Nonnull ConstantPool.TagRef getNameNTypeTagRef()
    {
        return nameNTypeTagRef;
    }

    public @Nullable ConstantTagNameNType getNameNTypeTag()
    {
        return nameNTypeTagRef.get(Type.NAME_N_TYPE, ConstantTagNameNType.class);
    }

    public void setNameNTypeTagRef(@Nonnull ConstantPool.TagRef nameNTypeTagRef)
    {
        this.nameNTypeTagRef = checkRef(Objects.requireNonNull(nameNTypeTagRef));
    }

    ConstantPool.TagRef classTagRef;

    ConstantPool.TagRef nameNTypeTagRef;
}
