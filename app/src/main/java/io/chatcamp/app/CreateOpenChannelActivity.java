package io.chatcamp.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.chatcamp.sdk.BaseChannel;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.GroupChannelListQuery;
import io.chatcamp.sdk.OpenChannel;

public class CreateOpenChannelActivity extends AppCompatActivity {

    private EditText channelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_open_channel);
        channelName = findViewById(R.id.channel_name);
        Button mButtonCreate = findViewById(R.id.button_channel_create);

        mButtonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (channelName.getText().toString().length() > 0) {
                    OpenChannel.create(channelName.getText().toString(), new OpenChannel.CreateListener() {
                        @Override
                        public void onResult(BaseChannel openChannel, ChatCampException e) {
                            Intent intent = new Intent(CreateOpenChannelActivity.this, ConversationActivity.class);
                            intent.putExtra("channelType", "open");
                            intent.putExtra("participantState", GroupChannelListQuery.ParticipantState.ALL.name());
                            intent.putExtra("channelId", openChannel.getId());
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });
    }
}
