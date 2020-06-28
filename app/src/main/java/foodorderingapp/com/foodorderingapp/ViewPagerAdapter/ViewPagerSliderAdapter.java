package foodorderingapp.com.foodorderingapp.ViewPagerAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import foodorderingapp.com.foodorderingapp.R;
import foodorderingapp.com.foodorderingapp.Model.ViewPagerSliderContainer;

public class ViewPagerSliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater myLayoutInflater;
    private ArrayList<ViewPagerSliderContainer> mySliderContainer=new ArrayList<>();


    public ViewPagerSliderAdapter(Context context,ArrayList<ViewPagerSliderContainer> any)
    {
        this.context=context;
        mySliderContainer=any;
    }

    @Override
    public int getCount() {
        return mySliderContainer.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view ==(ConstraintLayout)o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position)
    {
        myLayoutInflater=LayoutInflater.from(context);
        View view=myLayoutInflater.inflate(R.layout.view_pager_slider_external_layout,container,false);

        ImageView sliderImage=(ImageView)view.findViewById(R.id.imageViewSlider);
        TextView sliderHeading=(TextView)view.findViewById(R.id.textViewHeading);
        TextView sliderDescription=(TextView)view.findViewById(R.id.textViewDescription);

        sliderImage.setImageResource(mySliderContainer.get(position).getImgId());
        sliderHeading.setText(mySliderContainer.get(position).getHeading());
        sliderDescription.setText(mySliderContainer.get(position).getDescription());

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
