package nl.icsvertex.scansuite.activities.inventory;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import nl.icsvertex.scansuite.R;

public class InventoryActivity extends AppCompatActivity {
    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";
    public static final String VIEW_NAME_HEADER_TEXT = "detail:header:text";

    Context thisContext;
    Activity thisActivity;

    ImageView toolbarImage;
    TextView toolbarTitle;
    ImageView toolbarImageHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        thisContext = this;
        thisActivity = this;

        m_findViews();
        m_fieldsInitialize();
        m_setToolbar();
        m_setListeners();
        m_initProgram();

        ViewCompat.setTransitionName(toolbarImage, VIEW_NAME_HEADER_IMAGE);
        ViewCompat.setTransitionName(toolbarTitle, VIEW_NAME_HEADER_TEXT);
    }
    private void m_findViews() {
        toolbarImage = findViewById(R.id.toolbarImage);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarImageHelp = findViewById(R.id.toolbarImageHelp);
    }
    private void m_fieldsInitialize() {

    }
    private void m_setToolbar() {
        toolbarImage.setImageResource(R.drawable.ic_menu_inventory);
        toolbarTitle.setText(R.string.screentitle_inventory);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    private void m_setListeners() {

    }
    private void m_initProgram() {

    }
}
