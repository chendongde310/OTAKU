package lol.chendong.otaku;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * 作者：陈东  —  www.renwey.com
 * 日期：2016/12/27 - 17:04
 * 注释：Base
 */
public abstract class BaseActivity extends AppCompatActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public abstract void initView();

    public abstract void initData();

    public abstract void initListener();


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initView();
        initData();
        initListener();


    }
}
