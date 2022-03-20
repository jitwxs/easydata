package com.github.jitwxs.addax.common.exception;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:49
 */
public class AddaxMockException extends AddaxException {
    public AddaxMockException() {
        super();
    }

    public AddaxMockException(final String msg) {
        super(msg);
    }

    public AddaxMockException(final Throwable cause) {
        super(cause);
    }

    public AddaxMockException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
