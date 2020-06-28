package foodorderingapp.com.foodorderingapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import foodorderingapp.com.foodorderingapp.Interface.ItemClickListener;
import foodorderingapp.com.foodorderingapp.Model.Product;
import foodorderingapp.com.foodorderingapp.RecyclerViewHolders.RecylerViewAdapterFragments;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrinksFragment extends Fragment {
    RecyclerView productsRecycler;
    ArrayList<Product> productList;
    private FirebaseDatabase database;
    private DatabaseReference tableProducts;
    private RecylerViewAdapterFragments adapter;
    private String productId;
    ArrayList<Product> searchProductList;
    private RecylerViewAdapterFragments searchAdapter;
    List<String> suggestList=new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    public DrinksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance();
        tableProducts = database.getInstance().getReference("Products");
        View view = inflater.inflate(R.layout.fragment_drinks, container, false);


        productList = new ArrayList<>();
        searchProductList = new ArrayList<>();


        productsRecycler = view.findViewById(R.id.recyclerDrinks);
        productsRecycler.setLayoutManager(new GridLayoutManager(getContext(),1));


        adapter = new RecylerViewAdapterFragments(productList,getContext());
        searchAdapter=new RecylerViewAdapterFragments(searchProductList,getContext());
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
                if(!enabled){
                    productsRecycler.setAdapter(adapter);
                }

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


        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Toast.makeText(getContext(),"hii",Toast.LENGTH_SHORT).show();

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
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {

                    Product product = productSnapshot.getValue(Product.class);

                    product.setKey(productSnapshot.getKey());
                    if (product.getType().equals("drink")) {
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
    }
