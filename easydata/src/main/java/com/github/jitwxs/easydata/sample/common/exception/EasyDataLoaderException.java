package com.github.jitwxs.easydata.sample.common.exception;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:49
 */
public class EasyDataLoaderException extends EasyDataException {
    public EasyDataLoaderException() {
        super();
    }

    public EasyDataLoaderException(final String msg) {
        super(msg);
    }

    public EasyDataLoaderException(final Throwable cause) {
        super(cause);
    }

    public EasyDataLoaderException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
