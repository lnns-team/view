package com.lnsf.book.view;

import java.awt.MenuContainer;

import com.lnsf.book.controller.MenuController;
import com.lnsf.book.controller.RestaurantController;
import com.lnsf.book.controller.UserController;
import com.lnsf.book.dbutils.Input;
import com.lnsf.book.model.User;

public class UserView {
    /**
     * 登录页面
     */
    public static void login(){
        System.out.println("请输入用户名:");
        String username = Input.getString();
        System.out.println("请输入密码:");
        String password = Input.getString();
        switch (UserController.login(username, password)){
        case 0:
            Main.loginFail();
            return;
        case 1:
            RestaurantController.RID = -1;// 登录完这个值设置-1
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
        System.out.println("请输入需要注册的用户类型(1.普通用户 2.商家用户):");
        UserController.USER.setIdentify(Input.getInt("[1-2]"));
        System.out.println("请输入姓名");
        UserController.USER.setName(Input.getString(20));
        System.out.println("请输入用户名:");
        UserController.USER.setUsername(Input.getString(20));
        System.out.println("请输入密码:");
        UserController.USER.setPassword(Input.getString(20));
        if (UserController.registerUser(UserController.USER)){
          Main.success();
          if (UserController.USER.getIdentify() == 1){
              
          } else if (UserController.USER.getIdentify() == 2){
              UserController.USER.setId(
                      UserController.getUserIdByUsername(UserController.USER.getUsername()));
              RestaurantView.register();
          } else {
              System.err.println("身份既不是用户也不是商家错误");
          }
        } else {
          Main.fail();
          System.err.println("注册失败,可能是用户名重复,请重新输入用户名");
        }
        UserController.USER = new User(-1, "", -1, "", "");// 注册完全局用户置空,回跳回登录
    }
    /**
     * 用户登录成功页面
     */
    public static void loginSuccessful(){
        if (UserController.USER.getIdentify() == 1){
            System.out.println("用户登录成功");
            System.out.println(UserController.USER.getName() + "用户,欢迎您!");
        } else if (UserController.USER.getIdentify() == 2){
            System.out.println("商家登录成功");
            System.out.println(UserController.USER.getName() + "老板,欢迎您!");
        }
    }
    /**
     * 用户权限未找到
     */
    public static void userIdentifyNotFound(){
        System.err.println("用户权限未找到");
    }
    /**
     * 用户登录失败
     */
    public static void userLoginFailed(){
        System.err.println("用户登录失败");
    }
    /**
     * 显示当前用户类型
     */
    public static void showUserIdentify(){
        if (UserController.USER.getIdentify() == 1){
            System.out.println("您是普通用户");
        } else if (UserController.USER.getIdentify() == 2){
            System.out.println("您是商家用户");
        } else {
            userIdentifyNotFound();
        }
    }
    public static void nameIsEmpty() {
        System.err.println("姓名为空");
    }
    public static void userNameIsEmpty() {
        System.err.println("用户名为空");
    }
    public static void userPasswordIsEmpty() {
        System.err.println("密码为空");
    }
    public static void showUserName() {
        System.out.println("您的姓名为:" + UserController.USER.getName());
    }
    public static void showUserUsername() {
        System.out.println("您的用户名为:" + UserController.USER.getUsername());
    }
    public static void showUserPassword() {
        System.out.println("您的密码为:" + UserController.USER.getPassword());
    }
    public static void userNameExist() {
        System.err.println("用户名已存在");
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
        System.out.println("***************************");
        System.out.println("1.更改用户名");
        System.out.println("2.更改姓名");
        System.out.println("3.更改密码");
        System.out.println("0.返回上一层");
        System.out.println("***************************");
        switch (Input.getInt("[0-3]")){
        case 0:
            break;
        case 1:
            System.out.println("请输入新用户名:");
            updateUsername();
            break;
        case 2:
            System.out.println("请输入新姓名:");
            updateName();
            break;
        case 3:
            System.out.println("请输入新密码:");
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
            System.out.println("输入为空");
            break;
        case 1:
            Main.success();
            System.out.println("密码修改成功");
            break;
        case 2:
            Main.fail();
            System.out.println("密码修改失败");
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
            System.out.println("输入为空");
            break;
        case 1:
            Main.success();
            System.out.println("用户名修改成功");
            break;
        case 2:
            Main.fail();
            System.out.println("用户名重复");
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
            System.out.println("输入为空");
            break;
        case 1:
            Main.success();
            System.out.println("姓名修改成功");
            break;
        case 2:
            Main.fail();
            System.out.println("姓名修改失败");
            break;
        }
        
    }
    
    
    
    
    
    
}
