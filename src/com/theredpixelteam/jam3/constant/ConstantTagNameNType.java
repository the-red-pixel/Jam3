package com.theredpixelteam.jam3.constant;

import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Objects;

public class ConstantTagNameNType extends ConstantTag {
    ConstantTagNameNType(@Nonnull ConstantPool pool,
                         @Nonnegative int index)
    {
        super(Type.NAME_N_TYPE, pool, index);
    }

    public ConstantTagNameNType(@Nonnull ConstantPool pool,
                                @Nonnegative int index,
                                @Nonnull ConstantPool.TagRef nameTagRef,
                                @Nonnull ConstantPool.TagRef descriptorTagRef)
    {
        this(pool, index);

        setNameTagRef(nameTagRef);
        setDescriptorTagRef(descriptorTagRef);
    }

    public static @Nonnull ConstantTagNameNType from(@Nonnull ConstantPool pool,
                                                     @Nonnull ByteBuffer byteBuffer)
    {
        int nameTagRef = byteBuffer.getShort() & 0xFFFF;
        int descriptorTagRef = byteBuffer.getShort() & 0xFFFF;

        return pool.addTag(
                new ConstantTagNameNType(pool, pool.nextIndex(),
                        pool.new TagRef(nameTagRef),
                        pool.new TagRef(descriptorTagRef)));
    }

    public static void skip(@Nonnull ByteBuffer byteBuffer)
    {
        byteBuffer.position(byteBuffer.position() + 4);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[4];

        BigEndian.pushU2(byts, nameTagRef.getRefIndex(), 0);
        BigEndian.pushU2(byts, descriptorTagRef.getRefIndex(), 2);

        return byts;
    }

    public @Nonnull ConstantPool.TagRef getNameTagRef()
    {
        return nameTagRef;
    }

    public @Nullable ConstantTagUTF8 getNameTag()
    {
        return nameTagRef.get(Type.UTF8, ConstantTagUTF8.class);
    }

    public void setNameTagRef(@Nullable ConstantPool.TagRef nameTagRef)
    {
        this.nameTagRef = checkRef(Objects.requireNonNull(nameTagRef));
    }

    public @Nonnull ConstantPool.TagRef getDescriptorTagRef()
    {
        return descriptorTagRef;
    }

    public @Nullable ConstantTagUTF8 getDescriptorTag()
    {
        return descriptorTagRef.get(Type.UTF8, ConstantTagUTF8.class);
    }

    public void setDescriptorTagRef(@Nonnull ConstantPool.TagRef descriptorTagRef)
    {
        this.descriptorTagRef = checkRef(Objects.requireNonNull(descriptorTagRef));
    }

    private ConstantPool.TagRef nameTagRef;

    private ConstantPool.TagRef descriptorTagRef;
}
