package com.example.clammind;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private ArrayList<ChatMessage> chatMessages;
    private EditText messageInput;
    private Button sendButton;

    private static final String API_URL = "http://192.168.0.253:11434/api/generate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(v -> {
            String userMessage = messageInput.getText().toString().trim();
            if (!userMessage.isEmpty()) {
                addMessage(userMessage, true);
                fetchBotResponse(userMessage);
                messageInput.setText("");
            } else {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addMessage(String message, boolean isUser) {
        chatMessages.add(new ChatMessage(message, isUser));
        chatAdapter.notifyDataSetChanged();
        chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
    }

    private void addTypingIndicator() {
        runOnUiThread(() -> {
            chatMessages.add(new ChatMessage(true)); // Typing indicator
            chatAdapter.notifyDataSetChanged();
            chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
        });
    }

    private void removeTypingIndicator() {
        runOnUiThread(() -> {
            if (!chatMessages.isEmpty() && chatMessages.get(chatMessages.size() - 1).isTyping()) {
                chatMessages.remove(chatMessages.size() - 1);
                chatAdapter.notifyDataSetChanged();
            }
        });
    }

    private void fetchBotResponse(String userMessage) {
        addTypingIndicator(); // Show typing indicator

        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(30, TimeUnit.SECONDS) // Set timeout
                .build();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "llama3.2");
            jsonBody.put("prompt", userMessage);
            jsonBody.put("stream", false);

            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"), jsonBody.toString());

            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    removeTypingIndicator(); // Remove typing indicator
                    runOnUiThread(() -> addMessage("Error: Timeout or no response", false));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    removeTypingIndicator(); // Remove typing indicator
                    if (response.isSuccessful()) {
                        String botReply = null;
                        try {
                            botReply = new JSONObject(response.body().string()).getString("response");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        String finalBotReply = botReply;
                        runOnUiThread(() -> addMessage(finalBotReply, false));
                    } else {
                        runOnUiThread(() -> addMessage("Error: Unable to fetch response", false));
                    }
                }
            });
        } catch (Exception e) {
            removeTypingIndicator(); // Remove typing indicator
            addMessage("Error: " + e.getMessage(), false);
        }
    }
}