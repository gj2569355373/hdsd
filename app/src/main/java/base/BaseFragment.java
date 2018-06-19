package base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.zchd.hdsd.tool.DisplayUtil;
import com.zchd.library.network.http.IcssOkhttp;


import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


/**
 * Created by GJ on 2016/11/7.
 */
abstract public class BaseFragment extends Fragment {
    protected View view;
    public IcssOkhttp icssOkhttp;
    private boolean BiaoJ = true;
    private ProgressDialog dialog = null;
    Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == view) {
            view = inflater.inflate(getLayoutId(), container, false);
            unbinder = ButterKnife.bind(this, view);
            setDataBinding(view);
            init();
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        initall();
        return view;
    }
    public String[] getKey(){
        return null;
    }
    public void initall() {
        Log.e("tag", "initall");
    }

    abstract protected void setDataBinding(View view);

    abstract protected int getLayoutId();

    abstract protected void init();

    //创建Fragment会被回调；，only1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        icssOkhttp = new IcssOkhttp();
    }

    //当Fragment所在Activity启动完成后会调用；
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    //启动Fragment
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    //恢复Fragment时回调；，并且onStart（）调用时必然调用
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    //暂停Fragment
    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    //停止Fragment
    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    //销毁Fragment所包含的View组件是调用；
    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        unbinder.unbind();
    }

    //销毁Fragment会被回调
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        icssOkhttp.clear(this);
        super.onDestroy();
//        VcodeApplication.getRefWatcher(getActivity()).watch(this);
    }

    //Fragment从Activity删除时会回调,only1
    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
        super.onDetach();
    }


    /**
     * LENGTH_SHORT
     * 普通Toast
     */
    public void showShortToast(String message) {
        Toasty.normal(getActivity().getApplicationContext(), message).show();
    }

    /**
     * LENGTH_LONG
     * 普通Toast
     */
    public void showLongToast(String message) {
        Toasty.normal(getActivity().getApplicationContext(), message).show();
//        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void showToast(String message) {
        Toasty.normal(getActivity().getApplicationContext(), message).show();
    }

    public void GlideRoundDP(String path, ImageView imageView,int dp)
    {
        MultiTransformation multi = new MultiTransformation(
                new RoundedCornersTransformation(DisplayUtil.dip2px(getContext(),dp), 0, RoundedCornersTransformation.CornerType.ALL));
        Glide.with(this).load(path)
                .apply(new RequestOptions().centerCrop().bitmapTransform(multi))
                .into(imageView);
    }

    public void GlideRound(int path, ImageView imageView) {
        Glide.with(this).load(path)
                .apply(new RequestOptions().circleCrop())
                .into(imageView);
    }

    /*
       * 加载圆形图片
       *
       * */
    public void GlideRound(String path, ImageView imageView) {
        Glide.with(this).load(path)
                .apply(new RequestOptions().circleCrop())
                .into(imageView);
    }

    public void GlideRound(String path, ImageView imageView, int err) {
        Glide.with(this).load((path == "" || path == null) ? err : path)
                .apply(new RequestOptions().circleCrop())
                .into(imageView);
    }

    public void showProgressDialog(String dialogtext) {
        if (dialog != null) {
//            dialog.dismiss();
//            dialog=null;
        } else
            dialog = ProgressDialog.show(getActivity(), "", dialogtext, false, false);
    }

    public void dimssProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
