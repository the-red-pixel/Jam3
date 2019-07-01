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

public class AttributeEntrySignature extends AttributeEntry {
    public AttributeEntrySignature(@Nonnull AttributePool owner,
                                   @Nonnegative int index,
                                   @Nonnull ConstantPool constantPool,
                                   @Nonnull ConstantPool.TagRef nameTagRef,
                                   @Nonnull ConstantPool.TagRef signatureTagRef)
    {
        super(NAME, owner, index, constantPool, nameTagRef);

        setSignatureTagRef(signatureTagRef);
    }

    public static @Nonnull AttributeEntrySignature from(@Nonnull AttributePool attributePool,
                                                        @Nonnull ConstantPool constantPool,
                                                        @Nonnegative int length,
                                                        @Nonnull ConstantPool.TagRef nameTagRef,
                                                        @Nonnull ByteBuffer byteBuffer)
    {
        checkLen(length, 2);

        ConstantPool.TagRef signatureTagRef =
                constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

        return attributePool.addEntry(
                new AttributeEntrySignature(attributePool, attributePool.nextIndex(), constantPool,
                        nameTagRef,
                        signatureTagRef));
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[2];

        BigEndian.pushU2(byts, signatureTagRef.getRefIndex(), 0);

        return byts;
    }

    public @Nonnull ConstantPool.TagRef getSignatureTagRef()
    {
        return signatureTagRef;
    }

    public @Nullable ConstantTagUTF8 getSignatureTag()
    {
        return signatureTagRef.get(ConstantTag.Type.UTF8, ConstantTagUTF8.class);
    }

    public void setSignatureTagRef(@Nonnull ConstantPool.TagRef signatureTagRef)
    {
        this.signatureTagRef = checkRef(Objects.requireNonNull(signatureTagRef));
    }

    private ConstantPool.TagRef signatureTagRef;

    public static final String NAME = "Signature";
}
