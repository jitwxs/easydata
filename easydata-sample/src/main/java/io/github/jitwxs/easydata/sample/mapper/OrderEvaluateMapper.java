package io.github.jitwxs.easydata.sample.mapper;

import io.github.jitwxs.easydata.sample.bean.OrderEvaluate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-06-04 22:59
 */
@Mapper
public interface OrderEvaluateMapper {
    List<OrderEvaluate> selectAll();

    OrderEvaluate selectById(final String id);
}
