package com.zchd.library.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by GJ on 2016/11/15.
 */
public class TableAddView implements View.OnClickListener{
    private Context context;
    private  SparseArray<View> listViews;
    private TableListener tableListener;
    private String clickbc="#C1CDC1";
    private String bc="#FFFFFF";
    public TableAddView(Context context) {
        this.context = context;
    }
    public View addTableView(String[] list){
        if (list.length==0)
            return null;
        listViews=new SparseArray<View>();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout. LayoutParams.MATCH_PARENT);
        lp.setMargins(0,0,0,0);
        LinearLayout layout=new LinearLayout(context);
        layout.setLayoutParams(lp);//设置布局参数
        layout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams vlp =new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);
        vlp.weight=1;
        for (int i=0;i<list.length;i++) {
            TextView textView = new TextView(context);
            textView.setTextSize(14);
            textView.setLayoutParams(vlp);
            textView.setText(list[i]);
            textView.setGravity(Gravity.CENTER);
            textView.setTag(i);
            textView.setOnClickListener(this);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                textView.setBackground(textView.getResources().getDrawable(R.drawable.withmaskripplenoclick));
////                textView.setBackgroundResource(R.drawable.withmaskripple);
//            }
//            else
            textView.setBackgroundColor(Color.parseColor(bc));
            if (i==0)
            {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//                    textView.setBackground(textView.getResources().getDrawable(R.drawable.withmaskripple));
//                else
                    textView.setBackgroundColor(Color.parseColor(clickbc));
            }
            listViews.put(i,textView);
            layout.addView(textView);
        }
        return layout;
    }

    @Override
    public void onClick(View v) {
        setState((int)v.getTag(),v);
        tableListener.onClickTable((int)v.getTag());
    }
    private void setState(int tag,View view){
        for (int i=0;i<listViews.size();i++){
            if (i==tag)
            {
                listViews.get(i).setBackgroundColor(Color.parseColor(clickbc));
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    view.setBackground(view.getResources().getDrawable(R.drawable.withmaskripple));
////                    view.setBackgroundResource(R.drawable.withmaskripple);
//                }
//                else

            }
            else
            {

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    view.setBackground(view.getResources().getDrawable(R.drawable.withmaskripplenoclick));
////                    view.setBackgroundResource(R.drawable.withmaskripple);
//                }
//                else
                    listViews.get(i).setBackgroundColor(Color.parseColor(bc));
            }
        }
    }

    public void setTableListener(TableListener tableListener) {
        this.tableListener = tableListener;
    }

    public interface TableListener{
        public void onClickTable(int tag);
    }
}