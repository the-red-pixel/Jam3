package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.attribute.stackmap.Frame;
import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

@SuppressWarnings("all")
public class AttributeEntryStackMapTable extends AttributeEntry {
    public AttributeEntryStackMapTable(@Nonnull AttributePool owner,
                                       @Nonnegative int index,
                                       @Nonnull ConstantPool constantPool,
                                       @Nonnull ConstantPool.TagRef nameTagRef,
                                       @Nonnull Frame[] frames)
    {
        super(NAME, owner, index, constantPool, nameTagRef);

        setFrames(frames);
    }

    public static @Nonnull AttributeEntryStackMapTable from(@Nonnull AttributePool attributePool,
                                                            @Nonnull ConstantPool constantPool,
                                                            @Nonnegative int length,
                                                            @Nonnull ConstantPool.TagRef nameTagRef,
                                                            @Nonnull ByteBuffer byteBuffer)
    {
        int frameCount = byteBuffer.getShort() & 0xFFFF;

        Frame[] frames = new Frame[frameCount];
        for (int i = 0; i < frameCount; i++)
            frames[i] = Frame.from(constantPool, byteBuffer);

        return attributePool.addEntry(
                new AttributeEntryStackMapTable(attributePool, attributePool.nextIndex(), constantPool,
                        nameTagRef,
                        frames));
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            BigEndian.pushU2(baos, frames.length);

            for (int i = 0; i < frames.length; i++)
                baos.write(frames[i].toBytes());
        } catch (IOException e) {
            // unused
            throw new Error(e);
        }

        return baos.toByteArray();
    }

    public @Nonnull Frame[] getFrames()
    {
        return frames;
    }

    public void setFrames(@Nonnull Frame[] frames)
    {
        this.frames = Objects.requireNonNull(frames);
    }

    private Frame[] frames;

    public static final String NAME = "StackMapTable";
}
