package com.lnsf.book.view;

import com.lnsf.book.controller.MenuController;
import com.lnsf.book.controller.RestaurantController;
import com.lnsf.book.controller.UserController;
import com.lnsf.book.dbutils.Input;
import com.lnsf.book.dbutils.Output;
import com.lnsf.book.model.User;

public class FgMain {

    /**
     * 用户首页
     */
    public static void userMainView(){
        do {
            Output.printFormat(40);
            Output.formatterOutput("1.Choose a restaurant", 30);
            Output.formatterOutput("2.My shopping cart", 30);
            Output.formatterOutput("3.View historical orders", 30);
            Output.formatterOutput("4.View personal information", 30);
            Output.formatterOutput("0.Log out", 30);
            Output.printFormat(40);
            System.out.println(">Please enter the options:");
            switch (Input.getInt("[0-4]")) {
            case 0:
                UserController.USER = new User(-1, "", -1, "", "");
                RestaurantController.RID = -1;
                Output.printFormat(40);
                Output.formatterOutput("You have logged out", 30);
                Output.printFormat(40);
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
