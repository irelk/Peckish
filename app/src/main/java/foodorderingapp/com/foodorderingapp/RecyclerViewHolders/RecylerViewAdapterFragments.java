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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import foodorderingapp.com.foodorderingapp.Common.Common;
import foodorderingapp.com.foodorderingapp.Database.Database;
import foodorderingapp.com.foodorderingapp.DetailActivity;
import foodorderingapp.com.foodorderingapp.Interface.ItemClickListener;
import foodorderingapp.com.foodorderingapp.Model.Order;
import foodorderingapp.com.foodorderingapp.Model.Product;
import foodorderingapp.com.foodorderingapp.R;

public class RecylerViewAdapterFragments extends RecyclerView.Adapter<RecylerViewAdapterFragments.MyRecyclerAdapterViewHolder> implements
        View.OnClickListener

{
    ArrayList<Product> productList;
    private View.OnClickListener listener;
    Context context;
    Database localDB;
    ItemClickListener itemClickListener;
    public RecylerViewAdapterFragments(ArrayList<Product> productsList,Context context)
    {
        this.productList = productsList;
        this.context=context;
        localDB=new Database(context);
    }

    @NonNull
    @Override
    public MyRecyclerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_adapter_fragments, null, false);
        view.setOnClickListener(this);
        return new MyRecyclerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyRecyclerAdapterViewHolder holder, final int pos)
    {
        holder.productName.setText(productList.get(pos).getName());
        holder.productPrice.setText(String.format("$ %s",productList.get(pos).getPrice()));
        Picasso.get().load(productList.get(pos).getImageId()).into(holder.productImage);
        if(localDB.isFavorite(productList.get(pos).getKey()))
            holder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);

        holder.fav_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!localDB.isFavorite(productList.get(pos).getKey()))
                {
                    localDB.addToFavorites( productList.get(pos).getKey());
                    holder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                    Toast.makeText(context, "" + productList.get(pos).getName() + " was added to Favorites", Toast.LENGTH_SHORT).show();
                }
                else{
                    localDB.removeToFavorites(productList.get(pos).getKey());
                    holder.fav_image.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    Toast.makeText(context, "" + productList.get(pos).getName() + " was removed from Favorites", Toast.LENGTH_SHORT).show();
                }
                }
        });
        holder.quick_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(context).addToCart(new Order(productList.get(pos).getKey(),
                        productList.get(pos).getName(),
                        "1",
                        productList.get(pos).getPrice(),
                        productList.get(pos).getImageId()));
                Toast.makeText(context,"Added to Cart",Toast.LENGTH_SHORT).show();
            }
        });
      //  holder.quick_cart.setOnClickListener(this);
    }

    public void setOnClickListener(View.OnClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(!(view instanceof ImageView)) {
            if (listener != null) {
                listener.onClick(view);
            }
        }
        else {
            itemClickListener.onClick(view, 4, false);
        }

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener=itemClickListener;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public class MyRecyclerAdapterViewHolder extends RecyclerView.ViewHolder
    {

        TextView productName,productPrice;
        ImageView productImage,fav_image,quick_cart;
        public MyRecyclerAdapterViewHolder(@NonNull View itemView)
        {
            super(itemView);
            productName=(TextView)itemView.findViewById(R.id.textViewItemName);
            productPrice=(TextView)itemView.findViewById(R.id.textViewItemPrice);
            productImage=(ImageView)itemView.findViewById(R.id.imageviewItem);
            fav_image=(ImageView)itemView.findViewById(R.id.fav);
            quick_cart=(ImageView)itemView.findViewById(R.id.btn_quick_cart);
        }
    }


}

