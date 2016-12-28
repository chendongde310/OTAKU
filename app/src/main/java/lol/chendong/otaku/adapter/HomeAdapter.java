package lol.chendong.otaku.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import lol.chendong.data.meizhi.MeizhiBean;
import lol.chendong.otaku.R;

/**
 * 作者：陈东  —  www.renwey.com
 * 日期：2016/12/28 - 15:27
 * 注释：
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    private Context context;
    private List<MeizhiBean> meizhiBeanList;


    public HomeAdapter(Context context, List<MeizhiBean> data) {
        this.context = context;
        this.meizhiBeanList = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_meizhi_home, parent, false));
        return holder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MeizhiBean data = meizhiBeanList.get(position);
        holder.title.setText(data.getTitle());
        holder.img.setAspectRatio(0.6f);
        holder.img.setImageURI(data.getImage().getImgUrl());

    }

    @Override
    public int getItemCount() {
        return meizhiBeanList.size();
    }

    public void addData(List<MeizhiBean> data) {
        meizhiBeanList.clear();
        meizhiBeanList.addAll(data);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView img;  //图片
        TextView title; //标题
        TextView viewCount;  //查看次数


        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.meizi_title);
            img = (SimpleDraweeView) itemView.findViewById(R.id.meizi_rough_pic);


        }


    }

}
