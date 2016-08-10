# GuideView

 - 参考了
 [https://github.com/laxian/GuideView](https://github.com/laxian/GuideView)

 - gradle
 ```
   compile 'cn.Lemon:guideview:0.1.5'
 ```
 - 使用
 ```java
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
         TextView mHintView = new TextView(getActivity());
         mHintView.setText("列夫·托尔斯泰曾在《安娜·卡列尼娜》里说过：幸福的家庭都是相似的");
         mHintView.setTextSize(20f);
         mHintView.setTextColor(Color.WHITE);

         mGVOne = new GuideView.Builder(getActivity())
                 .setTargetView(R.id.text_one)
                 .setHintView(mHintView)
                 .setHintViewDirection(Direction.BOTTON)
                 .setTransparentOvalPadding(20)
                 .setHintViewMarginTop(100)
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
                 .setHintViewDirection(Direction.ABOVE_ALIGN_LEFT)
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
                 .setHintView(mHintView)
                 .setHintViewDirection(Direction.BOTTOM_ALIGN_RIGHT)
                 .setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         mGVThree.hide();
                     }
                 })
                 .create();
     }
 }
 ```

##License

This project is licensed under the terms of the Apache License 2.0

>