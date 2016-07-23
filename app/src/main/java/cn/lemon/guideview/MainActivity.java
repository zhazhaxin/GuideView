package cn.lemon.guideview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import cn.lemon.view.Direction;
import cn.lemon.view.GuideView;

public class MainActivity extends AppCompatActivity {

    private TextView mTestOne, mTestTwo, mTestThree;
    private TextView mHintView;
    private GuideView mG1, mG2, mG3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTestOne = (TextView) findViewById(R.id.text_one);
        mTestTwo = (TextView) findViewById(R.id.text_two);
        mTestThree = (TextView) findViewById(R.id.text_three);

    }

    @Override
    protected void onResume() {
        super.onResume();

        mHintView = new TextView(this);
        mHintView.setText("哇咔咔");
        mHintView.setTextSize(16f);
        mHintView.setTextColor(Color.WHITE);
        mHintView.setBackgroundColor(Color.GRAY);

        mG1 = new GuideView.Builder(this)
                .setTargetView(R.id.text_one)
                .setHintView(mHintView)
                .setHintViewDirection(Direction.RIGHT_BOTTOM)
                .setTransparentOvalPadding(20)
                .setBackgroundColor(0xcc789456)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mG1.hide();
                        mG2.show();
                    }
                })
                .create();
        mG1.show();

        mG2 = new GuideView.Builder(this)
                .setTargetView(R.id.text_two)
                .setHintView(mHintView)
                .setHintViewDirection(Direction.RIGHT_ABOVE)
                .setTransparentOvalPaddingLeft(20)
                .setTransparentOvalPaddingRight(20)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mG2.hide();
                        mG3.show();
                    }
                })
                .create();

        mG3 = new GuideView.Builder(this)
                .setTargetView(R.id.text_three)
                .setHintView(mHintView)
                .setHintViewDirection(Direction.LEFT_BOTTOM)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mG3.hide();
                    }
                })
                .create();

    }
}
