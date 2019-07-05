package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.attribute.annotation.TypeAnnotationInfo;
import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

@SuppressWarnings("all")
public abstract class AttributeEntryTypeAnnotationsBase extends AttributeEntry {
    public AttributeEntryTypeAnnotationsBase(@Nonnull String name,
                                             @Nonnull AttributePool owner,
                                             @Nonnegative int index,
                                             @Nonnull ConstantPool constantPool,
                                             @Nonnull ConstantPool.TagRef nameTagRef,
                                             @Nonnull TypeAnnotationInfo[] annotations)
    {
        super(name, owner, index, constantPool, nameTagRef);

        setAnnotations(annotations);
    }

    static <T extends AttributeEntryTypeAnnotationsBase> T from(@Nonnull AttributePool attributePool,
                                                                @Nonnull ConstantPool constantPool,
                                                                @Nonnegative int length,
                                                                @Nonnull ConstantPool.TagRef nameTagRef,
                                                                @Nonnull ByteBuffer byteBuffer,
                                                                @Nonnull Constructor<T> constructor)
    {
        int annotationCount = byteBuffer.getShort() & 0xFFFF;

        TypeAnnotationInfo[] annotations = new TypeAnnotationInfo[annotationCount];
        for (int i = 0; i < annotationCount; i++)
            annotations[i] = TypeAnnotationInfo.from(constantPool, byteBuffer);

        return attributePool.addEntry(
                constructor.construct(attributePool, attributePool.nextIndex(), constantPool,
                        nameTagRef,
                        annotations));
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            BigEndian.pushU2(baos, annotations.length);

            for (int i = 0; i < annotations.length; i++)
                baos.write(annotations[i].toBytes());
        } catch (IOException e) {
            // unused
            throw new Error(e);
        }

        return baos.toByteArray();
    }

    public @Nonnull TypeAnnotationInfo[] getAnnotations()
    {
        return annotations;
    }

    public void setAnnotations(@Nonnull TypeAnnotationInfo[] annotations)
    {
        this.annotations = Objects.requireNonNull(annotations);
    }

    private TypeAnnotationInfo[] annotations;

    static interface Constructor<T extends AttributeEntryTypeAnnotationsBase>
    {
        T construct(@Nonnull AttributePool owner,
                    @Nonnegative int index,
                    @Nonnull ConstantPool constantPool,
                    @Nonnull ConstantPool.TagRef nameTagRef,
                    @Nonnull TypeAnnotationInfo[] annotations);
    }
}
