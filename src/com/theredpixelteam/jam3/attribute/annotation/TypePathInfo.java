package com.theredpixelteam.jam3.attribute.annotation;

import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;
import java.util.Objects;

public class TypePathInfo {
    public TypePathInfo(@Nonnull PathInfo[] pathTable)
    {
        setPathTable(pathTable);
    }

    public static @Nonnull TypePathInfo from(@Nonnull ByteBuffer byteBuffer)
    {
        int pathTableSize = byteBuffer.get() & 0xFF;

        PathInfo[] pathTable = new PathInfo[pathTableSize];
        for (int i = 0; i < pathTableSize; i++)
            pathTable[i] = PathInfo.from(byteBuffer);

        return new TypePathInfo(pathTable);
    }

    public byte[] toBytes()
    {
        byte[] byts = new byte[1 + (pathTable.length << 1)];

        for (int i = 0; i < pathTable.length; i++)
            pathTable[i].push(byts, 1 + (i << 1));

        return byts;
    }

    public @Nonnull PathInfo[] getPathTable()
    {
        return pathTable;
    }

    public void setPathTable(@Nonnull PathInfo[] pathTable)
    {
        this.pathTable = Objects.requireNonNull(pathTable);
    }

    private PathInfo[] pathTable;

    public static class PathInfo
    {
        public PathInfo(@Nonnegative int typePathKind,
                        @Nonnegative int typeArgumentIndex)
        {
            setTypePathKind(typePathKind);
            setTypeArgumentIndex(typeArgumentIndex);
        }

        public static @Nonnull PathInfo from(@Nonnull ByteBuffer byteBuffer)
        {
            int typePathKind = byteBuffer.get() & 0xFF;

            int typeArgumentIndex = byteBuffer.get() & 0xFF;

            return new PathInfo(typePathKind, typeArgumentIndex);
        }

        public void push(@Nonnull byte[] byts, int off)
        {
            BigEndian.pushU1(byts, typePathKind, off);
            BigEndian.pushU1(byts, typeArgumentIndex, off + 1);
        }

        public @Nonnegative int getTypePathKind()
        {
            return typePathKind;
        }

        public void setTypePathKind(@Nonnegative int typePathKind)
        {
            this.typePathKind = typePathKind;
        }

        public @Nonnegative int getTypeArgumentIndex()
        {
            return typeArgumentIndex;
        }

        public void setTypeArgumentIndex(@Nonnegative int typeArgumentIndex)
        {
            this.typeArgumentIndex = typeArgumentIndex;
        }

        private int typePathKind;

        private int typeArgumentIndex;
    }
}
