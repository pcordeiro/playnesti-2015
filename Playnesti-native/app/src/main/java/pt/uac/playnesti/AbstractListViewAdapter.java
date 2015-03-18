package pt.uac.playnesti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrício Cordeiro <patricio.cordeiro@gmail.com> on 16-03-2015.
 */
public abstract class AbstractListViewAdapter<T> extends BaseAdapter {
    private final LayoutInflater inflater;
    private final List<T> items;

    protected AbstractListViewAdapter(final Context context) {
        this.inflater = LayoutInflater.from(context);
        this.items = new ArrayList<>();
    }

    public void loadItems(final List<T> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getView(final int position, View view, final ViewGroup parent) {
        final T item = items.get(position);
        final ViewHolder<T> holder;

        if (view == null) {
            view = inflateView(inflater);
            holder = createViewHolder(view);
        } else {
            holder = (ViewHolder<T>) view.getTag();
        }

        holder.bind(item);
        return view;
    }

    protected LayoutInflater getInflater() {
        return inflater;
    }

    protected abstract View inflateView(final LayoutInflater inflater);
    protected abstract ViewHolder<T> createViewHolder(final View view);

    protected static abstract class ViewHolder<T> {
        protected final Context context;

        protected ViewHolder(final View view) {
            this.context = view.getContext();
            view.setTag(this);
        }

        protected abstract void bind(final T item);
    }
}
