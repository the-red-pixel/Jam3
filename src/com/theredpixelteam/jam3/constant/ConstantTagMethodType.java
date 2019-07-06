package com.theredpixelteam.jam3.constant;

import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Objects;

public class ConstantTagMethodType extends ConstantTag {
    ConstantTagMethodType(@Nonnull ConstantPool pool,
                          @Nonnegative int index)
    {
        super(Type.METHOD_TYPE, pool, index);
    }

    public ConstantTagMethodType(@Nonnull ConstantPool pool,
                                 @Nonnegative int index,
                                 @Nonnull ConstantPool.TagRef descriptorTagRef)
    {
        this(pool, index);

        setDescriptorTagRef(descriptorTagRef);
    }

    public static @Nonnull ConstantTagMethodType from(@Nonnull ConstantPool pool,
                                                      @Nonnull ByteBuffer byteBuffer)
    {
        int descriptorTagRef = byteBuffer.getShort() & 0xFFFF;

        return pool.addTag(
                new ConstantTagMethodType(pool, pool.nextIndex(), pool.new TagRef(descriptorTagRef)));
    }

    public static void skip(@Nonnull ByteBuffer byteBuffer)
    {
        byteBuffer.position(byteBuffer.position() + 2);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[2];

        BigEndian.pushU2(byts, descriptorTagRef.getRefIndex(), 0);

        return byts;
    }

    public void setDescriptorTagRef(@Nonnull ConstantPool.TagRef descriptorTagRef)
    {
        this.descriptorTagRef = checkRef(Objects.requireNonNull(descriptorTagRef));
    }

    public @Nonnull ConstantPool.TagRef getDescriptorTagRef()
    {
        return descriptorTagRef;
    }

    public @Nullable ConstantTagUTF8 getDescriptorTag()
    {
        return descriptorTagRef.get(Type.UTF8, ConstantTagUTF8.class);
    }

    private ConstantPool.TagRef descriptorTagRef;
}
