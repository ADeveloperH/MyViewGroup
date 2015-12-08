package project.hjking.cn.myviewgroup;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by Administrator on 2015/12/6 0006.
 */
public class MyScrollowViewActivity extends Activity {
    private int[] imgesIds = new int[]{R.mipmap.a1, R.mipmap.a2
            ,R.mipmap.a3,R.mipmap.a4,R.mipmap.a5,R.mipmap.a6};
    private RadioGroup radioGroup;
    private MyScrollowView msv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myscrollowview);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        msv = (MyScrollowView) findViewById(R.id.msv);
    }

    private void initData() {
        for (int i = 0; i < imgesIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imgesIds[i]);
            //将图片添加至自定义的view中去
            msv.addView(imageView);
        }

        //添加RadioButton
        for (int i = 0; i < msv.getChildCount(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioGroup.addView(radioButton);

            //这里将id和下标设置成一样的，方便修改选中状态
            radioButton.setId(i);
            if (i == 0) {
                //默认选中第一个
                radioButton.setChecked(true);
            } else {
                radioButton.setChecked(false);
            }

        }
    }

    private void setListener() {
        msv.setOnPageChangeListener(new MyScrollowView.IOnPageChangeListener() {
            @Override
            public void onPageChanged(int index) {
                //当页面改变时回调该方法.每个radioButton的id和它所处位置的下标是一样的。
                radioGroup.check(index);
            }
        });

        //设置RadioGroup改变的监听
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //点击RadioButton后，跳转到指定的页面
                //这里的checkedId和它所处的页面下标是一样的
                msv.moveToDestination(checkedId);
            }
        });
    }
}
