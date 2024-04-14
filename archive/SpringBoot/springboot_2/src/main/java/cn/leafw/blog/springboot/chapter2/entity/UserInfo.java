package cn.leafw.blog.springboot.chapter2.entity;

public class UserInfo {

  private String userId;
  private String userName;
  private String userSex;
  private String userAge;


  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }


  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }


  public String getUserSex() {
    return userSex;
  }

  public void setUserSex(String userSex) {
    this.userSex = userSex;
  }


  public String getUserAge() {
    return userAge;
  }

  public void setUserAge(String userAge) {
    this.userAge = userAge;
  }

}
