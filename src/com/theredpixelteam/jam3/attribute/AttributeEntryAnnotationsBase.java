package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.attribute.annotation.AnnotationInfo;
import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

@SuppressWarnings("all")
public abstract class AttributeEntryAnnotationsBase extends AttributeEntry {
    public AttributeEntryAnnotationsBase(@Nonnull String name,
                                         @Nonnull AttributePool owner,
                                         @Nonnegative int index,
                                         @Nonnull ConstantPool constantPool,
                                         @Nonnull ConstantPool.TagRef nameTagRef,
                                         @Nonnull AnnotationInfo[] annotations)
    {
        super(name, owner, index, constantPool, nameTagRef);

        setAnnotations(annotations);
    }

    static @Nonnull <T extends AttributeEntryAnnotationsBase> T from(@Nonnull AttributePool attributePool,
                                                                     @Nonnull ConstantPool constantPool,
                                                                     @Nonnegative int length,
                                                                     @Nonnull ConstantPool.TagRef nameTagRef,
                                                                     @Nonnull ByteBuffer byteBuffer,
                                                                     @Nonnull Constructor<T> constructor)
    {
        int annotationCount = byteBuffer.getShort() & 0xFFFF;

        AnnotationInfo[] annotations = new AnnotationInfo[annotationCount];
        for (int i = 0; i < annotationCount; i++)
            annotations[i] = AnnotationInfo.from(constantPool, byteBuffer);

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

    public @Nonnull AnnotationInfo[] getAnnotations()
    {
        return annotations;
    }

    public void setAnnotations(@Nonnull AnnotationInfo[] annotations)
    {
        this.annotations = Objects.requireNonNull(annotations);
    }

    private AnnotationInfo[] annotations;

    static interface Constructor<T extends AttributeEntryAnnotationsBase>
    {
        T construct(@Nonnull AttributePool owner,
                    @Nonnull int index,
                    @Nonnull ConstantPool constantPool,
                    @Nonnull ConstantPool.TagRef nameTagRef,
                    @Nonnull AnnotationInfo[] annotations);
    }
}
