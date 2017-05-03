package com.example.gao.my2048;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by GAO on 2017/4/21.
 */
public class BestScode {
    private SharedPreferences sp;
    public BestScode(Context context) {
        sp = context.getSharedPreferences("bestScore", context.MODE_PRIVATE);
    }

    public int getBestScode(){
        int bestScode = sp.getInt("bestScode", 0);
        return bestScode;
    }

    public void setBestScode(int bestScode){
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("bestScode", bestScode);
        editor.commit();
    }
}
