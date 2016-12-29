package lol.chendong.data.meizhi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：陈东  —  www.renwey.com
 * 日期：2016/12/23 - 13:50
 * 注释：妹纸图对象
 */
public class MeizhiBean implements Parcelable {


    public static final Parcelable.Creator<MeizhiBean> CREATOR = new Parcelable.Creator<MeizhiBean>() {
        @Override
        public MeizhiBean createFromParcel(Parcel source) {
            return new MeizhiBean(source);
        }

        @Override
        public MeizhiBean[] newArray(int size) {
            return new MeizhiBean[size];
        }
    };
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

    protected MeizhiBean(Parcel in) {
        this.title = in.readString();
        this.time = in.readString();
        this.url = in.readString();
        this.view = in.readString();
        this.image = in.readParcelable(MeiZhiImage.class.getClassLoader());
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
        if (image != null) {
            return image;
        } else {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.time);
        dest.writeString(this.url);
        dest.writeString(this.view);
        dest.writeParcelable(this.image, flags);
    }

    public static class MeiZhiImage implements Parcelable {


        public static final Creator<MeiZhiImage> CREATOR = new Creator<MeiZhiImage>() {
            @Override
            public MeiZhiImage createFromParcel(Parcel source) {
                return new MeiZhiImage(source);
            }

            @Override
            public MeiZhiImage[] newArray(int size) {
                return new MeiZhiImage[size];
            }
        };
        int Width;
        int height;
        String imgUrl;//图片地址

        public MeiZhiImage(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public MeiZhiImage() {

        }

        protected MeiZhiImage(Parcel in) {
            this.Width = in.readInt();
            this.height = in.readInt();
            this.imgUrl = in.readString();
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.Width);
            dest.writeInt(this.height);
            dest.writeString(this.imgUrl);
        }
    }
}
