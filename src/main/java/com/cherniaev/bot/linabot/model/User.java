package com.cherniaev.bot.linabot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "users")
public class User {
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "chatId")
    private Long chatId;
    @Column(name = "username")
    private String username;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;
    @Column(name = "stateId")
    private Integer stateId;
    @Column(name = "isAdmin")
    private boolean isAdmin;

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", chatId=" + chatId +
            ", username='" + username + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", phone='" + phone + '\'' +
            ", email='" + email + '\'' +
            ", isAdmin=" + isAdmin +
            '}';
    }
}
