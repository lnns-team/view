package com.lnsf.book.view;

import java.util.ArrayList;
import java.util.List;

import com.lnsf.book.controller.MenuController;
import com.lnsf.book.controller.RestaurantController;
import com.lnsf.book.controller.TypeController;
import com.lnsf.book.controller.UserController;
import com.lnsf.book.dbutils.Input;
import com.lnsf.book.model.Menu;
import com.lnsf.book.service.impl.IBasicServiceImpl;

public class MenuView {
    /**
     * 自动判断用户身份并输出对应的菜单列表
     * 普通用户输出所有菜单列表
     * 商家输出自己的菜单列表
     */
    public static void showMenu(){
        List<Menu> list = new ArrayList<Menu>();
        if (UserController.USER.getIdentify() == 1) {
            TypeView.inputTypeId();
            int typeId = Input.getInt();
            if (!TypeController.isExist(typeId)){
                TypeView.typeNotFound();
                return;
            } else {
//                list = MenuController.getMenuListByTypeId(typeId);// 根据输入类别id返回Menu List
//                MenuView.showMenu(list);
            }
        } else if (UserController.USER.getIdentify() == 2) {
            list = MenuController.getMenuListByRid(UserController.USER.getId());// 根据rid返回Menu List
            MenuView.showMenu(list);
        } else {
            UserView.userIdentifyNotFound();
        }
    }
    /**
     * 根据传过来的List参数输出该菜单
     * 
     * @param list
     */
    public static void showMenu(List<Menu> list) {
        if (list.isEmpty()) {
            System.out.println("菜单为空");
        } else {
            for (Menu m : list) {
                System.out.println(m.getId() + "." + m.getName() + "价格:" + m.getPrice() + 
                        "类型:" + m.getType() + "描述:" + m.getMDescribe() + 
                        "店铺名:" + RestaurantController.getNameByRid(m.getRid()) );
            }
        }
    }

    /**
     * 菜单不存在的错误页面
     */
    public static void menuNotFound() {
        System.err.println("菜单未找到");
    }

    /**
     * 显示更新菜单的操作
     */
    public static void showUpdateOperation(int menuId) {
        System.out.println("1.更新菜式名称");
        System.out.println("2.更新菜式价格");
        System.out.println("3.删除该菜式");
        System.out.println("0.返回主菜单");
        System.out.print("请输入:");
        int choice = Input.getInt();
        switch (choice) {
        case 0:
            BgMain.businessMainView();
            return;
        case 1:
            MenuView.updateMenu(menuId);
            break;
        case 2:

            break;
        default:
            System.out.println("输入有误,跳转回主菜单");
            BgMain.businessMainView();
            break;
        }
    }

    public static String inputMenuName() {
        System.out.println("请输入新菜式名称:");
        return Input.getString();
    }
    /**
     * 商家添加新菜单
     */
    public static void addMenu() {
        if (!TypeController.isExistByRid(UserController.USER.getId())){
            System.out.println("你的类别为空,请先添加类别再添加菜式");
        } else {
            TypeView.showTypeByRid(UserController.USER.getId());
            System.out.println("请输入需要添加的菜式类别id(输入0返回首页):");
            int typeId = Input.getInt();
            if (typeId == 0){
                
            } else if (TypeController.isExistByIdAndRid(typeId, UserController.USER.getId())){
                System.out.println("请输入菜名:");
                String menuName = Input.getString(20);
                System.out.println("请输入价格:");
                int menuPrice = Input.getInt();
                System.out.println("请输入商品描述:");
                String menuMDescribe = Input.getString(40);
                System.out.println("插入的菜式信息:");
                System.out.println("菜名:" + menuName);
                System.out.println("价格:" + menuPrice);
                System.out.println("类型:" + TypeController.getTypeNameById(typeId));
                System.out.println("输入1确认插入,输入0返回首页");
                if (Input.getInt("[0-1]") == 1){
                    if (MenuController.addMenu(new Menu(-1, menuName, menuPrice, 
                            UserController.USER.getId(), menuMDescribe, typeId))){
                        Main.success();
                    } else {
                        Main.fail();
                    }
                } else {
                    
                }
                    
            } else {
                System.out.println("输入类别id不存在");
            }
        }
    }
    /**
     * 商家维护菜单操作
     */
    public static void operateMenu() {
        showMenu();
        System.out.println("输入id修改该菜式(输入-1添加菜式 输入0返回上一层 )");
        int menuId = Input.getInt();
        if (menuId == 0){
            
        } else if (menuId == -1){
            addMenu();
        } else if (MenuController.isExist(menuId, IBasicServiceImpl.rid)){
            updateMenu(menuId);
        } else {
            System.out.println("输入有误,返回上一层");
        }
    }
    /**
     * 更新菜单操作
     */
    public static void updateMenu(int menuId) {
        System.out.println("选择的菜式:");
        Menu menu = MenuController.getMenuByMenuId(menuId);
        System.out.println("菜式id" + menu.getId() + " 菜名:" + menu.getName() + " 价格:" + menu.getPrice() + 
                " 类型:" + menu.getType() + " 描述:" + menu.getMDescribe());
        System.out.println("请选择操作(1.修改菜名 2.修改价格 3.修改描述 4.删除菜式 0.返回主页");
        switch (Input.getInt("[0-4]")){
        case 0:
            return;
        case 1:
            menu.setName(Input.getString(20));
            break;
        case 2:
            menu.setPrice(Input.getInt());
            break;
        case 3:
            menu.setMDescribe(Input.getString(40));
            break;
        case 4:
            if (MenuController.delMenuById(menu.getId())){
                Main.success();
            } else {
                Main.fail();
            }
            return;
        }
        if (MenuController.updateMenu(menu)){
            Main.success();
        } else {
            Main.fail();
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
}
