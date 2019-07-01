package com.theredpixelteam.jam3.constant;

import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class ConstantTagInteger extends ConstantTag {
    ConstantTagInteger(@Nonnull ConstantPool pool,
                       @Nonnegative int index)
    {
        super(Type.INTEGER, pool, index);
    }

    public ConstantTagInteger(@Nonnull ConstantPool pool,
                              @Nonnegative int index,
                              int value)
    {
        this(pool, index);

        setValue(value);
    }

    public static @Nonnull ConstantTagInteger from(@Nonnull ConstantPool pool,
                                                   @Nonnull ByteBuffer byteBuffer)
    {
        int value = byteBuffer.getInt();

        return pool.addTag(
                new ConstantTagInteger(pool, pool.nextIndex(), value));
    }

    public static void skip(@Nonnull ByteBuffer byteBuffer)
    {
        byteBuffer.position(byteBuffer.position() + 4);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[4];

        BigEndian.pushU4(byts, value, 0);

        return byts;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    private int value;
}
