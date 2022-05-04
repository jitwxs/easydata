package io.github.jitwxs.easydata.sample.sample.bean;

import io.github.jitwxs.easydata.sample.sample.enums.SexEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WebsiteUser {

    private int id;

    private String firstName;

    private String lastName;

    private int age;

    private String email;

    private SexEnum gender;

    private String ipAddress;

    private String agent;

    private LocalDateTime createTime;
}