package com.github.jitwxs.addax.sample.bean;

import lombok.Data;

import java.util.List;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-26 11:49
 */
@Data
public class UserInfo {
    private String _id;

    private int index;

    private String guid;

    private boolean isActive;

    private String balance;

    private String picture;

    private int age;

    private String eyeColor;

    private String name;

    private String gender;

    private String company;

    private String email;

    private String phone;

    private String address;

    private String about;

    private String registered;

    private double latitude;

    private double longitude;

    private List<String> tags;

    private List<Friends> friends;

    private String greeting;

    private String favoriteFruit;
}
