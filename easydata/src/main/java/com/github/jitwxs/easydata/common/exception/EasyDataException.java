package com.github.jitwxs.easydata.common.exception;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:47
 */
public class EasyDataException extends RuntimeException {
    public EasyDataException() {
        super();
    }

    public EasyDataException(final String msg) {
        super(msg);
    }

    public EasyDataException(final Throwable cause) {
        super(cause);
    }

    public EasyDataException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
