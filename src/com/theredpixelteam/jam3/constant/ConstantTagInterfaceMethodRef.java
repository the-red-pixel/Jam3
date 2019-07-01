package com.theredpixelteam.jam3.constant;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class ConstantTagInterfaceMethodRef extends ConstantTagAccessibleObjectRef {
    ConstantTagInterfaceMethodRef(@Nonnull ConstantPool pool,
                                  @Nonnegative int index)
    {
        super(Type.INTERFACE_METHOD_REF, pool, index);
    }

    public ConstantTagInterfaceMethodRef(@Nonnull ConstantPool pool,
                                         @Nonnegative int index,
                                         @Nonnull ConstantPool.TagRef classTagRef,
                                         @Nonnull ConstantPool.TagRef nameNTypeTagRef)
    {
        this(pool, index);

        setClassTagRef(classTagRef);
        setNameNTypeTagRef(nameNTypeTagRef);
    }

    public static @Nonnull ConstantTagInterfaceMethodRef from(@Nonnull ConstantPool pool,
                                                              @Nonnull ByteBuffer byteBuffer)
    {
        int classTagRef = byteBuffer.getShort() & 0xFFFF;
        int nameNTypeTagRef = byteBuffer.getShort() & 0xFFFF;

        return pool.addTag(
                new ConstantTagInterfaceMethodRef(pool, pool.nextIndex(),
                        pool.new TagRef(classTagRef),
                        pool.new TagRef(nameNTypeTagRef)));
    }
}
