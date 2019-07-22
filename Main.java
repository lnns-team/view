package com.lnsf.book.view;

import java.util.Scanner;

import com.lnsf.book.controller.UserController;
import com.lnsf.book.dbutils.Input;
import com.lnsf.book.dbutils.Output;

public class Main {
    public static void main(String[] args) {
        Main.mianView();
    }

    /**
     * 主界面 
     */
    public static void mianView() {
        do {
            System.out.println("🥚🥗🥛🌭🌮🌯🍠🥟🍤🌮🍗🥩🧀");
            System.out.println("🍔  Do you feel hungry ?? 🍯");
            System.out.println("🍨🍮🍭🍬🍫🍥🍿🍪🍩🍾🍷🍹🥤🍖");
            Output.printFormat(40);
            Output.formatterOutput("1.Very Hungry!(Sign In)", 30);
            Output.formatterOutput("2.Just a little bit.(Sign Up)", 30);
            Output.printFormat(40);
            System.out.println(">Please enter the options,Or enter 0 to exit:");
            switch (Input.getInt("[0-2]")) {
            case 0:
                Output.printFormat(40);
                Output.formatterOutput("You are out of the system", 30);
                Output.printFormat(40);
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
        System.err.println("Logon failure");
    }
    /**
     * 操作失败输出
     */
    public static void fail(){
        System.err.println("Operation failure");
    }
    /**
     * 操作成功
     */
    public static void success(){
        System.out.println("Operation succeeded");
    }
    /**
     * 请重新输入
     */
    public static void again(){
        System.out.println(">Please re-enter:");
    }
    
    
    
}
