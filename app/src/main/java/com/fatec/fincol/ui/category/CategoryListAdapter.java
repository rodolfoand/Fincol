package com.fatec.fincol.ui.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fatec.fincol.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {


    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox categoryCheckBox;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryCheckBox = itemView.findViewById(R.id.categoryCheckBox);
        }
    }

    private final LayoutInflater mInflater;
    private List<String> mCategoryList;
    private List<String> selectedCategoryList;
    private AddSelectedCategory addSelectedCategory;
    private RemoveSelectedCategory removeSelectedCategory;

    public CategoryListAdapter(Context context,
                               AddSelectedCategory addSelectedCategory,
                               RemoveSelectedCategory removeSelectedCategory) {
        this.mInflater = LayoutInflater.from(context);
        this.mCategoryList = new ArrayList<>();
        this.selectedCategoryList = new ArrayList<>();
        this.addSelectedCategory = addSelectedCategory;
        this.removeSelectedCategory = removeSelectedCategory;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.card_category, parent, false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        if (mCategoryList != null){
            holder.categoryCheckBox.setText(mCategoryList.get(position));


            holder.categoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        addSelectedCategory.addSelectedCategory(buttonView.getText().toString());
                    else
                        removeSelectedCategory.removeSelectedCategory(buttonView.getText().toString());
                }
            });

            if (selectedCategoryList.contains(mCategoryList.get(position)))
                holder.categoryCheckBox.setChecked(true);
            else
                holder.categoryCheckBox.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        if (mCategoryList != null)
            return mCategoryList.size();
        else return 0;
    }

    public void setCategoryList(List<String> c, List<String> selectedCat){
        mCategoryList = c;
        selectedCategoryList = selectedCat;
        notifyDataSetChanged();
    }

    public interface AddSelectedCategory{
        void addSelectedCategory(String category);
    }
    public interface RemoveSelectedCategory{
        void removeSelectedCategory(String category);
    }

}
