package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private final String TAG = "ChatActivity";
    private final static String EXTRA_CURRENT_USER_ID = "current_id";
    private final static String EXTRA_OTHER_USER_ID = "other_id";

    private String currentUserId;
    private String otherUserId;

    private TextView textViewTitle;
    private RecyclerView recyclerViewMessage;
    private EditText editTextMessage;
    private View onlineStatus;
    private ImageView imageViewSendMessage;

    private MessagesAdapter messagesAdapter;

    private ChatViewModel viewModel;
    private ChatViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();
        currentUserId = getIntent().getStringExtra(EXTRA_CURRENT_USER_ID);
        otherUserId = getIntent().getStringExtra(EXTRA_OTHER_USER_ID);
        viewModelFactory = new ChatViewModelFactory(currentUserId, otherUserId);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(ChatViewModel.class);
        messagesAdapter = new MessagesAdapter(currentUserId);
        ArrayList<Message> messages = new ArrayList<>();
        Log.d(TAG, otherUserId + " " + currentUserId);
        messagesAdapter.setMessageList(messages);
        recyclerViewMessage.setAdapter(messagesAdapter);
        observeViewModel();
        editTextMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message(
                        currentUserId,
                        otherUserId,
                        editTextMessage.getText().toString().trim()
                );
                viewModel.sendMessage(message);
            }
        });
    }

    private void initViews() {
        textViewTitle = findViewById(R.id.textViewTitle);
        recyclerViewMessage = findViewById(R.id.recyclerViewMessage);
        editTextMessage = findViewById(R.id.editTextMessage);
        onlineStatus = findViewById(R.id.onlineStatus);
        imageViewSendMessage = findViewById(R.id.imageViewSendMessage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.setUserOnline(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.setUserOnline(false);
    }

    private void observeViewModel() {
        viewModel.getListMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                messagesAdapter.setMessageList(messages);
            }
        });
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    Toast.makeText(ChatActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel.getMessageSent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSent) {
                if (isSent) {
                    editTextMessage.setText("");
                }
            }
        });
        viewModel.getOtherUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    String userInfo = String.format("%s %s", user.getName(), user.getLastName());
                    textViewTitle.setText(userInfo);
                    int bgResId;
                    if (user.getStatus()) {
                        bgResId = R.drawable.green_circle;
                    } else {
                        bgResId = R.drawable.red_circle;
                    }
                    Drawable background = ContextCompat.getDrawable(//получение фона
                            ChatActivity.this,//контекст
                            bgResId//фон
                    );
                    onlineStatus.setBackground(background);
                }
            }
        });
    }

    public static Intent newIntent(Context context, String current_id, String other_id) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID, current_id);
        intent.putExtra(EXTRA_OTHER_USER_ID, other_id);
        return intent;
    }
}