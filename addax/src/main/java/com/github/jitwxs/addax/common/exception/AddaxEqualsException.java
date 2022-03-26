package com.github.jitwxs.addax.common.exception;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:49
 */
public class AddaxEqualsException extends AddaxException {
    public AddaxEqualsException() {
        super();
    }

    public AddaxEqualsException(final String msg) {
        super(msg);
    }

    public AddaxEqualsException(final Throwable cause) {
        super(cause);
    }

    public AddaxEqualsException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
