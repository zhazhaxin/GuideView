# 字节跳动 内推
### 抖音/番茄小说 Android/ios 急招，有其他岗位或者要求可以邮件私聊
欢迎发送简历到 1399750010@qq.com

# GuideView

 - gradle
 ```
   compile 'cn.Lemon:guideview:1.0.0'
 ```

 - 方法回调顺序
 ```
  * 方法回调：创建GuideView -- initParams(初始化参数) -- getTargetViewPosition(获取TargetView位置核心方法) -- show(添加GuideView进DecorView)
  *  -- addHintView -- GuideView.onMeasure -- GuideView.onLayout -- GuideView.onDraw
 ```

 - 使用
 ```java
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
 mHintView.setText("hello word");
 mHintView.setTextSize(15);
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

}
 ```

 - Demo效果图

<img src="demo.png" width="320" height="564"/>
<img src="netease_demo.png" width="320" height="564"/>

## License

This project is licensed under the terms of the Apache License 2.0

>
