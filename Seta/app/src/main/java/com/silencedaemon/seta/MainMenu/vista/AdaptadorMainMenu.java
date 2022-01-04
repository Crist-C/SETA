package com.silencedaemon.seta.MainMenu.vista;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.silencedaemon.seta.MainMenu.MainMenuContract;
import com.silencedaemon.seta.MainMenu.presenter.MainMenuPresenter;
import com.silencedaemon.seta.R;

import java.util.ArrayList;

public class AdaptadorMainMenu extends RecyclerView.Adapter<AdaptadorMainMenu.ItemMenuRVHolder> implements MainMenuContract.view {

    private final Activity contexto;
    private final ArrayList<ContenedorItemsMenu> contenedorItemsMenuArrayList;
    private MainMenuContract.presenter presenter = null;

    public AdaptadorMainMenu(Activity contexto, ArrayList<ContenedorItemsMenu> contenedorItemsMenuArrayList) {
        this.contexto = contexto;
        this.contenedorItemsMenuArrayList = contenedorItemsMenuArrayList;

    }

    @NonNull
    @Override
    public ItemMenuRVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_menu,parent,false);
        presenter = new MainMenuPresenter(this, contexto);
        return new ItemMenuRVHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemMenuRVHolder holder, int position) {
        ContenedorItemsMenu contenedorItemsMenu = contenedorItemsMenuArrayList.get(position);

        holder.iconItem.setImageResource(contenedorItemsMenu.getIconItemMainMenu());
        holder.tittleItem.setText(contenedorItemsMenu.getTittleItemMainMenu());
        holder.cardView.setOnClickListener(v -> {
            Toast.makeText(contexto, "Click on "+contenedorItemsMenu.getTittleItemMainMenu(), Toast.LENGTH_SHORT).show();
            presenter.goToActivity(contenedorItemsMenu.getActivityItem());
        });


    }

    @Override
    public int getItemCount() {
        return contenedorItemsMenuArrayList.size();
    }

    public static class ItemMenuRVHolder extends RecyclerView.ViewHolder {

        private final ImageView iconItem;
        private final TextView tittleItem;
        private final CardView cardView;

        public ItemMenuRVHolder(@NonNull View itemView) {
            super(itemView);

            iconItem = itemView.findViewById(R.id.iconItemMainMenu);
            tittleItem = itemView.findViewById(R.id.tittleItemMainMenu);
            cardView = itemView.findViewById(R.id.cvCardItemMainMenu);

        }
    }
}
