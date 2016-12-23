package lol.chendong.data.meizhi;

import com.orhanobut.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：陈东  —  www.renwey.com
 * 日期：2016/12/23 - 14:04
 * 注释：
 */
public class MeizhiData {


    public static final String 最热 = "hot/";
    public static final String 推荐 = "best/";
    public static final String 最新 = "";
    public static final String 性感 = "xinggan/";
    public static final String 日本 = "japan/";
    public static final String 台湾 = "taiwan/";
    public static final String 清纯妹纸 = "mm/";
    private  static final String 搜索 = "search/";


    private static MeizhiData ourInstance = new MeizhiData();


    private MeizhiData() {

    }

    public static MeizhiData getInstance() {
        return ourInstance;
    }

    /**
     * 获取主页列表
     *
     * @param type
     * @param page
     * @return
     */
    public Observable<List<MeizhiBean>> getHomeMeizhiList(final String type, final int page) {

        Observable<List<MeizhiBean>> meizhiOb = Observable.create(new Observable.OnSubscribe<List<MeizhiBean>>() {
            @Override
            public void call(Subscriber<? super List<MeizhiBean>> subscriber) {
                List<MeizhiBean> meizhiBeanList = new ArrayList<>();
                try {
                    Document doc = Jsoup.connect("http://www.mzitu.com/" + type + "page/" + page).get();
                    Element content = doc.getElementById("pins");
                    Elements dataList = content.getElementsByTag("li");
                    for (Element data : dataList) {
                        MeizhiBean mbean = new MeizhiBean();
                        mbean.setTime(data.getElementsByClass("time").text());
                        mbean.setView(data.getElementsByClass("view").text());
                        Element link = data.select("a").first();//查找第一个a元素
                        mbean.setUrl(link.attr("href"));
                        Element img = link.getElementsByTag("img").first();
                        mbean.setTitle(img.attr("alt"));
                        MeizhiBean.MeiZhiImage mimg = new MeizhiBean.MeiZhiImage();
                        mimg.setWidth(Integer.parseInt(img.attr("width")));
                        mimg.setHeight(Integer.parseInt(img.attr("height")));
                        mimg.setImgUrl(img.attr("data-original"));
                        mbean.setImage(mimg);
                        meizhiBeanList.add(mbean);
                    }
                    subscriber.onNext(meizhiBeanList);

                } catch (IOException e) {
                    subscriber.onError(e);
                }

            }
        });
        return meizhiOb
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());

    }


    /**
     * 搜索妹子
     *
     * @param search
     * @param page
     * @return
     */
    public Observable<List<MeizhiBean>> searchMeizhiList(final String search, final int page) {
        return getHomeMeizhiList(搜索 + search + "/", page);
    }

    /**
     * 获取详情
     *
     * @param meizhiBean
     * @return
     */
    public Observable<List<MeizhiBean>> getDetailMeizhiList(final MeizhiBean meizhiBean) {
        Observable<List<MeizhiBean>> meizhiOb = Observable.create(new Observable.OnSubscribe<List<MeizhiBean>>() {
            @Override
            public void call(Subscriber<? super List<MeizhiBean>> subscriber) {
                List<MeizhiBean> meizhiBeanList = new ArrayList<>();
                try {

                    int page = getDetailPage(meizhiBean.getUrl());
                    Logger.d("page" + page);
                    for (int i = 1; i <= page; i++) {
                        MeizhiBean mbean = new MeizhiBean(meizhiBean);
                        Document doc = Jsoup.connect(meizhiBean.getUrl() + "/" + i).get();
                        Element img = doc.getElementsByClass("main-image").first();
                        Element dataa = img.getElementsByTag("a").first();
                        Element data = dataa.getElementsByTag("img").first();

                        mbean.getImage().setImgUrl(data.attr("src"));
                        meizhiBeanList.add(mbean);
                    }
                    subscriber.onNext(meizhiBeanList);
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
        return meizhiOb
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
    }

    /**
     * 获取详情页的页数
     *
     * @param url
     * @return
     */
    private int getDetailPage(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Element link = doc.getElementsByClass("pagenavi").first();
            Element resultLinks = link.getElementsByClass("dots").first();
            Element page = resultLinks.nextElementSibling();
            return Integer.parseInt(page.text());
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
    }


    /**
     * 获取自拍的页数
     *
     * @return
     */
    private int getSharePage() {
        try {
            Document doc = Jsoup.connect("http://www.mzitu.com/share").get();
            Element link = doc.getElementsByClass("pagenavi-cm").first().getElementsByClass("page-numbers current").first();
            return Integer.parseInt(link.text());
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
    }


    /**
     * 自拍
     *
     * @param page
     * @return
     */
    public Observable<List<MeizhiBean>> getSharelMeizhiList(final int page) {
        Observable<List<MeizhiBean>> meizhiOb = Observable.create(new Observable.OnSubscribe<List<MeizhiBean>>() {
            @Override
            public void call(Subscriber<? super List<MeizhiBean>> subscriber) {
                List<MeizhiBean> meizhiBeanList = new ArrayList<>();

                int maxPage = getSharePage() + 1;
                if (page > maxPage) {
                    subscriber.onNext(meizhiBeanList);
                } else {
                    try {
                        Document doc = Jsoup.connect("http://www.mzitu.com/share/comment-page-" + (maxPage - page) + "#comments").get();
                        Element link = doc.getElementById("comments");
                        Elements dataList = link.getElementsByTag("li");
                        for (Element data : dataList) {
                            Element img = data.getElementsByTag("img").first();
                            MeizhiBean meizhiBean = new MeizhiBean();
                            meizhiBean.setTitle("美女自拍");
                            meizhiBean.setTime(data.getElementsByTag("a").first().text());
                            meizhiBean.setImage(new MeizhiBean.MeiZhiImage(img.attr("src")));
                            meizhiBeanList.add(meizhiBean);
                        }
                        subscriber.onNext(meizhiBeanList);
                    } catch (IOException e) {
                        subscriber.onError(e);
                    }

                }
            }
        });

        return meizhiOb
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
    }


    /**
     * 获取每日更新
     *
     * @return
     */
    public Observable<List<MeizhiBean>> getAllMeizhiList() {
        Observable<List<MeizhiBean>> meizhiOb = Observable.create(new Observable.OnSubscribe<List<MeizhiBean>>() {
            @Override
            public void call(Subscriber<? super List<MeizhiBean>> subscriber) {
                List<MeizhiBean> meizhiBeanList = new ArrayList<>();
                try {
                    Document doc = Jsoup.connect("http://www.mzitu.com/all").get();
                    Elements link = doc.getElementsByClass("archives");
                    for (Element data : link) {
                        for (Element d : data.getElementsByTag("a")) {
                            MeizhiBean meizhiBean = new MeizhiBean();
                            meizhiBean.setUrl(d.attr("href"));
                            meizhiBean.setTitle(d.text());
                            meizhiBeanList.add(meizhiBean);
                        }
                    }
                    subscriber.onNext(meizhiBeanList);
                } catch (IOException e) {
                    subscriber.onError(e);
                }


            }
        });

        return meizhiOb
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
    }


}


