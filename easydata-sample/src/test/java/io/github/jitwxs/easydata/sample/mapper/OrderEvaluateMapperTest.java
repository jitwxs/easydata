package io.github.jitwxs.easydata.sample.mapper;

import io.github.jitwxs.easydata.core.mybatis.MyBatisMapperInspect;
import io.github.jitwxs.easydata.core.mybatis.action.IMapperInspectAction;
import io.github.jitwxs.easydata.core.mybatis.action.ResultMapAttributeMapperInspectAction;
import io.github.jitwxs.easydata.sample.bean.OrderEvaluate;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class OrderEvaluateMapperTest extends MyBatisMapperInspect<OrderEvaluateMapper> {
    @Test
    public void selectAll() {
        final List<OrderEvaluate> evaluates = target.selectAll();
    }

    @Override
    public List<IMapperInspectAction> actionList() {
        return Arrays.asList(
                new ResultMapAttributeMapperInspectAction("ResultMap", new String[]{"sId", "javaBeanUnknownFields"})
        );
    }
}