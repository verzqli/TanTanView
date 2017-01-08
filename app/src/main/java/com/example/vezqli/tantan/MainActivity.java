package com.example.vezqli.tantan;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SwipeCardView swipeCardView;
    private List<SwipeBean> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeCardView = (SwipeCardView) findViewById(R.id.swipe);
        list = new ArrayList<>();

        list.add(new SwipeBean(R.drawable.s1,"美女一号"));
        list.add(new SwipeBean(R.drawable.s2,"美女二号"));
        list.add(new SwipeBean(R.drawable.s3,"美女三号"));
        list.add(new SwipeBean(R.drawable.s4,"美女四号"));
        list.add(new SwipeBean(R.drawable.s5,"美女五号"));
        list.add(new SwipeBean(R.drawable.s6,"美女六号"));
        list.add(new SwipeBean(R.drawable.s7,"美女七号"));
        list.add(new SwipeBean(R.drawable.s8,"美女八号"));
        list.add(new SwipeBean(R.drawable.s9,"美女九号"));
        list.add(new SwipeBean(R.drawable.s10,"美女十号"));
        list.add(new SwipeBean(R.drawable.s11,"美女十一号"));
        list.add(new SwipeBean(R.drawable.s12,"美女十二号"));
        list.add(new SwipeBean(R.drawable.s13,"美女十三号"));
        list.add(new SwipeBean(R.drawable.s14,"美女十四号"));
        swipeCardView.setShowCards(5)
                .setTransY(50)
                .setAdapter(new CardBaseAdapter(this,list));
    }
}
