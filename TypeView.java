package com.lnsf.book.view;

import java.util.ArrayList;
import java.util.List;

import com.lnsf.book.controller.MenuController;
import com.lnsf.book.controller.RestaurantController;
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
            System.out.println("Type is empty.");
        } else {
            for (Type t : list){
                System.out.println(t.getId() + "./t" + t.getName());
            }
            System.out.print(">Please enter a type id:");
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
            System.out.println("Type is empty.");
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
        System.out.print(">Please enter a type id:");
    }

    public static void typeNotFound() {
        System.err.println("Type not found");
    }

    public static void operateType() {
        showTypeByRid(RestaurantController.RID);
        System.out.println(">Please enter a type id:(-1.Add type 0.Back)");
        int typeId = Input.getInt("([0-9])|([1-9][0-9]+)|-1");
        if (typeId == 0) {

        } else if (typeId == -1) {
            addType();
        } else if (TypeController.isExistByIdAndRid(typeId, RestaurantController.RID)) {
            updateType(typeId);
        } else {
            typeNotFound();
        }

    }

    private static void updateType(int typeId) {
        System.out.println("The type you choose:");
        Type type = TypeController.getTypeById(typeId);
        System.out.println("Type id:" + type.getId() + " Type name:" + type.getName());
        System.out.println(">Please enter the options:(1.Update type name 2.Delete 0.Back");
        switch (Input.getInt("[0-2]")){
        case 0:
            return;
        case 1:
            System.out.println(">Please enter a new type name:");
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
        System.out.println(">Please enter a new type name:");
        String typeName = Input.getString(20);
        if (TypeController.addType(new Type(-1, typeName, RestaurantController.RID))){
            Main.success();
        } else {
            Main.fail();
        }
    }

}
