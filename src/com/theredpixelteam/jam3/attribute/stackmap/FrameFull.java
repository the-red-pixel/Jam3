package com.theredpixelteam.jam3.attribute.stackmap;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

public class FrameFull extends Frame {
    public FrameFull(@Nonnegative int tag,
                     @Nonnegative int offsetDelta,
                     @Nonnull VerificationTypeInfo[] locals,
                     @Nonnull VerificationTypeInfo[] stacks)
    {
        super(tag, Type.FULL);

        setOffsetDelta(offsetDelta);
        setLocals(locals);
        setStacks(stacks);
    }

    public static @Nonnull FrameFull from(@Nonnull ConstantPool constantPool,
                                          @Nonnegative int typeTag,
                                          @Nonnull ByteBuffer byteBuffer)
    {
        int offsetDelta = byteBuffer.getShort() & 0xFFFF;

        int localCount = byteBuffer.getShort() & 0xFFFF;

        VerificationTypeInfo[] locals = new VerificationTypeInfo[localCount];
        for (int i = 0; i < localCount; i++)
            locals[i] = VerificationTypeInfo.from(constantPool, byteBuffer);

        int stackCount = byteBuffer.getShort() & 0xFFFF;

        VerificationTypeInfo[] stacks = new VerificationTypeInfo[stackCount];
        for (int i = 0; i < stackCount; i++)
            stacks[i] = VerificationTypeInfo.from(constantPool, byteBuffer);

        return new FrameFull(typeTag, offsetDelta, locals, stacks);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            BigEndian.pushU1(baos, getTypeTag());

            BigEndian.pushU2(baos, offsetDelta);

            BigEndian.pushU2(baos, locals.length);
            for (int i = 0; i < locals.length; i++)
                baos.write(locals[i].toBytes());

            BigEndian.pushU2(baos, stacks.length);
            for (int i = 0; i < stacks.length; i++)
                baos.write(stacks[i].toBytes());
        } catch (IOException e) {
            // unused
            throw new Error(e);
        }

        return baos.toByteArray();
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

    public @Nonnull VerificationTypeInfo[] getLocals()
    {
        return locals;
    }

    public void setLocals(@Nonnull VerificationTypeInfo[] locals)
    {
        this.locals = Objects.requireNonNull(locals);
    }

    public @Nonnull VerificationTypeInfo[] getStacks()
    {
        return stacks;
    }

    public void setStacks(@Nonnull VerificationTypeInfo[] stacks)
    {
        this.stacks = Objects.requireNonNull(stacks);
    }

    private int offsetDelta;

    private VerificationTypeInfo[] locals;

    private VerificationTypeInfo[] stacks;
}
