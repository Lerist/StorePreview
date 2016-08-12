package com.bowyer.app.storepreview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bowyer.app.storepreview.preference.DataPreference;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

public class PreviewActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

  private static String KEY_SHORT = "key_short";
  private static String KEY_DESCRIPTION = "key_description";
  private static String SHARE_FORMAT = "【簡単な説明文】\n%s\n【詳細な説明文】\n%s";

  @Bind(R.id.short_text) TextView shortText;
  @Bind(R.id.description) TextView description;
  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.fab) FloatingActionButton fab;

  private String textShort;
  private String textDescription;

  private DataPreference mPrefs;

  public static void startActivity(Context context, String textShort, String textDescription) {
    Intent intent = new Intent(context, PreviewActivity.class);
    intent.putExtra(KEY_SHORT, textShort);
    intent.putExtra(KEY_DESCRIPTION, textDescription);
    context.startActivity(intent);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_preview);
    ButterKnife.bind(this);
    mPrefs = new DataPreference(this);
    initToolbar();
    initData();
  }

  private void initToolbar() {
    toolbar.setTitle(R.string.app_name);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    toolbar.setNavigationIcon(R.drawable.ic_close);
  }

  private void initData() {
    Intent intent = getIntent();

    textShort = intent.getStringExtra(KEY_SHORT);
    textDescription = intent.getStringExtra(KEY_DESCRIPTION);

    shortText.setText(textShort);
    String parceDescription = textDescription.replaceAll("\\n", "<br>");
    description.setText(Html.fromHtml(parceDescription));
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_preview, menu);
    return super.onCreateOptionsMenu(menu);
  }

  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
      case R.id.action_share:
        share();
        return true;
    }
    return false;
  }

  @OnClick(R.id.fab) void save() {
    mPrefs.saveShortText(textShort);
    mPrefs.saveSescriptionText(textDescription);
    Toast.makeText(this, R.string.message_saved, Toast.LENGTH_SHORT).show();
    finish();
  }

  private void share() {
    String message = String.format(SHARE_FORMAT, textShort, textDescription);
    ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(this);
    builder.setChooserTitle(getString(R.string.title_data_share));
    builder.setText(message);
    builder.setType("text/plain");
    builder.startChooser();
  }

  @Override public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

  }

  @Override public void onDownMotionEvent() {

  }

  @Override public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    if (scrollState == ScrollState.UP) {
      fab.hide();
    } else {
      fab.show();
    }
  }
}
