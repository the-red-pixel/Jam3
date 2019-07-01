package com.theredpixelteam.jam3.util;

public class MUTF8MalformationException extends RuntimeException {
    public MUTF8MalformationException()
    {
    }

    public MUTF8MalformationException(String msg)
    {
        super(msg);
    }

    public MUTF8MalformationException(Throwable cause)
    {
        super(cause);
    }

    public MUTF8MalformationException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
