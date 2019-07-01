package com.theredpixelteam.jam3.constant;

import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class ConstantTagFloat extends ConstantTag {
    ConstantTagFloat(@Nonnull ConstantPool pool,
                     @Nonnegative int index)
    {
        super(Type.FLOAT, pool, index);
    }

    public ConstantTagFloat(@Nonnull ConstantPool pool,
                            @Nonnegative int index,
                            float value)
    {
        this(pool, index);

        setValue(value);
    }

    public static @Nonnull ConstantTagFloat from(@Nonnull ConstantPool pool,
                                                 @Nonnull ByteBuffer byteBuffer)
    {
        float value = byteBuffer.getFloat();

        return pool.addTag(
                new ConstantTagFloat(pool, pool.nextIndex(), value));
    }

    public static void skip(@Nonnull ByteBuffer byteBuffer)
    {
        byteBuffer.position(byteBuffer.position() + 4);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[4];

        BigEndian.pushU4(byts, Float.floatToRawIntBits(value), 0);

        return byts;
    }

    public float getValue()
    {
        return value;
    }

    public void setValue(float value)
    {
        this.value = value;
    }

    private float value;
}
