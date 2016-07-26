package cn.lemon.guideview;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTestOne, mTestTwo, mTestThree;
    private TextView mHintView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new GuideViewFragment());
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
