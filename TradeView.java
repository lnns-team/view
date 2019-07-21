package com.lnsf.book.view;

import java.util.List;

import com.lnsf.book.controller.CarController;
import com.lnsf.book.controller.MenuController;
import com.lnsf.book.controller.RestaurantController;
import com.lnsf.book.controller.TradeController;
import com.lnsf.book.controller.UserController;
import com.lnsf.book.dbutils.Input;
import com.lnsf.book.model.Car;
import com.lnsf.book.model.Trade;

public class TradeView {

    public static void operateTrade() {
        System.out.println("1.显示未发货订单");
        System.out.println("2.显示已完成订单");
        System.out.println("0.返回主页");
        switch (Input.getInt("[0-2]")){
        case 0:
            break;
        case 1:
            showUnfinishedTrade();
            System.out.println("输入相应id执行发货,或按0返回");
            int tradeId = Input.getInt();
            if (tradeId == 0){
                
            } else if (TradeController.unfinishedTradeIsExist(tradeId, RestaurantController.RID)){
                TradeController.updateStatusToDelivered(tradeId);
                System.out.println("该订单发货成功,返回首页");
            } else {
                System.out.println("不存在该未完成订单,返回首页");
            }
            break;
        case 2:
            showFinishedTrade();
            break;
        }
    }

    private static void showFinishedTrade() {
        List<Trade> list = TradeController.getFinishedTradeByRid(RestaurantController.RID);
        if (list.isEmpty()){
            System.out.println("已完成订单列表为空");
        } else {
            for (Trade t : list){
                System.out.println("订单id:" + t.getId() + " 订单金额:" + t.getMoney() + 
                        " 收货人:" + TradeController.getUsernameByUserId(t.getUserid()) + 
                        " 收获地址" + t.getAddress());
            }
        }
    }
    /**
     * 显示商家未发货订单
     */
    private static void showUnfinishedTrade() {
        // 一条bug语句,原本应为通过rid查询,getUnfinishedTradeById方法内把商家id转成rid再查找
        List<Trade> list = TradeController.getUnfinishedTradeById(UserController.USER.getId());
        if (list.isEmpty()){
            System.out.println("未发货订单列表为空");
        } else {
            for (Trade t : list){
                System.out.println("订单id:" + t.getId() + " 订单金额:" + t.getMoney() + 
                        " 收货人:" + TradeController.getUsernameByUserId(t.getUserid()) + 
                        " 收获地址" + t.getAddress());
            }
        }
    }

    public static void showUserTrade() {
        System.out.println("***************************");
        System.out.println("1.显示未完成订单");
        System.out.println("2.显示已完成订单");
        System.out.println("0.返回上一层");
        System.out.println("***************************");
        switch (Input.getInt("[0-2]")){
        case 0:
            break;
        case 1:
           // TradeView.showUserUnfinishedTrade();
            break;
        }
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    }
    /**
     * 根据tid买单啦老板们
     * @param tid
     */
    public static void checkoutPlease(int tid) {
        Trade trade = TradeController.getTradeById(tid);
        if (trade.getUsertele().equals("")){
            System.out.println("电话为空,快输入你的电话号码:");
            trade.setUsertele(Input.getString(20));
        } 
        if (trade.getAddress().equals("")){
            System.out.println("地址都没填,快点输入地址:");
            trade.setAddress(Input.getString(40));
        }
        System.out.println("购物车详情:");
        CarView.showCar(trade.getId());
        int sum = 0;
        List<Car> list = CarController.getCarListByTid(tid);
        for (Car c : list){
            sum = c.getNum() * (MenuController.getMenuByMenuId(c.getMenuid())).getPrice();
        }
        System.out.println("商品总额:" + sum);
        trade.setMoney(sum);
        System.out.println("选择操作(1.确认付款 0.返回)");
        switch (Input.getInt("0-1")){
        case 0:
            break;
        case 1:
            trade.setStatus("已付款");
            if (TradeController.update(trade)){
                Main.success();
            } else {
                Main.fail();
            }
            break;
        }
    }

    public static void myShoppingCart() {
        
    }

    
    
    
    
    
    
    
    
    
}
