package com.theredpixelteam.jam3.constant;

import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class ConstantTagDouble extends ConstantTag {
    ConstantTagDouble(@Nonnull ConstantPool pool,
                      @Nonnegative int index)
    {
        super(Type.DOUBLE, pool, index);
    }

    public ConstantTagDouble(@Nonnull ConstantPool pool,
                             @Nonnegative int index,
                             double value)
    {
        this(pool, index);

        setValue(value);
    }

    public static @Nonnull ConstantTagDouble from(@Nonnull ConstantPool pool,
                                                  @Nonnull ByteBuffer byteBuffer)
    {
        double value = byteBuffer.getDouble();

        return pool.addTag(
                new ConstantTagDouble(pool, pool.nextIndex(), value));
    }

    public static void skip(@Nonnull ByteBuffer byteBuffer)
    {
        byteBuffer.position(byteBuffer.position() + 8);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[8];

        BigEndian.pushU8(byts, Double.doubleToRawLongBits(value), 0);

        return byts;
    }

    public double getValue()
    {
        return value;
    }

    public void setValue(double value)
    {
        this.value = value;
    }

    private double value;
}
