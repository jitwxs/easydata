package com.github.jitwxs.addax.common.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * matrix type data, such as: csv, mysql...
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 19:00
 */
@Getter
@NoArgsConstructor
public class MatrixBean {
    private String[] title;

    private List<String[]> dataList;

    public static MatrixBean FALLBACK_BEAN = new MatrixBean(Collections.singletonList(new String[0]));

    public MatrixBean(final List<String[]> lines) {
        this.title = lines.get(0);
        this.dataList = lines.subList(1, lines.size());
    }

    public String getTitle(final int index) {
        return this.title[index];
    }

    public static boolean ignore(final MatrixBean matrixBean) {
        return matrixBean == null || matrixBean == FALLBACK_BEAN;
    }
}
