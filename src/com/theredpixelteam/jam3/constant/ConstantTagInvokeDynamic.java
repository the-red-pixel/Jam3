package com.theredpixelteam.jam3.constant;

import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;
import java.util.Objects;

public class ConstantTagInvokeDynamic extends ConstantTag {
    ConstantTagInvokeDynamic(@Nonnull ConstantPool pool,
                             @Nonnegative int index)
    {
        super(Type.INVOKEDYNAMIC, pool, index);
    }

    public ConstantTagInvokeDynamic(@Nonnull ConstantPool pool,
                                    @Nonnegative int index,
                                    @Nonnegative int handleIndex,
                                    @Nonnull ConstantPool.TagRef nameNTypeTagRef)
    {
        this(pool, index);

        setHandleIndex(handleIndex);
        setNameNTypeTagRef(nameNTypeTagRef);
    }

    public static @Nonnull ConstantTagInvokeDynamic from(@Nonnull ConstantPool constantPool,
                                                         @Nonnull ByteBuffer byteBuffer)
    {
        int handleIndex = byteBuffer.getShort() & 0xFFFF;
        int nameNTypeTagRef = byteBuffer.getShort() & 0xFFFF;

        return constantPool.addTag(new ConstantTagInvokeDynamic(
                constantPool,
                constantPool.nextIndex(),
                handleIndex,
                constantPool.new TagRef(nameNTypeTagRef)));
    }

    public static void skip(@Nonnull ByteBuffer byteBuffer)
    {
        byteBuffer.position(byteBuffer.position() + 4);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[4];

        BigEndian.pushU2(byts, handleIndex, 0);
        BigEndian.pushU2(byts, nameNTypeTagRef.getRefIndex(), 2);

        return byts;
    }

    public @Nonnegative int getHandleIndex()
    {
        return handleIndex;
    }

    public void setHandleIndex(@Nonnegative int handleIndex)
    {
        this.handleIndex = handleIndex;
    }

    public @Nonnull ConstantPool.TagRef getNameNTypeTagRef()
    {
        return nameNTypeTagRef;
    }

    public void setNameNTypeTagRef(@Nonnull ConstantPool.TagRef nameNTypeTagRef)
    {
        this.nameNTypeTagRef = Objects.requireNonNull(checkRef(nameNTypeTagRef));
    }

    private int handleIndex;

    private ConstantPool.TagRef nameNTypeTagRef;
}
