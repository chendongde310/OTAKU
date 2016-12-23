package lol.chendong.data.meizhi;

/**
 * 作者：陈东  —  www.renwey.com
 * 日期：2016/12/23 - 13:50
 * 注释：妹纸图对象
 */
public class MeizhiBean {

    String title;
    String time;
    String url; //跳转页面
    String view;//点击次数
    MeiZhiImage image;
    public MeizhiBean() {
        image = new MeiZhiImage();
    }
    public MeizhiBean(MeizhiBean m) {
        title = m.getTitle();
        time = m.getTime();
        view = m.getView();
        image = new MeiZhiImage();
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MeiZhiImage getImage() {
        if(image!=null){
            return image;
        }else {
            return new MeiZhiImage();
        }

    }

    public void setImage(MeiZhiImage image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "MeizhiBean{" +
                "image=" + image.getImgUrl() +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", url='" + url + '\'' +
                ", view='" + view + '\'' +
                '}';
    }

    public static class MeiZhiImage {
        int Width;
        int height;
        String imgUrl;//图片地址

        public MeiZhiImage(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public MeiZhiImage( ) {

        }

        public int getWidth() {
            return Width;
        }

        public void setWidth(int width) {
            Width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }
}
