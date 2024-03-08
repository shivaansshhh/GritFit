package com.example.gritfitfitnessstudio;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerCommunityAdapter extends RecyclerView.Adapter<RecyclerCommunityAdapter.CommunityViewHolder> {

    ArrayList<UserModel>arrFeed;
    public RecyclerCommunityAdapter(ArrayList<UserModel>arrFeed){
        this.arrFeed = arrFeed;
    }
    @NonNull
    @Override
    public CommunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_feed, parent, false);

        return new CommunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityViewHolder holder, int position) {
        holder.name.setText(arrFeed.get(position).name);
        holder.username.setText(arrFeed.get(position).username);
        holder.caption.setText(arrFeed.get(position).caption);
        holder.name.setText(arrFeed.get(position).name);
        holder.uploadedPic.setImageResource(arrFeed.get(position).uploadedPic);
        holder.profilePic.setImageResource(arrFeed.get(position).profilePic);

    }

    @Override
    public int getItemCount() {
        return arrFeed.size();
    }

    public class CommunityViewHolder extends RecyclerView.ViewHolder {
        TextView name, username, caption;
        CircleImageView profilePic;
        ImageView uploadedPic;
        public CommunityViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            username = itemView.findViewById(R.id.username);
            caption = itemView.findViewById(R.id.caption);
            profilePic = itemView.findViewById(R.id.profilePic);
            uploadedPic = itemView.findViewById(R.id.uploadedPic);
        }
    }
}