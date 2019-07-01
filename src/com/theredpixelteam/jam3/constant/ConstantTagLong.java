package com.theredpixelteam.jam3.constant;

import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class ConstantTagLong extends ConstantTag {
    ConstantTagLong(@Nonnull ConstantPool pool,
                    @Nonnegative int index)
    {
        super(Type.LONG, pool, index);
    }

    public ConstantTagLong(@Nonnull ConstantPool pool,
                           @Nonnegative int index,
                           long value)
    {
        this(pool, index);

        setValue(value);
    }

    public static @Nonnull ConstantTagLong from(@Nonnull ConstantPool pool,
                                                @Nonnull ByteBuffer byteBuffer)
    {
        long value = byteBuffer.getLong();

        return pool.addTag(
                new ConstantTagLong(pool, pool.nextIndex(), value));
    }

    public static void skip(@Nonnull ByteBuffer byteBuffer)
    {
        byteBuffer.position(byteBuffer.position() + 8);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[8];

        BigEndian.pushU8(byts, value, 0);

        return byts;
    }

    public long getValue()
    {
        return value;
    }

    public void setValue(long value)
    {
        this.value = value;
    }

    private long value;
}
