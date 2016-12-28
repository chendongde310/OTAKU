package lol.chendong.otaku.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import lol.chendong.data.meizhi.MeizhiBean;
import lol.chendong.data.meizhi.MeizhiData;
import lol.chendong.otaku.BaseActivity;
import lol.chendong.otaku.R;
import lol.chendong.otaku.bean.MessageBean;
import rx.Subscriber;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, IMainView {

    List<MeizhiBean> data = new ArrayList<>();
    private Toolbar toolbar;
    private NavigationView navigationView;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private RecyclerView mRecyclerView;
    private TwinklingRefreshLayout refreshLayout;
    private LinearLayout otherButtonLayout;
    private LayoutAnimationController lac;
    private CircleImageView userPortraitImg;
    private TextView userNameTextView;
    private TextView userProfilesTextView;
    private HomeAdapter mAdapter;
    private int dataPage = 1; //数据页码
    private String dataType = MeizhiData.最新;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    public void initView() {
        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refreshLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        //初始化NavigationView里headerView中的控件
        View headerView = navigationView.getHeaderView(0);
        otherButtonLayout = (LinearLayout) headerView.findViewById(R.id.other_buttons_ly);
        userNameTextView = (TextView) headerView.findViewById(R.id.nav_user_name);
        userProfilesTextView = (TextView) headerView.findViewById(R.id.nav_user_profiles);
        userPortraitImg = (CircleImageView) headerView.findViewById(R.id.nav_portrait_image);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void initData() {

        getData();

        //添加旋转动画效果
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        lac = new LayoutAnimationController(animation);
        lac.setOrder(LayoutAnimationController.ORDER_REVERSE);
        lac.setDelay(0.3f);
        otherButtonLayout.setLayoutAnimation(lac);

    }

    private void getData() {
        MeizhiData.getData().getRoughPicList(dataType, dataPage)
                .subscribe(new Subscriber<List<MeizhiBean>>() {
                    @Override
                    public void onCompleted() {
                        dataPage++;
                        refreshLayout.finishLoadmore();
                        refreshLayout.finishRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<MeizhiBean> meizhiBeen) {
                        data.addAll(meizhiBeen);
                        if (mAdapter == null) {
                            mAdapter = new HomeAdapter(MainActivity.this, meizhiBeen);
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            mAdapter.addData(data);
                        }
                    }
                });
    }

    @Override
    public void initListener() {
        //设置Navigation点击监听
        navigationView.setNavigationItemSelectedListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MainActivity.this, TestActivity.class));
            }
        });
        //设置下拉刷新、上拉加载
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                dataPage = 1 ;
                data.clear();
                getData();
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                getData();
            }
        });

        //设置抽屉滑动监听
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (otherButtonLayout.getLayoutAnimation().isDone()) {
                    otherButtonLayout.startLayoutAnimation();
                }

            }
        });
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search: //搜索

                break;
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void messageCallbak(MessageBean msg) {

    }
}
