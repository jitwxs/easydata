package io.github.jitwxs.easydata.sample.convert;

import io.github.jitwxs.easydata.common.util.TimeUtils;
import io.github.jitwxs.easydata.sample.message.MessageProto;
import io.github.jitwxs.easydata.sample.bean.OrderEvaluate;

import java.math.BigDecimal;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 12:21
 */
public class OrderEvaluateConvert {
    public static MessageProto.OrderEvaluate db2Proto(final OrderEvaluate orderEvaluate) {
        return db2ProtoBuilder(orderEvaluate).build();
    }

    public static MessageProto.OrderEvaluate.Builder db2ProtoBuilder(final OrderEvaluate orderEvaluate) {
        return MessageProto.OrderEvaluate.newBuilder()
                .setId(orderEvaluate.getId())
                .setHasOpen(orderEvaluate.getHasOpen())
                .setUserId(orderEvaluate.getUserId())
                .setSId(orderEvaluate.getSId())
                .setUserScore(orderEvaluate.getUserScore().toPlainString())
                .setUserEvaluate(orderEvaluate.getUserEvaluate())
                .setUserDate(TimeUtils.ldtToMs(orderEvaluate.getUserDate()))
                .setCourierId(orderEvaluate.getCourierId())
                .setCourierScore(orderEvaluate.getCourierScore().toPlainString())
                .setCourierEvaluate(orderEvaluate.getCourierEvaluate())
                .setVersion(orderEvaluate.getVersion())
                .setCourierDate(TimeUtils.ldtToMs(orderEvaluate.getCourierDate()))
                .setUpdateDate(TimeUtils.ldtToMs(orderEvaluate.getUpdateDate()));
    }

    public static OrderEvaluate proto2Db(final MessageProto.OrderEvaluate orderEvaluate) {
        return OrderEvaluate.builder()
                .id(orderEvaluate.getId())
                .hasOpen(orderEvaluate.getHasOpen())
                .userId(orderEvaluate.getUserId())
                .sId(orderEvaluate.getSId())
                .userScore(new BigDecimal(orderEvaluate.getUserScore()))
                .userEvaluate(orderEvaluate.getUserEvaluate())
                .userDate(TimeUtils.msToLdt(orderEvaluate.getUserDate()))
                .courierId(orderEvaluate.getCourierId())
                .courierScore(new BigDecimal(orderEvaluate.getCourierScore()))
                .courierEvaluate(orderEvaluate.getCourierEvaluate())
                .version(orderEvaluate.getVersion())
                .courierDate(TimeUtils.msToLdt(orderEvaluate.getCourierDate()))
                .updateDate(TimeUtils.msToLdt(orderEvaluate.getUpdateDate()))
                .build();
    }
}
