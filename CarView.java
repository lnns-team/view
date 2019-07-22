package com.lnsf.book.view;

import java.util.Formatter;
import java.util.List;

import com.lnsf.book.controller.CarController;
import com.lnsf.book.controller.MenuController;
import com.lnsf.book.dbutils.Input;
import com.lnsf.book.dbutils.Output;
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
            Output.printFormat(40);
            Output.formatterOutput("Shopping cart is empty.", 30);
            Output.printFormat(40);
        } else {
            Output.printFormat(55);
            Output.formatter.format("|%-10s|%-20s|%-10s|%-10s|\n", "Item Id", "Item name", "Price", "Number");
            for (Car c : list) {
                Menu menu = MenuController.getMenuByMenuId(c.getMenuid());
                Output.formatter.format("|%-10d|%-20s|%-10d|%-10d|\n", menu.getId(), menu.getName(), menu.getPrice(), 
                        c.getNum());
            }
            Output.printFormat(55);

        }

    }

    public static void updateCarByTidAndMid(int tid, int mid) {
        
        System.out.println("The shopping cart item you choose is:");
        Car car = CarController.getCarByTidAndMid(tid, mid);
        Output.printFormat(44);
        Output.formatter.format("|%-10s|%-20s|%-10s|\n", "Cart Id", "Menu name", "Number");
        System.out.printf("|%-10d|%-20s|%-10d|\n", car.getId(), 
                MenuController.getMenuByMenuId(car.getMenuid()).getName(), 
                car.getNum());
        Output.printFormat(44);
        System.out.println(">Please enter the options:(1.Modify item number2.Remove item 0.Back)");
        switch (Input.getInt("[0-2]")) {
        case 0:
            break;
        case 1:
            int stock = MenuController.getStockById(mid);
            if (stock == 0) {
                if (CarController.deleteByTidAndMid(tid, mid)) {
                    System.out.println("Item stock is 0.");
                    Main.fail();
                } else {
                    System.err.println("库存为0但是删除失败,不知道为什么(恭喜你找到了一个bug)");
                }
            } else {
                System.out.println(">Please enter the number of changes:(Item stock is:" + stock + ")");
                int updateNum = Input.getInt("([0-9])|([1-9][0-9]+)");
                if (updateNum > stock) {
                    System.out.println("Input value is greater than stock.");
                    Main.fail();
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
                System.out.println("Remove item succeed");
            } else {
                System.err.println("Remove item fail");
            }
        }
    }

}
