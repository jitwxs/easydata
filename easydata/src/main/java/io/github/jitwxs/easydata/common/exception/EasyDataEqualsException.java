package io.github.jitwxs.easydata.common.exception;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:49
 */
public class EasyDataEqualsException extends EasyDataException {
    public EasyDataEqualsException() {
        super();
    }

    public EasyDataEqualsException(final String msg) {
        super(msg);
    }

    public EasyDataEqualsException(final Throwable cause) {
        super(cause);
    }

    public EasyDataEqualsException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
