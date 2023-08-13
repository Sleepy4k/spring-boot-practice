package com.sleepy4k.practice.api.model;

public class User {
  private int id;
  private String name;
  private int age;
  private String email;

  public User(int id, String name, int age, String email) {
    this.setId(id);
    this.setName(name);
    this.setAge(age);
    this.setEmail(email);
  }

  public User() {
    this.setId(0);
    this.setName("");
    this.setAge(0);
    this.setEmail("");
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    if (id < 0) {
      throw new IllegalArgumentException("id must be greater than 0");
    }

    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("name must not be null");
    }

    this.name = name;
  }

  public int getAge() {
    return this.age;
  }

  public void setAge(int age) {
    if (age < 0) {
      throw new IllegalArgumentException("age must be greater than 0");
    }

    this.age = age;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    if (email == null) {
      throw new IllegalArgumentException("email must not be null");
    }

    this.email = email;
  }

  @Override
  public String toString() {
    return "User{" +
      "id=" + this.id +
      ", name='" + this.name + '\'' +
      ", age='" + this.age + '\'' +
      ", email=" + this.email +
      '}';
  }
}
