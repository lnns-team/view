package com.lnsf.book.view;

import java.util.List;

import com.lnsf.book.controller.CarController;
import com.lnsf.book.controller.MenuController;
import com.lnsf.book.dbutils.Input;
import com.lnsf.book.model.Car;

public class CarView {
    /**
     * 根据订单tid打印购物车详情 购物车id.菜单名 数量
     * 
     * @param tid
     */
    public static void showCar(int tid) {
        List<Car> list = CarController.getCarListByTid(tid);
        if (list.isEmpty()) {
            System.out.println("当前购物车为空");
        } else {
            for (Car c : list) {
                System.out.println(c.getId()
                        + "."
                        + (MenuController.getMenuByMenuId(c.getMenuid()))
                                .getName() + " 数量:" + c.getNum());
            }

        }

    }

    public static void updateCarByTidAndMid(int tid, int mid) {

        System.out.println("您选择的购物车条目为:");
        Car car = CarController.getCarByTidAndMid(tid, mid);
        System.out.println(car.getId() + "."
                + MenuController.getMenuByMenuId(car.getMenuid()).getName()
                + " 数量:" + car.getNum());
        System.out.println("请选择操作(1.修改数量 0.返回)");
        switch (Input.getInt("[0-1]")) {
        case 0:
            break;
        case 1:
            car.setNum(Input.getInt("([0-9])|([1-9][0-9]+)"));
            if (CarController.updateCar(car)) {
                Main.success();
            } else {
                Main.fail();
            }
            break;
        }
    }

}
