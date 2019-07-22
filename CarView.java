package com.lnsf.book.view;

import java.util.List;

import com.lnsf.book.controller.CarController;
import com.lnsf.book.controller.MenuController;
import com.lnsf.book.dbutils.Input;
import com.lnsf.book.model.Car;
import com.lnsf.book.model.Menu;

public class CarView {
    /**
     * 根据订单tid打印购物车详情 购物车id.菜单名 单价 数量
     * 
     * @param tid
     */
    public static void showCar(int tid) {
        List<Car> list = CarController.getCarListByTid(tid);
        if (list.isEmpty()) {
            System.out.println("当前购物车为空");
        } else {
            for (Car c : list) {
                Menu menu = MenuController.getMenuByMenuId(c.getMenuid());
                System.out.println("\t" + "购物车id:" + c.getId() + " 商品id:" + menu.getId()
                        + " 菜名:" + menu.getName() + " 单价:" + menu.getPrice()
                        + " 数量:" + c.getNum());
            }

        }

    }

    public static void updateCarByTidAndMid(int tid, int mid) {

        System.out.println("您选择的购物车条目为:");
        Car car = CarController.getCarByTidAndMid(tid, mid);
        System.out.println(car.getId() + "."
                + MenuController.getMenuByMenuId(car.getMenuid()).getName()
                + " 数量:" + car.getNum());
        System.out.println("请选择操作(1.修改数量 2.删除该项 0.返回)");
        switch (Input.getInt("[0-2]")) {
        case 0:
            break;
        case 1:
            int stock = MenuController.getStockById(mid);
            if (stock == 0) {
                if (CarController.deleteByTidAndMid(tid, mid)) {
                    System.out.println("该商品目前库存为0,无法继续修改,现已将其在购物车删除");
                } else {
                    System.out.println("库存为0但是删除失败,不知道为什么");
                }
            } else {
                System.out.println("请输入修改数量:(当前库存为:" + stock + ")");
                int updateNum = Input.getInt("([0-9])|([1-9][0-9]+)");
                if (updateNum > stock) {
                    System.out.println("输入的数值大于库存.修改失败.");
                } else {
                    car.setNum(updateNum);
                    if (CarController.updateCar(car)) {
                        Main.success();
                    } else {
                        Main.fail();
                    }
                }
            }
            break;
        case 2:
            if (CarController.deleteByTidAndMid(tid, mid)) {
                System.out.println("删除成功");
            } else {
                System.out.println("删除失败");
            }
        }
    }

}
