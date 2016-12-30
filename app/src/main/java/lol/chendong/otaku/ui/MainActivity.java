package lol.chendong.otaku.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import lol.chendong.data.meizhi.MeizhiBean;
import lol.chendong.data.meizhi.MeizhiData;
import lol.chendong.otaku.BaseActivity;
import lol.chendong.otaku.R;
import lol.chendong.otaku.adapter.MzHomeAdapter;
import lol.chendong.otaku.constant.Str;
import rx.Observer;

import static lol.chendong.data.meizhi.MeizhiData.自拍;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<MeizhiBean> data = new ArrayList<>();
    Observer<List<MeizhiBean>> observer;
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
    private MzHomeAdapter mAdapter;
    private int dataPage = 1; //数据页码
    private String dataType = MeizhiData.最新;
    private MaterialSearchView searchView;
    private RelativeLayout navHeaderRl;
    private String search;
    private StaggeredGridLayoutManager oneLine = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    private StaggeredGridLayoutManager twoLine = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    private boolean isShowUI = true; // 是否显示UI

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDataObserver();
        //获取默认主页
        MeizhiData.getData().getRoughPicList(dataType, dataPage).subscribe(observer);
    }

    @Override
    public void initView() {
        // startActivity(new Intent(MainActivity.this,ScrollPicActivity.class));
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
        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        //初始化NavigationView里headerView中的控件
        View headerView = navigationView.getHeaderView(0);
        otherButtonLayout = (LinearLayout) headerView.findViewById(R.id.other_buttons_ly);
        userNameTextView = (TextView) headerView.findViewById(R.id.nav_user_name);
        userProfilesTextView = (TextView) headerView.findViewById(R.id.nav_user_profiles);
        userPortraitImg = (CircleImageView) headerView.findViewById(R.id.nav_portrait_image);
        navHeaderRl = (RelativeLayout) headerView.findViewById(R.id.nav_header_rl);
        //初始化RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    public void initData() {
        //添加旋转动画效果
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        lac = new LayoutAnimationController(animation);
        lac.setOrder(LayoutAnimationController.ORDER_REVERSE);
        lac.setDelay(0.3f);
        otherButtonLayout.setLayoutAnimation(lac);
    }

    /**
     * 设置数据接收方法
     */
    private Observer<List<MeizhiBean>> initDataObserver() {
        observer = new Observer<List<MeizhiBean>>() {
            @Override
            public void onCompleted() {
                dataPage++;
                refreshLayout.finishLoadmore();
                refreshLayout.finishRefreshing();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                refreshLayout.finishLoadmore();
                refreshLayout.finishRefreshing();
            }

            @Override
            public void onNext(List<MeizhiBean> meizhiBeen) {
                data.addAll(meizhiBeen);

                if (mAdapter == null) {
                    mAdapter = new MzHomeAdapter(MainActivity.this, data);
                    mRecyclerView.setAdapter(mAdapter);
                    setAdapterListener();
                } else {
                    mAdapter.notifyDataSetChanged();
                    if (dataPage == 1) {
                        mRecyclerView.smoothScrollToPosition(0);
                    }
                }
            }
        };
        return observer;
    }

    private void setAdapterListener() {
        mAdapter.setOnItemClickListener(new MzHomeAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                if (!dataType.equals(自拍)) {
                    Intent intent = new Intent(MainActivity.this, ScrollPicActivity.class);
                    intent.putExtra(Str.MEIZHI_DATA, data.get(postion));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void initListener() {
        //设置Navigation点击监听
        navigationView.setNavigationItemSelectedListener(this);
        //设置nav头部背景监听
        navHeaderRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMsgAnimation();
            }
        });
        //设置fab监听
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });
        //设置搜索按钮监听
        setSearchListener();
        //设置列表滚动监听
        setListScrollListener();
        //设置下拉刷新、上拉加载
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                refreshData();
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                loadData();
            }
        });

        //设置抽屉滑动监听
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //TODO- 如果有信息的话就播放风车动画
                playMsgAnimation();
            }
        });

    }

    /**
     * 设置列表滚动监听,上滑隐藏GUI，下滑显示GUI
     */
    private void setListScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            final int OFFSET = 30; // 偏移量

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    if (searchView.isSearchOpen()) {
                        searchView.closeSearch();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy > OFFSET) {
                    if (isShowUI) {
                        hideGUI();
                    }
                }
                if (dy < -OFFSET) {
                    if (!isShowUI) {
                        showGUI();
                    }
                }
            }
        });

    }

    /**
     * 显示GUI
     */
    private void showGUI() {
        isShowUI = true;
        toolbar.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏GUI
     */
    private void hideGUI() {
        isShowUI = false;
        toolbar.setVisibility(View.GONE);
        fab.setVisibility(View.GONE);

    }

    /**
     * 设置搜索按钮监听
     */
    private void setSearchListener() {
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (query.equals(search)) {
                    Logger.d("搜索");
                    dataType = MeizhiData.搜索;
                    dataPage = 1;
                    refreshData();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && newText.length() > 0) {
                    search = newText;
                }
                return false;
            }
        });
    }

    /**
     * 播放风车动画
     */
    private void playMsgAnimation() {
        if (otherButtonLayout.getLayoutAnimation().isDone()) {
            otherButtonLayout.startLayoutAnimation();
        }
    }

    /**
     * 刷新数据
     */
    private void refreshData() {
        dataPage = 1;
        data.clear();
        loadData();
    }

    /**
     * 加载数据
     */
    private void loadData() {
        //更改布局
        if (mRecyclerView.getLayoutManager() != twoLine) {
            mRecyclerView.setLayoutManager(twoLine);
        }
        if (dataType.equals(MeizhiData.台湾) ||
                dataType.equals(MeizhiData.性感) ||
                dataType.equals(MeizhiData.推荐) ||
                dataType.equals(MeizhiData.日本) ||
                dataType.equals(MeizhiData.最新) ||
                dataType.equals(MeizhiData.清纯妹纸) ||
                dataType.equals(MeizhiData.最热)) {
            MeizhiData.getData().getRoughPicList(dataType, dataPage).subscribe(observer);
        } else if (dataType.equals(自拍)) {
            mRecyclerView.setLayoutManager(oneLine);
            MeizhiData.getData().getSharelMeizhiList(dataPage).subscribe(observer);
        } else if (dataType.equals(MeizhiData.搜索)) {
            if (search != null && search.length() > 0) {
                MeizhiData.getData().getSearchMeizhiList(search, dataPage).subscribe(observer);
                Logger.d("搜索词:" + search);
            } else {
                searchView.showSearch();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
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

        if (id == R.id.nav_hot) {
            changeType( MeizhiData.最热);
        } else if (id == R.id.nav_best) {
            changeType( MeizhiData.推荐);
        } else if (id == R.id.nav_new) {
            changeType( MeizhiData.最新);
        } else if (id == R.id.nav_sexy) {
            changeType( MeizhiData.性感);
        } else if (id == R.id.nav_riben) {
            changeType( MeizhiData.日本);
        } else if (id == R.id.nav_taiwan) {
            changeType( MeizhiData.台湾);
        } else if (id == R.id.nav_qingchun) {
            changeType( MeizhiData.清纯妹纸);
        } else if (id == R.id.nav_share) {
            changeType(MeizhiData.自拍);
        }
        else if (id == R.id.nav_info_1 ||
                id == R.id.nav_info_2 ||
                id == R.id.nav_info_3) {
            Uri uri = Uri.parse("https://github.com/chendongde310/OTAKU");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void changeType(String s){
        if(!dataType.equals(s)) {
            dataType = s;
            refreshData();
        }
    }


}
