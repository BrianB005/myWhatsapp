package com.brianbett.whatsapp.retrofit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface MyApi {

    @GET("messages/allChats")
    Call<List<Message>> getAllChats(@Header("Authorization") String token);

    @Headers({"Accept: application/json"})
    @POST("users/createUser")
    Call<AuthUser> createUser(@Body User user);

    @Headers({"Accept: application/json"})
    @POST("messages/createMessage")
    Call<ChatMessage> createMessage(@Header("Authorization")String token, @Body HashMap<String,String> message);

//    getting a single chat messages
    @Headers({"Accept: application/json"})
    @POST("messages/singleChat")
    Call<List<ChatMessage>> getAllMessages(@Header("Authorization")String token,@Body HashMap<String,String> recipient);

//    getting a list of all database contacts
    @GET("users/allUsers")
    Call<List<Contact>> getAllContacts();

    @PUT("users/updateUser")
    Call<User> updateUser(@Header("Authorization")String token, @Body HashMap<String,String> update);

    @Headers({"Accept: application/json"})
    @GET("users/getCurrentUser")
    Call<User> getCurrentUser(@Header ("Authorization") String token);

    @Headers({"Accept: application/json"})
    @POST("users/getUser")
    Call<User> getUser(@Body HashMap<String,String> user);

//    uploading file
    @Multipart
    @POST("uploadImage")
    Call<ResponseBody> uploadImage(
            @Header("Authorization") String token,
            @Part MultipartBody.Part file
    );

    @POST("statuses/createTyped")
    Call<TypedStatus> createTypedStatus(@Header ("Authorization") String token,@Body TypedStatus typedStatus );

//    @POST("uploadStatus")
//    Call<TypedStatus> createImageStatus(@Header ("Authorization") String token,@Body ImageStatus typedStatus );



//    uploading image status
    @POST ("statuses/createImaged")
    Call<ResponseBody> uploadImageStatus(@Header("Authorization") String token,@Body ImageStatus imageStatus);

    @GET("statuses/getMyStatuses")
    Call<List<MyRetrievedStatus>> getMyStatuses(@Header ("Authorization") String token);

    @GET("statuses/getMyStatuses/last")
    Call<List<RetrievedStatus>> getMyLastStatus(@Header ("Authorization") String token);

    @POST("statuses/getFriendsStatuses")
    Call<List<RetrievedStatus>> getFriendsStatuses(@Header ("Authorization") String token,@Body HashMap<String, ArrayList<String>> contacts);

    @GET("images/{imageName}")
    Call<ResponseBody> fetchImage(@Path(value="imageName",encoded=true) String imageUrl);

    @GET("statuses/contactStatuses/{contactId}")
    Call<List<RetrievedStatus>> getAContactStatuses(@Header ("Authorization") String token,@Path(value="contactId",encoded=true) String contactId);

    @PUT("statuses/viewStatus/{statusId}")
    Call<ResponseBody> viewStatus(@Header ("Authorization") String token,@Path(value="statusId",encoded=true) String statusId);
}
