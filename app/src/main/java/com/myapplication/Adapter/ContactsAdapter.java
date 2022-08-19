package com.myapplication.Adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapplication.R;
import com.myapplication.dto.System.MenuVM;
import com.myapplication.utils.StringUtil;

import java.util.List;
import static com.myapplication.Scan.Sound.context;
/**
 * Created by Vfun01 on 2017-10-24.
 */

public class ContactsAdapter extends RecyclerView.Adapter {
    //先定义两个ItemViewType，0代表头，1代表表格中间的部分
    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;
    //数据源
     private List<String> dataList;
    private List<Integer> mDatas ;
    private  List<String> valueList;
    private List<MenuVM> menuList;
    //构造函数
    public ContactsAdapter(List<String> dataList, List<String> valueList, List<Integer> mDatas) {
        this.dataList = dataList;
        this.mDatas = mDatas;
        this.valueList=valueList;
    }

    public ContactsAdapter(List<MenuVM> menuList) {
        this.menuList=menuList;
    }

    /**
     * 判断当前position是否处于第一个
     * @param position
     * @return
     */
    public boolean isHeader(int position) {
        return position == 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //在onCreateViewHolder方法中，我们要根据不同的ViewType来返回不同的ViewHolder
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            //对于Header，我们应该返回填充有Header对应布局文件的ViewHolder（再次我们返回的都是一个布局文件，请根据不同的需求做相应的改动）
            return new HeaderViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.business_layout, null));
        } else {
            //对于Body中的item，我们也返回所对应的ViewHolder
            return new BodyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.business_layout, null));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder,final  int position) {
        if (isHeader(position)) {
            String imgName=menuList.get(position).getPdaMenuIcon();
            int resId =0;
            if(!StringUtil.isEmpty(imgName)){
                resId = context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());
            }
            //大家在这里面处理头，这里只有一个TextView，大家可以根据自己的逻辑做修改
            if(menuList.get(position ).getPdaMenuName()!=null){
                ((HeaderViewHolder)viewHolder).getTextView().setText(menuList.get(position ).getPdaMenuName());
            }
            if(menuList.get(position ).getPdaMenuUrl()!=null){
                ((HeaderViewHolder) viewHolder).getValueView().setText(menuList.get(position ).getPdaMenuUrl());
            }
            ((HeaderViewHolder) viewHolder).getiamgeView().setImageResource(resId);
            //如果设置了回调，则设置点击事件
            if (mOnItemClickLitener != null)
            {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        mOnItemClickLitener.onItemClick(((HeaderViewHolder)viewHolder).itemView, position);
                    }
                });

            }
        }else {
            //其他条目中的逻辑在此
            String imgName=menuList.get(position).getPdaMenuIcon();
            int resId =0;
            if(!StringUtil.isEmpty(imgName)){
                resId = context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());
            }
            //大家在这里面处理头，这里只有一个TextView，大家可以根据自己的逻辑做修改
            if(menuList.get(position ).getPdaMenuName()!=null){
                ((BodyViewHolder)viewHolder).getTextView().setText(menuList.get(position ).getPdaMenuName());
            }
            if(menuList.get(position ).getPdaMenuUrl()!=null){
                ((BodyViewHolder) viewHolder).getValueView().setText(menuList.get(position ).getPdaMenuUrl());
            }
            ((BodyViewHolder) viewHolder).getiamgeView().setImageResource(resId);


            //如果设置了回调，则设置点击事件
            if (mOnItemClickLitener != null)
            {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        mOnItemClickLitener.onItemClick(((BodyViewHolder)viewHolder).itemView, position);
                    }
                });

            }
        }



    }

    /**
     * 总条目数量是数据源数量+1，因为我们有个Header
     * @return
     */
    @Override
    public int getItemCount() {
        if(menuList==null){
            return 0;
        }else {
            return menuList.size();
        }
    }

    /**
     *
     * 复用getItemViewType方法，根据位置返回相应的ViewType
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        //如果是0，就是头，否则则是其他的item
        return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
    }

    /**
     * 给头部专用的ViewHolder，大家根据需求自行修改
     */
    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private TextView valueView;
        private ImageView imageView;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_tv);
            valueView = (TextView) itemView.findViewById(R.id.value_tv);
            imageView = (ImageView) itemView.findViewById(R.id.id_index_gallery_item_image);
        }
        public TextView getTextView() {
            return textView;
        }
        public TextView getValueView() {
            return valueView;
        }
        public ImageView getiamgeView() {
            return imageView;
        }
    }

    /**
     * 给GridView中的条目用的ViewHolder，里面只有一个TextView
     */
    public class BodyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;
        private TextView valueView;
        public BodyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_tv);
            valueView = (TextView) itemView.findViewById(R.id.value_tv);
            imageView = (ImageView) itemView.findViewById(R.id.id_index_gallery_item_image);

        }
        public TextView getTextView() {
            return textView;
        }
        public TextView getValueView() {
            return valueView;
        }
        public ImageView getiamgeView() {
            return imageView;
        }
    }







    /**
     * ItemClick的回调接口
     * @author zhy
     *
     */
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }




}
