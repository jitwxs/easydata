package com.github.jitwxs.addax.sample.convert;

import com.github.jitwxs.addax.sample.bean.OrderEvaluate;
import com.github.jitwxs.addax.sample.message.MessageProto;
import com.github.jitwxs.addax.sample.utils.TimeUtils;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 12:21
 */
public class OrderEvaluateConvert {
    public MessageProto.OrderEvaluate db2Proto(final OrderEvaluate orderEvaluate) {
        return MessageProto.OrderEvaluate.newBuilder()
                .setId(orderEvaluate.getId())
                .setHasOpen(orderEvaluate.getHasOpen())
                .setUserId(orderEvaluate.getUserId())
                .setUserSource(orderEvaluate.getUserScore().toPlainString())
                .setUserEvaluate(orderEvaluate.getUserEvaluate())
                .setUserDate(TimeUtils.ldtToMs(orderEvaluate.getUserDate()))
                .setCourierId(orderEvaluate.getCourierId())
                .setCourierScore(orderEvaluate.getCourierScore().toPlainString())
                .setCourierEvaluate(orderEvaluate.getCourierEvaluate())
                .setVersion(orderEvaluate.getVersion())
                .setCourierDate(TimeUtils.ldtToMs(orderEvaluate.getCourierDate()))
                .setUpdateDate(TimeUtils.ldtToMs(orderEvaluate.getUpdateDate()))
                .build();
    }
}
