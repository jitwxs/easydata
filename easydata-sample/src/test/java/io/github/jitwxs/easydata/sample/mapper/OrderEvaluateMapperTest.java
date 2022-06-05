package io.github.jitwxs.easydata.sample.mapper;

import io.github.jitwxs.easydata.core.mybatis.MyBatisMapperInspect;
import io.github.jitwxs.easydata.core.mybatis.action.ColumnsMapperInspectAction;
import io.github.jitwxs.easydata.core.mybatis.action.IMapperInspectAction;
import io.github.jitwxs.easydata.core.mybatis.action.ResultMapAttributeMapperInspectAction;
import io.github.jitwxs.easydata.core.mybatis.action.SqlStatementMapperInspectAction;
import io.github.jitwxs.easydata.sample.bean.OrderEvaluate;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderEvaluateMapperTest extends MyBatisMapperInspect<OrderEvaluateMapper> {
    @Test
    public void selectAll() {
        final List<OrderEvaluate> evaluates = target.selectAll();
    }

    @Test
    public void selectById() {
        final OrderEvaluate select = target.selectById("11");
    }

    @Test
    public void selectByIdAndName() {
        final OrderEvaluate evaluate = target.selectByIdAndName("22", "33");
    }

    @Test
    public void selectByCondition() {
        final Map<String, Object> conditionMap = new HashMap<String, Object>() {{
            put("id", "22");
            put("name", "33");
        }};

        final List<OrderEvaluate> evaluates = target.selectByCondition(conditionMap);
    }

    @Override
    public List<IMapperInspectAction> actionList() {
        return Arrays.asList(
                new ResultMapAttributeMapperInspectAction("ResultMap", new String[]{"sId", "javaBeanUnknownFields"}),
                new ColumnsMapperInspectAction("columns", "ResultMap"),
                new SqlStatementMapperInspectAction()
        );
    }
}