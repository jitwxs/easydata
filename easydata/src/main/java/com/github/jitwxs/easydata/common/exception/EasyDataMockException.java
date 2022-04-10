package com.github.jitwxs.easydata.common.exception;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:49
 */
public class EasyDataMockException extends EasyDataException {
    public EasyDataMockException() {
        super();
    }

    public EasyDataMockException(final String msg) {
        super(msg);
    }

    public EasyDataMockException(final Throwable cause) {
        super(cause);
    }

    public EasyDataMockException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
