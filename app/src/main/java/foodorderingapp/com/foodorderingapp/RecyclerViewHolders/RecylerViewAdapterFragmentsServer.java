package foodorderingapp.com.foodorderingapp.RecyclerViewHolders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import foodorderingapp.com.foodorderingapp.Common.Common;
import foodorderingapp.com.foodorderingapp.Model.Product;
import foodorderingapp.com.foodorderingapp.R;

public class RecylerViewAdapterFragmentsServer extends RecyclerView.Adapter<RecylerViewAdapterFragmentsServer.MyRecyclerAdapterViewHolder> implements
        View.OnClickListener

{
    ArrayList<Product> productList;

    private View.OnClickListener listener;
    public RecylerViewAdapterFragmentsServer(ArrayList<Product> productsList)
    {
        this.productList = productsList;

    }
    @NonNull
    @Override
    public MyRecyclerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_adapter_fragments, null, false);

        view.setOnClickListener(this);


        return new MyRecyclerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerAdapterViewHolder holder, int pos)
    {
        holder.productName.setText(productList.get(pos).getName());
        holder.productPrice.setText("$" + productList.get(pos).getPrice());
        Picasso.get().load(productList.get(pos).getImageId()).into(holder.productImage);
    }

    public void setOnClickListener(View.OnClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null){
            listener.onClick(view);
        }

    }
    @Override
    public int getItemCount() {
        return productList.size();
    }


    public class MyRecyclerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener
    {

        TextView productName,productPrice;
        ImageView productImage;
        public MyRecyclerAdapterViewHolder(@NonNull View itemView)
        {
            super(itemView);
            productName=(TextView)itemView.findViewById(R.id.textViewItemName);
            productPrice=(TextView) itemView.findViewById(R.id.textViewItemPrice);
            productImage=(ImageView)itemView.findViewById(R.id.imageviewItem);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select the action");

            menu.add(0,0,getAdapterPosition(), Common.UPDATE);
            menu.add(0,1,getAdapterPosition(),Common.DELETE);
        }
    }

}
