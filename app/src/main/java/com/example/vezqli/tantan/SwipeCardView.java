package com.example.vezqli.tantan;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by verzqli on 2016/12/21 16:56
 */
public class SwipeCardView extends ViewGroup {
    private static final String TAG = "SwipeCardView";

    public static int TRANS_Y_GAP;
    //卡片阶梯之间的宽度，单位px
    private int transY = 12;
    private ViewDragHelper mDragHelper;
    //最顶层页面，随着手指滑动
    private View topView;
    //卡片中心点
    private int centerX,centerY;
    //手指离开屏幕的判断
    private boolean isRelise;
    //加载数据的adapter
    private CardBaseAdapter adapter;
    //可见的卡片页面
    private int showCards = 3;
    //随手指滑动 卡片旋转的角度
    private int ROTATION = 20;
    //左滑右滑判断
    private boolean swipeLeft = false;
    //已经删除的页面的数量
    private int deleteNum;
    //子view的行宽度,高度
    int childWidth, childHeight;
    public SwipeCardView(Context context) {
        this(context, null);
    }


    public SwipeCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TRANS_Y_GAP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, transY, context.getResources().getDisplayMetrics());
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
                    @Override
                    public boolean tryCaptureView(View child, int pointerId) {
                        return child == topView;
                    }

                    @Override
                    public int clampViewPositionHorizontal(View changedView, int left, int dx) {

                        if (isRelise) {
                            isRelise = false;
                        }

                        for (int i = 1; i < getChildCount()-1; i++) {
                            View view = getChildAt(i);
                            view.setTranslationY((childHeight*0.025f+TRANS_Y_GAP) * ( getChildCount()-1- i)
                                                -getCenterX(changedView)*(childHeight*0.025f+TRANS_Y_GAP));
                            view.setScaleX(1-( getChildCount()-1-i)*0.05f + getCenterX(changedView) * 0.05f);
                            view.setScaleY(1-( getChildCount()-1-i)*0.05f + getCenterX(changedView) * 0.05f);
                        }
                        if (topView!=null){
                            if (swipeLeft){
                                topView.setRotation(-getCenterX(changedView) * ROTATION);
                            }else {
                                topView.setRotation(getCenterX(changedView) * ROTATION);
                            }

                        }
                        return left;
                    }
                    @Override
                    public int clampViewPositionVertical(View child, int top, int dy) {
                        return top;
                    }

                    @Override
                    public void onViewReleased(View releasedChild, float xvel, float yvel) {
//                super.onViewReleased(releasedChild, xvel, yvel);
                        //mAutoBackView手指释放时可以自动回去

                        if (releasedChild.getLeft() / 2 > 300) {

                            if (releasedChild == topView) {
                                removeView(topView);
                                deleteNum++;
                                for (int i = 1; i < getChildCount()-1; i++) {
                                    View view = getChildAt(i);
                                    int level =  getChildCount()-1-i;
                                    view.setTranslationY((childHeight*0.025f+TRANS_Y_GAP) * (level));
                                    view.setScaleX(1 - 0.05f * ( level));
                                    view.setScaleY(1 - 0.05f * ( level));
                                }
                                adapter.notifyDataSetChanged();


                            }
                        } else {

                            isRelise = true;
                            mDragHelper.settleCapturedViewAt((int) (centerX-childWidth/2),(int) (centerY-childHeight/2));
                            invalidate();
                        }
                    }

                    @Override
                    public void onViewPositionChanged(View changedView, int left, int top, int dx,
                                                      int dy) {
                        super.onViewPositionChanged(changedView, left, top, dx, dy);
                        //当手指松开后对顶层卡片进行移动
                        if (changedView == topView && isRelise) {

                            for (int i = 1; i < getChildCount()-1; i++) {
                                View view = getChildAt(i);
                                int level =  getChildCount()-1-i;
                                view.setTranslationY((childHeight*0.025f+TRANS_Y_GAP) * ( level)-
                                        getCenterX(changedView)*(childHeight*0.025f+TRANS_Y_GAP));
                                view.setScaleX(1-(level)*0.05f + getCenterX(changedView) * 0.05f);
                                view.setScaleY(1-(level)*0.05f + getCenterX(changedView) * 0.05f);
                            }
                            if (topView!=null){
                                //根据角度来对卡片旋转角度进行测算
                                if (swipeLeft){
                                    topView.setRotation(-getCenterX(changedView) * ROTATION);
                                }else {
                                    topView.setRotation(getCenterX(changedView) * ROTATION);
                                }

                            }
                        }
                    }
                }

        );

        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    private float getCenterX(View child) {
        if (child.getWidth() / 2 + child.getX() - centerX<0){
            swipeLeft = true;
        }else {
            swipeLeft = false;
        }
        float width = Math.abs(child.getWidth() / 2 + child.getX() - centerX);
        if (width > centerX) {
            width = centerX;
        }
        return width / centerX;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        centerX = widthSize / 2;
        centerY = heightSize/2;
        measureChildren( widthMeasureSpec, heightMeasureSpec);

        //子view
        View child = null;
        //获取子view的margin值
        MarginLayoutParams params = null;
            if (getChildCount()>0){
                child = getChildAt(0);
                //这里我就是用第一个页面的大小来当做长款，因为后面不可能比他大了
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                params = (MarginLayoutParams) child.getLayoutParams();
                childWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
                childHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            }

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        topView = getChildAt(getChildCount()-1);

        int level = getChildCount()  - 1;
        View view;

        if (getChildCount() > 1) {
            for (int j = 0; j<=getChildCount() -1; j++) {
                view = getChildAt(j);

                view.layout((int) (centerX-childWidth/2),(int) (centerY-childHeight/2),
                        (int) (centerX+childWidth/2), (int) (centerY+childHeight/2));
                view.setTranslationY((childHeight*0.025f+TRANS_Y_GAP) * (level - 1));
                view.setScaleX(1 - 0.05f * (level - 1));
                view.setScaleY(1 - 0.05f * (level - 1));
                if (j!=0){
                    level--;
                }

            }

        }else    if (getChildCount() > 0) {
            view = getChildAt(0);
            view.layout((int) (centerX-childWidth/2),(int) (centerY-childHeight/2),
                    (int) (centerX+childWidth/2), (int) (centerY+childHeight/2));
        }
    }

    public void setAdapter(@NonNull CardBaseAdapter adapter) {
        if (adapter == null) throw new NullPointerException("Adapter不能为空");
        this.adapter = adapter;
        //初始化数据 你需要显示几个页面
        changeViews();
        adapter.registerDataSetObserver(new DataSetObserver() {

            @Override
            public void onChanged() {
                getMore();
            }

            @Override
            public void onInvalidated() {

                getMore();
            }
        });
    }

    public void getMore() {
        if (getChildCount()+deleteNum<adapter.getCount()){
            View view = adapter.getView(getChildCount()+deleteNum,
                    getChildAt(getChildCount()),this);
            //后面加载进来数据都放在最底层
            addView(view,0);
        }

    }
    private void changeViews() {
        View view = null;
        /**
         *   showCards 是你需要显示几张卡片，showCards-j是为了排列顺序
         *   viewgroup是最先加进来的view是在最底层的，所以我为了让第一个加进来的放在最上层，用了这个
         *   举个栗子:显示3张页面 showCards = 3，先加载第四个页面（因为最底层还要有一个你看不到的页面）放在最底层，
         *   到最后j=3时 加载第一张页面数据，同时将它显示优先级设为最高addView(view,j);
         *   deleteNum是你右滑删掉的页面数量
          */
        for (int j = 0; j <=showCards; j++) {
            if (j+deleteNum<adapter.getCount()){
                view = adapter.getView(showCards-j, getChildAt(j),this);
                addView(view,j);
            }


        }
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return mDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }


    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    public SwipeCardView setShowCards(int showCards) {
        this.showCards = showCards;
        return this;
    }

    public SwipeCardView setTransY(int transY) {
        this.transY = transY;
        return this;
    }
}
