package com.coxtunes.learnfirebase.NotificationHelper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationAPIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAzl1bo4o:APA91bHwP8UcpU8IdZdQtBj3r7NquWHfTtZ33C1736ktIC6b6h4YewnP9E2HDZ_ozyAGQ_gqOrXIHHasKbvpNoXeFz9uVTA2sVHHptckHfeJcqh5GJFjVZ7jY6qkCpTXJIFLX6OIjlyf" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponseModel> sendNotification(@Body NotificationSenderModel body);
}

