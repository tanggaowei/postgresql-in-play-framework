package controllers;

import play.mvc.*;
import models.*;
import util.GeoUtils;

public class Application extends Controller {

    public static void index() {
        UserModel user = UserModel.find("name=?", "test").first();
        if(user == null){
            user = new UserModel();
            user.name = "test";
            user.save();
        }
        else{
            // 修改地理位置字段
            user.location = GeoUtils.createPoint(31.581112, 114.381800);
            user.save();
        }
        renderText("hello");
    }

}