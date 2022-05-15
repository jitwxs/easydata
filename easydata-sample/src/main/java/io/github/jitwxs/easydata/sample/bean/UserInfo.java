package io.github.jitwxs.easydata.sample.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-26 11:49
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private String _id;

    private int index;

    private String guid;

    private boolean active;

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

    public UserInfo copy() {
        return this.toBuilder().build();
    }
}
