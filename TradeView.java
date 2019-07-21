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
        List<Trade> list = TradeController.getTradeListByRidAndNotInStatus(RestaurantController.getRidByUserId(
                UserController.USER.getId()), "未付款");
        if (list.isEmpty()) {
            System.out.println("已完成订单列表为空");
        } else {
            for (Trade t : list) {
                System.out.println("订单id:" + t.getId() + " 订单状态:" + t.getStatus() + " 订单金额:"
                        + t.getMoney() + " 收货人:"
                        + TradeController.getUsernameByUserId(t.getUserid())
                        + " 收获地址" + t.getAddress());
            }
            System.out.println("输入订单id查看订单详情(0.返回)");
            int tradeId = Input.getInt("([0-9])|([1-9][0-9]+)");
            if (tradeId == 0){
                return;
            } else if (TradeController.isExistByUseridAndTidAndNotInStatus(
                    UserController.USER.getId(), tradeId, "未付款")){
                System.out.println("选择的订单:");
                showTradeInfo(TradeController.getTradeById(tradeId));
                showTradeItemByTid(tradeId);
            } else {
                System.out.println("输入订单有误");
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
        List<Trade> tradeList = TradeController
                .getTradeListByUseridAndNotInStatus(
                        UserController.USER.getId(), "未付款");
        if (tradeList.isEmpty()) {
            System.out.println("你还没有付款下单的订单噢!");
        } else {
            tradeManagement(tradeList);
        }
    }

    private static void tradeManagement(List<Trade> tradeList) {
        for (Trade trade : tradeList) {
            System.out.println("订单id:" + trade.getId() + " 店铺名:"
                    + RestaurantController.getNameByRid(trade.getRid())
                    + " 订单状况:" + trade.getStatus());
        }
        System.out.println("输入订单id进一步操作(0.返回上一层):");
        int tid = Input.getInt("([0-9])|([1-9][0-9]+)");
        if (tid == 0) {
            return;
        } else if (TradeController.isExistByUseridAndTidAndNotInStatus(
                UserController.USER.getId(), tid, "未付款")) {
            Trade trade = TradeController.getTradeById(tid);
            if (trade.getStatus().equals("已付款")) {
                
                System.out.println("选择的订单:");
                TradeView.showTradeInfo(trade);
                CarView.showCar(trade.getId());
                System.out.println("请选择操作(1.退款 0.返回):");
                switch (Input.getInt("[0-1]")) {
                case 0:
                    break;
                case 1:
                    changeTradeStatusToWhat(trade, "已退款");
                    break;
                }
                
            } else if (trade.getStatus().equals("已发货")) {
                
                System.out.println("选择的订单:");
                TradeView.showTradeInfo(trade);
                CarView.showCar(trade.getId());
                System.out.println("请选择操作(1.确认收货 0.返回):");
                switch (Input.getInt("[0-1]")) {
                case 0:
                    break;
                case 1:
                    changeTradeStatusToWhat(trade, "已完成");
                    break;
                }
                
            } else if (trade.getStatus().equals("已完成")) {
                
                System.out.println("该订单已完成,来评价一波?");
                System.out.println("选择的订单:");
                TradeView.showTradeInfo(trade);
                CarView.showCar(trade.getId());
                System.out.println("请选择操作(1.评价该店铺 0.返回):");
                switch (Input.getInt("[0-1]")) {
                case 0:
                    break;
                case 1:
                    if (AppraiseController.isExist(UserController.USER.getId(), trade.getRid())){
                        System.out.println("你曾经评价过这家店铺,你之前的评价是这样的:");
                        AppraiseController.getAboutByUidAndRid(UserController.USER.getId(), trade.getRid());
                        System.out.println("请选择操作:1.修改评价 0.返回");
                        switch (Input.getInt("[0-1]")){
                        case 0:
                            break;
                        case 1:
                            System.out.println("输入新的评价:");
                            if (AppraiseController.updateAbout(UserController.USER.getId(), 
                                    trade.getRid(), Input.getString(40))){
                                System.out.println("修改评价成功");
                                changeTradeStatusToWhat(trade, "已评价");
                            } else {
                                System.out.println("修改评价失败");
                            }
                        }
                    } else {
                        System.out.println("你是第一次评价这家店铺,请输入你的评价:");
                        if (AppraiseController.insertAbout(UserController.USER.getId(), 
                                trade.getRid(), Input.getString(40))){
                            System.out.println("添加评价成功");
                            changeTradeStatusToWhat(trade, "已评价");
                        } else {
                            System.out.println("添加评价失败");
                        }
                    }
                    break;
                }
                
            } else if (trade.getStatus().equals("已评价")) {
                
                System.out.println("评价完成了就没有操作了");
                
            } else if (trade.getStatus().equals("已退款")) {

                System.out.println("退款完成了就没有操作了");
                
            } else {
                System.out.println("神奇的订单状态,不信你看一下数据库");
            }
        } else {
            System.out.println("输入的订单id不存在,返回上一层");
        }
    }

    private static void changeTradeStatusToWhat(Trade trade, String what) {
        trade.setStatus(what);
        if (TradeController.update(trade)) {
            System.out.println("已更新订单信息");
        } else {
            System.out.println("更新订单信息失败");
        }
    }

    /**
     * 显示订单id,店铺名,订单状态,订单金额
     * 
     * @param trade
     */
    private static void showTradeInfo(Trade trade) {
        System.out.println("订单id:" + trade.getId() + " 店铺名:"
                + RestaurantController.getNameByRid(trade.getRid()) + " 订单状态:"
                + trade.getStatus() + " 订单金额:" + trade.getMoney());
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
        if (tradeList.isEmpty()) {
            System.out.println("当前购物车订单为空,快去买东西吧");
        } else {
            boolean flag = false;
            for (Trade trade : tradeList) {
                if (CarController.isExistByTid(trade.getId())) {
                    System.out.println("订单id:" + trade.getId() + " 店铺名:"
                            + RestaurantController.getNameByRid(trade.getRid())
                            + " 订单状况:" + trade.getStatus());
                    CarView.showCar(trade.getId());
                    flag = true;
                } else {

                }
            }
            if (flag == false) {
                System.out.println("你的购物车还是空的,快去点菜吧");
                return;
            }
            System.out.println("选择订单id继续操作(0.返回上一层):");
            int tid = Input.getInt("([0-9])|([1-9][0-9]+)");
            if (tid == 0) {

            } else if (TradeController.isExist(tid)) {
                RestaurantView.orderMenu(TradeController.getTradeById(tid)
                        .getRid());
            } else {
                System.out.println("输入有误,没有这个订单");
            }
        }
    }

}
