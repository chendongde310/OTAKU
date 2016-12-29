package lol.chendong.otaku.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import lol.chendong.data.meizhi.MeizhiBean;
import lol.chendong.otaku.R;

/**
 * 作者：陈东  —  www.renwey.com
 * 日期：2016/12/28 - 15:27
 * 注释：
 */
public class MzHomeAdapter extends RecyclerView.Adapter<MzHomeAdapter.MyViewHolder>  {

    private Context context;
    private List<MeizhiBean> meizhiBeanList;
    private onItemClickListener mItemClickListener;
    private onItemLongClickListener mItemLongClickListener;


    public MzHomeAdapter(Context context, List<MeizhiBean> data) {
        this.context = context;
        this.meizhiBeanList = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_meizhi_home, parent, false)
                , mItemClickListener, mItemLongClickListener);
        return holder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MeizhiBean data = meizhiBeanList.get(position);
        holder.title.setText(data.getTitle());
        //holder.img.setImageURI(data.getImage().getImgUrl());
        setImg(holder.img, Uri.parse(data.getImage().getImgUrl()));

    }


    @Override
    public int getItemCount() {
        return meizhiBeanList.size();
    }


    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(onItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(onItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }

    public void addData(List<MeizhiBean> data) {
        meizhiBeanList.clear();
        meizhiBeanList.addAll(data);
        notifyDataSetChanged();
    }

    private void setImg(final SimpleDraweeView img, Uri uri) {
        img.setAspectRatio(0.6f);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setTapToRetryEnabled(true)
                .setOldController(img.getController())
                .build();
        img.setController(controller);

    }



    public interface onItemClickListener {
        void onItemClick(View view, int postion);
    }


    public interface onItemLongClickListener {
        void onItemLongClick(View view, int postion);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        SimpleDraweeView img;  //图片
        TextView title; //标题
        TextView viewCount;  //查看次数
        private onItemClickListener mListener;
        private onItemLongClickListener mLongClickListener;


        public MyViewHolder(View itemView, onItemClickListener listener, onItemLongClickListener longClickListener) {
            super(itemView);
            mListener = listener;
            mLongClickListener = longClickListener;
            title = (TextView) itemView.findViewById(R.id.meizi_title);
            img = (SimpleDraweeView) itemView.findViewById(R.id.meizi_rough_pic);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        /**
         * 点击监听
         */
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        /**
         * 长按监听
         */
        @Override
        public boolean onLongClick(View arg0) {
            if (mLongClickListener != null) {
                mLongClickListener.onItemLongClick(arg0, getPosition());
            }
            return true;
        }


    }

}
