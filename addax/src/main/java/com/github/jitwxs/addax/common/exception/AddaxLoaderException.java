package com.github.jitwxs.addax.common.exception;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:49
 */
public class AddaxLoaderException extends AddaxException {
    public AddaxLoaderException() {
        super();
    }

    public AddaxLoaderException(final String msg) {
        super(msg);
    }

    public AddaxLoaderException(final Throwable cause) {
        super(cause);
    }

    public AddaxLoaderException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
