package com.theredpixelteam.jam3.attribute.stackmap;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;
import java.util.Objects;

public class FrameSameLocal1StackItem extends Frame {
    public FrameSameLocal1StackItem(@Nonnegative int tag,
                                    @Nonnull VerificationTypeInfo stack)
    {
        super(tag, Type.SAME_LOCAL_1_STACK_ITEM);

        setStack(stack);
    }

    public static @Nonnull FrameSameLocal1StackItem from(@Nonnull ConstantPool constantPool,
                                                         @Nonnegative int typeTag,
                                                         @Nonnull ByteBuffer byteBuffer)
    {
        VerificationTypeInfo stack = VerificationTypeInfo.from(constantPool, byteBuffer);

        return new FrameSameLocal1StackItem(typeTag, stack);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] vtByts = stack.toBytes();
        byte[] byts = new byte[vtByts.length + 1];

        BigEndian.pushU1(byts, getTypeTag(), 0);

        System.arraycopy(vtByts, 0, byts, 1, vtByts.length);

        return byts;
    }

    @Override
    public int getOffsetDelta()
    {
        return getTypeTag() - Type.SAME_LOCAL_1_STACK_ITEM.getLowerBound();
    }

    @Override
    public void setOffsetDelta(int offsetDelta)
    {
        checkOffsetDeltaRange(offsetDelta, Type.SAME_LOCAL_1_STACK_ITEM);

        setTypeTag(Type.SAME_LOCAL_1_STACK_ITEM.getLowerBound() + offsetDelta);
    }

    public @Nonnull VerificationTypeInfo getStack()
    {
        return stack;
    }

    public void setStack(@Nonnull VerificationTypeInfo stack)
    {
        this.stack = Objects.requireNonNull(stack);
    }

    private VerificationTypeInfo stack;
}
