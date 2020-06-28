package foodorderingapp.com.foodorderingapp.Remote;

import foodorderingapp.com.foodorderingapp.Model.MyResponse;
import foodorderingapp.com.foodorderingapp.Model.Sender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAR_xCLH4:APA91bGfSJxL7vI5APhjO0-iLI-s9wkkgF9TbVEJ1jbTrLEMqe18cZmmv9QbOdvRieflifR-7aSv2phrZFwQ0Z9PD2PrwwOEVI6VgktBsQO1VBbBnVgbd-du-V-7oTZjudO6RU_L8tsj"
    })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
