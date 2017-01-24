package com.adec.firebasestorekeeper.Adapter.AutoCompleteAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.adec.firebasestorekeeper.Model.Product;
import com.adec.firebasestorekeeper.R;

import java.util.ArrayList;

/**
 * Created by Sohel on 1/13/2017.
 */

public class ProductAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private ArrayList<Product> originalList;
    private ArrayList<Product> suggestions = new ArrayList<>();
    private Filter filter = new CustomFilter();

    public ProductAdapter(Context context, ArrayList<Product> originalList) {
        this.context = context;
        this.originalList = originalList;
    }

    @Override
    public int getCount() {
        return suggestions.size(); // Return the size of the suggestions list.
    }

    @Override
    public Object getItem(int position) {
        return suggestions.get(position).getName();
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.single_product,
                    parent,
                    false);
            holder = new ViewHolder();
            holder.productName = (TextView) convertView.findViewById(R.id.product_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.productName.setText(suggestions.get(position).getName());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private static class ViewHolder {
        TextView productName;
    }


    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            suggestions.clear();

            if (originalList != null && constraint != null) { // Check if the Original List and Constraint aren't null.
                for (int i = 0; i < originalList.size(); i++) {
                    if (originalList.get(i).getName().toLowerCase().contains(constraint)) { // Compare item in original list if it contains constraints.
                        suggestions.add(originalList.get(i)); // If TRUE add item in Suggestions.
                    }
                }
            }
            FilterResults results = new FilterResults(); // Create new Filter Results and return this to publishResults;
            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
