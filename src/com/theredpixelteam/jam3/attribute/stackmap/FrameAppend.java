package com.theredpixelteam.jam3.attribute.stackmap;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

public class FrameAppend extends Frame {
    public FrameAppend(@Nonnegative int tag,
                       @Nonnegative int offsetDelta,
                       @Nonnull VerificationTypeInfo[] locals)
    {
        super(tag, Type.APPEND);

        setLocals(locals, tag);
    }

    public static @Nonnull FrameAppend from(@Nonnull ConstantPool constantPool,
                                            @Nonnegative int typeTag,
                                            @Nonnull ByteBuffer byteBuffer)
    {
        int offsetDelta = byteBuffer.getShort() & 0xFFFF;

        int localCount = typeTag - 251;

        VerificationTypeInfo[] locals = new VerificationTypeInfo[localCount];
        for (int i = 0; i < localCount; i++)
            locals[i] = VerificationTypeInfo.from(constantPool, byteBuffer);

        return new FrameAppend(typeTag, offsetDelta, locals);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            BigEndian.pushU1(baos, getTypeTag());

            BigEndian.pushU2(baos, offsetDelta);

            for (int i = 0; i < locals.length; i++)
                baos.write(locals[i].toBytes());
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

    void setLocals(VerificationTypeInfo[] locals, int tag)
    {
        Objects.requireNonNull(locals);

        int length = tag - 251;

        if (locals.length != length)
            throw new IllegalArgumentException("Corrupt local verification type length in APPEND frame. "
                    + length + " expected but " + locals.length + " provided.");

        this.locals = locals;

        setTypeTag(tag);
    }

    public @Nonnull VerificationTypeInfo[] getLocals()
    {
        return locals;
    }

    public void setLocals(@Nonnull VerificationTypeInfo[] locals)
    {
        Objects.requireNonNull(locals);

        if (locals.length == 0 || locals.length > 3)
            throw new IllegalArgumentException("Count of verification type(s) should be [1, 3]");

        this.locals = locals;

        setTypeTag(locals.length + 251);
    }

    private int offsetDelta;

    private VerificationTypeInfo[] locals;
}
