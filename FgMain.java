package com.lnsf.book.view;

import com.lnsf.book.controller.MenuController;
import com.lnsf.book.controller.RestaurantController;
import com.lnsf.book.controller.UserController;
import com.lnsf.book.dbutils.Input;
import com.lnsf.book.model.User;

public class FgMain {

    /**
     * 用户首页
     */
    public static void userMainView(){
        do {
            System.out.println("-------------------------");
            System.out.println("|\t 1.选择餐厅 \t|");
            System.out.println("|\t 2.我的购物车 \t|");
            System.out.println("|\t 3.查看历史订单 \t|");
            System.out.println("|\t 4.查看个人信息 \t|");
            System.out.println("|\t 0.退出登录 \t|");
            System.out.println("-------------------------");
            System.out.println(">请输入: ");
            switch (Input.getInt("[0-4]")) {
            case 0:
                UserController.USER = new User(-1, "", -1, "", "");
                RestaurantController.RID = -1;
                System.out.println("-------------------------");
                System.out.println("|\t您已经退出登录! \t|");
                System.out.println("-------------------------\n");
                return;
            case 1:
                RestaurantView.operateRestaurant();
                break;
            case 2:
                TradeView.myShoppingCart();
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
