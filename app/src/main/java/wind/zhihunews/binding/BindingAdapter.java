package wind.zhihunews.binding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wind on 2016/8/18.
 */
public class BindingAdapter<B extends ViewDataBinding, D> extends RecyclerView.Adapter<BindingAdapter.BindingHolder> {

    public interface OnItemClickListener<B, T> {

        void onItemClickListener(B binding, T data, int position);

    }

    private List<D> data = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;

    private int variableID;

    private int layoutID;

    public BindingAdapter(@LayoutRes int layoutID, int variableID) {
        this.variableID = variableID;
        this.layoutID = layoutID;
    }

    public void setData(List<D> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    public void addData(List<D> stories) {
        int size = data.size();
        this.data.addAll(stories);
        notifyItemRangeInserted(size, stories.size());
    }

    protected void onBind(B binding, D data) {

    }

    public void setOnItemClickListener(OnItemClickListener<B, D> onClickListener) {
        mOnItemClickListener = onClickListener;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BindingHolder(LayoutInflater.from(parent.getContext())
                .inflate(layoutID, parent, false));
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        holder.bind(data.get(position));
    }


    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class BindingHolder<VB extends ViewDataBinding, VD> extends RecyclerView.ViewHolder {

        private B binding;

        public BindingHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void bind(@NonNull final D data) {
            binding.setVariable(variableID, data);
            binding.executePendingBindings();
            if (mOnItemClickListener != null) {
                itemView.setOnClickListener(view -> mOnItemClickListener.onItemClickListener(binding, data, getAdapterPosition()));
            }
            onBind(binding, data);
        }

    }
}
