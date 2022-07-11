package com.brianbett.whatsapp.retrofit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.brianbett.whatsapp.MainActivity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHandler {

//    retrieving the token
    static SharedPreferences preferences;
    static String token;

    //        configuring retrofit
    static  Retrofit retrofit=new Retrofit.Builder()
            .baseUrl("https://my-whatsapp-api-backend.herokuapp.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


   private static final MyApi myApi =retrofit.create(MyApi.class);

//   getting all chats


    public static void getAllChats(Context context, MessagesInterface messagesInterface){
        preferences=context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        token=preferences.getString("token",null);




        Call<List<Message>> apiCall = myApi.getAllChats("Bearer "+token);
        apiCall.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse( @NonNull Call<List<Message>> call, @NonNull Response<List<Message>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show();
                }
                else{
                    List<Message> allChats=response.body();

                    messagesInterface.success(allChats);
                }
            }

            @Override
            public void onFailure( @NonNull Call<List<Message>> call, @NonNull Throwable t) {
//                Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                messagesInterface.failure(t);
            }
        });

    }

//    posting user to database
    public static void registerUser(Context context,View view, String username, int phoneNumber){
        view.setVisibility(View.VISIBLE);
        SharedPreferences preferences;
        SharedPreferences.Editor editor;
        preferences=context.getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
        editor=preferences.edit();


//        retrofit

        User userDetails=new User(username,phoneNumber);



        Call<User> userCall=myApi.createUser(userDetails);
//        Call<User> userCall=myApi.createUser(username, phoneNumber);

        userCall.enqueue(new Callback<User>() {

            @Override
            public void onResponse( @NonNull Call<User> call,  @NonNull Response<User> response) {
                view.setVisibility(View.GONE);
                if(!response.isSuccessful()){

//                    Log.d("Exception",response.errorBody().toString());

                    Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show();
                }
                else{
                    User user=response.body();
                    assert user != null;
                    String token=user.getToken();
                    //int phoneNumber=user.getUserDetails().getPhoneNumber();;
                    Intent intent=new Intent(context, MainActivity.class);
                    intent.putExtra("user",String.valueOf(phoneNumber));

                    try{
                        editor.putString("token",token).apply();

                    }catch (Exception e){
                        Log.d("Exception",e.getMessage());
                    }
                    try{

                        editor.putString("userId",user.getUserId()).apply();
                    }catch (Exception e){
                        Log.d("Exception",e.getMessage());
                    }
                    Toast.makeText(context,"Welcome "+username,Toast.LENGTH_SHORT).show();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);


                }
            }

            @Override
            public void onFailure( @NonNull Call<User> call,  @NonNull Throwable t) {
                Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


    }

    public static void createMessage(Context context,String title,String recipient,View view){
        preferences=context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        token=preferences.getString("token",null);
        HashMap<String,String> message=new HashMap<>();
        message.put("recipient",recipient);
        message.put("title",title);

        Call<ChatMessage> messageCall= myApi.createMessage("Bearer "+token,message);
        view.setVisibility(View.VISIBLE);
        messageCall.enqueue(new Callback<ChatMessage>() {
            @Override
            public void onResponse( @NonNull Call<ChatMessage> call,  @NonNull Response<ChatMessage> response) {
                view.setVisibility(View.GONE);
                if(!response.isSuccessful()){
                    Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure( @NonNull Call<ChatMessage> call, @NonNull Throwable t) {
                Log.d("Exception ",t.getMessage());
                view.setVisibility(View.GONE);
            }
        });
    }

    public  static  void getAllMessages(Context context,String recipient,ChatMessagesInterface chatMessagesInterface){
        preferences=context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        token=preferences.getString("token",null);

        HashMap<String,String> recipientMap=new HashMap<>();
        recipientMap.put("recipient",recipient);

        Call<List<ChatMessage>> messagesCall= myApi.getAllMessages("Bearer "+token,recipientMap);

        messagesCall.enqueue(new Callback<List<ChatMessage>>() {
            @Override
            public void onResponse( @NonNull Call<List<ChatMessage>> call, @NonNull Response<List<ChatMessage>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show();
                }
                else {
                    List<ChatMessage> retrievedMessages=response.body();

                   chatMessagesInterface.success(retrievedMessages);

                }
            }

            @Override
            public void onFailure( @NonNull Call<List<ChatMessage>> call, @NonNull Throwable t) {
                chatMessagesInterface.failure(t);

            }
        });



    }

    public static void getDBContacts(Context context,ContactsInterface contactsInterface){

        Call<List<Contact>> contactsCall= myApi.getAllContacts();

        contactsCall.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse( @NonNull Call<List<Contact>> call, @NonNull Response<List<Contact>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show();
                }else {
                    List<Contact> retrievedContacts=response.body();
                    contactsInterface.success(retrievedContacts);


                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Contact>> call,  @NonNull Throwable t) {
                contactsInterface.failure(t);
            }
        });

    }
    public static  void getUser(Context context,String user,UserInterface userInterface){
        HashMap<String,String> recipientMap=new HashMap<>();
        recipientMap.put("recipient",user);


        Call<User> userCall=myApi.getUser(recipientMap);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call,@NonNull Response<User> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show();
                }
                else{
                    User user=response.body();
                    assert user!=null;
                    fetchImage(user.getProfilePic(), new ImageInterface() {
                        @Override
                        public void success(Bitmap bitmap) {
                            user.setProfileBitmap(bitmap);

                            userInterface.success(user);
                        }

                        @Override
                        public void failure(Throwable throwable) {
                            userInterface.failure(throwable);
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call,@NonNull Throwable t) {
                userInterface.failure(t);
            }
        });


    }
    public  static void getCurrentUser(Context context,UserInterface userInterface){
        preferences=context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        token=preferences.getString("token",null);
         Call<User> userCall=myApi.getCurrentUser("Bearer "+token);

         userCall.enqueue(new Callback<User>() {
             @Override
             public void onResponse(@NonNull Call<User> call,@NonNull Response<User> response) {
                 if(!response.isSuccessful()){
                     Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show();
                 }
                 else{
                     User user= response.body();
                     assert user!=null;
                     fetchImage(user.getProfilePic(), new ImageInterface() {
                         @Override
                         public void success(Bitmap bitmap) {
                             user.setProfileBitmap(bitmap);

                             userInterface.success(user);
                         }

                         @Override
                         public void failure(Throwable throwable) {
                             userInterface.failure(throwable);
                         }
                     });

                 }
             }

             @Override
             public void onFailure( @NonNull Call<User> call, @NonNull Throwable t) {
                userInterface.failure(t);
             }
         });
    }

    public static  void updateUser(HashMap<String ,String> update,Context context,UserInterface userInterface){
        String token= MyPreferences.getSavedItem(context,"token");

        Call<User> userCall= myApi.updateUser("Bearer "+token,update);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse( @NonNull Call<User> call, @NonNull Response<User> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show();
                }else {
                    userInterface.success(response.body());
                }
            }

            @Override
            public void onFailure( @NonNull Call<User> call, @NonNull Throwable t) {
                userInterface.failure(t);
            }
        });
    }

    public static void uploadFile(Uri fileUri,Context context){

//        File file = FileUtils.getFile(context, fileUri);

        File file=new File(fileUri.getPath());


        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getMimeType(file)),
                        file
                );


        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        String descriptionString = "User profile";
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, descriptionString);

        // finally, execute the request

        String token =MyPreferences.getSavedItem(context,"token");
        Call<ResponseBody> call = myApi.uploadImage("Bearer "+token,description, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse( @NonNull Call<ResponseBody> call,
                                    @NonNull Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    Log.v("Upload", response.message());
                }else {
                    Log.v("Upload", "success");

                }
            }

            @Override
            public void onFailure( @NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });

    }
    public static  void getMyStatuses(Context context,StatusesInterface statusesInterface){
        String token =MyPreferences.getSavedItem(context,"token");
        Call<List<RetrievedStatus>> statusCall=myApi.getMyStatuses("Bearer "+token);

        statusCall.enqueue(new Callback<List<RetrievedStatus>>() {
            @Override
            public void onResponse( @NonNull Call<List<RetrievedStatus>> call, @NonNull Response<List<RetrievedStatus>> response) {
                if(!response.isSuccessful()){
                    Log.e("Exception",response.message());
                }else{
                    assert response.body() != null;
                    statusesInterface.success(response.body());

                }
            }

            @Override
            public void onFailure( @NonNull Call<List<RetrievedStatus>> call,@NonNull Throwable t) {
                statusesInterface.failure(t);
            }
        });
//        List<RetrievedStatus> statuses=null;
//        try {
//
//            statuses=statusCall.execute().body();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.d("Exception",e.getMessage());
//
//        }
//        return statuses;
    }




    private static String getMimeType(@NonNull File file){
        String type;
        String filePath=file.toString();
        String extension= MimeTypeMap.getFileExtensionFromUrl(filePath);
        if(extension!=null){
            type=MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        }else{
            type="image/*";
        }
        return type;

    }

    public static  void createTypedStatus(Context context, TypedStatus typedStatus, ProgressBar progressBar, EditText statusInput){
        String token=MyPreferences.getSavedItem(context,"token");

        Call<TypedStatus> typedStatusCall= myApi.createTypedStatus("Bearer "+token,typedStatus);

        progressBar.setVisibility(View.VISIBLE);
        typedStatusCall.enqueue(new Callback<TypedStatus>() {
            @Override
            public void onResponse( @NonNull Call<TypedStatus> call,@NonNull Response<TypedStatus> response) {
                progressBar.setVisibility(View.GONE);

                if(!response.isSuccessful()){
                    Log.d("Error",response.message());

                }else {
                    assert response.body() != null;
                    Log.d("Success", response.body().toString());
                    statusInput.setText("");
                }
            }

            @Override
            public void onFailure( @NonNull Call<TypedStatus> call, @NonNull Throwable t) {
                Log.e("Exception",t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });

    }


    public static void fetchImage(String imageName,ImageInterface imageInterface){
        Call<ResponseBody> imageCall= myApi.fetchImage(imageName);

        imageCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,@NonNull Response<ResponseBody> response) {
                assert response.body()!=null;
                Bitmap bitmap= BitmapFactory.decodeStream(response.body().byteStream());
                imageInterface.success(bitmap);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call,@NonNull Throwable t) {
                imageInterface.failure(t);

            }
        });

    }


}
