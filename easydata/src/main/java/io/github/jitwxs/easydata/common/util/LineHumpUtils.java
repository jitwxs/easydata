package io.github.jitwxs.easydata.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Naming rules convert utils
 *
 * - line, such as: `hello_world`
 * - hump, such as: `helloWorld`
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 20:00
 */
public class LineHumpUtils {
    private static final Pattern linePattern = Pattern.compile("_(\\w)"), humpPattern = Pattern.compile("[A-Z]");

    /**
     * convert line naming rules to hump
     *
     * @param str line type string, such as lower_case
     * @return hump type string, such as lowerCase
     */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * convert hump naming rules to line
     *
     * @param str hump type string, such as lowerCase
     * @return line type string, such as lower_case
     */
    public static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
