package com.lnsf.book.view;

import java.util.List;

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

    public static void showAllRestaurant() {
        List<Restaurant> list = RestaurantController.getAllRestaurantList();
        if (list.isEmpty()){
            System.out.println("Ohhhhhh倒霉了居然没餐厅");
        } else {
            for (Restaurant r : list){
                System.out.println(r.getId() + "." + r.getName() + " 店主:" + 
            r.getUserid() + " 店铺地址:" + r.getAddress());
            }
        }
    }

    public static void operateRestaurant() {
        showAllRestaurant();
        System.out.println("请输入序号选择餐厅(输入0返回)");
        int rid = Input.getInt();
        if (rid == 0){
            
        } else if (RestaurantController.isExist(rid)){
            
        } else {
            System.out.println("没有这个餐厅噢");
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}
