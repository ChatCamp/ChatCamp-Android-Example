package io.chatcamp.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by shubhamdhabhai on 15/02/18.
 */

public class ImagePreviewActivity extends AppCompatActivity {

    public static final String  IMAGE_URI = "image_uri";

    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_preview_layout);

        final Intent intent = getIntent();
        final String uri = intent.getStringExtra(IMAGE_URI);
        imageView = (ImageView)findViewById(R.id.preview_image_view);
        TextView sendButton = findViewById(R.id.tv_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent();
                intent1.putExtra(IMAGE_URI, uri);
                setResult(RESULT_OK, intent1);
                finish();
            }
        });
        imageView.setImageURI(Uri.parse(uri));
        imageView.setClickable(true);
    }
}
