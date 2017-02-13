package com.example.geomslayer.hseproject.mainscreen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.geomslayer.hseproject.R;
import com.example.geomslayer.hseproject.networking.Article;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<Article> newsList;
    private Context context;
    private OnItemClickListener listener;

    public NewsAdapter(Context context, List<Article> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    public void setOnitemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View topicView = inflater.inflate(R.layout.news_item, parent, false);
        return new ViewHolder(topicView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article curNews = newsList.get(position);
        holder.title.setText(curNews.title);
        holder.text.setText(curNews.text);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView text;

        ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.txt_itemTitle);
            text = (TextView) itemView.findViewById(R.id.txt_itemText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            listener.onItemClick(view, pos);
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
}
