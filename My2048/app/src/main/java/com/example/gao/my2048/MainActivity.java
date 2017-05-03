package com.example.gao.my2048;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
/**
 * Created by GAO on 2017/4/3.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tvScore,maxScore;
    private static MainActivity mainActivity = null;
    private Button restart,quitGame;
    private int currentBestScore;

    public MainActivity(){          //可以在外界访问MainActivity的实例
        mainActivity = this;        //MainActivity一旦被构建，就给MainActivity静态变量赋值，就可以从外界进行访问
    }

    public static MainActivity getMainActivity(){
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvScore = (TextView) findViewById(R.id.tvScore);
        maxScore = (TextView) findViewById(R.id.maxScore);
        BestScode bs = new BestScode(this);
        currentBestScore = bs.getBestScode();
        quitGame = (Button) findViewById(R.id.quit);
        restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(this);
        quitGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        try{
            switch (v.getId()){
                case R.id.quit:
                    System.exit(0);
                    break;
                case R.id.restart:
                    //new GameView(this,attrs,defStylcAttr).startGame();//调用方法不成功
                    // Toast.makeText(this, "Not complete", Toast.LENGTH_SHORT).show();
                    GameView gameView = new GameView(this);
                    gameView = (GameView)findViewById(R.id.gameView);
                    gameView.startGame();
                    break;
            }
        }catch (NullPointerException e){
            //空指针异常， 用一个null对象调用了它的某一个方法导致java.lang.NullPointerException错误
            e.printStackTrace();
        }
    }

    //计分实现：
    private int score = 0;

    public void clearScore(){
        score = 0;
        showScore();
    }
    public void showScore(){
        tvScore.setText(score + "");
    }
    //使用方法添加分数并显示出来
    public void addScore(int s){
        score += s;
        showScore();
        if(score > currentBestScore ) {
            currentBestScore = score;
            BestScode bs = new BestScode(this);
            bs.setBestScode(currentBestScore);
            maxScore.setText("" + currentBestScore);

        }
    }
    public void showBestScore(){
        maxScore.setText(currentBestScore + "");
    }

    public void setScore(int score){
        this.score = score;
    }
    public int getScore(){
        return score;
    }
}
