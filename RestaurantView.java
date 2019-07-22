package com.lnsf.book.view;

import java.util.List;

import com.lnsf.book.controller.AppraiseController;
import com.lnsf.book.controller.CarController;
import com.lnsf.book.controller.MenuController;
import com.lnsf.book.controller.RestaurantController;
import com.lnsf.book.controller.TradeController;
import com.lnsf.book.controller.UserController;
import com.lnsf.book.dbutils.Input;
import com.lnsf.book.dbutils.Output;
import com.lnsf.book.model.Appraise;
import com.lnsf.book.model.Car;
import com.lnsf.book.model.Menu;
import com.lnsf.book.model.Restaurant;
import com.lnsf.book.model.Trade;

public class RestaurantView {

    public static void register() {
        System.out.println(">Please enter a restaurant name:");
        Restaurant restaurant = new Restaurant(-1, UserController.USER.getId(),
                "", "");
        restaurant.setName(Input.getString(20));
        System.out.println(">Please enter a restaurant address:");
        restaurant.setAddress(Input.getString(40));
        if (RestaurantController.insertRestaurant(restaurant)) {
            Main.success();
        } else {
            Main.fail();
        }
    }

    public static void showAllRestaurant() {
        List<Restaurant> list = RestaurantController.getAllRestaurantList();
        if (list.isEmpty()) {
            System.err.println("Ohhh restaurant not found");
        } else {
            for (Restaurant r : list) {
                System.out.println(r.getId() + "." + r.getName() + " Owner:"
                        + UserController.getUsernameByUserId(r.getUserid())
                        + " Restaurant address:" + r.getAddress());
            }
        }
    }

    /**
     * 用户选择餐厅操作 测试
     */
    public static void operateRestaurant() {
        showAllRestaurant();
        System.out.println(">Please enter a restaurant id:(0.Back)");
        int rid = Input.getInt("([0-9])|([1-9][0-9]+)");
        if (rid == 0) {

        } else if (RestaurantController.isExist(rid)) {
            orderMenu(rid);
        } else {
            System.out.println("Restaurant not found");
        }
    }

    /**
     * 用户根据店铺id来点餐啦
     * 
     * @param rid
     */
    public static void orderMenu(int rid) {
        if (!TradeController.isExistByUserIdAndRidAndStatus(
                UserController.USER.getId(), rid, "未付款")) {
            TradeController.init(new Trade(-1, UserController.USER.getId(), "",
                    rid, "未付款", "", -1));
        }
        List<Trade> list = TradeController
                .getTradeListIdByUseridAndRidAndStatus(
                        UserController.USER.getId(), rid, "未付款");// 根据用户id和店家id和状态返回一个List<Trade>
        if (list.isEmpty()) {
            System.err.println("购物车竟然为空???怎么肥事?(这是一个bug)");
        }
        Trade trade = list.get(0);
        int tid = trade.getId();
        while (true) {
            if ((TradeController.getTradeById(tid)).getStatus().equals("已付款")) {
                return;
            }
            System.out.println("Your shopping cart:");
            CarView.showCar(tid);
            System.out
                    .println(">Please enter a shopping cart item id:(0.Back -1.Add item -2.Checkout -3.View more information):");
            int mid = Input.getInt("([0-9])|([1-9][0-9]+)|(-[1-3])");
            if (mid == 0) {
                return;
            } else if (mid == -1) {

                MenuView.showAllMenuByRid(rid);
                System.out.println(">Please enter a item id:");
                int addmid = Input.getInt("([0-9])|([1-9][0-9]+)");
                if (MenuController.isExist(addmid, rid)
                        && (MenuController.getStockById(addmid) != 0)) {
                    System.out.println("The items you choose:");
                    Menu addMenu = MenuController.getMenuByMenuId(addmid);
                    System.out.println(addMenu.getId() + "."
                            + addMenu.getName() + " Item stock:" + addMenu.getStock());
                    System.out.println(">Please enter choose quantity:");
                    int addNum = Input.getInt("([0-9])|([1-9][0-9]+)");
                    if (addNum > MenuController.getStockById(addmid)) {
                        System.out.println("Input value is greater than stock");
                        Main.fail();
                    } else {
                        addItemToCar(tid, addmid, addNum);
                    }
                } else {
                    System.out.println("Item not found or item stock is 0");
                }

            } else if (CarController.isExist(tid, mid)) {

                CarView.updateCarByTidAndMid(tid, mid);

            } else if (mid == -2) {

                if (CarController.getCarListByTid(tid).isEmpty()) {
                    System.err.println("Your shopping cart is empty");
                } else {
                    TradeView.checkoutPlease(tid);
                }

            } else if (mid == -3) {

                showRestaurantInfo(rid);
                showRestaurantAppraise(rid);

            } else {
                System.out.println("Item not found");
            }
        }
    }

    private static void showRestaurantAppraise(int rid) {
        List<Appraise> list = AppraiseController.getAppraiseListByRid(rid);
        if (list.isEmpty()){
            System.out.println("Restaurant review is empty");
        } else {
            for (Appraise a : list){
                System.out.println("User:" + UserController.getUsernameByUserId(a.getUid()) + "'s review:" + 
                        a.getAbout());
            }
        }
    }

    /**
     * 添加指定数量商品到购物车,存在则原数量增加,不存在则插入新记录
     * 
     * @param tid
     * @param mid
     * @param int1
     */
    private static void addItemToCar(int tid, int mid, int num) {
        if (CarController.isExist(tid, mid)) {
            Car car = CarController.getCarByTidAndMid(tid, mid);
            car.setNum(num + car.getNum());
            if (CarController.updateCar(car)) {
                Main.success();
            } else {
                Main.fail();
            }
        } else {
            if (CarController.insertCar(new Car(-1, mid, num, tid))) {
                Main.success();
            } else {
                Main.fail();
            }
        }
    }

    public static void updateRestaurantInfo() {
        Restaurant restaurant = RestaurantController
                .getRestaurantByRid(RestaurantController.RID);
        RestaurantView.showRestaurantInfo(restaurant.getId());
        Output.printFormat(40);
        Output.formatterOutput("1.Update restaurant name", 30);
        Output.formatterOutput("2.Update restaurant address", 30);
        Output.formatterOutput("0.Back", 30);
        Output.printFormat(40);
        switch (Input.getInt("[0-2]")) {
        case 0:
            break;
        case 1:
            System.out.println(">Please enter a new restaurant name:");
            restaurant.setName(Input.getString(20));
            break;
        case 2:
            System.out.println(">Please enter a new restaurant address:");
            restaurant.setAddress(Input.getString(40));
            break;
        }
        if (RestaurantController.updateRestaurant(restaurant)) {
            Main.success();
        } else {
            Main.fail();
        }
    }

    private static void showRestaurantInfo(int rid) {
        Restaurant restaurant = RestaurantController.getRestaurantByRid(rid);
        System.out.println("Restaurant name:" + restaurant.getName());
        System.out.println("Owner name:"
                + UserController.getUserById(restaurant.getUserid()).getName());
        System.out.println("Restaurant address:" + restaurant.getAddress());
    }

}
