package foodorderingapp.com.foodorderingapp.RecyclerViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import foodorderingapp.com.foodorderingapp.Model.Rating;
import foodorderingapp.com.foodorderingapp.R;

public class ShowCommentViewHolder extends RecyclerView.ViewHolder {
    public TextView txtUserPhone,txtComment;
    public RatingBar ratingBar;

    public ShowCommentViewHolder(@NonNull View itemView) {
        super(itemView);
        txtComment=(TextView)itemView.findViewById(R.id.txtComment);
        txtUserPhone=(TextView)itemView.findViewById(R.id.txtUserPhone);
        ratingBar=(RatingBar) itemView.findViewById(R.id.ratingBar);

    }
}
