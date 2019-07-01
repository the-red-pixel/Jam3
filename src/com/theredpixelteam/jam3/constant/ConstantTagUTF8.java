package com.theredpixelteam.jam3.constant;

import com.theredpixelteam.jam3.util.BigEndian;
import com.theredpixelteam.jam3.util.MUTF8;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;
import java.util.Objects;

public class ConstantTagUTF8 extends ConstantTag {
    ConstantTagUTF8(@Nonnull ConstantPool pool,
                    @Nonnegative int index)
    {
        super(Type.UTF8, pool, index);
    }

    public ConstantTagUTF8(@Nonnull ConstantPool pool,
                           @Nonnegative int index,
                           @Nonnull byte[] byts)
    {
        this(pool, index);

        setBytes(Objects.requireNonNull(byts));
    }

    public static @Nonnull ConstantTagUTF8 from(@Nonnull ConstantPool pool,
                                                @Nonnull ByteBuffer byteBuffer)
    {
        int len = byteBuffer.getShort() & 0xFFFF;

        byte[] byts = new byte[len];
        byteBuffer.get(byts);

        return pool.addTag(
                new ConstantTagUTF8(pool, pool.nextIndex(), byts));
    }

    public static void skip(@Nonnull ByteBuffer byteBuffer)
    {
        int len = byteBuffer.getShort() & 0xFFFF;

        byteBuffer.position(byteBuffer.position() + len);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        int len = this.byts.length;

        byte[] byts = new byte[len + 2];

        BigEndian.pushU2(byts, len, 0);

        System.arraycopy(this.byts, 0, byts, 2, len);

        return byts;
    }

    public @Nonnull String getString()
    {
        if (string != null)
            return string;

        return string = MUTF8.toString(this.byts);
    }

    public void setString(@Nonnull String string)
    {
        Objects.requireNonNull(string);

        this.string = string;
        this.byts = MUTF8.toBytes(string);
    }

    public @Nonnull byte[] getBytes()
    {
        return byts;
    }

    public void setBytes(@Nonnull byte[] byts)
    {
        this.byts = Objects.requireNonNull(byts);
        this.string = null;
    }

    private String string;

    private byte[] byts;
}
