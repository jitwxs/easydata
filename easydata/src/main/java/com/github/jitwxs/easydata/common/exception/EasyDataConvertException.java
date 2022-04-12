package com.github.jitwxs.easydata.common.exception;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:49
 */
public class EasyDataConvertException extends EasyDataException {
    public EasyDataConvertException() {
        super();
    }

    public EasyDataConvertException(final String msg) {
        super(msg);
    }

    public EasyDataConvertException(final Throwable cause) {
        super(cause);
    }

    public EasyDataConvertException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
