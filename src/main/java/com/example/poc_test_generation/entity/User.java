package com.example.poc_test_generation.entity;

//@Entity
public class User {
//    @Id @GeneratedValue
    private Long id;
    private String name;
    private int age;

    public User() {}
    public User(String name, int age) { this.name = name; this.age = age; }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
}
