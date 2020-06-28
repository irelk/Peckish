package foodorderingapp.com.foodorderingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

import foodorderingapp.com.foodorderingapp.Common.Common;
import foodorderingapp.com.foodorderingapp.Database.Database;
import foodorderingapp.com.foodorderingapp.Model.Order;
import foodorderingapp.com.foodorderingapp.Model.Rating;

public class DetailActivity extends AppCompatActivity implements RatingDialogListener {

    TextView food_name,food_price,food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnRating;
    CounterFab btnCart;
    ElegantNumberButton numberButton;
    String NameOfItem;
    String PriceOfItem;
    String  InfoOfItem;
    String ImgOfItem;
    String ProductId;
    RatingBar ratingBar;
    FirebaseDatabase database;
    DatabaseReference foods;
    DatabaseReference ratingTbl;
    Button btnShowComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        btnShowComment=(Button)findViewById(R.id.btnShowComment);
        btnShowComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DetailActivity.this,ShowComment.class);
                intent.putExtra(Common.INTENT_FOOD_ID,ProductId);
                startActivity(intent);
            }
        });

    numberButton=(ElegantNumberButton)findViewById(R.id.number_button2);
    btnCart=(CounterFab) findViewById(R.id.btnCart);
    btnRating=(FloatingActionButton)findViewById(R.id.btn_rating);
    ratingBar=(RatingBar)findViewById(R.id.ratingBar);

    btnRating.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showRatingDialog();
        }
    });

    database=FirebaseDatabase.getInstance();
    foods=database.getReference("Products");
    ratingTbl=database.getReference("Rating");


    food_description=(TextView)findViewById(R.id.food_description);
    food_name=(TextView)findViewById(R.id.food_name);
    food_price=(TextView)findViewById(R.id.food_price);
    food_image=(ImageView)findViewById(R.id.food_image);

    collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing);
    collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
    collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

    if (getIntent()!=null){

        Intent intent = getIntent();
         NameOfItem = intent.getExtras().getString("Name");
         PriceOfItem = intent.getExtras().getString("Price");
         InfoOfItem = intent.getExtras().getString("Info");
         ImgOfItem = intent.getExtras().getString("Img");
         ProductId = intent.getExtras().getString("Product ID");
        Picasso.get().load(ImgOfItem).into(food_image);
        collapsingToolbarLayout.setTitle(NameOfItem);

        food_price.setText(PriceOfItem);
        food_name.setText(NameOfItem);
        food_description.setText(InfoOfItem);

    }
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getApplicationContext()).addToCart(new Order(ProductId,
                        NameOfItem,
                        numberButton.getNumber(),
                        PriceOfItem,
                        ImgOfItem));
                Toast.makeText(DetailActivity.this,"Added to Cart",Toast.LENGTH_SHORT).show();
            }
        });
    btnCart.setCount(new Database(this).getCountCart());
    getRatingFood(ProductId);
    }

    private void getRatingFood(String productId) {
        Query foodRating=ratingTbl.orderByChild("foodId").equalTo(productId);

        foodRating.addValueEventListener(new ValueEventListener() {
            int count=0,sum=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for(DataSnapshot postSnapShot:dataSnapshot.getChildren()){
                Rating item=postSnapShot.getValue(Rating.class);
                sum+=Integer.parseInt(item.getRateValue());
                count++;
            }

            if(count!=0){
                float average=sum/count;
                ratingBar.setRating(average);
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad","Not Good","Quite Ok","Very Good","Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate this food")
                .setDescription("Please select some stars and give your feedback")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please write your comment here...")
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimary)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(DetailActivity.this)
                .show();
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int i, String s) {
        final Rating rating=new Rating(Common.currentUser.getPhone(),
                ProductId,
                String.valueOf(i),
                s,Common.currentUser.getName());
        //Fix user can rate multiple times
        ratingTbl.push().setValue(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            Toast.makeText(DetailActivity.this,"Thank you for submitting the rating",Toast.LENGTH_SHORT).show();
            }
        });
        /*
        ratingTbl.child(Common.currentUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(Common.currentUser.getPhone()).exists()) {
                    ratingTbl.child(Common.currentUser.getPhone()).removeValue();

                    ratingTbl.child(Common.currentUser.getPhone()).setValue(rating);
                }
                else {
                    ratingTbl.child(Common.currentUser.getPhone()).setValue(rating);

                }
                Toast.makeText(DetailActivity.this,"Thank you for your feedback!!!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */
    }
}
