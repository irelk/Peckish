package foodorderingapp.com.foodorderingapp.RecyclerViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import foodorderingapp.com.foodorderingapp.Model.Order;
import foodorderingapp.com.foodorderingapp.R;

class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView name, quantity, price;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.product_name);
        quantity = (TextView) itemView.findViewById(R.id.product_quantity);
        price = (TextView) itemView.findViewById(R.id.product_price);
    }
}
    public class OrderdetailAdapter extends RecyclerView.Adapter<MyViewHolder>{
       List<Order> myOrder;

        public OrderdetailAdapter(List<Order> myOrder) {
            this.myOrder = myOrder;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_detail_layout_recycler,viewGroup,false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Order order=myOrder.get(i);
        myViewHolder.name.setText(String.format("Name : %s",order.getProductName()));
        myViewHolder.price.setText(String.format("Price : $ %s",order.getPrice()));
        myViewHolder.quantity.setText(String.format("Quantity : %s",order.getQuantity()));

        }

        @Override
        public int getItemCount() {
            return myOrder.size();
        }
    }

