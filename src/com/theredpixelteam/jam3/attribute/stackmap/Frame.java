package com.theredpixelteam.jam3.attribute.stackmap;

import com.theredpixelteam.jam3.constant.ConstantPool;
import sun.util.calendar.LocalGregorianCalendar;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Objects;

public abstract class Frame {
    public Frame(int tag,
                 @Nonnull Type type)
    {
        this.tag = tag;
        this.type = Objects.requireNonNull(type);
    }

    public static @Nonnull Frame from(@Nonnull ConstantPool constantPool,
                                      @Nonnull ByteBuffer byteBuffer)
    {
        int typeTag = byteBuffer.get() & 0xFF;

        Type type = Type.getType(typeTag);

        if (type == null)
            throw new ClassFormatError("Unknown stack map frame type: " + typeTag);

        return type.getInterpreter().from(constantPool, typeTag, byteBuffer);
    }

    public abstract @Nonnull byte[] toBytes();

    public abstract @Nonnegative int getOffsetDelta();

    public abstract void setOffsetDelta(@Nonnegative int offsetDelta);

    static void checkOffsetDeltaRange(int delta, Type type)
    {
        if (delta < 0 || delta > (type.getUpperBound() - type.getLowerBound()))
            throw new IllegalArgumentException("The offset delta should be [0, "
                    + type.getUpperBound() + "] in the " + type.name() + " frame");
    }

    public int getTypeTag()
    {
        return tag;
    }

    void setTypeTag(int tag)
    {
        this.tag = tag;
    }

    public @Nonnull Type getType()
    {
        return type;
    }

    private int tag;

    private final Type type;

    public static enum Type
    {
        SAME                            (0,     63,  FrameSame::from),
        SAME_LOCAL_1_STACK_ITEM         (64,    127, FrameSameLocal1StackItem::from),
        SAME_LOCAL_1_STACK_ITEM_EXTENDED(247,   247, FrameSameLocal1StackItemExtended::from),
        CHOP                            (248,   250, FrameChop::from),
        SAME_EXTENDED                   (251,   251, FrameSameExtended::from),
        APPEND                          (252,   254, FrameAppend::from),
        FULL                            (255,   255, FrameFull::from);

        private Type(int lowerBound, int upperBound, FrameInterpreter interpreter)
        {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
            this.interpreter = interpreter;
        }

        public int getLowerBound()
        {
            return lowerBound;
        }

        public int getUpperBound()
        {
            return upperBound;
        }

        public @Nonnull FrameInterpreter getInterpreter()
        {
            return interpreter;
        }

        public static @Nullable Type getType(int typeTag)
        {
            for (int i = 0; i < VALUES.length; i++)
            {
                if (typeTag < VALUES[i].getLowerBound())
                    return null;

                if (typeTag > VALUES[i].getUpperBound())
                    continue;

                return VALUES[i];
            }

            return null;
        }

        private final int lowerBound;

        private final int upperBound;

        private final FrameInterpreter interpreter;

        private static final Type[] VALUES
                = {SAME, SAME_LOCAL_1_STACK_ITEM, SAME_LOCAL_1_STACK_ITEM_EXTENDED, CHOP, SAME_EXTENDED, APPEND, FULL};

        public static interface FrameInterpreter
        {
            public @Nonnull Frame from(@Nonnull ConstantPool constantPool,
                                       @Nonnegative int typeTag,
                                       @Nonnull ByteBuffer byteBuffer);
        }
    }
}
