package foodorderingapp.com.foodorderingapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import foodorderingapp.com.foodorderingapp.Model.ViewPagerSliderContainer;
import foodorderingapp.com.foodorderingapp.ViewPagerAdapter.ViewPagerSliderAdapter;

public class ViewPagerActivity extends AppCompatActivity {
    private ViewPager mySlideViewPager;
    private LinearLayout myDotLayout;
    private ArrayList<ViewPagerSliderContainer> myItemsList=new ArrayList<>();
    private TextView[] noOfDotsTextView;
    private ViewPagerSliderAdapter mySliderAdapter;

    private Button nextButton;
    private Button prevButton;
    private int CurrentPage;

    public void add(){

        // TODO Change the image for view pager image view
        myItemsList.add(new ViewPagerSliderContainer("Order Food Online",R.drawable.order,""));
        myItemsList.add(new ViewPagerSliderContainer("Online Menu",R.drawable.menu,""));
        myItemsList.add(new ViewPagerSliderContainer("Free Delivery",R.drawable.delivery,""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        mySlideViewPager=(ViewPager)findViewById(R.id.slideViewPager);
        myDotLayout=(LinearLayout)findViewById(R.id.dotsLayout);
        add();

        nextButton=(Button) findViewById(R.id.btnNext);
        prevButton=(Button) findViewById(R.id.btnPrev);

        mySliderAdapter=new ViewPagerSliderAdapter(this,myItemsList);
        mySlideViewPager.setAdapter(mySliderAdapter);

        addDotsIndicator(0);
        mySlideViewPager.addOnPageChangeListener(viewListener);

        //Button Onclick Listeners
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CurrentPage==noOfDotsTextView.length-1) {
                    // TODO Change where the finish button takes you
                    startActivity(new Intent(ViewPagerActivity.this, SignupLoginPage.class));
                    finish();
                }
                mySlideViewPager.setCurrentItem(CurrentPage+1);
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySlideViewPager.setCurrentItem(CurrentPage-1);
            }
        });
    }

    public void addDotsIndicator(int position){
        noOfDotsTextView=new TextView[myItemsList.size()];
        myDotLayout.removeAllViews();

        for(int i=0;i<noOfDotsTextView.length;i++){
            noOfDotsTextView[i]=new TextView(this);
            noOfDotsTextView[i].setText(Html.fromHtml("&#8226"));//setting the text as dots
            noOfDotsTextView[i].setTextSize(35);
            noOfDotsTextView[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
            myDotLayout.addView(noOfDotsTextView[i]);
        }
        if(noOfDotsTextView.length>0){
            noOfDotsTextView[position].setTextColor(Color.BLACK);
        }
    }
    ViewPager.OnPageChangeListener viewListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            CurrentPage=i;
            if(i==0){
                nextButton.setEnabled(true);
                prevButton.setEnabled(false);
                prevButton.setVisibility(View.INVISIBLE);

                nextButton.setText("Next");
                prevButton.setText("");
            }
            else if(i==noOfDotsTextView.length-1){
                nextButton.setEnabled(true);
                prevButton.setEnabled(true);
                prevButton.setVisibility(View.VISIBLE);

                nextButton.setText("Finish");
                prevButton.setText("Back");
            }
            else{
                nextButton.setEnabled(true);
                prevButton.setEnabled(true);
                prevButton.setVisibility(View.VISIBLE);

                nextButton.setText(" Next");
                prevButton.setText("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);

    }
}
