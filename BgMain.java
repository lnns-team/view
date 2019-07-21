package com.lnsf.book.view;

import com.lnsf.book.controller.MenuController;
import com.lnsf.book.controller.RestaurantController;
import com.lnsf.book.controller.UserController;
import com.lnsf.book.dbutils.Input;
import com.lnsf.book.model.User;

public class BgMain {
    /**
     * 商家首页
     */
    public static void businessMainView(){
        do {
            System.out.println("你的店铺id是:" + RestaurantController.RID);
            System.out.println("***************************\n");
            System.out.println("\t 1.查看订单\n");
            System.out.println("\t 2.查看菜单\n");
            System.out.println("\t 3.查看类别\n");
            System.out.println("\t 4.查看个人信息\n");
            System.out.println("\t 0.退出登录");
            System.out.println("***************************");
            System.out.println("请输入:");
            int choice = Input.getInt();
            switch (choice) {
            case 0:
                UserController.USER = new User(-1, "", -1, "", "");
                System.out.println("------------------");
                System.out.println("您已经退出登录!");
                System.out.println("------------------");
                return;
            case 1:
                TradeView.operateTrade();
                break;
            case 2:
                MenuView.operateMenu();
                break;
            case 3:
                TypeView.operateType();
                break;
            case 4:
                UserView.updateUserInfo();
                break;
            default:
                System.err.println("无此操作");
                break;
            }
        } while (true);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
