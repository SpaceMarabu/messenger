package com.example.messenger;

public class User {
    private String uid;
    private String name;
    private String lastName;
    private String age;

    private Boolean status;

    public User() {
    }

    public User(String uid, String name, String lastName, String age, Boolean status) {
        this.uid = uid;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.status = status;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age='" + age + '\'' +
                ", status=" + status +
                '}';
    }
}
