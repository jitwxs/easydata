package io.github.jitwxs.easydata.sample.sample.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvaluate {
    /**
     * 订单ID
     */
    private String id;
    /**
     * 评论是否开启（1：开启；0：关闭）
     */
    private Boolean hasOpen;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户评分
     */
    private BigDecimal userScore;
    /**
     * 用户评价
     */
    private String userEvaluate;
    /**
     * 用户评价时间
     */
    private LocalDateTime userDate;
    /**
     * 用户ID
     */
    private String courierId;
    /**
     * 用户评分
     */
    private BigDecimal courierScore;
    /**
     * 用户评价
     */
    private String courierEvaluate;

    private Integer version;

    /**
     * 用户评价时间
     */
    private LocalDateTime courierDate;

    private LocalDateTime updateDate;
}