package com.lnsf.book.view;

import java.awt.MenuContainer;

import com.lnsf.book.controller.MenuController;
import com.lnsf.book.controller.RestaurantController;
import com.lnsf.book.controller.UserController;
import com.lnsf.book.dbutils.Input;
import com.lnsf.book.dbutils.Output;
import com.lnsf.book.model.User;

public class UserView {
    /**
     * 登录页面
     */
    public static void login(){
        System.out.println(">Please enter username:");
        String username = Input.getString(20);
        System.out.println(">Please enter password:");
        String password = Input.getString(20);
        switch (UserController.login(username, password)){
        case 0:
            Main.loginFail();
            return;
        case 1:
            RestaurantController.RID = -1;// 登录完这个值设置-1
            UserView.loginSuccessful();
            FgMain.userMainView();
            break;
        case 2:
            BgMain.businessMainView();
            break;
        case -1:
        	Main.loginFail();
        	return;
        }
    }
    /**
     * 注册页面
     */
    public static void register(){
        System.out.println(">What user type do you want to register?(1.Normal user 2.Restaurant Owner 0.Back):");
        int input = Input.getInt("[0-2]");
        if (input == 0)
            return;
        UserController.USER.setIdentify(input);
        System.out.println(">Please enter your name:");
        UserController.USER.setName(Input.getString(20));
        System.out.println(">Please enter your username:");
        UserController.USER.setUsername(Input.getString(20));
        System.out.println(">Please enter your password:");
        UserController.USER.setPassword(Input.getString(20));
        if (UserController.registerUser(UserController.USER)){
          Main.success();
          if (UserController.USER.getIdentify() == 1){
              
          } else if (UserController.USER.getIdentify() == 2){
              UserController.USER.setId(
                      UserController.getUserIdByUsername(UserController.USER.getUsername()));
              RestaurantView.register();
          } else {
              System.err.println("身份既不是用户也不是商家错误(bug)");
          }
        } else {
          Main.fail();
          System.err.println("The user name may repeated.");
        }
        UserController.USER = new User(-1, "", -1, "", "");// 注册完全局用户置空,回跳回登录
    }
    /**
     * 用户登录成功页面
     * 好像没人调用?
     */
    public static void loginSuccessful(){
        if (UserController.USER.getIdentify() == 1){
            System.out.println("User login success");
            System.out.println(UserController.USER.getName() + ",welcome!");
        } else if (UserController.USER.getIdentify() == 2){
            System.out.println("Owner login success");
            System.out.println(UserController.USER.getName() + " owner,welcome!");
        }
    }
    /**
     * 用户权限未找到
     */
    public static void userIdentifyNotFound(){
        System.err.println("User identify not found");
    }
    /**
     * 用户登录失败
     */
    public static void userLoginFailed(){
        System.err.println("User login failed.");
    }
    /**
     * 显示当前用户类型
     */
    public static void showUserIdentify(){
        if (UserController.USER.getIdentify() == 1){
            System.out.println("You are normal user.");
        } else if (UserController.USER.getIdentify() == 2){
            System.out.println("You are restaurant owner.");
        } else {
            userIdentifyNotFound();
        }
    }
    public static void nameIsEmpty() {
        System.err.println("Name is empty.");
    }
    public static void userNameIsEmpty() {
        System.err.println("Username is empty.");
    }
    public static void userPasswordIsEmpty() {
        System.err.println("Password is empty.");
    }
    public static void showUserName() {
        System.out.println("Your name is :" + UserController.USER.getName());
    }
    public static void showUserUsername() {
        System.out.println("Your username is:" + UserController.USER.getUsername());
    }
    public static void showUserPassword() {
        System.out.println("Your password is:" + UserController.USER.getPassword());
    }
    public static void userNameExist() {
        System.err.println("Username already exists");
    }
    /**
     * 输出用户用户名,姓名,用户类型,密码
     */
    public static void showUserInfo() {
        showUserUsername();
        showUserName();
        showUserIdentify();
        showUserPassword();
    }
    /**
     * 更新用户信息页面
     */
    public static void updateUserInfo() {
        UserView.showUserInfo();
        Output.printFormat(40);
        Output.formatterOutput("1.Update username", 30);
        Output.formatterOutput("2.Update name", 30);
        Output.formatterOutput("3.Update password", 30);
        Output.formatterOutput("0.Back", 30);
        Output.printFormat(40);
        System.out.println(">Please enter the options:");
        switch (Input.getInt("[0-3]")){
        case 0:
            break;
        case 1:
            System.out.println(">Please enter a new username:");
            updateUsername();
            break;
        case 2:
            System.out.println(">Please enter a new name:");
            updateName();
            break;
        case 3:
            System.out.println(">Please enter a new password:");
            updatePassword();
            break;
        }
    }
    /**
     * 更新密码
     */
    private static void updatePassword() {
        switch (UserController.updateUserPassword(Input.getString(20))){
        case 0:
            Main.fail();
            System.out.println("输入为空(bug)");
            break;
        case 1:
            Main.success();
            System.out.println("Password modification was successful.");
            break;
        case 2:
            Main.fail();
            break;
        }
    }
    /**
     * 更新用户名
     */
    public static void updateUsername() {
        switch (UserController.updateUserUsername(Input.getString(20))){
        case 0:
            Main.fail();
            System.out.println("输入为空(bug)");
            break;
        case 1:
            Main.success();
            break;
        case 2:
            Main.fail();
            System.err.println("Username already exists.");
            break;
        }
    }
    /**
     * 更新姓名
     */
    private static void updateName() {
        switch (UserController.updateUserName(Input.getString(20))){
        case 0:
            Main.fail();
            System.out.println("输入为空bug");
            break;
        case 1:
            Main.success();
            break;
        case 2:
            Main.fail();
            break;
        }
        
    }
    
    
    
    
    
    
}
