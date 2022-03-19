package com.github.jitwxs.addax.common.exception;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:49
 */
public class AddaxConvertException extends AddaxException {
    public AddaxConvertException() {
        super();
    }

    public AddaxConvertException(final String msg) {
        super(msg);
    }

    public AddaxConvertException(final Throwable cause) {
        super(cause);
    }

    public AddaxConvertException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
