package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.constant.ConstantTag;
import com.theredpixelteam.jam3.constant.ConstantTagUTF8;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Objects;

public class AttributeEntrySourceFile extends AttributeEntry {
    public AttributeEntrySourceFile(@Nonnull AttributePool owner,
                                    @Nonnegative int index,
                                    @Nonnull ConstantPool constantPool,
                                    @Nonnull ConstantPool.TagRef nameTagRef,
                                    @Nonnull ConstantPool.TagRef sourceFileTagRef)
    {
        super(NAME, owner, index, constantPool, nameTagRef);

        setSourceFileTagRef(sourceFileTagRef);
    }

    public static @Nonnull AttributeEntrySourceFile from(@Nonnull AttributePool attributePool,
                                                         @Nonnull ConstantPool constantPool,
                                                         @Nonnegative int length,
                                                         @Nonnull ConstantPool.TagRef nameTagRef,
                                                         @Nonnull ByteBuffer byteBuffer)
    {
        checkLen(length, 2);

        ConstantPool.TagRef sourceFileTagRef
                = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

        return attributePool.addEntry(
                new AttributeEntrySourceFile(attributePool, attributePool.nextIndex(), constantPool,
                        nameTagRef,
                        sourceFileTagRef));
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[2];

        BigEndian.pushU2(byts, sourceFileTagRef.getRefIndex(), 0);

        return byts;
    }

    public @Nonnull ConstantPool.TagRef getSourceFileTagRef()
    {
        return sourceFileTagRef;
    }

    public @Nullable ConstantTagUTF8 getSourceFileTag()
    {
        return sourceFileTagRef.get(ConstantTag.Type.UTF8, ConstantTagUTF8.class);
    }

    public void setSourceFileTagRef(@Nonnull ConstantPool.TagRef sourceFileTagRef)
    {
        this.sourceFileTagRef = checkRef(Objects.requireNonNull(sourceFileTagRef));
    }

    private ConstantPool.TagRef sourceFileTagRef;

    public static final String NAME = "SourceFile";
}
