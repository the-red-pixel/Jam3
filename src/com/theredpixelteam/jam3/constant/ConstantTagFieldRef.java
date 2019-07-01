package com.theredpixelteam.jam3.constant;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class ConstantTagFieldRef extends ConstantTagAccessibleObjectRef {
    ConstantTagFieldRef(@Nonnull ConstantPool pool,
                        @Nonnegative int index)
    {
        super(Type.FIELD_REF, pool, index);
    }

    public ConstantTagFieldRef(@Nonnull ConstantPool pool,
                               @Nonnegative int index,
                               @Nonnull ConstantPool.TagRef classTagRef,
                               @Nonnull ConstantPool.TagRef nameNTypeTagRef)
    {
        this(pool, index);

        setClassTagRef(classTagRef);
        setNameNTypeTagRef(nameNTypeTagRef);
    }

    public static @Nonnull ConstantTagFieldRef from(@Nonnull ConstantPool pool,
                                                    @Nonnull ByteBuffer byteBuffer)
    {
        int classTagRef = byteBuffer.getShort() & 0xFFFF;
        int nameNTypeTagRef = byteBuffer.getShort() & 0xFFFF;

        return pool.addTag(
                new ConstantTagFieldRef(pool, pool.nextIndex(),
                        pool.new TagRef(classTagRef),
                        pool.new TagRef(nameNTypeTagRef)));
    }
}
