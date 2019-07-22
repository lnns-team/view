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
            System.out.println("🥚🥗🥛🌭🌮🌯🍠🥟🍤🍖🍗🥩🧀");
            System.out.println("🌮🌯🍤 欢迎使用🍔了么🍫🍿🍪🍾");
            System.out.println("🍨🍮🍭🍬🍫🍥🍿🍪🍩🍾🍷🍹🥤🍯");
            System.out.println("-------------------------");
            System.out.println("|\t 1.登录🥢  \t|");
            System.out.println("|\t 2.注册🍽  \t|");
            System.out.println("-------------------------");
            System.out.println("<--请输入选项,或者按0退出.-->");
            switch (Input.getInt("[0-2]")) {
            case 0:
                System.out.println("🥚🥗🥛🌭🌮🌯🍠🥟🍤🍖🍗🥩🧀");
                System.out.println("\t您已经退出系统!");
                System.out.println("🍨🍮🍭🍬🍫🍥🍿🍪🍩🍾🍷🍹🥤🍯");
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
        System.out.println("<--\t登录失败\t\t-->");
    }
    /**
     * 操作失败输出
     */
    public static void fail(){
        System.err.println("<--\t操作失败\t\t-->");
    }
    /**
     * 操作成功
     */
    public static void success(){
        System.out.println("<--\t操作成功\t\t-->");
    }
    /**
     * 请重新输入
     */
    public static void again(){
        System.out.println("<--\t请重新输入:\t-->");
    }
    
    
    
}
