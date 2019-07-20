package com.lnsf.book.view;

import com.lnsf.book.controller.MenuController;
import com.lnsf.book.controller.UserController;
import com.lnsf.book.dbutils.Input;
import com.lnsf.book.model.User;

public class FgMain {

    /**
     * 用户首页
     */
    public static void userMainView(){
        do {
            System.out.println("***************************\n");
            System.out.println("\t 1.选择餐厅\n");
            System.out.println("\t 2.我的购物车\n");
            System.out.println("\t 3.我的订单\n");
            System.out.println("\t 4.查看个人信息\n");
            System.out.println("\t 0.退出登录");
            System.out.println("***************************");
            System.out.print("请输入:");
            switch (Input.getInt("[0-4]")) {
            case 0:
                UserController.USER = new User(-1, "", -1, "", "");
                System.out.println("------------------");
                System.out.println("您已经退出登录!");
                System.out.println("------------------");
                return;
            case 1:
//                MenuController.showMenu();
                break;
            case 2:
//                TradeView.showShoppingCart();
                break;
            case 3:
                TradeView.showUserTrade();
                break;
            case 4:
                UserView.updateUserInfo();
                break;
            }
        } while (true);
    }
}
