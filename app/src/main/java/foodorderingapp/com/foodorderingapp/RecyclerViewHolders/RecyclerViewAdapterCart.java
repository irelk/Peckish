package foodorderingapp.com.foodorderingapp.RecyclerViewHolders;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import foodorderingapp.com.foodorderingapp.Cart;
import foodorderingapp.com.foodorderingapp.Common.Common;
import foodorderingapp.com.foodorderingapp.Database.Database;
import foodorderingapp.com.foodorderingapp.Model.Order;
import foodorderingapp.com.foodorderingapp.R;


class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {

  public TextView txt_cart_name,txt_price;
  public ElegantNumberButton btn_quantity;
  public ImageView cart_image;

  private AdapterView.OnItemClickListener itemClickListener;
    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_cart_name=(TextView)itemView.findViewById(R.id.cart_item_name);
        txt_price=(TextView)itemView.findViewById(R.id.cart_item_Price);
        btn_quantity=(ElegantNumberButton)itemView.findViewById(R.id.btn_quantity);
        cart_image=(ImageView) itemView.findViewById(R.id.cart_image);

        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select Action");
        menu.add(0,0,getAdapterPosition(),Common.DELETE);
    }
}

public class RecyclerViewAdapterCart extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listData=new ArrayList<>();
    private Cart cart;

    public RecyclerViewAdapterCart(List<Order> listData, Cart cart) {
        this.listData = listData;
        this.cart = cart;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(cart);
        View itemView=inflater.inflate(R.layout.cart_layout,viewGroup,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, final int position) {

        Picasso.get().load(listData.get(position).getImage()).into(cartViewHolder.cart_image);
        cartViewHolder.btn_quantity.setNumber(listData.get(position).getQuantity());
        cartViewHolder.btn_quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Order order=listData.get(position);

                order.setQuantity(String.valueOf(newValue));
                new Database(cart).updateCart(order);


                int total=0;
                List<Order> orders=new Database(cart).getCart();

                for(Order item:orders)
                    total+=Double.parseDouble(item.getPrice())*(Integer.parseInt(item.getQuantity()));
                Locale locale=new Locale("en","US");
                NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);

                cart.txtTotalPrice.setText(fmt.format(total));
notifyDataSetChanged();


            }
        });
        Locale locale=new Locale("en","US");
        NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);
        Double price=Double.parseDouble(listData.get(position).getPrice())*Integer.parseInt(listData.get(position).getQuantity());//*Integer.parseInt(listData.get(position).getQuantity())
        cartViewHolder.txt_price.setText(fmt.format(price));

        cartViewHolder.txt_cart_name.setText(listData.get(position).getProductName());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
