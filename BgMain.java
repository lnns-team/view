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
            System.out.println("-------------------------");
            System.out.println("|\t 1.查看订单 \t|");
            System.out.println("|\t 2.查看菜单 \t|");
            System.out.println("|\t 3.查看类别 \t|");
            System.out.println("|\t 4.查看个人信息 \t|");
            System.out.println("|\t 5.查看店铺信息 \t|");
            System.out.println("|\t 0.退出登录 \t|");
            System.out.println("-------------------------");
            System.out.println(">请输入:");
            int choice = Input.getInt("[0-5]");
            switch (choice) {
            case 0:
                UserController.USER = new User(-1, "", -1, "", "");
                RestaurantController.RID = -1;
                System.out.println("-------------------------");
                System.out.println("|\t您已经退出登录! \t|");
                System.out.println("-------------------------\n");
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
            case 5:
                RestaurantView.updateRestaurantInfo();
                break;
            default:
                System.err.println("无此操作");
                break;
            }
        } while (true);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
