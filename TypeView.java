package com.lnsf.book.view;

import java.util.ArrayList;
import java.util.List;

import com.lnsf.book.controller.MenuController;
import com.lnsf.book.controller.TypeController;
import com.lnsf.book.controller.UserController;
import com.lnsf.book.dao.impl.TypedaoImpl;
import com.lnsf.book.dbutils.Input;
import com.lnsf.book.model.Menu;
import com.lnsf.book.model.Type;

public class TypeView {
    /**
     * 显示类别
     * 
     * 
     */
    public static void showType(){
        List<Type> list = new ArrayList<Type>();
        //list = TypeController.
        if (list.isEmpty()){
            System.out.println("类别表为空");
        } else {
            for (Type t : list){
                System.out.println(t.getId() + "./t" + t.getName());
            }
            System.out.print("请选择类别:");
        }
        
    }

    /**
     * 通过给定商家id输出类别
     * 
     * @param rid
     */
    public static void showTypeByRid(int rid) {
        List<Type> list = new ArrayList<>();
        list = TypeController.getTypeListByRid(rid);
        if (list.isEmpty()) {
            System.out.println("类别为空");
        } else {
            showTypeList(list);
        }
    }

    private static void showTypeList(List<Type> list) {
        for (Type p : list) {
            System.out.println(p.getId() + "." + p.getName());
        }
    }

    /**
     * 输入类别id提示
     */
    public static void inputTypeId() {
        System.out.print("请输入类别id:");
    }

    public static void typeNotFound() {
        System.err.println("未找到该类型");
    }

    public static void operateType() {
        showTypeByRid(UserController.USER.getId());
        System.out.println("输入id修改该菜式(输入-1添加类别 输入0返回上一层 )");
        int typeId = Input.getInt();
        if (typeId == 0) {

        } else if (typeId == -1) {
            addType();
        } else if (TypeController.isExistByIdAndRid(typeId, UserController.USER.getId())) {
            updateType(typeId);
        } else {
            System.out.println("输入有误,返回上一层");
        }

    }

    private static void updateType(int typeId) {
        System.out.println("您选择的类别:");
        Type type = TypeController.getTypeById(typeId);
        System.out.println("类别id" + type.getId() + " 类别名:" + type.getName());
        System.out.println("请选择操作(1.修类别名 2.删除类别 0.返回主页");
        switch (Input.getInt("[0-2]")){
        case 0:
            return;
        case 1:
            type.setName(Input.getString(20));
            break;
        case 2:
            if (TypeController.delTypeById(type.getId())){
                Main.success();
            } else {
                Main.fail();
            }
            return;
        }
        if (TypeController.updateType(type)){
            Main.success();
        } else {
            Main.fail();
        }
        
    }

    private static void addType() {
        System.out.println("请输入新增类别名:");
        String typeName = Input.getString(20);
        if (TypeController.addType(new Type(-1, typeName, UserController.USER.getId()))){
            Main.success();
        } else {
            Main.fail();
        }
    }

}
