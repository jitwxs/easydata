package io.github.jitwxs.easydata.common.exception;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:49
 */
public class EasyVerifyEqualsException extends EasyDataException {
    public EasyVerifyEqualsException() {
        super();
    }

    public EasyVerifyEqualsException(final String msg) {
        super(msg);
    }

    public EasyVerifyEqualsException(final Throwable cause) {
        super(cause);
    }

    public EasyVerifyEqualsException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
