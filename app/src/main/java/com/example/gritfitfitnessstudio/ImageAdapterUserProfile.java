package com.example.gritfitfitnessstudio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapterUserProfile extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> mImagesUrls;

    public ImageAdapterUserProfile(Context context, ArrayList<String> imagesUrls){
        mContext = context;
        mImagesUrls = imagesUrls;
    }

    @Override
    public int getCount() {
        return mImagesUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return mImagesUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View gridView = view;
        if (gridView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.grid_item_layout, null);
        }
        ImageView imageView = gridView.findViewById(R.id.imageView);
        Picasso.get().load(mImagesUrls.get(position)).into(imageView);

        return gridView;
    }
}
