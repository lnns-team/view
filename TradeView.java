package com.lnsf.book.view;

import java.io.ObjectInputStream.GetField;
import java.util.List;

import com.lnsf.book.controller.AppraiseController;
import com.lnsf.book.controller.CarController;
import com.lnsf.book.controller.MenuController;
import com.lnsf.book.controller.RestaurantController;
import com.lnsf.book.controller.TradeController;
import com.lnsf.book.controller.UserController;
import com.lnsf.book.dbutils.Input;
import com.lnsf.book.dbutils.Output;
import com.lnsf.book.model.Car;
import com.lnsf.book.model.Menu;
import com.lnsf.book.model.Trade;

public class TradeView {

    public static void operateTrade() {
        Output.printFormat(40);
        Output.formatterOutput("1.View unshipped orders", 30);
        Output.formatterOutput("2.View completed orders", 30);
        Output.formatterOutput("0.Back", 30);
        Output.printFormat(40);
        switch (Input.getInt("[0-2]")) {
        case 0:
            break;
        case 1:
            showUnfinishedTrade();
            System.out.println(">Please enter order id:(0.Back)");
            int tradeId = Input.getInt("([0-9])|([1-9][0-9]+)");
            if (tradeId == 0) {

            } else if (TradeController.unfinishedTradeIsExist(tradeId,
                    RestaurantController.RID)) {
                System.out.println("Order information:");
                showTradeItemByTid(tradeId);
                System.out.println(">Please enter the options:(1.Update to delivery 0.Back)");
                switch (Input.getInt("[0-1]")) {
                case 0:
                    return;
                case 1:
                    if (TradeController.updateStatusToDelivered(tradeId)) {
                        System.out.println("The order was delivered successfully");
                    } else {
                        Main.fail();
                    }
                }
            } else {
                System.out.println("There is no such unfinished order");
            }
            break;
        case 2:
            showFinishedTrade();
            break;
        }
    }

    /**
     * 商家调用,查看订单详情
     * 
     * @param tradeId
     */
    private static void showTradeItemByTid(int tradeId) {
        List<Car> list = CarController.getCarListByTid(tradeId);
        if (list.isEmpty()) {
            System.out.println("提交的订单居然为空?又想白嫖东西了?(这是一个bug)");
        } else {
            for (Car c : list) {
                Menu menu = MenuController.getMenuByMenuId(c.getMenuid());
                System.out.println("\tItem id:" + c.getMenuid() + " Item name:"
                        + menu.getName() + " Item price:" + menu.getPrice() + " Item number:"
                        + c.getNum());
            }
            System.out.println("The total amount of items:"
                    + TradeController.getTradeById(tradeId).getMoney());
        }
    }

    private static void showFinishedTrade() {
        List<Trade> list = TradeController.getTradeListByRidAndNotInStatus(RestaurantController.getRidByUserId(
                UserController.USER.getId()), "未付款");
        if (list.isEmpty()) {
            System.out.println("The list of finished orders is empty");
        } else {
            for (Trade t : list) {
                System.out.println("Order id:" + t.getId() + " Order status:" + t.getStatus() + " Order amount:"
                        + t.getMoney() + " The consignee:"
                        + TradeController.getUsernameByUserId(t.getUserid())
                        + " Shipping address:" + t.getAddress());
            }
            System.out.println(">Please enter order id for more information(0.Back)");
            int tradeId = Input.getInt("([0-9])|([1-9][0-9]+)");
            if (tradeId == 0){
                return;
            } else if (TradeController.isExistByUseridAndTidAndNotInStatus(
                    UserController.USER.getId(), tradeId, "未付款")){
                System.out.println("The order you choose:");
                showTradeInfo(TradeController.getTradeById(tradeId));
                showTradeItemByTid(tradeId);
            } else {
                System.err.println("Order not found");
            }
        }
    }

    /**
     * 显示商家未发货订单
     */
    private static void showUnfinishedTrade() {
        List<Trade> list = TradeController
                .getUnfinishedTradeById(RestaurantController.getRidByUserId(UserController.USER.getId()));
        if (list.isEmpty()) {
            System.out.println("Unshipped order list is empty");
        } else {
            Output.printFormat(85);
            Output.formatter.format("|%-10s|%-15s|%-20s|%-40s|\n", "Order Id", "Order amount", "The consignee", "Shipping address");
            for (Trade t : list) {
                System.out.printf("|%-10d|%-15d|%-20s|%-40s|\n", t.getId(), t.getMoney(), 
                        UserController.getUserById(t.getUserid()).getName(), t.getAddress());
            }
            Output.printFormat(85);
        }
    }

