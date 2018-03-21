package com.zchd.library.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.zchd.library.entity.ContactInfo;
import com.zchd.library.indexrecycler.HanziToPinyin;

import java.util.List;



/**
 * Created by GJ on 2016/12/1.
 * 获取通讯录
 */
public abstract class ContactInfoParser {
    List<ContactInfo>list;
    Activity activity;
    /**
     * 获取系统全部联系人的api方法
     *
     * @param context
     * @return
     */
    public static boolean findAll(Activity context)
    {
       if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {
            //申请权限  第二个参数是一个 数组 说明可以同时申请多个权限
            ActivityCompat.requestPermissions(context, new String[]{android.Manifest.permission.READ_CONTACTS}, 1);
        } else {//已授权
           return true;
       }
        return false;
    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            ContentResolver resolver = activity.getContentResolver();
            // 1.查询raw_contacts表，把联系人的id取出来
            Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
            Uri datauri = Uri.parse("content://com.android.contacts/data");
            Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    null, null, null);
            boolean csb=false;
            String xzftext;
//            final ContactInfo infoxzf = new ContactInfo();
            while (cursor.moveToNext()) {
//                String id = cursor.getString(0);
//                if (id != null) {
                    String name = cursor.getString(cursor
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                if (name==null||name.equals(""))
                {
                    name="#";
                }
                    String ch = HanziToPinyin.getFirstPinYinChar(name);//获取文字首字母mItemModels.get(i)
                    if (ch=="#"||ch == null || ch.isEmpty() || !Character.isUpperCase(ch.codePointAt(0))) {
                        name= "#"+name;
                    }
                    //读取通讯录的号码
                    String number = cursor.getString(cursor
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    ContactInfo infos = new ContactInfo(name, number.trim());
                    if (infos.getPhone()!=null&&infos.getName()!=null) {
                        if (!infos.getName().equals("") && !infos.getPhone().equals("") && infos.getPhone().length() == 11)
                            list.add(infos);
//                    }
//                    System.out.println("联系人id: " + id);
//                    ContactInfo info = new ContactInfo();
//                    info.setId(id);
//                    // 2.根据联系人的id，查询data表，把这个id的数据取出来
//                    // 系统api查询data表的时候不是真正的查询的data表，而是查询data表的视图
//                    Cursor dataCursor = resolver.query(datauri, new String[] {
//                                    "data1", "mimetype" }, "raw_contact_id=?",
//                            new String[] { id }, null);
//                    if (dataCursor!=null) {
//                        xzftext="/";
//                        while (dataCursor.moveToNext()) {
//                            String data1 = dataCursor.getString(0);
//                            String mimetype = dataCursor.getString(1);
//
//                            if ("vnd.android.cursor.item/name".equals(mimetype)) {
//                                Log.e("tag", "姓名=" + data1);
//                                String ch = HanziToPinyin.getFirstPinYinChar(data1);//获取文字首字母mItemModels.get(i)
//                                if (ch=="#"||ch == null || ch.isEmpty() || !Character.isUpperCase(ch.codePointAt(0))) {
//                                    data1= "#"+data1;
//                                }
//                                info.setName(data1);
//                            } else if ("vnd.android.cursor.item/email_v2"
//                                    .equals(mimetype)) {
//                                System.out.println("邮箱=" + data1);
//                                info.setEmail(data1);
//                            } else if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
//                                System.out.println("电话=" + data1);
//                                String str = data1.replace(" ", "");
//                                if (str.trim().startsWith("+86")) {
//                                    str = str.trim().substring(3);
//                                }
//                                info.setPhone(str);
//                            } else if ("vnd.android.cursor.item/im".equals(mimetype)) {
//                                System.out.println("QQ=" + data1);
//                                info.setQq(data1);
//                            }
//                        }
//                    }
////                    if (info.getName()!=null){
////                        if (info.getName().trim().equals("许兆丰")){
////                            infoxzf.setName(info.getName());
////                            infoxzf.setPhone(info.getPhone());
////                            infoxzf.setEmail(dataCursor.getColumnNames().toString());
////                        }
////                    }
//                    if (info.getPhone()!=null&&info.getName()!=null) {
//                        if (!info.getName().equals("") && !info.getPhone().equals("") && info.getPhone().length() == 11)
//                            list.add(info);
//                    }

                    System.out.println("-------");
//                    dataCursor.close();
                }
            }
            cursor.close();
            if (activity!=null) {
                Handler handler = new Handler(activity.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        findok();
//                        Toast.makeText(activity,"/"+infoxzf.getName()+infoxzf.getPhone()+"/",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    public abstract void findok();
    public void findList(Activity activity,List<ContactInfo>list) {
        this.list=list;
        this.activity=activity;
        new Thread(runnable).start();
    }
}
