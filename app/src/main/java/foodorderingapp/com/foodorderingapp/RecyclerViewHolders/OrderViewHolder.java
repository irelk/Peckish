package foodorderingapp.com.foodorderingapp.RecyclerViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import foodorderingapp.com.foodorderingapp.Interface.ItemClickListener;
import foodorderingapp.com.foodorderingapp.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtOrderId,txtOrderStatus,txtOrderPhone,txtOrderAddress;
    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderAddress=(TextView)itemView.findViewById(R.id.order_address);
        txtOrderId=(TextView)itemView.findViewById(R.id.order_id);
        txtOrderStatus=(TextView)itemView.findViewById(R.id.order_status);
        txtOrderPhone=(TextView)itemView.findViewById(R.id.order_phone);
    }



    @Override
    public void onClick(View v) {

    }

}
