package com.lnsf.book.view;

import java.util.ArrayList;
import java.util.List;

import com.lnsf.book.controller.MenuController;
import com.lnsf.book.controller.RestaurantController;
import com.lnsf.book.controller.TradeController;
import com.lnsf.book.controller.TypeController;
import com.lnsf.book.controller.UserController;
import com.lnsf.book.dbutils.Input;
import com.lnsf.book.dbutils.Output;
import com.lnsf.book.model.Menu;
import com.lnsf.book.model.Trade;
import com.lnsf.book.service.impl.IBasicServiceImpl;

public class MenuView {
    /**
     * 自动判断用户身份并输出对应的菜单列表 普通用户输出所有菜单列表 商家输出自己的菜单列表
     */
    public static void showMenu() {
        List<Menu> list = new ArrayList<Menu>();
        // 貌似没有普通用户调用这个方法
        if (UserController.USER.getIdentify() == 1) {
            TypeView.inputTypeId();
            int typeId = Input.getInt();
            if (!TypeController.isExist(typeId)) {
                TypeView.typeNotFound();
                return;
            } else {
                // list = MenuController.getMenuListByTypeId(typeId);//
                // 根据输入类别id返回Menu List
                // MenuView.showMenu(list);
            }
        } else if (UserController.USER.getIdentify() == 2) {
            list = MenuController.getMenuListByRid(RestaurantController.RID);// 根据rid返回Menu
                                                                             // List
            MenuView.showMenu(list);
        } else {
            UserView.userIdentifyNotFound();
        }
    }

    /**
     * 根据传过来的List参数输出该菜单所有属性
     * 
     * @param list
     */
    public static void showMenu(List<Menu> list) {
        if (list.isEmpty()) {
            System.out.println("The menu is empty.");
        } else {
            Output.printFormat(138);
            Output.formatter.format(
                    "|%-10s|%-20s|%-10s|%-10s|%-20s|%-20s|%-40s|\n", "Menu Id",
                    "Menu name", "Price", "Number", "Type name",
                    "Restaurant name", "Menu describe");
            for (Menu m : list) {
                System.out.printf(
                        "|%-10d|%-20s|%-10d|%-10d|%-20s|%-20s|%-40s|\n",
                        m.getId(), m.getName(), m.getPrice(), m.getStock(),
                        TypeController.getTypeNameById(m.getType()),
                        RestaurantController.getNameByRid(m.getRid()),
                        m.getMDescribe());
            }
            Output.printFormat(138);
        }
    }

    public static void showMenuAndHideStockIs0(List<Menu> list) {
        if (list.isEmpty()) {
            System.out.println("Menu is empty.");
        } else {
            Output.printFormat(138);
            Output.formatter.format(
                    "|%-10s|%-20s|%-10s|%-10s|%-20s|%-40s|%-20s|\n", "Menu Id",
                    "Menu name", "Price", "Number", "Type name",
                    "Menu describe", "Restaurant name");
            for (Menu m : list) {
                if (m.getStock() == 0)
                    continue;
                System.out.printf(
                        "|%-10d|%-20s|%-10d|%-10d|%-20s|%-40s|%-20s|\n",
                        m.getId(), m.getName(), m.getPrice(), m.getStock(),
                        TypeController.getTypeNameById(m.getType()),
                        m.getMDescribe(),
                        RestaurantController.getNameByRid(m.getRid()));
            }
            Output.printFormat(138);
        }
    }

    /**
     * 菜单不存在的错误页面
     */
    public static void menuNotFound() {
        System.err.println("Menu not found");
    }

    /**
     * 显示更新菜单的操作
     */
    // public static void showUpdateOperation(int menuId) {
    // Output.printFormat(40);
    // Output.formatterOutput("1.Update menu name", 30);
    // Output.formatterOutput("2.Update menu price", 30);
    // Output.formatterOutput("3.Delete menu", 30);
    // Output.formatterOutput("0.Back", 30);
    // Output.printFormat(40);
    // System.out.print(">Please enter the options:");
    // int choice = Input.getInt("[0-3]");
    // switch (choice) {
    // case 0:
    // BgMain.businessMainView();
    // return;
    // case 1:
    // MenuView.updateMenu(menuId);
    // break;
    // case 2:
    //
    // break;
    // default:
    // System.out.println("输入有误,跳转回主菜单");
    // BgMain.businessMainView();
    // break;
    // }
    // }

    public static String inputMenuName() {
        System.out.println(">Please enter a new menu name:");
        return Input.getString();
    }

