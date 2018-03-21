package com.zchd.library.indexrecycler;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zchd.library.R;
import com.zchd.library.adapter.RecyclerViewHolder;



/**
 * Author: seewhy
 * Date: 2016/1/14
 */
public class IndexableRecyclerView extends RelativeLayout {
    public int DEFAULT_COLUMN_NUMBER = 3;
    private RecyclerView mRecyclerView;
    private TextView mTipText;
    private LetterBar mLetterBar;//右边索引
    private Context mContext;
    public int mColumnNumber = DEFAULT_COLUMN_NUMBER;
    private SectionedRecyclerAdapter mRecyclerAdapter;

    public IndexableRecyclerView(Context context) {
        this(context, null);
    }

    public IndexableRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexableRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initViews(context);

        init(attrs, defStyleAttr);
    }
    public void setLetterBaryGONE(){
        mLetterBar.setVisibility(View.GONE);
    }
    public void letterPostInvalidates(){
        mLetterBar.postInvalidate();
//        mLetterBar.setVisibility(View.VISIBLE);
    }
    public void setTipTextBaryGONE(){
        mTipText.setVisibility(View.GONE);
    }
    private void initViews(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_indexable, this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLetterBar = (LetterBar) view.findViewById(R.id.letter_bar);
        mTipText = (TextView) view.findViewById(R.id.tip_text);
    }

    public void setAdapter(RecyclerView.Adapter<RecyclerViewHolder> adapter) {
        mRecyclerAdapter = new SectionedRecyclerAdapter(mContext, R.layout.title_item, R.id.tvName, adapter);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.IndexableRecyclerView, defStyleAttr, 0);
        mColumnNumber = DEFAULT_COLUMN_NUMBER;
        mColumnNumber = typedArray.getInteger(R.styleable.IndexableRecyclerView_recyclerColumns, DEFAULT_COLUMN_NUMBER);
        typedArray.recycle();

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, mColumnNumber);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mRecyclerAdapter.isSectionHeaderPosition(position) ? mColumnNumber : 1;
            }
        });
        mLetterBar.setOnLetterSelectListener(new LetterBar.OnLetterSelectListener() {
            @Override
            public void onLetterSelect(int position, String letter, boolean confirmed) {
                if (confirmed) {
                    mTipText.setVisibility(View.GONE);
                } else {
                    mTipText.setVisibility(View.VISIBLE);
                    mTipText.setText(letter);
                }
                Integer sectionPosition = mRecyclerAdapter.getSectionPosition(position);
                if (sectionPosition != null)
                    gridLayoutManager.scrollToPositionWithOffset(sectionPosition, 0);
            }
        });
    }
    /*
    * 获取数据的真实坐标
    *
    *
    * */
    public int getPositions(int p,String text){
        String ch =HanziToPinyin.getFirstPinYinChar(text);
        if (ch == null || ch.isEmpty() || !Character.isUpperCase(ch.codePointAt(0))) {
            return p + 1;
        }
        return p+mRecyclerAdapter.getmKeyPositionMap().indexOfKey(ch.charAt(0)-64)+1;
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (visibility==View.GONE)
            mTipText.setVisibility(View.GONE);
    }
    public void upadapter(){
        mRecyclerAdapter.notifyDataSetChanged();
    }
}
