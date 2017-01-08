package com.example.vezqli.tantan;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by verzqli on 2016/12/28 11:37
 */
public class CardBaseAdapter extends VBaseAdapter<SwipeBean, CardBaseAdapter.ViewHolder>{
    private Context context;
    private LayoutInflater inflater;
    public CardBaseAdapter(Context context, List dataSource) {
        super(context, dataSource);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    protected ViewHolder createHolder(int position, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_swipe, parent,false);
        return new ViewHolder(view);
    }

    @Override
    protected void bindViewHoder(ViewHolder holder, int position, SwipeBean data) {
        Log.i("ddd", "bindViewHoder: "+position);
        holder. txtTitle.setText(data.getName());
        holder. num.setText(position+"/" + getCount());
        Glide.with(context)
                .load(data.getImageUrl())
                .into(holder.imageView);

    }

    static class ViewHolder extends VBaseAdapter.BaseViewHolder {
        private TextView txtTitle;
        private ImageView imageView;
        private TextView num;
        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = getView(R.id.name);
            imageView = getView(R.id.image);
            num = getView(R.id.num);
        }
    }
}
