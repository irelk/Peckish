package foodorderingapp.com.foodorderingapp;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import foodorderingapp.com.foodorderingapp.Common.Common;
import foodorderingapp.com.foodorderingapp.Database.Database;
import foodorderingapp.com.foodorderingapp.Model.MyResponse;
import foodorderingapp.com.foodorderingapp.Model.Notification;
import foodorderingapp.com.foodorderingapp.Model.Order;
import foodorderingapp.com.foodorderingapp.Model.Request;
import foodorderingapp.com.foodorderingapp.Model.Sender;
import foodorderingapp.com.foodorderingapp.Model.Token;
import foodorderingapp.com.foodorderingapp.RecyclerViewHolders.RecyclerViewAdapterCart;
import foodorderingapp.com.foodorderingapp.Remote.APIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    public TextView txtTotalPrice;
    Button btnPlace;

    List<Order> cart=new ArrayList<>();

    RecyclerViewAdapterCart adapterCart;

    APIService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mService=Common.getFCMService();

        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");

        recyclerView=(RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice=(TextView)findViewById(R.id.total);
        btnPlace=(Button) findViewById(R.id.btnPlaceOrder);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cart.size() >0)
               showAlertDialog();
                else
                    Toast.makeText(Cart.this,"Your cart is empty!!!",Toast.LENGTH_SHORT).show();
            }
        });

        loadListFood();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());
        return true;
    }

    private void deleteCart(int position) {
        cart.remove(position);
        new Database(this).cleanCart();

        for(Order item:cart)
            new Database(this).addToCart(item);

        loadListFood();
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more step!");
        alertDialog.setMessage("Enter your address: ");

        LayoutInflater inflater=this.getLayoutInflater();
        View order_address_comment=inflater.inflate(R.layout.order_address_comment,null);

        final MaterialEditText edtAddress=(MaterialEditText)order_address_comment.findViewById(R.id.edtAddress);
        final MaterialEditText edtComment=(MaterialEditText)order_address_comment.findViewById(R.id.edtComment);

        alertDialog.setView(order_address_comment);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request=new Request(Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        edtAddress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        edtComment.getText().toString(),
                        cart);

                String order_number=String.valueOf(System.currentTimeMillis());

                requests.child(order_number).setValue(request);
                new Database(getBaseContext()).cleanCart();

                sendNotificationOrder(order_number);
//                Toast.makeText(Cart.this,"Thank you, Order Placed",Toast.LENGTH_SHORT).show();
//                finish();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }

    private void sendNotificationOrder(final String order_number) {
    DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
    Query data=tokens.orderByChild("isServerToken").equalTo(true);

    data.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
            {
                Token serverToken=postSnapshot.getValue(Token.class);
                //Create raw payload to send
                Notification notification=new Notification("Food Ordering","You have new order "+order_number);
                Sender content=new Sender(serverToken.getToken(),notification);

                mService.sendNotification(content)
                        .enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                //only run when get result
                               if (response.code()==200)
                               {
                                   if (response.body().success == 1) {
                                       Toast.makeText(Cart.this, "Thank you, Order Placed", Toast.LENGTH_SHORT).show();
                                       finish();
                                   } else {
                                       Toast.makeText(Cart.this, "Shop closed", Toast.LENGTH_SHORT).show();
                                   }
                               }
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {
                                Log.e("ERROR",t.getMessage());
                            }
                        });
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

    }

    private void loadListFood() {
        cart=new Database(this).getCart();
        adapterCart=new RecyclerViewAdapterCart(cart,this);
        adapterCart.notifyDataSetChanged();

        recyclerView.setAdapter(adapterCart);

        int total=0;
        for(Order order:cart)
            total+=Double.parseDouble(order.getPrice())*Integer.parseInt(order.getQuantity());
        Locale locale=new Locale("en","US");
        NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));

    }
}
