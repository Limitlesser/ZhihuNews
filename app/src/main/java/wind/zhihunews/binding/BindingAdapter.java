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

    public class BindingHolder<B extends ViewDataBinding, D> extends RecyclerView.ViewHolder implements View.OnClickListener {

        private B binding;

        public BindingHolder(View itemView) {
            super(itemView);
            if (mOnItemClickListener != null) {
                itemView.setOnClickListener(this);
            }
            binding = DataBindingUtil.bind(itemView);
        }

        public void bind(@NonNull D data) {
            binding.setVariable(variableID, data);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClickListener(binding, data.get(position), position);
                }
            }
        }
    }
}
