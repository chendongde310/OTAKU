package lol.chendong.otaku.view;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.OnAnimEndListener;

import lol.chendong.otaku.R;

/**
 * 作者：陈东  —  www.renwey.com
 * 日期：2016/12/27 - 18:02
 * 注释：
 */
public class SwipRefreshHeader extends FrameLayout implements IHeaderView {


    private View rootView;
    private ImageView refreshArrow;
    private TextView refreshTextView;
    private ImageView loadingView;

    public SwipRefreshHeader(Context context) {
        super(context);

    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();


        if (rootView == null) {
            rootView = View.inflate(getContext(), R.layout.view_sinaheader, null);
            refreshArrow = (ImageView) rootView.findViewById(R.id.iv_arrow);
            refreshTextView = (TextView) rootView.findViewById(R.id.tv);
            loadingView = (ImageView) rootView.findViewById(R.id.iv_loading);
            addView(rootView);
        }
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingDown(float fraction, float maxHeadHeight, float headHeight) {

    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {

    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {

    }

    @Override
    public void onFinish(OnAnimEndListener animEndListener) {

    }

    @Override
    public void reset() {

    }
}
