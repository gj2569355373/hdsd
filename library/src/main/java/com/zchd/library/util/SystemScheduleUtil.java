package com.zchd.library.util;

import android.app.Application;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;

import com.zchd.library.entity.DateYMDHM;
import com.zchd.library.entity.ScheduleEvents;

import java.util.Calendar;
import java.util.TimeZone;



/**
 * Created by GJ on 2016/11/21.
 * 日历日程工具类
 * 需日历日程读写权限
 *     <uses-permission android:name="android.permission.READ_CALENDAR" />
 *     <uses-permission android:name="android.permission.WRITE_CALENDAR" />
 *     参考http://blog.csdn.net/jhoneson/article/details/47020437
 */
public class SystemScheduleUtil {
    /*
    *
    * 插入一个日程
    * startdate
    *
    * */
    public static void addSchedule(Application application, DateYMDHM startdate, DateYMDHM enddate, ScheduleEvents scheduleEvents){
        //先定义一个URL，到时作为调用系统日历的uri的参数
        String calanderRemiderURL = "";
        if (Build.VERSION.SDK_INT >= 8) {
            calanderRemiderURL =  "content://com.android.calendar/reminders";
        } else {
            calanderRemiderURL = "content://calendar/reminders";
        }

        long calID = 3;
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(startdate.year, startdate.month, startdate.day, startdate.hour, startdate.minute);  //注意，月份的下标是从0开始的
        startMillis = beginTime.getTimeInMillis();  //插入日历时要取毫秒计时
        Calendar endTime = Calendar.getInstance();
        endTime.set(enddate.year, enddate.month, enddate.day, enddate.hour, enddate.minute);
        endMillis = endTime.getTimeInMillis();

        ContentValues eValues = new ContentValues();  //插入事件
        ContentValues rValues = new ContentValues();  //插入提醒，与事件配合起来才有效
        TimeZone tz = TimeZone.getDefault();//获取默认时区

        //插入日程
        eValues.put(CalendarContract.Events.DTSTART, startMillis);//事件的启动时间，使用从纪元开始的UTC毫秒计时
        eValues.put(CalendarContract.Events.DTEND, endMillis);//事件的结束时间，使用从纪元开始的UTC毫秒计时
        eValues.put(CalendarContract.Events.TITLE, scheduleEvents.title);// 	事件的标题
        eValues.put(CalendarContract.Events.DESCRIPTION, scheduleEvents.description);//事件的描述
        eValues.put(CalendarContract.Events.CALENDAR_ID, calID);//事件所属的日历的_ID
        eValues.put(CalendarContract.Events.EVENT_LOCATION,scheduleEvents.events_location);// 	事件发生的地点
        eValues.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID());//事件所针对的时区
        Uri uri =application.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, eValues);

        //插完日程之后必须再插入以下代码段才能实现提醒功能
        String myEventsId = uri.getLastPathSegment(); // 得到当前表的_id
        rValues.put(CalendarContract.Reminders.EVENT_ID, myEventsId);
        rValues.put(CalendarContract.Reminders.MINUTES, -1); //提前多少分钟提醒//- 1指定我们应该使用系统的默认值
        rValues.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);   //如果需要有提醒,必须要有这一行
        application.getContentResolver().insert(Uri.parse(calanderRemiderURL),rValues);
    }
    /*
    * 删除某日程
    * */
    public static  void deleteSchedule(Application application,String description){
        //用rows保存删除的行数，以备有用
        int rows = application.getContentResolver().delete(CalendarContract.Events.CONTENT_URI, CalendarContract.Events.DESCRIPTION+"=?", new String[]{description});
    }
    /*
    * 删除某日程
    * */
    public static  void deleteSchedule(Application application,String[] description){
        //用rows保存删除的行数，以备有用
        int rows = application.getContentResolver().delete(CalendarContract.Events.CONTENT_URI, CalendarContract.Events.DESCRIPTION+"=?",description);
    }
}
