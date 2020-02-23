package com.list.listview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Context mContext = this;
    private  static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<NotificationNumber> notificationNumbers;
    private Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//disable top bar
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/users")
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
                Log.d(TAG,"onResponse:" + json);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        parseJSON(json);
                    }
                });
            }
        });
    }
    private void parseJSON(String json) {//get json
        notificationNumbers = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                NotificationNumber number = new NotificationNumber(object);
                notificationNumbers.add(number);
            }
            //Log.d(TAG,"avatar_url:" + avatar_url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NotificationAdapter adapter = new NotificationAdapter();
        recyclerView.setAdapter(adapter);
    }

    public class NotificationAdapter extends  RecyclerView.Adapter<NotificationAdapter.NotificationHolder>{
        @NonNull
        @Override
        public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(
                    R.layout.item_notiflcation,parent,false
            );
            return new NotificationHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
            final NotificationNumber notification = notificationNumbers.get(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    i = new Intent(mContext, activity_deatil.class);
                    i.putExtra("username",String.valueOf(notification.getLogin()));
                    mContext.startActivity(i);
                }
            });
            holder.bindTo(notification);
        }

        @Override
        public int getItemCount() {//get list size
            return notificationNumbers.size();
        }

        public class NotificationHolder extends RecyclerView.ViewHolder{
            ImageView image;
            TextView name;
            TextView status;
            public NotificationHolder(View itemView){
                super(itemView);
                image = itemView.findViewById(R.id.iTem_image);
                name = itemView.findViewById(R.id.iTem_Name);
                status = itemView.findViewById(R.id.iTem_status);
            }

            public void bindTo(NotificationNumber notification) {
                name.setText(String.valueOf(notification.getLogin()));
                if(!notification.getSite_admin().equals("false")) {
                    status.setText("  STAFF  ");
                }
                Picasso.get().load(notification.getAvatar_url()).into(image);//get images url
            }
        }
    }

}
