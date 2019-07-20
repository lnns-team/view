package com.lnsf.book.view;

import java.util.List;

import com.lnsf.book.controller.TradeController;
import com.lnsf.book.controller.UserController;
import com.lnsf.book.dbutils.Input;
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
                
            } else if (TradeController.unfinishedTradeIsExist(tradeId, UserController.USER.getId())){
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
        List<Trade> list = TradeController.getFinishedTradeByRid(UserController.USER.getId());
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

    private static void showUnfinishedTrade() {
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

    
    
    
    
    
    
    
    
    
}
