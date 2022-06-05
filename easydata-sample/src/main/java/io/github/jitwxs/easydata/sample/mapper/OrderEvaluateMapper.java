package io.github.jitwxs.easydata.sample.mapper;

import io.github.jitwxs.easydata.sample.bean.OrderEvaluate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-06-04 22:59
 */
@Mapper
public interface OrderEvaluateMapper {
    List<OrderEvaluate> selectAll();

    OrderEvaluate selectById(final String id);

    OrderEvaluate selectByIdAndName(@Param("id") final String id, @Param("name") final String name);

    List<OrderEvaluate> selectByCondition(final Map<String, Object> conditionMap);
}
