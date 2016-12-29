package lol.chendong.otaku.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lol.chendong.data.meizhi.MeizhiBean;
import lol.chendong.data.meizhi.MeizhiData;
import lol.chendong.otaku.BaseActivity;
import lol.chendong.otaku.R;
import lol.chendong.otaku.adapter.MzDetailAdapter;
import lol.chendong.otaku.constant.Str;
import rx.Subscriber;

public class ScrollPicActivity extends BaseActivity {

    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 1000;
    private static final int UI_ANIMATION_DELAY = 150;
    private final Handler mHideHandler = new Handler();
    ImageView backButton;
    ImageView shareButton;
    private FrameLayout.LayoutParams layoutParams;
    private RecyclerView mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private FloatingActionButton downloadImg;
    private boolean mVisible;
    private RelativeLayout toolbar;
    private TextView title;
    private MeizhiBean meizhiBean;
    private List<MeizhiBean> dataList = new ArrayList<>();
    private FrameLayout frameLayout;
    private MzDetailAdapter mAdapter;
    private int dataPage = 1;//默认从第一页开始
    private boolean isShow = false; //是否显示标题栏
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            toolbar.setVisibility(View.VISIBLE);
            downloadImg.setVisibility(View.VISIBLE);
            changeViewMargins(true);
        }
    };
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    private boolean getDataToDo = false;  //防止重复获取数据的阻断器
    private MeizhiBean choiceMzData;

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    /**
     * 改变宽度
     *
     * @param is 是否显示标题栏
     */
    private void changeViewMargins(boolean is) {
        isShow = is;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 10; i++) {
                    if (isShow) {
                        layoutParams.setMargins(dip2px(i * 4), dip2px(i * 8), dip2px(i * 4), dip2px(i * 8));
                    } else {
                        layoutParams.setMargins(dip2px(40 - i * 4), dip2px(80 - i * 8), dip2px(40 - i * 4), dip2px(80 - i * 8));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContentView.setLayoutParams(layoutParams);
                        }
                    });
                    try {
                        Thread.sleep(12);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_pic);

    }

    public int dip2px(float dpValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void initView() {
        frameLayout = (FrameLayout) findViewById(R.id.scroll_pic_fl);
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        downloadImg = (FloatingActionButton) findViewById(R.id.pic_content_controls);
        mContentView = (RecyclerView) findViewById(R.id.pic_content_list);
        backButton = (ImageView) findViewById(R.id.bar_back_img);
        shareButton = (ImageView) findViewById(R.id.bar_share_img);
        title = (TextView) findViewById(R.id.bar_title_text);
        mContentView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        mContentView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void initData() {
        layoutParams = new FrameLayout.LayoutParams(mContentView.getLayoutParams());
        mVisible = true;
        meizhiBean = getIntent().getParcelableExtra(Str.MEIZHI_DATA);
        if (meizhiBean != null) {
            title.setText(meizhiBean.getTitle());
            getData();
        } else {

        }
    }

    private void getData() {
        if (getDataToDo) {
            return;
        }
        getDataToDo = true;
        MeizhiData.getData().getDetailMeizhiList(meizhiBean, dataPage).subscribe(new Subscriber<List<MeizhiBean>>() {
            @Override
            public void onCompleted() {
                dataPage++;
                getDataToDo = false;
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getDataToDo = false;
            }

            @Override
            public void onNext(List<MeizhiBean> meizhiBeen) {
                dataList.addAll(meizhiBeen);
                if (mAdapter == null) {
                    mAdapter = new MzDetailAdapter(ScrollPicActivity.this, dataList);
                    mContentView.setAdapter(mAdapter);
                    setAdapterListener();
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });


    }

    private void setAdapterListener() {
        mAdapter.setOnItemClickListener(new MzDetailAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                mContentView.smoothScrollToPosition(postion);
                toggle();
                choiceMzData = dataList.get(postion);
            }
        });
    }

    /**
     * 保存图片
     */
    public String saveBitmap() {
        String path = "";
        ImageRequest imageRequest = ImageRequest.fromUri(choiceMzData.getImage().getImgUrl());
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                .getEncodedCacheKey(imageRequest, ScrollPicActivity.this);
        BinaryResource resource = ImagePipelineFactory.getInstance()
                .getMainFileCache().getResource(cacheKey);
        File b = ((FileBinaryResource) resource).getFile();
        String dirName = getSDPath() + "/otaku/";
        String fileName = "Mz" + System.currentTimeMillis() + ".jpg";
        Bitmap bmp = BitmapFactory.decodeFile(b.getPath());
        // 判断sd卡是否存在
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File dir = new File(dirName);
            // 判断文件夹是否存在，不存在则创建
            if (!dir.exists()) {
                dir.mkdir();
            }

            File file = new File(dirName + fileName);
            // 判断文件是否存在，不存在则创建
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            path = file.getPath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                // 第一参数是图片格式，第二个是图片质量，第三个是输出流
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                // 用完关闭
                fos.flush();
                fos.close();
            } catch (IOException e) {
                Toast.makeText(this, "保存图片失败", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        return path;
    }


    public void shareMsg() {
        String imgPath = saveBitmap();
        String msgTitle = "福利分享";

        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                Logger.d("设置分享文件为图片类型");
                intent.setType("image/jpg");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, getTitle()));
    }

    @Override
    public void initListener() {
        //下载图片
        downloadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AUTO_HIDE) {
                    delayedHide(AUTO_HIDE_DELAY_MILLIS);
                }
                 saveBitmap ();
                Toast.makeText(ScrollPicActivity.this, "图片已经保存", Toast.LENGTH_SHORT).show();
            }
        });

        //焦点图捕捉滚动事件
        mContentView.setOnTouchListener(new View.OnTouchListener() {
            final int OFFSET = 30; // 偏移量
            public float startX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isShow) {
                    int i = event.getAction();
                    if (i == MotionEvent.ACTION_DOWN) {
                        startX = event.getX();
                    } else if (i == MotionEvent.ACTION_UP) {
                        if (this.startX <= event.getX() + OFFSET && startX >= event.getX() - OFFSET) {
                            hide();
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
        //点击外边界退出焦点图界面
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow) {
                    hide();
                }
            }
        });
        //返回按钮
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollPicActivity.this.finish();
            }
        });
        //分享按钮
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareMsg();
            }
        });
        //列表滚动监听
        mContentView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isSlideToBottom(recyclerView)) {
                    getData();
                }
            }
        });

    }

    /**
     * 判断是否在底部
     *
     * @param recyclerView
     * @return
     */
    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    @Override
    public void onBackPressed() {
        if (isShow) {
            hide();
        } else {
            super.onBackPressed();
        }
    }

    private void hide() {
        toolbar.setVisibility(View.GONE);
        downloadImg.setVisibility(View.GONE);

        mVisible = false;
        changeViewMargins(false);
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
