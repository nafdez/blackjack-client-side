package es.ignaciofp.blackjackclient.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.ignaciofp.blackjackclient.R;
import es.ignaciofp.blackjackclient.models.Card;

public class AdapterCard extends RecyclerView.Adapter<AdapterCard.CardViewHolder> {

    final List<Card> models;

    public AdapterCard(List<Card> models) {
        this.models = models;
    }

    @NonNull
    @Override
    public AdapterCard.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCard.CardViewHolder holder, int position) {
        holder.bind(models.get(position));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemCardImageView);
        }

        public void bind(@NonNull Card card) {
            imageView.setImageDrawable(card.getCardImageDrawable());
        }
    }
}
