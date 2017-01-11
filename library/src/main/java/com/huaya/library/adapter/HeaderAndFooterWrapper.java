package com.huaya.library.adapter;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhuruqiao on 2017/1/10.
 * e-mail:563325724@qq.com
 */

public class HeaderAndFooterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int BASE_ITEM_TYPE_HEADER = 100000;

    private static final int BASE_ITEM_TYPE_FOOTER = 200000;
    private Context context;

    private LayoutInflater inflater;

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();

    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();

    private RecyclerView.Adapter oldAdapter;

    public HeaderAndFooterWrapper(Context context, RecyclerView.Adapter oldAdapter) {
        this.oldAdapter = oldAdapter;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {

            return new HeaderOrFooterViewHolder(mHeaderViews.get(viewType));

        } else if (mFootViews.get(viewType) != null) {
            return new HeaderOrFooterViewHolder(mFootViews.get(viewType));
        }
        return oldAdapter.onCreateViewHolder(parent, viewType);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPos(position)) {
            return;
        }
        if (isFooterViewPos(position)) {
            return;
        }
        oldAdapter.onBindViewHolder(holder, position - getHeadersCount());
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return mFootViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }
        return oldAdapter.getItemViewType(position - getHeadersCount());
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    private boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }


    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFootViews.size();
    }

    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
        notifyItemInserted(mHeaderViews.size() - 1);
    }

    public void addFootView(View view) {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view);
        notifyItemInserted(getItemCount());
    }

    public int getRealItemCount() {
        return oldAdapter.getItemCount();
    }

    public class HeaderOrFooterViewHolder extends RecyclerView.ViewHolder {

        public HeaderOrFooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void removeHeader() {
        mHeaderViews.remove(BASE_ITEM_TYPE_HEADER);
        notifyItemRemoved(mHeaderViews.size());
    }

    public void removeFooter() {

        mFootViews.remove(BASE_ITEM_TYPE_FOOTER);
        notifyItemRemoved(getItemCount());
    }
}
