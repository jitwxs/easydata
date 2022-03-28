package com.github.jitwxs.addax.common.enums;

import com.github.jitwxs.addax.common.exception.AddaxException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 19:05
 */
@Getter
@AllArgsConstructor
public enum FileFormatEnum {
    CSV(".csv"),
    JSON(".json"),
    ;

    private final String suffix;

    /**
     * Analyze the file format from the path suffix
     *
     * @param path path name
     * @return resolve format from path
     */
    public static FileFormatEnum delegate(final String path) {
        for (FileFormatEnum suffixEnum : values()) {
            if (path.endsWith(suffixEnum.suffix)) {
                return suffixEnum;
            }
        }

        throw new AddaxException("Not Support File Suffix: " + path);
    }
}
