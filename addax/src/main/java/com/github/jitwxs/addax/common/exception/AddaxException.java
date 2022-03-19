package com.github.jitwxs.addax.common.exception;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:47
 */
public class AddaxException extends RuntimeException {
    public AddaxException() {
        super();
    }

    public AddaxException(final String msg) {
        super(msg);
    }

    public AddaxException(final Throwable cause) {
        super(cause);
    }

    public AddaxException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
