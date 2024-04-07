package com.impetus.newsreader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.impetus.newsreader.Models.NewsHeadlines;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private Context context;
    private List<NewsHeadlines> headlines;
    private SelectListener listener;

    public CustomAdapter(Context context, List<NewsHeadlines> headlines, SelectListener listener) {
        this.context = context;
        this.headlines = headlines;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.headlines_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, @SuppressLint("RecyclerView") int position) {
        NewsHeadlines currentHeadline = headlines.get(position);


        // Log the title and image URL for debugging
        Log.d("Adapter", "Title: " + currentHeadline.getTitle());
        Log.d("Adapter", "Image URL: " + currentHeadline.getUriToImage());

        // Set title and source
        holder.title.setText(currentHeadline.getTitle());
        holder.source.setText(currentHeadline.getSource().getName());

//        if (headlines.get(position).getUriToImage() != null){
//            Picasso.get().load(headlines.get(position).getUriToImage()).into(holder.image_headline); // This is line 36
//        }

        if (currentHeadline.getUriToImage() != null && !currentHeadline.getUriToImage().isEmpty()) {
            Picasso.get()
                    .load(currentHeadline.getUriToImage())
                    .placeholder(R.drawable.placeholder) // Placeholder image resource ID
                    .error(R.drawable.noimage) // Error image resource ID
                    .into(holder.image_headline, new Callback() {
                        @Override
                        public void onSuccess() {
                            // Do something if image loading is successful
                            Log.d("Picasso", "Image loaded successfully");
                        }

                        @Override
                        public void onError(Exception e) {
                            // Log or handle error when image fails to load
                            Log.e("Picasso", "Error loading image: " + e.getMessage());
                        }
                    });
        } else {
            // Clear the image view if URI is not available
            holder.image_headline.setImageDrawable(null);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnNewsClicked(headlines.get(position));
            }
        });
    }


    @Override
    public int getItemCount() {
        return headlines.size();
    }
}
