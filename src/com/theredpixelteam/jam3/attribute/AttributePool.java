package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.constant.ConstantPool;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

public class AttributePool {
    public List<AttributeEntry> getEntires()
    {
        return Collections.unmodifiableList(list);
    }

    public AttributeEntry getEntry(int index)
    {
        return list.get(index);
    }

    public int size()
    {
        return list.size();
    }

    int nextIndex()
    {
        return list.size();
    }

    <T extends AttributeEntry> T addEntry(T entry)
    {
        if (entry.getIndex() != nextIndex())
            throw new ConcurrentModificationException();

        list.add(entry);

        return entry;
    }

    public static void from(@Nonnull AttributePool dstPool,
                            @Nonnegative int length,
                            @Nonnull ConstantPool constantPool,
                            @Nonnull ByteBuffer byteBuffer)
    {
        for (int i = 0; i < length; i++)
            AttributeEntry.from(dstPool, constantPool, byteBuffer);
    }

    public @Nonnull byte[] toBytes()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            for (AttributeEntry entry : list)
            {
                dos.writeShort(entry.getNameTagRef().getRefIndex());

                byte[] byts = entry.toBytes();

                dos.writeInt(byts.length);
                dos.write(byts);
            }
        } catch (IOException e) {
            throw new Error(e);

            // unused
        }

        return baos.toByteArray();
    }

    private final List<AttributeEntry> list = new ArrayList<>();

    public class EntryRef
    {
        public EntryRef(@Nonnegative int refIndex)
        {
            this.refIndex = refIndex;
        }

        public AttributePool getOwner()
        {
            return AttributePool.this;
        }

        public boolean available()
        {
            return refIndex < list.size();
        }

        public int getRefIndex()
        {
            return refIndex;
        }

        public AttributeEntry get()
        {
            return get(null, null);
        }

        public AttributeEntry get(@Nullable String expectingName)
        {
            return get(expectingName, null);
        }

        public <T extends AttributeEntry> T get(@Nullable Class<T> expectingType)
        {
            return get(null, expectingType);
        }

        @SuppressWarnings("unchecked")
        public <T extends AttributeEntry> T get(@Nullable String expectingName,
                                                @Nullable Class<T> expectingType)
        {
            if (!available())
                return null;

            AttributeEntry entry = list.get(refIndex);

            if (entry == null)
                throw new NullPointerException("Null element in pool");

            if (expectingName != null && !expectingName.equals(entry.getName()))
                throw new ClassFormatError("Expecting \"" + expectingName + "\" attribute, but \""
                        + entry.getName() + "\" found");

            if (expectingType != null && !expectingType.isInstance(entry))
                throw new ClassCastException(entry.getClass().getCanonicalName() + " cannot be cast to " +
                        expectingType.getCanonicalName());

            return (T) entry;
        }

        private final int refIndex;
    }
}
