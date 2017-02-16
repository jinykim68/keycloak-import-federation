package com.changefirst.model;

import java.io.Serializable;

/**
 * Created by istvano on 16/02/2017.
 */
public class UserCredentialsDto implements Serializable {
    private String password;


    public UserCredentialsDto(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserCredentialsDto that = (UserCredentialsDto) o;

        return password != null ? password.equals(that.password) : that.password == null;
    }

    @Override
    public int hashCode() {
        return password != null ? password.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserCredentialsDto{" +
                "password='" + password + '\'' +
                '}';
    }
}
