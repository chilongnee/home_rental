package com.example.myapplication.model;

public class User {
    String fullname, username, email, password, address, gender, date, phonenumber;
        public User(){};

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public User(String fullname, String username, String email, String password, String address, String gender, String date, String phonenumber) {
            this.fullname = fullname;
            this.username = username;
            this.email = email;
            this.address = address;
            this.gender = gender;
            this.date = date;
            this.phonenumber = phonenumber;
            this.password = password;
        }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

   // public String getPhonenumber() {
      //  return phonenumber;
   //}

    //public void setPhonenumber(String phonenumber) {
      //  this.phonenumber = phonenumber;
   // }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
