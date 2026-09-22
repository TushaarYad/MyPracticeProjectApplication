package com.tushaar.MyPracticeProject.model;

//Made abstract, cannot be used to create objects directly. It serves as a blueprint or common base for other classes,
// allowing you to achieve partial abstraction by mixing fully implemented methods with unimplemented ones.




public abstract class Employee {

    public void setId(int id) {
        this.id = id;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    //Constructor
    public Employee(int id, String name, String password, String profilePicture) {
        //Can use a logger to prevent info from exposing but here its fine.
        System.out.println("employee Constructor Just to let you know that the super class employee was activated with the values: ");
        System.out.println("id: " + id + " name: " + name + " password: " + password + " profilePicture: " + profilePicture);
        this.id = id;
        this.name = name;
        this.password = password;
        this.profilePicture = profilePicture;
    }

    private int id;
    private String name;
    private String password;
    private String profilePicture;
}
