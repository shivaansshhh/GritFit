package com.example.gritfitfitnessstudio;

public class UserModel {
    String weight;
    String gender;
    String name, username, caption ;
    String email;
    String UID;
    String bio;
    Integer profilePic;
    String uploadedPic;

    public UserModel() {
        // Default constructor to initialize fields to default values

    }

    public UserModel(String name, String username,String UID,String bio, String email, String gender, String weight, Integer profilePic){
        this.name = name;
        this.username = username;
        this.UID = UID;
        this.bio = bio;
        this.email = email;
        this.gender = gender;
        this.weight = weight;
        this.profilePic = profilePic;
    }


    // Constructor to initialize all the fields
    public UserModel(String name, String username, String caption, String uploadedPic, Integer profilePic) {
        this.name = name;
        this.username = username;
        this.caption = caption;
        this.uploadedPic = uploadedPic;
        this.profilePic = profilePic;
    }
    public UserModel(String uploadedPic){
        this.uploadedPic = uploadedPic;
    }

    public String getBio(){
        return bio;
    }
    public void setBio(String bio){
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getUID(){
        return UID;
    }

    public void setUID(String UID){
        this.UID = UID;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUploadedPic() {
        return uploadedPic;
    }

    public void setUploadedPic(String uploadedPic) {
        this.uploadedPic = uploadedPic;
    }

    public Integer getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Integer profilePic) {
        this.profilePic = profilePic;
    }

    public void setGender(String gender){
        this.gender = gender;
    }

    public String getGender(){
        return gender;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }
    public void setWeight(String weight){
        this.weight = weight;
    }

    public String getWeight(){
        return weight;
    }

}