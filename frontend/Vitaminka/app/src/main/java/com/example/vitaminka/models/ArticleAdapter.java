package com.example.vitaminka.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitaminka.R;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
    private List<Article> articleList;

    public ArticleAdapter(List<Article> articleList) {
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public ArticleAdapter.ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleAdapter.ArticleViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.bind(article, position);
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }


    public class ArticleViewHolder extends RecyclerView.ViewHolder{
        ImageView ivArticleImage;
        TextView tvArticleName;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            ivArticleImage = itemView.findViewById(R.id.iv_article_image);
            tvArticleName = itemView.findViewById(R.id.tv_article_name);
        }

        public void bind(Article article, int position) {
            ivArticleImage.setImageResource(article.getImageResId());
            tvArticleName.setText(article.getText());
        }
    }
}
