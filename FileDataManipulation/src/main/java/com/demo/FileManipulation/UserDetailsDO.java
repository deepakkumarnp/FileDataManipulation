package com.demo.FileManipulation;

import java.util.Date;

/**
 * Created by DeepakKumar_N01 on 08/12/2016.
 */
public class UserDetailsDO {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDetailsDO)) return false;

        UserDetailsDO that = (UserDetailsDO) o;

        if (!getUserName().equals(that.getUserName())) return false;
        if (!getDateOfBirth().equals(that.getDateOfBirth())) return false;
        return getGender().equals(that.getGender());

    }

    @Override
    public int hashCode() {
        int result = getUserName().hashCode();
        result = 31 * result + getDateOfBirth().hashCode();
        result = 31 * result + getGender().hashCode();
        return result;
    }

    public String getUserName() {
        return userName;

    }

    @Override
    public String toString() {
        return "UserDetailsDO{" +
                "userName='" + userName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender='" + gender + '\'' +
                '}';
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    private String userName;
    private Date dateOfBirth;
    private String gender;

}
