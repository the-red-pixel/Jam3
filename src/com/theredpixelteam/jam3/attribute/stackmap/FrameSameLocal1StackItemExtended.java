package com.theredpixelteam.jam3.attribute.stackmap;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;
import java.util.Objects;

public class FrameSameLocal1StackItemExtended extends Frame {
    public FrameSameLocal1StackItemExtended(@Nonnegative int tag,
                                            @Nonnegative int offsetDelta,
                                            @Nonnull VerificationTypeInfo stack)
    {
        super(tag, Type.SAME_LOCAL_1_STACK_ITEM_EXTENDED);

        setOffsetDelta(offsetDelta);
        setStack(stack);
    }

    public static @Nonnegative FrameSameLocal1StackItemExtended from(@Nonnull ConstantPool constantPool,
                                                                     @Nonnegative int typeTag,
                                                                     @Nonnull ByteBuffer byteBuffer)
    {
        int offsetDelta = byteBuffer.get() & 0xFFFF;

        VerificationTypeInfo stack = VerificationTypeInfo.from(constantPool, byteBuffer);

        return new FrameSameLocal1StackItemExtended(typeTag, offsetDelta, stack);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] vtBytes = stack.toBytes();
        byte[] byts = new byte[vtBytes.length + 3];

        BigEndian.pushU1(byts, getTypeTag(), 0);

        BigEndian.pushU2(byts, offsetDelta, 1);

        System.arraycopy(vtBytes, 0, byts, 3, vtBytes.length);

        return byts;
    }

    @Override
    public @Nonnegative int getOffsetDelta()
    {
        return offsetDelta;
    }

    @Override
    public void setOffsetDelta(@Nonnegative int offsetDelta)
    {
        this.offsetDelta = offsetDelta;
    }

    public @Nonnull VerificationTypeInfo getStack()
    {
        return stack;
    }

    public void setStack(@Nonnull VerificationTypeInfo stack)
    {
        this.stack = Objects.requireNonNull(stack);
    }

    private int offsetDelta;

    private VerificationTypeInfo stack;
}
