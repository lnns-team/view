package com.lnsf.book.view;

import java.util.List;

import com.lnsf.book.controller.AppraiseController;
import com.lnsf.book.controller.CarController;
import com.lnsf.book.controller.MenuController;
import com.lnsf.book.controller.RestaurantController;
import com.lnsf.book.controller.TradeController;
import com.lnsf.book.controller.UserController;
import com.lnsf.book.dbutils.Input;
import com.lnsf.book.model.Appraise;
import com.lnsf.book.model.Car;
import com.lnsf.book.model.Menu;
import com.lnsf.book.model.Restaurant;
import com.lnsf.book.model.Trade;

public class RestaurantView {

    public static void register() {
        System.out.println("请输入店铺名称:");
        Restaurant restaurant = new Restaurant(-1, UserController.USER.getId(),
                "", "");
        restaurant.setName(Input.getString(20));
        System.out.println("请输入店铺地址");
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
            System.out.println("-------------------------");
            System.out.println("|\tOhhhhhh倒霉了居然没餐厅\t|");
            System.out.println("-------------------------");
        } else {
            for (Restaurant r : list) {
                System.out.println(r.getId() + "." + r.getName() + " 店主:"
                        + UserController.getUsernameByUserId(r.getUserid())
                        + " 店铺地址:" + r.getAddress());
            }
        }
    }

    /**
     * 用户选择餐厅操作 测试
     */
    public static void operateRestaurant() {
        showAllRestaurant();
        System.out.println("<--\t请输入序号选择餐厅(输入0返回)\t-->");
        int rid = Input.getInt("([0-9])|([1-9][0-9]+)");
        if (rid == 0) {

        } else if (RestaurantController.isExist(rid)) {
            orderMenu(rid);
        } else {
            System.out.println("没有这个餐厅噢");
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
            System.out.println("无购物车,创建购物车");
            TradeController.init(new Trade(-1, UserController.USER.getId(), "",
                    rid, "未付款", "", -1));
            System.out.println("创建购物车成功");
        }
        List<Trade> list = TradeController
                .getTradeListIdByUseridAndRidAndStatus(
                        UserController.USER.getId(), rid, "未付款");// 根据用户id和店家id和状态返回一个List<Trade>
        if (list.isEmpty()) {
            System.err.println("购物车竟然为空???怎么肥事?");
        }
        Trade trade = list.get(0);
        int tid = trade.getId();
        while (true) {
            if ((TradeController.getTradeById(tid)).getStatus().equals("已付款")) {
                return;
            }
            System.out.println("当前的购物车:");
            CarView.showCar(tid);
            System.out
                    .println("请选择相应购物车商品id执行操作(0.退出 -1.添加商品 -2.老板我要买单! -3.查看该店铺的信息及评价):");
            int mid = Input.getInt("([0-9])|([1-9][0-9]+)|(-[1-2])");
            if (mid == 0) {
                return;
            } else if (mid == -1) {

                MenuView.showAllMenuByRid(rid);
                System.out.println("输入对应id添加:");
                int addmid = Input.getInt("([0-9])|([1-9][0-9]+)");
                if (MenuController.isExist(addmid, rid)
                        && (MenuController.getStockById(addmid) != 0)) {
                    System.out.println("选择的菜式:");
                    Menu addMenu = MenuController.getMenuByMenuId(addmid);
                    System.out.println(addMenu.getId() + "."
                            + addMenu.getName() + " 库存:" + addMenu.getStock());
                    System.out.println("请选择添加数量:");
                    int addNum = Input.getInt("([0-9])|([1-9][0-9]+)");
                    if (addNum > MenuController.getStockById(addmid)) {
                        System.out.println("输入的菜式数目大于库存量,添加失败");
                    } else {
                        addItemToCar(tid, addmid, addNum);
                        System.out.println("添加成功");
                    }
                } else {
                    System.out.println("菜式不存在或者库存为0");
                }

            } else if (CarController.isExist(tid, mid)) {

                CarView.updateCarByTidAndMid(tid, mid);

            } else if (mid == -2) {

                if (CarController.getCarListByTid(tid).isEmpty()) {
                    System.out.println("东西都没有还想买单?");
                } else {
                    TradeView.checkoutPlease(tid);
                }

            } else if (mid == -3) {

                showRestaurantInfo(rid);
                showRestaurantAppraise(rid);

            } else {
                System.out.println("条目应该不存在吧?仔细检查看看");
                Main.again();
            }
        }
    }

    private static void showRestaurantAppraise(int rid) {
        List<Appraise> list = AppraiseController.getAppraiseListByRid(rid);
        if (list.isEmpty()){
            System.out.println("这家店还没有用户评价它");
        } else {
            for (Appraise a : list){
                System.out.println("用户 " + UserController.getUsernameByUserId(a.getUid()) + " 评价这家店说:" + 
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
        System.out.println("***************************");
        System.out.println("1.更改店铺名");
        System.out.println("2.更改店铺地址");
        System.out.println("0.返回上一层");
        System.out.println("***************************");
        switch (Input.getInt("[0-2]")) {
        case 0:
            break;
        case 1:
            System.out.println("请输入新店铺名:");
            restaurant.setName(Input.getString(20));
            break;
        case 2:
            System.out.println("请输入新店铺地址:");
            restaurant.setAddress(Input.getString(40));
            break;
        }
        if (RestaurantController.updateRestaurant(restaurant)) {
            System.out.println("店铺信息更新成功");
        } else {
            System.out.println("店铺信息更新失败");
        }
    }

    private static void showRestaurantInfo(int rid) {
        Restaurant restaurant = RestaurantController.getRestaurantByRid(rid);
        System.out.println("店铺名:" + restaurant.getName());
        System.out.println("店主姓名:"
                + UserController.getUserById(restaurant.getUserid()).getName());
        System.out.println("店铺地址:" + restaurant.getAddress());
    }

}
