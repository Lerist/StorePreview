package com.bowyer.app.storepreview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bowyer.app.storepreview.preference.DataPreference;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class InputActivity extends AppCompatActivity {

  private static final String KEY_DESCRIPTION = "key_description";
  @Bind(R.id.short_text) EditText editShort;
  @Bind(R.id.description) EditText editDescription;

  private DataPreference mPrefs;

  public static void startActivity(Context context, String description) {
    Intent intent = new Intent(context, InputActivity.class);
    intent.putExtra(KEY_DESCRIPTION, description);
    context.startActivity(intent);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Fabric.with(this, new Crashlytics());
    setContentView(R.layout.activity_input);
    ButterKnife.bind(this);
    mPrefs = new DataPreference(this);
    initData();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_input, menu);
    return super.onCreateOptionsMenu(menu);
  }

  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_preview:
        showPreview();
        return true;
    }
    return false;
  }

  private void initData() {
    if (getIntent().hasExtra(KEY_DESCRIPTION)) {
      setDataFromIntent();
      return;
    }
    String shortText = mPrefs.getShortText();
    if (!TextUtils.isEmpty(shortText)) {
      editShort.setText(shortText);
    }
    String descriptionText = mPrefs.getDescriptionText();
    if (!TextUtils.isEmpty(descriptionText)) {
      editDescription.setText(descriptionText);
    }
  }

  private void setDataFromIntent() {
    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    String description = extras.getString(KEY_DESCRIPTION);
    editDescription.setText(description);
  }

  private void showPreview() {
    String shortText = editShort.getText().toString();
    String description = editDescription.getText().toString();
    if (TextUtils.isEmpty(shortText) || TextUtils.isEmpty(description)) {
      Toast.makeText(this, R.string.text_need, Toast.LENGTH_SHORT).show();
      return;
    }
    PreviewActivity.startActivity(this, shortText, description);
  }
}
