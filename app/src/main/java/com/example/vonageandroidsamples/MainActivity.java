package com.example.vonageandroidsamples;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vonageandroidsamples.config.OpenTokConfig;
import com.example.vonageandroidsamples.github_official.BasicVideoCapturer_Java;
import com.example.vonageandroidsamples.github_official.BasicVideoChat;
import com.example.vonageandroidsamples.yukari.MediaTransformerYuka;
import com.example.vonageandroidsamples.network.APIService;
import com.example.vonageandroidsamples.network.RetrofitClient;
import com.example.vonageandroidsamples.network.SessionResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private String[] activities = {"Basic-Video-Chat-Java", "Basic-Video-Capturer-Camera-2-Java", "Basic-Audio-Driver-Java", "Screen-Sharing-Java", "Media-Transformer-Yuka"};
    private EditText editTextRoom;
    private TextView sessionDetailsText;
    private ProgressBar progressBar;
    // Shared Preferences keys
    private static final String SHARED_PREFS_NAME = "SessionPrefs";
    private static final String KEY_ROOM_NAME = "roomName";
    private static final String KEY_SESSION_ID = "sessionId";
    private static final String KEY_TOKEN = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextRoom = findViewById(R.id.editText);
        sessionDetailsText = findViewById(R.id.textView);
        progressBar = findViewById(R.id.progressBar);

        ListView listView = findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, activities);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedActivity = activities[position];
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(MainActivity.this, BasicVideoChat.class);
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this, BasicVideoCapturer_Java.class);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, ActivityThree.class);
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, ActivityFour.class);
                        break;
                    case 4:
                        intent = new Intent(MainActivity.this, MediaTransformerYuka.class);
                        break;
                    default:
                        return;
                }
                intent.putExtra("activity_name", selectedActivity);
                startActivity(intent);
            }
        });

        // Load saved session details from SharedPreferences if they exist
        loadSessionDetailsFromPrefs();
    }

    // onClick handler for the button
    public void onSubmitButtonClick(View view) {
        // Your button click action goes here
       // Toast.makeText(this, "Button clicked!", Toast.LENGTH_SHORT).show();

        String roomName = editTextRoom.getText().toString().trim();
        if (!roomName.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            getSessionCredentials(roomName);
        } else {
            Toast.makeText(MainActivity.this, "Please enter a room name", Toast.LENGTH_SHORT).show();
        }
    }

    private void getSessionCredentials(String roomName) {
        // Create an instance of the Retrofit client and API service
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);

        // Make the API call
        Call<SessionResponse> call = apiService.getSessionCredentials(roomName);
        call.enqueue(new Callback<SessionResponse>() {
            @Override
            public void onResponse(Call<SessionResponse> call, Response<SessionResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    // Retrieve the session data from the response

                    String apiKey = response.body().getApiKey();
                    String sessionId = response.body().getSessionId();
                    String token = response.body().getToken();

                    Log.d("API Response", "API Key: " + apiKey);
                    Log.d("API Response", "Session ID: " + sessionId);
                    Log.d("API Response", "Token: " + token);

                    OpenTokConfig.API_KEY = "47807831";
                    OpenTokConfig.SESSION_ID = sessionId;
                    OpenTokConfig.TOKEN = token;

                    // Populate the TextView with the session details
                    SpannableString formattedText = new SpannableString(
                            "Room name: " + roomName + "\n\n" +
                                    "Session ID: " + sessionId + "\n\n" +
                                    "Token: " + token);

                    // Apply bold style to "Room name", "Session ID", and "Token"
                    formattedText.setSpan(new StyleSpan(Typeface.BOLD), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // "Room name: "
                    formattedText.setSpan(new StyleSpan(Typeface.BOLD), 11 + roomName.length(), 23 + roomName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // "Session ID: "
                    formattedText.setSpan(new StyleSpan(Typeface.BOLD), 25 + roomName.length() + sessionId.length(), 32 + roomName.length() + sessionId.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // "Token: "

                    // Populate the TextView with the formatted session details
                    sessionDetailsText.setText(formattedText);
                    // Save session details to SharedPreferences
                    saveSessionDetailsToPrefs(roomName, sessionId, token);

                    // Call a method to initialize the session or handle the retrieved data
                    initializeSession(apiKey, sessionId, token);
                } else {
                    Log.e("API Response", "Failed to get session credentials.");
                }
            }

            @Override
            public void onFailure(Call<SessionResponse> call, Throwable t) {
                Log.e("API Response", "Error: " + t.getMessage());
            }
        });
    }

    private void initializeSession(String apiKey, String sessionId, String token) {
        // Logic for initializing the session with the provided credentials
        Log.d("Initialize Session", "Initializing session with API Key: " + apiKey);
    }

    // Save session details to SharedPreferences
    private void saveSessionDetailsToPrefs(String roomName, String sessionId, String token) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_ROOM_NAME, roomName);
        editor.putString(KEY_SESSION_ID, sessionId);
        editor.putString(KEY_TOKEN, token);

        // Apply changes
        editor.apply();

        Log.d("SharedPrefs", "Session details saved to SharedPreferences.");
    }

    // Load session details from SharedPreferences
    private void loadSessionDetailsFromPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);

        // Check if session details exist in SharedPreferences
        String roomName = sharedPreferences.getString(KEY_ROOM_NAME, null);
        String sessionId = sharedPreferences.getString(KEY_SESSION_ID, null);
        String token = sharedPreferences.getString(KEY_TOKEN, null);

        if (roomName != null && sessionId != null && token != null) {
            // Populate the TextView with the saved session details
            SpannableString formattedText = new SpannableString(
                    "Room name: " + roomName + "\n\n" +
                            "Session ID: " + sessionId + "\n\n" +
                            "Token: " + token);

            OpenTokConfig.API_KEY = "47807831";
            OpenTokConfig.SESSION_ID = sessionId;
            OpenTokConfig.TOKEN = token;

            // Apply bold style to "Room name", "Session ID", and "Token"
            formattedText.setSpan(new StyleSpan(Typeface.BOLD), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // "Room name: "
            formattedText.setSpan(new StyleSpan(Typeface.BOLD), 11 + roomName.length(), 23 + roomName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // "Session ID: "
            formattedText.setSpan(new StyleSpan(Typeface.BOLD), 25 + roomName.length() + sessionId.length(), 32 + roomName.length() + sessionId.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // "Token: "

            // Populate the TextView with the formatted session details
            sessionDetailsText.setText(formattedText);

            Log.d("SharedPrefs", "Loaded session details from SharedPreferences.");
        } else {
            Log.d("SharedPrefs", "No session details found in SharedPreferences.");
            sessionDetailsText.setText("There's no session data saved yet. Use \"Join Room\" option to create a one.");
        }
    }

}