    public static void showUserTrade() {
        List<Trade> tradeList = TradeController
                .getTradeListByUseridAndNotInStatus(
                        UserController.USER.getId(), "未付款");
        if (tradeList.isEmpty()) {
            System.out.println("You don't have such order.");
        } else {
            tradeManagement(tradeList);
        }
    }

    private static void tradeManagement(List<Trade> tradeList) {
        for (Trade trade : tradeList) {
            System.out.println("Order id:" + trade.getId() + " Restaurant name:"
                    + RestaurantController.getNameByRid(trade.getRid())
                    + " Order status:" + trade.getStatus());
        }
        System.out.println(">Please enter a order id:(0.Back):");
        int tid = Input.getInt("([0-9])|([1-9][0-9]+)");
        if (tid == 0) {
            return;
        } else if (TradeController.isExistByUseridAndTidAndNotInStatus(
                UserController.USER.getId(), tid, "未付款")) {
            Trade trade = TradeController.getTradeById(tid);
            if (trade.getStatus().equals("已付款")) {
                
                System.out.println("The order you choose:");
                TradeView.showTradeInfo(trade);
                CarView.showCar(trade.getId());
                System.out.println(">Please enter the options:(1.Refund 0.Back)");
                switch (Input.getInt("[0-1]")) {
                case 0:
                    break;
                case 1:
                    List<Car> carList = CarController.getCarListByTid(trade.getId());
                    for (Car c : carList){
                        Menu menu = MenuController.getMenuByMenuId(c.getMenuid());
                        menu.setStock(menu.getStock() + c.getNum());
                        if (MenuController.updateMenu(menu)){
                            
                        } else {
                            System.out.println("退款时对应菜式的库存更新失败(这是一个bug)");
                        }
                    }
                    changeTradeStatusToWhat(trade, "已退款");
                    break;
                }
                
            } else if (trade.getStatus().equals("已发货")) {
                
                System.out.println("The order you choose:");
                TradeView.showTradeInfo(trade);
                CarView.showCar(trade.getId());
                System.out.println(">Please enter the options:(1.I got the items. 0.Back):");
                switch (Input.getInt("[0-1]")) {
                case 0:
                    break;
                case 1:
                    changeTradeStatusToWhat(trade, "已完成");
                    break;
                }
                
            } else if (trade.getStatus().equals("已完成")) {
                
                System.out.println("The order has been completed,review the order?");
                System.out.println("The order you choose:");
                TradeView.showTradeInfo(trade);
                CarView.showCar(trade.getId());
                System.out.println(">Please enter the options:(1.review 0.Back):");
                switch (Input.getInt("[0-1]")) {
                case 0:
                    break;
                case 1:
                    if (AppraiseController.isExist(UserController.USER.getId(), trade.getRid())){
                        System.out.print("Your review:");
                        System.out.println(AppraiseController.getAboutByUidAndRid(UserController.USER.getId(), trade.getRid()));
                        System.out.println(">Please enter the options:(1.Update review 0.Back):");
                        switch (Input.getInt("[0-1]")){
                        case 0:
                            break;
                        case 1:
                            System.out.println(">Please enter a new review:");
                            if (AppraiseController.updateAbout(UserController.USER.getId(), 
                                    trade.getRid(), Input.getString(40))){
                                Main.success();
                                changeTradeStatusToWhat(trade, "已评价");
                            } else {
                                Main.fail();
                            }
                        }
                    } else {
                        System.out.println(">Please enter a review:");
                        if (AppraiseController.insertAbout(UserController.USER.getId(), 
                                trade.getRid(), Input.getString(40))){
                            Main.success();
                            changeTradeStatusToWhat(trade, "已评价");
                        } else {
                            Main.fail();
                        }
                    }
                    break;
                }
                
            } else if (trade.getStatus().equals("已评价")) {
                
                System.out.println("Your review:");
                AppraiseController.getAboutByUidAndRid(UserController.USER.getId(), trade.getRid());
                System.out.println(">Please enter the options:(1.Update review 0.Back):");
                switch (Input.getInt("[0-1]")){
                case 0:
                    break;
                case 1:
                    System.out.println(">Please enter a new review:");
                    if (AppraiseController.updateAbout(UserController.USER.getId(), 
                            trade.getRid(), Input.getString(40))){
                        Main.success();
                        changeTradeStatusToWhat(trade, "已评价");
                    } else {
                        Main.fail();
                    }
                }
                
            } else if (trade.getStatus().equals("已退款")) {

                System.out.println("You can't operate.Haha...");
                
            } else {
                System.out.println("神奇的订单状态,不信你看一下数据库(bug)");
            }
        } else {
            System.err.println("Order id not found");
        }
    }

