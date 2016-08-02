package cn.lemon.guideview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    private GuideView mGVOne,mGVTwo,mGVThree;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_guideview,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView mHintView = new TextView(getActivity());
        mHintView.setText("哇咔咔");
        mHintView.setTextSize(16f);
        mHintView.setTextColor(Color.WHITE);
        mHintView.setBackgroundColor(Color.GRAY);

        mGVOne = new GuideView.Builder(getActivity())
                .setTargetView(R.id.text_one)
                .setHintView(mHintView)
                .setHintViewDirection(Direction.RIGHT_BOTTOM)
                .setTransparentOvalPadding(20)
                .setBackgroundColor(0xcc789456)
                .showOnce(false)
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
                .setHintView(mHintView)
                .setHintViewDirection(Direction.RIGHT_ABOVE)
                .setTransparentOvalPaddingLeft(20)
                .setTransparentOvalPaddingRight(20)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGVTwo.hide();
                        mGVThree.show();
                    }
                })
                .showOnce(false)
                .create();

        mGVThree = new GuideView.Builder(getActivity())
                .setTargetView(R.id.text_three)
                .setHintView(mHintView)
                .setHintViewDirection(Direction.LEFT_BOTTOM)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGVThree.hide();
                    }
                })
                .showOnce(false)
                .create();

    }
}
