package base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.ButterKnife;
import com.zchd.library.network.http.IcssOkhttp;
import com.zchd.library.widget.CircleTransform;

/**
 * Created by GJ on 2016/11/7.
 */
abstract public class BaseFragments extends Fragment {
    protected View view=null;
    public IcssOkhttp icssOkhttp;
    private boolean BiaoJ=true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view = inflater.inflate(getLayoutId(), container, false);
            ButterKnife.bind(this, view);
            setDataBinding(view);
            init();
        return view;
    }
    abstract protected void setDataBinding(View view);
    abstract protected int getLayoutId();
    abstract protected void init();
    //创建Fragment会被回调；，only1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        icssOkhttp=new IcssOkhttp();
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
     *LENGTH_SHORT
     * 普通Toast
     */
    public void showShortToast(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    /**
     *LENGTH_LONG
     * 普通Toast
     */
    public void showLongToast(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
    /*
  * 加载圆形图片
  *
  * */
    public void GlideRound(String path, ImageView imageView)
    {
        Glide.with(this).load(path)
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CircleTransform(getActivity()))
                .crossFade()
                .into(imageView);
    }

}
