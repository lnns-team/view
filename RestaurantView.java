package com.lnsf.book.view;

import com.lnsf.book.controller.RestaurantController;
import com.lnsf.book.controller.UserController;
import com.lnsf.book.dbutils.Input;
import com.lnsf.book.model.Restaurant;

public class RestaurantView {

    public static void register() {
        System.out.println("请输入店铺名称:");
        Restaurant restaurant = new Restaurant(-1, UserController.USER.getId(), "", "");
        restaurant.setName(Input.getString(20));
        System.out.println("请输入店铺地址");
        restaurant.setAddress(Input.getString(40));
        if (RestaurantController.insertRestaurant(restaurant)){
            Main.success();
        } else {
            Main.fail();
        }
    }

}
