package com.theredpixelteam.jam3.constant;

import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Objects;

public class ConstantTagClass extends ConstantTag {
    ConstantTagClass(@Nonnull ConstantPool pool,
                     @Nonnegative int index)
    {
        super(Type.CLASS, pool, index);
    }

    public ConstantTagClass(@Nonnull ConstantPool pool,
                            @Nonnegative int index,
                            @Nonnull ConstantPool.TagRef utf8TagRef)
    {
        this(pool, index);

        setUTF8TagRef(utf8TagRef);
    }

    public static @Nonnull ConstantTagClass from(@Nonnull ConstantPool pool,
                                                 @Nonnull ByteBuffer byteBuffer)
    {
        int tagRef = byteBuffer.getShort() & 0xFFFF;

        return pool.addTag(
                new ConstantTagClass(pool, pool.nextIndex(), pool.new TagRef(tagRef)));
    }

    public static void skip(@Nonnull ByteBuffer byteBuffer)
    {
        byteBuffer.position(byteBuffer.position() + 2);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[2];

        BigEndian.pushU2(byts, utf8TagRef.getRefIndex(), 0);

        return byts;
    }

    public @Nullable ConstantPool.TagRef getUTF8TagRef()
    {
        return utf8TagRef;
    }

    public void setUTF8TagRef(@Nonnull ConstantPool.TagRef utf8TagRef)
    {
        this.utf8TagRef = checkRef(Objects.requireNonNull(utf8TagRef));
    }

    public @Nullable ConstantTagUTF8 getUTF8Tag()
    {
        return utf8TagRef.get(Type.UTF8, ConstantTagUTF8.class);
    }

    private ConstantPool.TagRef utf8TagRef;
}
