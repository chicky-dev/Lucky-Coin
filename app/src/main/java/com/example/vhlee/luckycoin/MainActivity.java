package com.example.vhlee.luckycoin;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final float DEGREES_DEFAULT = 0;
    public static final float DEGREES_FLIP = 180;
    public static final int RANDOM_VALUE = 2;
    public static final int COUNT_SAME_SIDE = 7;
    public static final int COUNT_DIFFERENCE_SIDE = 8;
    public static final int ANIMATE_TIME = 150;
    private static final String BUNDLE_COIN_SIDE = "side";
    private ImageView mCoin;
    private ImageButton mLucky;
    private final int[] mSides = {R.drawable.side_heads, R.drawable.side_tails};
    private int mCoinSide;
    private MediaPlayer mSoundStart;
    private MediaPlayer mSoundEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        if (savedInstanceState != null) {
            mCoin.setImageResource(savedInstanceState.getInt(BUNDLE_COIN_SIDE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(BUNDLE_COIN_SIDE, mCoinSide);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_lucky:
                flipCoin();
                break;
        }
    }

    private void initUI() {
        mCoinSide = mSides[0];
        mSoundStart = MediaPlayer.create(this, R.raw.coin_start);
        mSoundEnd = MediaPlayer.create(this, R.raw.coin_end);
        mCoin = findViewById(R.id.image_coin);
        mLucky = findViewById(R.id.button_lucky);
        mLucky.setOnClickListener(this);
    }

    public void flipCoin() {
        Random random = new Random();
        int numb = random.nextInt(RANDOM_VALUE);
        Log.d("side", numb + "");
        if (numb == 0) {
            boolean stayTheSame = (mCoinSide == mSides[1]);
            animateCoin(stayTheSame);
            mCoinSide = mSides[1];
        } else {
            boolean stayTheSame = (mCoinSide == mSides[0]);
            animateCoin(stayTheSame);
            mCoinSide = mSides[1];
        }
    }

    private void animateCoin(boolean stayTheSame) {
        Flip3dAnimation animation;
        if (mCoinSide == mSides[0]) {
            animation = new Flip3dAnimation(mCoin, mSides[0], mSides[1],
                    DEGREES_DEFAULT, DEGREES_FLIP,
                    DEGREES_DEFAULT, DEGREES_DEFAULT, DEGREES_DEFAULT, DEGREES_DEFAULT);
        } else {
            animation = new Flip3dAnimation(mCoin, mSides[1], mSides[0],
                    DEGREES_DEFAULT, DEGREES_FLIP,
                    DEGREES_DEFAULT, DEGREES_DEFAULT, DEGREES_DEFAULT, DEGREES_DEFAULT);
        }
        if (stayTheSame) animation.setRepeatCount(COUNT_SAME_SIDE);
        else animation.setRepeatCount(COUNT_DIFFERENCE_SIDE);
        animation.setDuration(ANIMATE_TIME);
        animation.setInterpolator(new LinearInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mSoundStart.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSoundEnd.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mCoin.startAnimation(animation);
    }
}
