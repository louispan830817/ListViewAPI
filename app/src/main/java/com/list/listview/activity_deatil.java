package com.list.listview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class activity_deatil extends AppCompatActivity {
    private Context mContext = this;
    private  static final String TAG = MainActivity.class.getSimpleName();
    //private List<NotificationNumber> notificationNumbers;
    private TextView Textlogin;
    private TextView Textname;
    private TextView Textstatus;
    private TextView Textlocation;
    private TextView Textlink;
    private TextView Textbio;
    private ImageView imageView;
    private ImageView closeimage;
    static String avatar_url;
    String username;
    String name;
    String bio;
    String login;
    String site_admin;
    String location;
    String blog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//disable top bar
        setContentView(R.layout.activity_deatil);
        imageView = (ImageView) findViewById(R.id.imageView);
        closeimage = (ImageView) findViewById(R.id.closeimage);
        Intent intent = this.getIntent();
        username = intent.getStringExtra("username");
        Log.d(TAG,"username:" + username);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/users/" + username)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new AlertDialog.Builder(mContext)
                        .setTitle("Error")
                        .setIcon(R.drawable.error)
                        .setMessage("Please try later")
                        .setPositiveButton("Confirm",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String json = response.body().string();
                Log.d(TAG,"onResponseDetail:" + json);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        parseJSON(json);
                    }
                });
            }
        });
        closeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, MainActivity.class);
                mContext.startActivity(i);
            }
        });

    }
    private void parseJSON(String json) {//get json
        //notificationNumbers = new ArrayList<>();
        try {
                JSONObject object = new JSONObject(json);
                avatar_url = object.getString("avatar_url");
                name = object.getString("name");
                bio = object.getString("bio");
                login = object.getString("login");
                site_admin = object.getString("site_admin");
                location = object.getString("location");
                blog = object.getString("blog");
            Textlogin = findViewById(R.id.deatil_login);
            Textlink = findViewById(R.id.detail_link);
            Textlocation =findViewById(R.id.detail_location);
            Textname = findViewById(R.id.detail_name);
            Textstatus = findViewById(R.id.detail_status);
            Textbio = findViewById(R.id.detail_bio);
            Textlogin.setText(login);
            if(site_admin != "false") {
                Textstatus.setText("  STAFF  ");
            }
            Textlink.setText(blog);
            Textname.setText(name);
            Textlocation.setText(location);
            if(bio != "null") {
                Textbio.setText(bio);
            }
            Picasso.get().load(avatar_url).into(imageView);
            //LoadImageFromWebOperations(avatar_url);
            } catch (JSONException ex) {
            ex.printStackTrace();
        }
        Log.d(TAG,"bio:" + bio);
    }
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, avatar_url);
            return d;
        } catch (Exception e) {
            return null;
        }
    }

}
