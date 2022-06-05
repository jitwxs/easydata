package io.github.jitwxs.easydata.common.exception;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:49
 */
public class EasyDataVerifyException extends EasyDataException {
    public EasyDataVerifyException() {
        super();
    }

    public EasyDataVerifyException(final String msg) {
        super(msg);
    }

    public EasyDataVerifyException(final Throwable cause) {
        super(cause);
    }

    public EasyDataVerifyException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
