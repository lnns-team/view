package com.lnsf.book.view;

import java.util.Scanner;

import com.lnsf.book.controller.UserController;
import com.lnsf.book.dbutils.Input;

public class Main {
    public static void main(String[] args) {
        Main.mianView();
    }

    /**
     * 主界面 
     */
    public static void mianView() {
        do {
            System.out.println("***************************");
            System.out.println("*******欢迎使用饱了么********");
            System.out.println("***************************");
            System.out.println("\t 1.登录");
            System.out.println("\t 2.注册");
            System.out.println("***************************");
            System.out.println("请输入选项,或者按0退出.");
            switch (Input.getInt("[0-2]")) {
            case 0:
                System.out.println("------------------");
                System.out.println("您已经退出系统!");
                System.out.println("------------------");
                System.exit(1);
                break;
            case 1:
                UserView.login();
                break;
            case 2:
                UserView.register();
                break;
            }
        } while (true);

    }
    public static void loginFail(){
        System.out.println("登录失败");
    }
    /**
     * 操作失败输出
     */
    public static void fail(){
        System.err.println("操作失败");
    }
    /**
     * 操作成功
     */
    public static void success(){
        System.out.println("操作成功");
    }
    /**
     * 请重新输入
     */
    public static void again(){
        System.out.println("请重新输入:");
    }
    
    
    
}
