package com.lnsf.book.view;

import java.util.List;

import com.lnsf.book.controller.CarController;
import com.lnsf.book.controller.MenuController;
import com.lnsf.book.controller.RestaurantController;
import com.lnsf.book.controller.TradeController;
import com.lnsf.book.controller.UserController;
import com.lnsf.book.dbutils.Input;
import com.lnsf.book.model.Car;
import com.lnsf.book.model.Menu;
import com.lnsf.book.model.Restaurant;
import com.lnsf.book.model.Trade;

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
            UserController.getUsernameByUserId(r.getUserid()) + " 店铺地址:" + r.getAddress());
            }
        }
    }
    /**
     * 用户选择餐厅操作
     * 测试
     */
    public static void operateRestaurant() {
        showAllRestaurant();
        System.out.println("请输入序号选择餐厅(输入0返回)");
        int rid = Input.getInt();
        System.out.println("rid" + rid);
        if (rid == 0){
            System.out.println("输入为0");
        } else if (RestaurantController.isExist(rid)){
            System.out.println("进入了这一层q");
            if (!TradeController.isExistByUserIdAndRidAndStatus(
                    UserController.USER.getId(), rid, "未付款")){
                System.out.println("无购物车,创建购物车");
                TradeController.init(new Trade(-1, UserController.USER.getId(), 
                        "", rid, "未付款", "", -1));
                System.out.println("创建购物车成功");
            } 
            List<Trade> list = TradeController.getTradeListIdByUseridAndRidAndStatus(UserController.USER.getId(), 
                    rid, "未付款");// 根据用户id和店家id和状态返回一个List<Trade>
            if (list.isEmpty()){
                System.err.println("购物车竟然为空???怎么肥事?");
            }
            Trade trade = list.get(0);
            orderMenu(rid, trade.getId());
        } else {
            System.out.println("没有这个餐厅噢");
        }
    }

    private static void orderMenu(int rid, int tid) {
        while (true){
            if ((TradeController.getTradeById(tid)).getStatus().equals("已付款")){
                return;
            }
            System.out.println("当前的购物车:");
            CarView.showCar(tid);
            System.out.println("请选择相应购物车商品执行操作(0.退出 -1.添加商品 -2.老板我要买单!):");
            int mid = Input.getInt();
            if (mid == 0){
                return;
            } else if (mid == -1){
                MenuView.showAllMenuByRid(rid);
                System.out.println("输入对应id添加:");
                int addmid = Input.getInt();
                if (MenuController.isExist(addmid, rid)){
                    System.out.println("选择的菜式:");
                    Menu addMenu = MenuController.getMenuByMenuId(addmid);
                    System.out.println(addMenu.getId() + "." + addMenu.getName());
                    System.out.println("请选择添加数量:");
                    addItemToCar(tid, addmid, Input.getInt("([0-9])|([1-9][0-9]+)"));
                    System.out.println("添加成功");
                } else {
                    System.out.println("菜式不存在");
                }
            } else if (CarController.isExist(tid, mid)){
                CarView.updateCarByTidAndMid(tid, mid);
            } else if (mid == -2){
                TradeView.checkoutPlease(tid);
            } else {
                System.out.println("条目应该不存在吧?仔细检查看看");
                Main.again();
            }
        }
    }
    /**
     * 添加指定数量商品到购物车,存在则原数量增加,不存在则插入新记录
     * @param tid
     * @param mid
     * @param int1
     */
    private static void addItemToCar(int tid, int mid, int num) {
        if (CarController.isExist(tid, mid)){
            Car car = CarController.getCarByTidAndMid(tid, mid);
            car.setNum(num + car.getNum());
            if (CarController.updateCar(car)){
                Main.success();
            } else {
                Main.fail();
            }
        } else {
            if (CarController.insertCar(new Car(-1, mid, num, tid))){
                Main.success();
            } else {
                Main.fail();
            }
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}
