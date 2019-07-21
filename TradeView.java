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
import com.lnsf.book.model.Trade;

public class TradeView {

    public static void operateTrade() {
        System.out.println("1.显示未发货订单");
        System.out.println("2.显示已完成订单");
        System.out.println("0.返回主页");
        switch (Input.getInt("[0-2]")) {
        case 0:
            break;
        case 1:
            showUnfinishedTrade();
            System.out.println("输入相应id查看未发货订单,或按0返回");
            int tradeId = Input.getInt();
            if (tradeId == 0) {

            } else if (TradeController.unfinishedTradeIsExist(tradeId,
                    RestaurantController.RID)) {
                System.out.println("订单详情:");
                showTradeItemByTid(tradeId);
                System.out.println("请选择操作:1.执行发货 0.返回");
                switch (Input.getInt("[0-1]")) {
                case 0:
                    return;
                case 1:
                    if (TradeController.updateStatusToDelivered(tradeId)) {
                        System.out.println("该订单发货成功,返回首页");
                    } else {
                        System.out.println("发货失败,返回首页");
                    }
                }
            } else {
                System.out.println("不存在该未完成订单,返回首页");
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
            System.out.println("提交的订单居然为空?又想白嫖东西了?");
        } else {
            for (Car c : list) {
                Menu menu = MenuController.getMenuByMenuId(c.getMenuid());
                System.out.println("\t菜式id:" + c.getMenuid() + " 菜名:"
                        + menu.getName() + " 单价:" + menu.getPrice() + " 数量:"
                        + c.getNum());
            }
            System.out.println("商品总额:"
                    + TradeController.getTradeById(tradeId).getMoney());
        }
    }

    private static void showFinishedTrade() {
        List<Trade> list = TradeController
                .getFinishedTradeByRid(RestaurantController.RID);
        if (list.isEmpty()) {
            System.out.println("已完成订单列表为空");
        } else {
            for (Trade t : list) {
                System.out.println("订单id:" + t.getId() + " 订单金额:"
                        + t.getMoney() + " 收货人:"
                        + TradeController.getUsernameByUserId(t.getUserid())
                        + " 收获地址" + t.getAddress());
            }
        }
    }

    /**
     * 显示商家未发货订单
     */
    private static void showUnfinishedTrade() {
        // 一条bug语句,原本应为通过rid查询,getUnfinishedTradeById方法内把商家id转成rid再查找
        List<Trade> list = TradeController
                .getUnfinishedTradeById(UserController.USER.getId());
        if (list.isEmpty()) {
            System.out.println("未发货订单列表为空");
        } else {
            for (Trade t : list) {
                System.out.println("订单id:" + t.getId() + " 订单金额:"
                        + t.getMoney() + " 收货人:"
                        + TradeController.getUsernameByUserId(t.getUserid())
                        + " 收获地址" + t.getAddress());
            }
        }
    }

    public static void showUserTrade() {
        List<Trade> tradeList = TradeController.getTradeListByUseridAndNotInStatus(UserController.USER.getId(), "未付款");
        if (tradeList.isEmpty()){
            System.out.println("你还没有付款下单的订单噢!");
        } else {
            for (Trade trade : tradeList){
                System.out.println("订单id:" + trade.getId() + 
                        " 店铺名:" + RestaurantController.getNameByRid(trade.getRid()) + 
                        " 订单状况:" + trade.getStatus() );
                CarView.showCar(trade.getId());
            }
            System.out.println("输入订单id进一步操作(0.返回上一层):");
            int tid = Input.getInt("([0-9])|([1-9][0-9]+)");
            if (tid == 0){
                return;
            } else if (TradeController.isExistByUseridAndTidAndNotInStatus(
                    UserController.USER.getId(), tid, "未付款")){
                Trade trade = TradeController.getTradeById(tid);
                if (trade.getStatus().equals("已付款")){
                    System.out.println("选择的订单:");
                    TradeView.showTradeInfo(trade);
                    CarView.showCar(trade.getId());
                    System.out.println("请选择操作(1.退款 0.返回):");
                    switch (Input.getInt("[0-1]")){
                    case 0:
                        break;
                    case 1:
                        trade.setStatus("已退款");
                        if (TradeController.update(trade)){
                            System.out.println("退款成功吧.");
                        } else {
                            System.out.println("可能你样衰,退款失败了");
                        }
                        break;
                    }
                } else if (trade.getStatus().equals("已发货")){
                    System.out.println("坐等吃饭吧,看多两眼外卖也没这么快到的.");
                } else if (trade.getStatus().equals("已完成")){
                    
                } else if (trade.getStatus().equals("已评价")){
                    
                }  else if (trade.getStatus().equals("已退款")){
                    
                } else {
                    System.out.println("神奇的订单状态,不信你看一下数据库");
                }
            } else {
                System.out.println("输入的订单id不存在,返回上一层");
            }
        }
    }
    /**
     * 显示订单id,店铺名,订单状态,订单金额
     * @param trade
     */
    private static void showTradeInfo(Trade trade) {
        System.out.println("订单id:" + trade.getId() + " 店铺名:" + RestaurantController.getNameByRid(trade.getRid()) + 
                " 订单状态:" + trade.getStatus() + " 订单金额:" + trade.getMoney());
    }

    /**
     * 根据tid买单啦老板们
     * 
     * @param tid
     */
    public static void checkoutPlease(int tid) {
        Trade trade = TradeController.getTradeById(tid);
        if (trade.getUsertele().equals("")) {
            System.out.println("电话为空,快输入你的电话号码:");
            trade.setUsertele(Input.getString(20));
        }
        if (trade.getAddress().equals("")) {
            System.out.println("地址都没填,快点输入地址:");
            trade.setAddress(Input.getString(40));
        }
        System.out.println("购物车详情:");
        CarView.showCar(trade.getId());
        int sum = 0;
        List<Car> list = CarController.getCarListByTid(tid);
        for (Car c : list) {
            sum = c.getNum()
                    * (MenuController.getMenuByMenuId(c.getMenuid()))
                            .getPrice();
        }
        System.out.println("商品总额:" + sum + "元");
        trade.setMoney(sum);
        System.out.println("选择操作(1.确认付款 0.返回)");
        switch (Input.getInt("[0-1]")) {
        case 0:
            if (TradeController.update(trade)) {
                Main.success();
            } else {
                Main.fail();
            }
            break;
        case 1:
            trade.setStatus("已付款");
            if (TradeController.update(trade)) {
                Main.success();
            } else {
                Main.fail();
            }
            break;
        }
    }

    public static void myShoppingCart() {
        System.out.println("我的购物车:");
        List<Trade> tradeList = TradeController.getTradeListByUseridAndStatus(
                UserController.USER.getId(), "未付款");
        if (tradeList.isEmpty()){
            System.out.println("当前购物车订单为空,快去买东西吧");
        } else {
            boolean flag = false;
            for (Trade trade : tradeList){
                if (CarController.isExistByTid(trade.getId())){
                System.out.println("订单id:" + trade.getId() + 
                        " 店铺名:" + RestaurantController.getNameByRid(trade.getRid()) + 
                        " 订单状况:" + trade.getStatus() );
                CarView.showCar(trade.getId());
                flag = true;
                } else {
                    
                }
            }
            if (flag == false){
                System.out.println("你的购物车还是空的,快去点菜吧");
                return;
            }
            System.out.println("选择订单id继续操作(0.返回上一层):");
            int tid = Input.getInt("([0-9])|([1-9][0-9]+)");
            if (tid == 0){
                
            } else if (TradeController.isExist(tid)){
                RestaurantView.orderMenu(TradeController.getTradeById(tid).getRid());
            } else {
                System.out.println("输入有误,没有这个订单");
            }
        }
    }

    
    
    
    
    
    
    
    
    
    
    
}
