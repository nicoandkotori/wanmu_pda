package com.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.myapplication.R;
import com.myapplication.dto.basicinfo.Warehouse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vfun01 on 2017-11-16.
 */

public class WarehouseSearchAdapter extends BaseAdapter implements Filterable
{
    private Context context;
    private List<Warehouse> list;
    private LayoutInflater layoutInflater;
    private ArrayFilter mFilter;
    private final Object mLock = new Object();
    private ArrayList<Warehouse> mOriginalValues;
    public WarehouseSearchAdapter(Context context, List<Warehouse> list)
    {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    public int getCount()
    {
        return list.size();
    }
    public Object getItem(int position)
    {
        return list.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        View view;
        ViewHolder viewHolder;
        Warehouse iconInformation = list.get(position);
        if(convertView == null)
        {
            view = layoutInflater.inflate(R.layout.search_warehouse_item, null);
            viewHolder = new ViewHolder();
            viewHolder.whCode = (TextView) view.findViewById(R.id.cwhcode);
            viewHolder.whName = (TextView) view.findViewById(R.id.cwhname);

            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.whCode.setText(iconInformation.getCwhcode());
        viewHolder.whName.setText(iconInformation.getCwhname());

        return view;
    }
    private class ViewHolder
    {
        TextView whCode;
        TextView whName;


    }
    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }
    /**
     * 过滤数据的类
     */
    /**
     * <p>An array filter constrains the content of the array adapter with
     * a prefix. Each item that does not start with the supplied prefix
     * is removed from the list.</p>
     * <p/>
     * 一个带有首字母约束的数组过滤器，每一项不是以该首字母开头的都会被移除该list。
     */
    private class ArrayFilter extends Filter {
        //执行刷选
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();//过滤的结果
            //原始数据备份为空时，上锁，同步复制原始数据
            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<>(list);
                }
            }
            //当首字母为空时
            if (prefix == null || prefix.length() == 0) {
                ArrayList<Warehouse> mDatas;
                synchronized (mLock) {//同步复制一个原始备份数据
                    mDatas = new ArrayList<>(mOriginalValues);
                }
                results.values = mDatas;
                results.count = mDatas.size();//此时返回的results就是原始的数据，不进行过滤
            } else {
                String prefixString = prefix.toString().toLowerCase();//转化为小写

                ArrayList<Warehouse> values;
                synchronized (mLock) {//同步复制一个原始备份数据
                    values = new ArrayList<>(mOriginalValues);
                }
                final int count = values.size();
                final ArrayList<Warehouse> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    final Warehouse value = values.get(i);//从List<User>中拿到User对象
//                    final String valueText = value.toString().toLowerCase();
                    final String valueText = value.getCwhcode().toString().toLowerCase();//User对象的name属性作为过滤的参数
                    final String valueText1 = value.getCwhname().toString().toLowerCase();//User对象的name属性作为过滤的参数

                    // First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString) || valueText.indexOf(prefixString.toString()) != -1  ||valueText1.startsWith(prefixString) || valueText1.indexOf(prefixString.toString()) != -1 ) {//第一个字符是否匹配
                        newValues.add(value);//将这个item加入到数组对象中
                    } else {//处理首字符是空格
                        final String[] words = valueText.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with space(s)
                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].startsWith(prefixString)) {//一旦找到匹配的就break，跳出for循环
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }
                results.values = newValues;//此时的results就是过滤后的List<User>数组
                results.count = newValues.size();
            }
            return results;
        }

        //刷选结果
        @Override
        protected void publishResults(CharSequence prefix, FilterResults results) {
            //noinspection unchecked
            list = (List<Warehouse>) results.values;//此时，Adapter数据源就是过滤后的Results
            if (results.count > 0) {
                notifyDataSetChanged();//这个相当于从mDatas中删除了一些数据，只是数据的变化，故使用notifyDataSetChanged()
            } else {
                /**
                 * 数据容器变化 ----> notifyDataSetInValidated

                 容器中的数据变化  ---->  notifyDataSetChanged
                 */
                notifyDataSetInvalidated();//当results.count<=0时，此时数据源就是重新new出来的，说明原始的数据源已经失效了
            }
        }
    }
}
