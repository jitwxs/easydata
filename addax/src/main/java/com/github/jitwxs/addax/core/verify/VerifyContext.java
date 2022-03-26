package com.github.jitwxs.addax.core.verify;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-26 19:05
 */
@Data
public class VerifyContext {
    private final Set<String> ignoreFields = new HashSet<>();

    private final Set<String> validateFields = new HashSet<>();

    /**
     * 忽略类型的差异
     */
    private boolean ignoreClassDiff = false;

    public String[] toIgnoreFields() {
        return ignoreFields.toArray(new String[0]);
    }

    public String[] toValidateFields() {
        return validateFields.toArray(new String[0]);
    }
}
