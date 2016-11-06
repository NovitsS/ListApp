package com.appitem.uniquetest.myapplication.recyclerviewadapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appitem.uniquetest.myapplication.R;
import com.appitem.uniquetest.myapplication.datebase.ListDb;

/**
 * Created by HUSTy on 2016/11/2.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder> implements View.OnClickListener{
    private Context mContext;
    private Cursor mCursor;
    private OnRecyclerViewItemListener listener=null;

    public RecyclerViewAdapter(Context context,Cursor cursor){
        this.mContext=context;
        this.mCursor=cursor;
    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item,parent,false);
        ItemViewHolder holder=new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        String str;
        mCursor.moveToPosition(position);
        holder.mTextViewTitle.setText(mCursor.getString(mCursor.getColumnIndex(ListDb.TITLE)));
        holder.mTextViewCTime.setText("创建时间："+mCursor.getString(mCursor.getColumnIndex(ListDb.CTIME)));
        holder.mTextViewFTime.setText("截止时间："+mCursor.getString(mCursor.getColumnIndex(ListDb.FTIME)));
        holder.mTextViewRank.setText(mCursor.getString(mCursor.getColumnIndex(ListDb.RANK)));
        str=mCursor.getString(mCursor.getColumnIndex(ListDb.FINISH));
        if(!str.equals("未完成"))
            holder.mImageView.setImageResource(R.drawable.ic_check_black_24dp);
        holder.mTextViewTitle.setOnClickListener(this);
        holder.mTextViewTitle.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView mTextViewTitle,mTextViewFTime,mTextViewCTime,mTextViewRank;
        ImageView mImageView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            mTextViewTitle=(TextView)itemView.findViewById(R.id.item_text);
            mTextViewCTime=(TextView)itemView.findViewById(R.id.item_ctime);
            mTextViewFTime=(TextView)itemView.findViewById(R.id.item_ftime);
            mImageView=(ImageView)itemView.findViewById(R.id.item_fstate);
            mTextViewRank=(TextView)itemView.findViewById(R.id.item_rank);
        }
    }

    public static interface OnRecyclerViewItemListener{
        public void onItemClick(View v,int position);
    }

    public void setOnRecyclerViewItemListener(OnRecyclerViewItemListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View v) {
        if(listener!=null)
            listener.onItemClick(v,(Integer) v.getTag());
    }
}
