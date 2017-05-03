package com.example.gao.my2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by GAO on 2017/4/16.
 */
public class Card extends FrameLayout{
    private int num = 0;
    private TextView label;

    public Card(Context context) {
        super(context);
        //初始化文本
        label = new TextView(getContext());
        //设置文本的大小
        label.setTextSize(32);
        //设置背景颜色
        label.setBackgroundColor(0x335F9EA0);
        //卡片在中心显示
        label.setGravity(Gravity.CENTER);
        //用来初始化layout控件textView里的高和宽属性，（-1，-1）表示填充满整个父级容器
        LayoutParams lp = new LayoutParams(-1,-1);
        //设置每个卡片左和上的间隔
        lp.setMargins(15,15,0,0);
        //为label控件加上已经初始化了的高和宽属性
        addView(label,lp);
    }

    public int getNum(){
        return num;
    }

    public void setNum(int num){
        this.num = num;
        label.setBackgroundColor(getBackColor(num));    //设置卡片不同数字的颜色
        if(num <= 0){
            label.setText("");
        }
        else {
            label.setText(num + "");
        }
    }
    //设置背景色
    private int defaultBackColor = 0x338B8B00;
    //得到不同数字卡片的颜色
    private int getBackColor(int num) {

        int bgcolor = defaultBackColor;
        switch (num) {
            case 0:
                bgcolor = 0xffCCC0B3;
                break;
            case 2:
                bgcolor = 0xffEEE4DA;
                break;
            case 4:
                bgcolor = 0xffEDE0C8;
                break;
            case 8:
                bgcolor = 0xffF2B179;// #F2B179
                break;
            case 16:
                bgcolor = 0xffF49563;
                break;
            case 32:
                bgcolor = 0xffF5794D;
                break;
            case 64:
                bgcolor = 0xffF55D37;
                break;
            case 128:
                bgcolor = 0xffEEE863;
                break;
            case 256:
                bgcolor = 0xffEDB04D;
                break;
            case 512:
                bgcolor = 0xffECB04D;
                break;
            case 1024:
                bgcolor = 0xffEB9437;
                break;
            case 2048:
                bgcolor = 0xffEA7821;
                break;
            default:
                bgcolor = 0xffEA7821;
                break;
        }
        return bgcolor;
    }

    //判断两张卡片数字是否一样
    public boolean equals(Card c){
        return getNum() == c.getNum();
    }
}
