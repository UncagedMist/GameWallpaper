package tbc.uncagedmist.gamewallpaper.ViewHolder;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import tbc.uncagedmist.gamewallpaper.Interface.ItemClickListener;
import tbc.uncagedmist.gamewallpaper.R;

public class ListWallpaperViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView wallpaper;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ListWallpaperViewHolder(View itemView) {
        super(itemView);

        wallpaper = itemView.findViewById(R.id.imageView);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition());
    }
}