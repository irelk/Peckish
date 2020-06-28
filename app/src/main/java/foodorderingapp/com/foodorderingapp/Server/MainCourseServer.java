package foodorderingapp.com.foodorderingapp.Server;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import foodorderingapp.com.foodorderingapp.Common.Common;
import foodorderingapp.com.foodorderingapp.DetailActivity;
import foodorderingapp.com.foodorderingapp.Model.Product;
import foodorderingapp.com.foodorderingapp.R;
import foodorderingapp.com.foodorderingapp.RecyclerViewHolders.RecylerViewAdapterFragments;
import foodorderingapp.com.foodorderingapp.RecyclerViewHolders.RecylerViewAdapterFragmentsServer;

import static android.app.Activity.RESULT_OK;

public class MainCourseServer extends Fragment {
    RecyclerView productsRecycler;
    ArrayList<Product> productList;
    private FirebaseDatabase database;
    private DatabaseReference tableProducts;
    private RecylerViewAdapterFragmentsServer adapter;
    private String productId;
    ArrayList<Product> searchProductList;
    private RecylerViewAdapterFragmentsServer searchAdapter;
    List<String> suggestList=new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    MaterialEditText edtNmae,edtDescription,edtPrice;
    Button btnSelect,btnUpload;

    FirebaseStorage storage;
    StorageReference storageReference;


    Uri saveUri;

    public MainCourseServer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance();
        tableProducts = database.getInstance().getReference("Products");
        View view = inflater.inflate(R.layout.fragment_main_course_server, container, false);

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();


        productList = new ArrayList<>();
        searchProductList = new ArrayList<>();


        productsRecycler = view.findViewById(R.id.recyclerMainCourse);
        productsRecycler.setLayoutManager(new GridLayoutManager(getContext(),1));


        adapter = new RecylerViewAdapterFragmentsServer(productList);
        searchAdapter=new RecylerViewAdapterFragmentsServer(searchProductList);

        additems();

        materialSearchBar=(MaterialSearchBar)view.findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter your food");
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest=new ArrayList<String>();
                for(String search:suggestList){
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //when search bar is closed
                if(!enabled)
                    productsRecycler.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                productsRecycler.setAdapter(searchAdapter);
                startsearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });



        // TODO Still to load the Detail Activity
        adapter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), DetailActivity.class);

                intent.putExtra("Name",productList.get(productsRecycler.getChildAdapterPosition(view)).getName());
                intent.putExtra("Price",productList.get(productsRecycler.getChildAdapterPosition(view)).getPrice());
                intent.putExtra("Info",productList.get(productsRecycler.getChildAdapterPosition(view)).getInfo());
                intent.putExtra("Img",productList.get(productsRecycler.getChildAdapterPosition(view)).getImageId());
                intent.putExtra("Product ID",productList.get(productsRecycler.getChildAdapterPosition(view)).getKey());

                startActivity(intent);

                Toast.makeText(getContext(),"Selected: "+productList.get
                        (productsRecycler.getChildAdapterPosition(view))
                        .getName(),Toast.LENGTH_SHORT).show();
            }
        });



        searchAdapter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), DetailActivity.class);

                intent.putExtra("Name",searchProductList.get(productsRecycler.getChildAdapterPosition(view)).getName());
                intent.putExtra("Price",searchProductList.get(productsRecycler.getChildAdapterPosition(view)).getPrice());
                intent.putExtra("Info",searchProductList.get(productsRecycler.getChildAdapterPosition(view)).getInfo());
                intent.putExtra("Img",searchProductList.get(productsRecycler.getChildAdapterPosition(view)).getImageId());
                intent.putExtra("Product ID",searchProductList.get(productsRecycler.getChildAdapterPosition(view)).getKey());

                startActivity(intent);

                Toast.makeText(getContext(),"Selected: "+searchProductList.get
                        (productsRecycler.getChildAdapterPosition(view))
                        .getName(),Toast.LENGTH_SHORT).show();
            }
        });

        productsRecycler.setAdapter(adapter);


        return view;
    }

    private void startsearch(final CharSequence text) {
        searchProductList.clear();
        tableProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot productSnapshot : dataSnapshot.getChildren())
                {

                    Product product = productSnapshot.getValue(Product.class);
                    product.setKey(productSnapshot.getKey());
                    if (product.getName().equals(text.toString())) {
                        searchProductList.add(product);
                    }
                }
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                //  Toast.makeText(getContext(),"Something goes wrong!!!", Toast.LENGTH_LONG).show();

            }
        });


    }

    private void additems() {

        tableProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productList.clear();
                suggestList.clear();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren())
                {

                    Product product = productSnapshot.getValue(Product.class);
                    product.setKey(productSnapshot.getKey());
                    if (product.getType().equals("main course")) {
                        productList.add(product);
                        suggestList.add(product.getName());
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                //  Toast.makeText(getContext(),"Something goes wrong!!!", Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE))
        {
            showUpdateFoodDialog(productList.get(item.getOrder()).getKey(),productList.get(item.getOrder()));
        }
        else if(item.getTitle().equals(Common.DELETE))
        {
            deleteFood(productList.get(item.getOrder()).getKey());

        }
        return super.onContextItemSelected(item);
    }

    private void deleteFood(String key) {
        tableProducts.child(key).removeValue();
    }

    private void showUpdateFoodDialog(final String key, final Product product) {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Edit main-course item");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater=this.getLayoutInflater();
        View view=inflater.inflate(R.layout.add_new_food_layout,null);
        edtNmae=view.findViewById(R.id.edtName);
        btnSelect=view.findViewById(R.id.btnSelect);
        btnUpload=view.findViewById(R.id.btnUpload);
        edtDescription=view.findViewById(R.id.edtDescription);
        edtPrice=view.findViewById(R.id.edtPrice);

        edtNmae.setText(product.getName());
        edtDescription.setText(product.getInfo());
        edtPrice.setText(product.getPrice());

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(product);
            }
        });

        alertDialog.setView(view);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();


                product.setName(edtNmae.getText().toString());
                product.setInfo(edtDescription.getText().toString());
                product.setPrice(edtPrice.getText().toString());
                tableProducts.child(key).setValue(product);
                Toast.makeText(getContext(),product.getName()+" was edited",Toast.LENGTH_SHORT).show();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Common.PICK_IMAGE_REQUEST && resultCode==RESULT_OK
                && data!=null && data.getData()!=null){
            saveUri=data.getData();
            btnSelect.setText("Image Selected");
        }
    }

    private void changeImage(final Product product) {
        if(saveUri!=null){
            String imageName=UUID.randomUUID().toString();
            final StorageReference imageFolder=storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(),"Uploaded!!!",Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            product.setImageId(uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        }
                    });
        }
    }

    private void chooseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),Common.PICK_IMAGE_REQUEST);

    }

}
