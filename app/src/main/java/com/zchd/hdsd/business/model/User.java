package com.zchd.hdsd.business.model;

import android.app.Activity;
import android.util.Log;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by GJ on 2016/12/24.
 */
public class User {
   //正式版
   public static String url="http://www.pzhys.com:8080";//
   public static String imgurl="";

   //测试版
//	public static String url="http://120.24.49.7:8008";
//	public static String imgurl="";

   public static ArrayList<String>list_courseId=new ArrayList<String>();
   public static String bugtags="e96097ff724971481c5afa62bccb7387";
   public static String pagesize="10";
   public static List<String> setStringList(String str){
      List<String>list=new ArrayList<String>();
      int x=str.indexOf(",");
      if (x==-1) {
         x=str.length();
      }
      list.add(str.substring(0, x));
      while(str.length()>x){
         int y=x+1;
         x=str.indexOf(",", y);
         if (x==-1) {
            x=str.length();
         }
         list.add(str.substring(y, x));
      }
      return list;
   }

   //待付款界面是否需要刷新数据
   public static boolean isRefresh = false;
   /**
    * 判断是否属于已激活课程
    * 参数课程ID和父课程ID
    * */
   public static boolean getMycourses(String parentCourseId,String courseIds)
   {
      Boolean isd=false;
      for (int i = 0; i < User.list_courseId.size(); i++)
      {
         if(parentCourseId.equals(User.list_courseId.get(i))||courseIds.equals(User.list_courseId.get(i)))
         {
            isd=true;
            break;
         }
      }
      return isd;
   }

}
