package com.example.gao.my2048;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GAO on 2017/4/10.
 */
public class GameView extends GridLayout{
    public GameView(Context context, AttributeSet attrs, int defStyleAttr){ //构造方法
        super(context, attrs, defStyleAttr);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs){
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context){
        super(context);
        initGameView();
    }

    private void initGameView(){    //初始化游戏
        setColumnCount(4);  //指定为4列
        setBackgroundColor(0xffF0FFF0);

            setOnTouchListener(new OnTouchListener() {  //设置触摸监听
                private float startX,startY,offsetX,offsetY;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            startX = event.getX();
                            startY = event.getY();
                            break;
                        case MotionEvent.ACTION_UP:
                            offsetX = event.getX() - startX;
                            offsetY = event.getY() - startY;

                            if(Math.abs(offsetX) > Math.abs(offsetY)){
                                //如果用户斜着滑动，X的偏移量大于Y的偏移量，说明用户有向X方向移动的意图
                                if(offsetX < -5){
                                    swipeLeft();
                                }
                                else if(offsetX > 5){
                                    swipeRight();
                                }
                            }
                                else {
                                    if(offsetY < -5){
                                        swipeUp();
                                    }
                                    else if(offsetY > 5){
                                       swipeDown();
                                    }
                                }
                                break;
                    }
                    return true;
                }
            });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        //该方法是宽高发生改变的时候我们可以得到当前的宽高是多少
        //该方法也是在游戏一被创建的时候就调用，也就是用来初始宽高的方法
        super.onSizeChanged(w, h, oldw, oldh);

        //获取手机较窄的长度，-15是用来间隔每个卡片的距离
        int cardWidth = (Math.min(w,h) - 15) / 4;
        //该方法初始化的时候新建16个卡片
        addCards(cardWidth,cardWidth);
        startGame();
    }

    private Card[][] cardsMap = new Card[4][4];

    private void addCards(int cardWidth,int cardHeight){
        Card c;
        for(int y = 0; y < 4; y++){
            for(int x = 0; x < 4; x++){
                c = new Card(getContext());
                c.setNum(0);    //给卡片设置初始化数字
                addView(c,cardWidth,cardHeight);

                cardsMap[x][y] = c;  //把16张卡片全部添加进这个二维数组中，方便以后的使用

            }
        }
    }

    //开始游戏模块
    public void startGame(){
        MainActivity.getMainActivity().clearScore();  //分数清0
        MainActivity.getMainActivity().showBestScore();  //显示最高分

        for(int y = 0; y < 4; y++){
            for(int x = 0; x < 4; x++){
                cardsMap[x][y].setNum(0);  //清0
            }
        }

        addRandomNum();     //调用随机数模块
        addRandomNum();
    }

    //添加一个List来存放Point来控制随机数方法里的随机数
    private List<Point> emptyPoints = new ArrayList<Point>();

    //添加随机数模块
    private void addRandomNum(){

        emptyPoints.clear();  //把Point清空，每次调用添加随机数时就清空之前所控制的指针

        for(int y = 0; y < 4; y++){   //对所有的位置进行遍历，为每个卡片添加了一个指针
            for(int x = 0; x < 4; x++){
                if(cardsMap[x][y].getNum() <= 0){
                    //只有当卡片上没有数字时才会添加
                    emptyPoints.add(new Point(x,y));
                }
            }
        }

        Point p = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));  //Math.random()随机获取0-1之间的数
        cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);     //2和4出现的概率为9：1
    }


    //向左滑动模块
    private void swipeLeft(){
       boolean merge = false;  //控制每滑动一次画面就加一个随机数到画面，也就是在下面所有for循环之后
        for(int y = 0; y < 4; y++){
            for(int x = 0; x < 4; x++){
                for(int x1 = x + 1; x1 < 4; x1++){      //在水平固定一个格子之后再继续遍历其他格子
                    if(cardsMap[x1][y].getNum() > 0){
                        if( cardsMap[x][y].getNum() <= 0){
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());    //把与被固定的格子同一水平的格子的数字赋值给被固定的格子
                            cardsMap[x1][y].setNum(0);

                            x--;  //同一层的不同列退一格继续循环
                            merge = true;

                        }else if (cardsMap[x][y].equals(cardsMap[x1][y])){
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x1][y].setNum(0);
                            //给MainActivity中的score加上分数
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        //若merge为真执行添加随机数方法
        if (merge){
            addRandomNum();
            checkComplete();
        }
    }

    //向右滑动模块
    private void swipeRight(){
       boolean merge = false;
        for(int y = 0; y < 4; y++){
            for(int x = 3; x >= 0; x--){
                for(int x1 = x - 1; x1 >= 0; x1--){
                    if(cardsMap[x1][y].getNum() > 0){
                        if(cardsMap[x][y].getNum() <= 0){
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);

                            x++;
                            merge = true;

                        }else if (cardsMap[x][y].equals(cardsMap[x1][y])){
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x1][y].setNum(0);

                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;

                        }
                        break;
                    }
                }
            }
        }

        if(merge){
            addRandomNum();
            checkComplete();
        }
    }

    //向上滑动模块
    private void swipeUp(){
       boolean merge = false;
        for(int x = 0; x < 4; x++){
            for(int y = 0; y < 4; y++){
                for(int y1 = y + 1; y1 < 4; y1++){
                    if(cardsMap[x][y1].getNum() > 0){
                        if(cardsMap[x][y].getNum() <= 0){
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);

                            y--;
                            merge = true;

                        }else if (cardsMap[x][y].equals(cardsMap[x][y1])){
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x][y1].setNum(0);

                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;

                        }
                        break;
                    }
                }
            }
        }

        if(merge){
            addRandomNum();
            checkComplete();
        }
    }

    //向下滑动模块
    private void swipeDown(){
        boolean merge = false;
        for(int x = 0; x < 4; x++){
            for(int y = 3; y >= 0; y--){

                for(int y1 = y - 1; y1 >= 0; y1--){
                    if(cardsMap[x][y1].getNum() > 0){
                        if(cardsMap[x][y].getNum() <= 0){
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);

                            y++;
                            merge = true;

                        }else if (cardsMap[x][y].equals(cardsMap[x][y1])){
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x][y1].setNum(0);

                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;

                        }
                        break;
                    }
                }
            }
        }
        if(merge){
            addRandomNum();
            checkComplete();
        }
    }

    //检查游戏是否结束
    private void checkComplete() {
        boolean complete = true;
   ALL:
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardsMap[x][y].getNum() == 0 ||
                        (x > 0 && cardsMap[x][y].equals(cardsMap[x - 1][y])) ||
                        (x < 3 && cardsMap[x][y].equals(cardsMap[x + 1][y])) ||
                        (y > 0 && cardsMap[x][y].equals(cardsMap[x][y - 1])) ||
                        (y < 3 && cardsMap[x][y].equals(cardsMap[x][y + 1]))
                        ){
                    complete = false;
                    break ALL;
                }

            }
        }
        if (complete) {
            new AlertDialog.Builder(getContext())
                    .setTitle("游戏结束")
                    .setMessage("您的分数为：" + MainActivity.getMainActivity().getScore())
                    .setPositiveButton("重新开始",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            startGame();
                        }
                    })
                    .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                        public void onClick (DialogInterface dialog,int which){
                            MainActivity.getMainActivity().finish();
                }
            }).show();
        }
    }
}