    private static void changeTradeStatusToWhat(Trade trade, String what) {
        trade.setStatus(what);
        if (TradeController.update(trade)) {
            System.out.println("Update order information successfully");
        } else {
            System.out.println("Failed to update order information");
        }
    }

    /**
     * 显示订单id,店铺名,订单状态,订单金额
     * 
     * @param trade
     */
    private static void showTradeInfo(Trade trade) {
        System.out.println("Order id:" + trade.getId() + " Restaurant name:"
                + RestaurantController.getNameByRid(trade.getRid()) + " Order status:"
                + trade.getStatus() + " :" + trade.getMoney());
    }

    /**
     * 根据tid买单啦老板们
     * 
     * @param tid
     */
    public static void checkoutPlease(int tid) {
        Trade trade = TradeController.getTradeById(tid);
        if (trade.getUsertele().equals("")) {
            System.out.println("The phone number is empty.");
            System.out.println(">Please enter your phone number:");
            trade.setUsertele(Input.getString(20));
        }
        if (trade.getAddress().equals("")) {
            System.out.println("The shipping address is empty.");
            System.out.println(">Please enter your shipping address:");
            trade.setAddress(Input.getString(40));
        }
        System.out.println("Shopping cart information:");
        CarView.showCar(trade.getId());
        int sum = 0;
        List<Car> list = CarController.getCarListByTid(tid);
        for (Car c : list) {
            sum += c.getNum()
                    * MenuController.getMenuByMenuId(c.getMenuid())
                            .getPrice();
        }
        System.out.println("The consignee is :" + UserController.USER.getName());
        System.out.println("The phone number is :" + trade.getUsertele());
        System.out.println("The shipping address is :" + trade.getAddress());
        System.out.println("The total amount of items is " + sum + "yuan.");
        System.out.println(">Please enter the options:(1.Confirm the payment 0.Back)");
        switch (Input.getInt("[0-1]")) {
        case 0:
            if (TradeController.update(trade)) {
                Main.success();
            } else {
                Main.fail();
            }
            break;
        case 1:
            trade.setMoney(sum);
            trade.setStatus("已付款");
            for (Car c : list){
                Menu menu = MenuController.getMenuByMenuId(c.getMenuid());
                if (menu.getStock() < c.getNum()){
                    System.out.println("Item:" + menu.getId() + "." + menu.getName() + "'s stock is less than" + c.getNum() + ".");
                    Main.fail();
                    return;
                }
            }
            if (TradeController.update(trade)) {
                for (Car c : list){
                    Menu menu = MenuController.getMenuByMenuId(c.getMenuid());
                    menu.setStock(menu.getStock() - c.getNum());
                    MenuController.updateMenu(menu);
                }
                Main.success();
            } else {
                Main.fail();
            }
            break;
        }
    }

    public static void myShoppingCart() {
        System.out.println("My shopping cart:");
        List<Trade> tradeList = TradeController.getTradeListByUseridAndStatus(
                UserController.USER.getId(), "未付款");
        if (tradeList.isEmpty()) {
            System.out.println("Shopping cart is empty.");
        } else {
            boolean flag = false;
            for (Trade trade : tradeList) {
                if (CarController.isExistByTid(trade.getId())) {
                    System.out.println("Order id:" + trade.getId() + " Restaurant name:"
                            + RestaurantController.getNameByRid(trade.getRid())
                            + " Order status:" + trade.getStatus());
                    CarView.showCar(trade.getId());
                    flag = true;
                } else {

                }
            }
            if (flag == false) {
                System.out.println("Shopping cart is empty.");
                return;
            }
            System.out.println(">Please enter a order id:(0.Back):");
            int tid = Input.getInt("([0-9])|([1-9][0-9]+)");
            if (tid == 0) {

            } else if (TradeController.isExist(tid)) {
                RestaurantView.orderMenu(TradeController.getTradeById(tid)
                        .getRid());
            } else {
                System.out.println("Order not found");
            }
        }
    }

}
