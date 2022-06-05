package io.github.jitwxs.easydata.sample.controller;

import io.github.jitwxs.easydata.sample.bean.OrderEvaluate;
import io.github.jitwxs.easydata.sample.bean.ResponseResult;
import io.github.jitwxs.easydata.sample.mapper.OrderEvaluateMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-06-04 23:10
 */
@RestController
@RequestMapping("/order-evaluate")
public class OrderEvaluateController {
    @Resource
    private OrderEvaluateMapper orderEvaluateMapper;

    @GetMapping()
    public ResponseResult queryAll() {
        final List<OrderEvaluate> orderEvaluateList = orderEvaluateMapper.selectAll();

        return ResponseResult.success(orderEvaluateList);
    }

    @GetMapping("/{id}")
    public ResponseResult queryById(@PathVariable() String id) {
        final OrderEvaluate orderEvaluate = orderEvaluateMapper.selectById(id);

        return ResponseResult.success(orderEvaluate);
    }
}
