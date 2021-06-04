package com.amarjeet.android.teckatproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder> implements Filterable {

    ArrayList<Article> articles;
    ArrayList<Article> articlesAll;

    public ArticleAdapter() {
        articles =new ArrayList<>();
        articlesAll=new ArrayList<>();
    }
    public void setData(ArrayList<Article> articles){
        this.articlesAll=articles;
        this.articles=articles;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater layoutInflater= LayoutInflater.from(context);
        View articleView = layoutInflater.inflate(R.layout.recycler_row,parent,false);
        return new ArticleViewHolder(articleView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article =articles.get(position);
        holder.payMobile.setText(article.payMobile);
        holder.payName.setText(article.payName);
        holder.payCourse.setText(article.payCourse);
        holder.payAmount.setText(article.payAmount);
        holder.payStatus.setText(article.payStatus);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    @Override
    public Filter getFilter() {

        return filter;
    }
    Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Article> filteredList = new ArrayList<>();
            if(charSequence.toString().isEmpty()) {
                filteredList.addAll(articlesAll);
            }
            else {
                for (Article searchElement:articlesAll) {
                    if(searchElement.getPayName().toUpperCase().contains(charSequence.toString().toUpperCase()) || searchElement.getPayMobile().toUpperCase().contains(charSequence.toString().toUpperCase())) {
                        filteredList.add(searchElement);
                    }
                    System.out.println(filteredList);
                    Log.d("ArticleAdapter",filteredList.toString());
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            articles.clear();
            articles.addAll((Collection<? extends Article>) filterResults.values);
            notifyDataSetChanged();

        }
    };
}
