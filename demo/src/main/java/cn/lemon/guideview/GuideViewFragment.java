package cn.lemon.guideview;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.lemon.view.Direction;
import cn.lemon.view.GuideView;

/**
 * Created by linlongxin on 2016/7/25.
 */

public class GuideViewFragment extends Fragment {

    private GuideView mGVOne, mGVTwo, mGVThree;
    private View fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_guideview, container, false);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragment.post(new Runnable() {
            @Override
            public void run() {
                showGuideViews();
            }
        });
    }

    public void showGuideViews() {
        TextView mHintViewOne = new TextView(getActivity());
        mHintViewOne.setText("hello word");
        mHintViewOne.setTextSize(15);
        mHintViewOne.setTextColor(Color.WHITE);

        mGVOne = new GuideView.Builder(getActivity())
                .setTargetView(R.id.text_one)
                .setHintView(mHintViewOne)
                .setHintViewDirection(Direction.RIGHT_BOTTOM)
                .setTransparentOvalPadding(20)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGVOne.hide();
                        mGVTwo.show();
                    }
                })
                .create();
        mGVOne.show();

        mGVTwo = new GuideView.Builder(getActivity())
                .setTargetView(R.id.text_two)
                .setHintView(mHintViewOne)
                .setHintViewDirection(Direction.BOTTOM_ALIGN_LEFT)
                .setTransparentOvalPaddingLeft(20)
                .setTransparentOvalPaddingRight(20)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGVTwo.hide();
                        mGVThree.show();
                    }
                })
                .create();

        mGVThree = new GuideView.Builder(getActivity())
                .setTargetView(R.id.text_three)
                .setHintView(mHintViewOne)
                .setHintViewDirection(Direction.LEFT_BOTTOM)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGVThree.hide();
                    }
                })
                .create();
    }
}
