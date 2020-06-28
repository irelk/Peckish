package foodorderingapp.com.foodorderingapp.RecyclerViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import foodorderingapp.com.foodorderingapp.Common.Common;
import foodorderingapp.com.foodorderingapp.Interface.ItemClickListener;
import foodorderingapp.com.foodorderingapp.R;

public class OrderViewHolderServer extends RecyclerView.ViewHolder
           //,View.OnLongClickListener
{

    public Button btnEdit,btnRemove,btnDetail;
    public TextView txtOrderId,txtOrderStatus,txtOrderPhone,txtOrderAddress;
    public OrderViewHolderServer(@NonNull View itemView) {
        super(itemView);

        txtOrderAddress=(TextView)itemView.findViewById(R.id.order_address);
        txtOrderId=(TextView)itemView.findViewById(R.id.order_id);
        txtOrderStatus=(TextView)itemView.findViewById(R.id.order_status);
        txtOrderPhone=(TextView)itemView.findViewById(R.id.order_phone);
        btnEdit=(Button)itemView.findViewById(R.id.btnEdit);
        btnRemove=(Button)itemView.findViewById(R.id.btnRemove);
        btnDetail=(Button)itemView.findViewById(R.id.btnDetail);
    }
}
