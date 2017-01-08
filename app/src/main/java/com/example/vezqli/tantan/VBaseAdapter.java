package com.example.vezqli.tantan;

import android.content.Context;
import android.support.annotation.IdRes;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by verzqli on 2016/12/28 10:54
 */
public abstract class VBaseAdapter<E,V extends VBaseAdapter.BaseViewHolder> extends BaseAdapter {
    private static final String TAG = "VBaseAdapter";
    private Context context;
    private List<E> dataSource = new ArrayList<>(); //初始化一个防止getCount()空指针

    public VBaseAdapter(Context context, List<E> dataSource) {
        this.context = context;
        this.dataSource = dataSource;
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public E getItem(int i) {
        return dataSource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        V viewHolder = null;

        if (convertView == null){
            viewHolder = createHolder(i,viewGroup);
            Log.i(TAG, "getView: "+viewHolder.getItemView().getWidth()+viewHolder.getItemView().getHeight());
            if (viewHolder == null || viewHolder.getItemView()== null) {
                throw new NullPointerException("createViewHolder不能返回null或view为null的实例");
            }
            convertView = viewHolder.getItemView();
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (V) convertView.getTag();
        }
        viewHolder.setPosition(i);
        bindViewHoder(viewHolder,i,getItem(i));
        return viewHolder.getItemView();
    }
    protected  abstract V createHolder(int position, ViewGroup parent);
    protected  abstract void bindViewHoder(V holder, int position, E data);
    public static class BaseViewHolder {
        private View itemView;
        private SparseArray<View> cacheViewList = new SparseArray<>();
        private int position;
        public View getItemView(){
            return  this.itemView;

        }
        public  int getPosition(){
            return position;
        }
        public void setPosition(int position){
            this.position = position;
        }
        public BaseViewHolder (View itemView){
            this.itemView = itemView;

        }
        public <R> R getView(@IdRes int viewId){
            View viewCache = cacheViewList.get(viewId);
            if (viewCache==null){
                viewCache = itemView.findViewById(viewId);
                Log.e("itemView","viewCache"+viewCache);
                cacheViewList.put(viewId,viewCache);
            }
            return (R) viewCache;
        }
    }
}
