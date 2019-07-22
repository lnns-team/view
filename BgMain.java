package com.lnsf.book.view;

import com.lnsf.book.controller.MenuController;
import com.lnsf.book.controller.RestaurantController;
import com.lnsf.book.controller.UserController;
import com.lnsf.book.dbutils.Input;
import com.lnsf.book.dbutils.Output;
import com.lnsf.book.model.User;

public class BgMain {
    /**
     * 商家首页
     */
    public static void businessMainView(){
        do {
            Output.printFormat(40);
            Output.formatterOutput("1.View the list of orders", 30);
            Output.formatterOutput("2.View Menu", 30);
            Output.formatterOutput("3.View categories", 30);
            Output.formatterOutput("4.View personal information", 30);
            Output.formatterOutput("5.View restaurant information", 30);
            Output.formatterOutput("0.Log out", 30);
            Output.printFormat(40);
            System.out.println(">Please enter the options:");
            int choice = Input.getInt("[0-5]");
            switch (choice) {
            case 0:
                UserController.USER = new User(-1, "", -1, "", "");
                RestaurantController.RID = -1;
                Output.printFormat(40);
                Output.formatterOutput("You have logged out", 30);
                Output.printFormat(40);
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
            }
        } while (true);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