    /**
     * 商家添加新菜单
     */
    public static void addMenu() {
        if (!TypeController.isExistByRid(RestaurantController.RID)) {
            System.out
                    .println("Your category is empty, please add category first and then add menu.");
        } else {
            TypeView.showTypeByRid(RestaurantController.RID);
            System.out.println(">Please enter a type id(0.Back):");
            int typeId = Input.getInt("([0-9])|([1-9][0-9]+)");
            if (typeId == 0) {

            } else if (TypeController.isExistByIdAndRid(typeId,
                    RestaurantController.RID)) {
                System.out.println(">Please enter a menu name:");
                String menuName = Input.getString(20);
                System.out.println(">Please enter a menu price:");
                int menuPrice = Input.getInt("([0-9])|([1-9][0-9]+)");
                System.out.println(">Please enter a menu stock");
                int menuStock = Input.getInt("([0-9])|([1-9][0-9]+)");
                System.out.println(">Please enter a menu describe");
                String menuMDescribe = Input.getString(40);
                System.out.println("Menu information:");
                System.out.println("Menu name:" + menuName);
                System.out.println("Menu price:" + menuPrice);
                System.out.println("Menu stock:" + menuStock);
                System.out.println("Menu type:"
                        + TypeController.getTypeNameById(typeId));
                System.out.println(">Enter 1 to confirm or 0 to exit");
                if (Input.getInt("[0-1]") == 1) {
                    if (MenuController.addMenu(new Menu(-1, menuName,
                            menuPrice, RestaurantController.RID, menuMDescribe,
                            typeId, menuStock))) {
                        Main.success();
                    } else {
                        Main.fail();
                    }
                } else {

                }
            } else {
                System.out.println("Menu type id not found");
            }
        }
    }

    /**
     * 商家维护菜单操作 添加,修改菜单
     */
    public static void operateMenu() {
        showMenu();
        System.out.println(">Enter menu id to modify the menu(-1.Add ,0.Back)");
        int menuId = Input.getInt("([0-9])|([1-9][0-9]+)|-1");
        if (menuId == 0) {

        } else if (menuId == -1) {
            addMenu();
        } else if (MenuController.isExist(menuId, RestaurantController.RID)) {
            updateMenu(menuId);
        } else {
            System.out.println("Invalid input,auto back");
        }
    }

    /**
     * 更新菜式操作 更新菜名,更新价格,更新描述 删除菜式
     */
    public static void updateMenu(int menuId) {
        System.out.println("The menu item you choose:");
        Menu menu = MenuController.getMenuByMenuId(menuId);
        System.out.println("Menu id:" + menu.getId() + " Menu name:"
                + menu.getName() + " Menu price:" + menu.getPrice()
                + " Menu stock" + menu.getStock() + " Menu type:"
                + menu.getType() + " Menu describe:" + menu.getMDescribe());
        System.out
                .println(">Please enter the options:(1.Update name 2.Update price 3.Update stock 4.Update describe 5.Delete 0.Back");
        switch (Input.getInt("[0-5]")) {
        case 0:
            return;
        case 1:
            System.out.println(">Please enter a new name:");
            menu.setName(Input.getString(20));
            break;
        case 2:
            System.out.println(">Please enter a new price:");
            menu.setPrice(Input.getInt("([0-9])|([1-9][0-9]+)"));
            break;
        case 3:
            System.out.println(">Please enter a new stock");
            menu.setStock(Input.getInt("([0-9])|([1-9][0-9]+)"));
        case 4:
            System.out.println(">Please enter a new describe:");
            menu.setMDescribe(Input.getString(40));
            break;
        case 5:
            List<Trade> tradeList = TradeController
                    .getTradeListByRidAndNotInStatus(RestaurantController.RID,
                            "");
            for (Trade t : tradeList) {
                if (MenuController.isExistByTid(t.getId())) {
                    System.err
                            .println("The menu binds the order,can not delete.");
                } else {

                    if (MenuController.delMenuById(menu.getId())) {
                        Main.success();
                    } else {
                        Main.fail();
                    }
                }
            }
            return;
        }
        if (MenuController.updateMenu(menu)) {
            Main.success();
        } else {
            Main.fail();
        }
    }

    /**
     * 根据rid打印该店铺所有菜式
     * 
     * @param rid
     */
    public static void showAllMenuByRid(int rid) {
        List<Menu> list = MenuController.getMenuListByRid(rid);
        showMenuAndHideStockIs0(list);
    }

}
