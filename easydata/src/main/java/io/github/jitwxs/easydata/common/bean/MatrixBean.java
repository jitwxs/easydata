package io.github.jitwxs.easydata.common.bean;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * matrix type data, such as: csv, mysql...
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 19:00
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MatrixBean {
    private String[] title;

    private List<String[]> dataList;

    public static MatrixBean FALLBACK_BEAN = new MatrixBean();

    public static MatrixBean newInstance(final List<String[]> lines) {
        if (CollectionUtils.isEmpty(lines)) {
            return FALLBACK_BEAN;
        } else {
            final MatrixBean bean = new MatrixBean();
            bean.title = lines.get(0);
            bean.dataList = lines.subList(1, lines.size());

            return bean;
        }
    }

    public String getTitle(final int index) {
        return this.title[index];
    }

    public static boolean ignore(final MatrixBean matrixBean) {
        return matrixBean == null || matrixBean == FALLBACK_BEAN;
    }
}
