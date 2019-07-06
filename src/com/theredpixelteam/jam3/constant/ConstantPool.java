package com.theredpixelteam.jam3.constant;

import com.theredpixelteam.jam3.util.Jumper;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

public class ConstantPool {
    public ConstantPool()
    {
        (tags = new ArrayList<>()).add(null);
    }

    public static void from(@Nonnull ConstantPool dstPool,
                            @Nonnegative int length,
                            @Nonnull ByteBuffer byteBuffer)
    {
        Jumper jumper = new Jumper();

        for (int i = 0; i < length; i++)
        {
            if (jumper.enabled())
            {
                dstPool.tags.add(null);

                jumper.disable();
                continue;
            }

            ConstantTag.from(dstPool, byteBuffer, jumper);
        }
    }

    public @Nonnull byte[] toBytes()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            for (ConstantTag tag : tags)
            {
                if (tag == null)
                    continue;

                baos.write(tag.getTagType().getTag());
                baos.write(tag.toBytes());
            }
        } catch (IOException e) {
            throw new Error(e);

            // unused
        }

        return baos.toByteArray();
    }

    public List<ConstantTag> getTags()
    {
        return Collections.unmodifiableList(tags);
    }

    int nextIndex()
    {
        return tags.size();
    }

    <T extends ConstantTag> T addTag(T tag)
    {
        if (tag.getIndex() != nextIndex())
            throw new ConcurrentModificationException();

        tags.add(tag);

        return tag;
    }

    public @Nullable ConstantTag getTag(int index)
    {
        return tags.get(index);
    }

    public int getLength()
    {
        return tags.size();
    }

    private final List<ConstantTag> tags;

    public class TagRef
    {
        public TagRef(@Nonnegative int refIndex)
        {
            this.refIndex = refIndex;
        }

        public int getRefIndex()
        {
            return refIndex;
        }

        public boolean available()
        {
            return refIndex < tags.size();
        }

        public @Nonnull ConstantPool getOwner()
        {
            return ConstantPool.this;
        }

        public boolean isNull()
        {
            return refIndex == 0;
        }

        public @Nullable ConstantTag get()
        {
            return get(null, null);
        }

        public @Nullable <T extends ConstantTag> T get(@Nullable ConstantTag.Type expectingType)
        {
            return get(expectingType, null);
        }

        @SuppressWarnings("unchecked")
        public @Nullable <T extends ConstantTag> T get(@Nullable ConstantTag.Type expectingType,
                                                       @Nullable Class<T> instanceType)
        {
            if (!available())
                throw new IllegalStateException("Tag ref not available, or illegal index");

            ConstantTag tag = ConstantPool.this.getTag(refIndex);

            if (tag != null)
            {
                if (expectingType != null && !expectingType.equals(tag.getTagType()))
                    throw new ClassFormatError("Expecting " + expectingType + " tag on TagRef (" + refIndex
                            + "), but found " + tag.getTagType());

                if (instanceType != null && !instanceType.isInstance(tag))
                    throw new ClassCastException(tag.getClass().getCanonicalName() +
                            " cannot be cast to " +
                            instanceType.getCanonicalName());
            }
            else
                return null;

            return (T) tag;
        }

        final int refIndex;
    }
}
