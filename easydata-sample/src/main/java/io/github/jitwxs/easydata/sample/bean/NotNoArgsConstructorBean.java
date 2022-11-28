package io.github.jitwxs.easydata.sample.bean;

import lombok.Getter;

/**
 * check when not exist no args constructor, the mock can success work
 * @date 2022-11-27 23:35
 */
@Getter
public class NotNoArgsConstructorBean {
    private final int one;

    private final double two;

    public NotNoArgsConstructorBean(int one, double two) {
        this.one = one;
        this.two = two;
    }
}
