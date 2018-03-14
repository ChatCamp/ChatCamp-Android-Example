package io.chatcamp.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import io.chatcamp.sdk.BaseChannel;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.GroupChannel;
import io.chatcamp.sdk.OpenChannel;

/**
 * A login screen that offers login via email/password.
 */
public class ChannelCreateActivity extends AppCompatActivity {

    private Spinner mSpinner;
    private TextView mChannelName;
    private TextView mChannelParticipants;
    private Button mButtonCreate;
    private Switch mSwitchDistinct;

    private View.OnClickListener mButtonCreateListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            System.out.println(mSwitchDistinct.isChecked());
            if (mSpinner.getSelectedItemPosition() == 0 && mChannelName.getText().toString().length() > 0) {
                OpenChannel.create(mChannelName.getText().toString(), new OpenChannel.CreateListener() {
                    @Override
                    public void onResult(BaseChannel openChannel, ChatCampException e) {
                        Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                        startActivity(intent);
                    }
                });
            } else if (mSpinner.getSelectedItemPosition() == 1 && mChannelName.getText().toString().length() > 0 && mChannelParticipants.getText().toString().length() > 0) {
                String[] participants = mChannelParticipants.getText().toString().split(",");
                GroupChannel.create(mChannelName.getText().toString(), participants, mSwitchDistinct.isChecked(), new GroupChannel.CreateListener() {
                    @Override
                    public void onResult(BaseChannel groupChannel, ChatCampException e) {
                        Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_channel_create);

        mSpinner = (Spinner) findViewById(R.id.spinner_channel);
        mChannelName = (TextView) findViewById(R.id.channel_name);
        mChannelParticipants = (TextView) findViewById(R.id.channel_participants);
        mButtonCreate = (Button) findViewById(R.id.button_channel_create);
        mSwitchDistinct = (Switch) findViewById(R.id.switch_distinct);
        mButtonCreate.setOnClickListener(mButtonCreateListener);


    }


}

