package foodorderingapp.com.foodorderingapp.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import foodorderingapp.com.foodorderingapp.Model.Request;
import foodorderingapp.com.foodorderingapp.Model.User;
import foodorderingapp.com.foodorderingapp.Remote.APIService;
import foodorderingapp.com.foodorderingapp.Remote.RetrofitClient;


public class Common {
    public static User currentUser;
    public static Request currentRequest;

    public static final String INTENT_FOOD_ID="FoodId";

    private  static final String BASE_URL="https://fcm.googleapis.com/";
    public static APIService getFCMService()
    {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

    public static APIService getFCMClient()
    {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

    public static final String UPDATE="Update";
    public static final String DELETE="Delete";
    public static final int PICK_IMAGE_REQUEST=71;

    public static String convertCodeToStatus(String status) {
        if(status.equals("0"))
            return "Placed";
        else if(status.equals("1"))
            return "On my way";
        else
            return "Shipped";
    }
    public static boolean isConnectedToInternet(Context context)
    {
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager!=null)
        {
            NetworkInfo[] info=connectivityManager.getAllNetworkInfo();
            if(info!=null)
            {
                for (int i=0;i<info.length;i++)
                {
                    if(info[i].getState()==NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

}
