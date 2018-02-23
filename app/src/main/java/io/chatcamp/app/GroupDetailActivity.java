package io.chatcamp.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.security.cert.CollectionCertStoreParameters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.chatcamp.app.R;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.GroupChannel;
import io.chatcamp.sdk.Participant;
import io.chatcamp.sdk.User;

public class GroupDetailActivity extends AppCompatActivity implements GroupDetailAdapter.OnParticipantClickedListener {
    public  static final String KEY_GROUP_ID = "key_group_id";


    private RecyclerView participantRv;
    private ImageView toolbarIv;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private GroupDetailAdapter adapter;
    private GroupChannel groupChannelGLobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        participantRv  = findViewById(R.id.rv_participant_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        participantRv.setLayoutManager(manager);
        adapter = new GroupDetailAdapter(this);
        adapter.setParticipantClickedListener(this);
        participantRv.setAdapter(adapter);
        toolbarIv = findViewById(R.id.toolbarImage);
        String id = getIntent().getStringExtra(KEY_GROUP_ID);
        GroupChannel.get(id, new GroupChannel.GetListener() {
            @Override
            public void onResult(GroupChannel groupChannel, ChatCampException e) {
                populateUi(groupChannel);
            }
        });

    }

    private void populateUi(GroupChannel groupChannel) {
        groupChannelGLobal = groupChannel;
        collapsingToolbarLayout.setTitle(groupChannel.getName());
        Picasso.with(this).load(groupChannel.getAvatarUrl()).into(toolbarIv);
        String[] participantIds = groupChannel.getAcceptedParticipants();
        List<ParticipantView> participantList = new ArrayList<>();
        for(Map.Entry<String, Participant> entry : groupChannel.getParticipantsList().entrySet()) {
            participantList.add(new ParticipantView(entry.getValue()));
        }
        adapter.clear();
        adapter.addAll(participantList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_group_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        } else if(item.getItemId() == R.id.action_edit_group) {
            //Toast.makeText(this, "Edit group", Toast.LENGTH_LONG).show();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(GroupDetailActivity.this);
            alertDialog.setTitle("Update Group");
            alertDialog.setMessage("Enter Group Name");

            final EditText input = new EditText(GroupDetailActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            alertDialog.setView(input);

            alertDialog.setPositiveButton("UPDATE",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(TextUtils.isEmpty(input.getText())) {
                                input.setError("Group name could not be empty");
                            } else {
                                groupChannelGLobal.update(input.getText().toString(), null, null, new GroupChannel.UpdateListener() {
                                    @Override
                                    public void onResult(GroupChannel groupChannel, ChatCampException e) {
                                        populateUi(groupChannel);
                                    }
                                });
                            }
                        }
                    });

            alertDialog.setNegativeButton("CANCEL",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddParticipantClicked() {
        Toast.makeText(this, "Add Participant Clicked", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onParticipantClicked(Participant participant) {
        Toast.makeText(this, participant.getDisplayName() + " Participant Clicked", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra(UserProfileActivity.KEY_PARTICIPANT_ID, participant.getId());
        startActivity(intent);
    }

    @Override
    public void onExitGroupClicked() {
        groupChannelGLobal.leave(new GroupChannel.LeaveListener() {
            @Override
            public void onResult(ChatCampException e) {
                finish();
            }
        });
    }
}