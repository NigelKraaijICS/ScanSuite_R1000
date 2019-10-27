package nl.icsvertex.scansuite.Activities.general;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cResult;
import ICS.Utils.cUserInterface;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.Authorisations.cAuthorisation;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Licenses.cLicense;
import nl.icsvertex.scansuite.Activities.inventory.InventoryorderSelectActivity;
import nl.icsvertex.scansuite.Activities.ship.ShiporderSelectActivity;
import nl.icsvertex.scansuite.Activities.sort.SortorderSelectActivity;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.Activities.pick.PickorderSelectActivity;

public class MenuActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    //End Region Public Properties

    //Region Private Properties

    //Region views
    private ShimmerFrameLayout shimmerViewContainer;
    private RecyclerView recyclerViewMenu;

    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private ImageView toolbarImageHelp;
    //End region views

    //End Region Private Properties

    //Region Default Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_menu);

        // Standard methods to initialize the Activity
        this.mActivityInitialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mStartShimmering();
        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mStopShimmering();
        cBarcodeScan.pUnregisterBarcodeReceiver();
    }

    //End Region Default Methods

    //Region iICSDefaultActivity defaults
    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_menu));

        this.mFieldsInitialize();

        this.mSetListeners();

        this.mInitScreen();

        cBarcodeScan.pRegisterBarcodeReceiver();

    }

    @Override
    public void mSetAppExtensions() {
        cAppExtension.context = this;
        cAppExtension.fragmentActivity  = this;
        cAppExtension.activity = this;
        cAppExtension.fragmentManager  = getSupportFragmentManager();
    }

    @Override
    public void mFindViews() {
        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarImageHelp = findViewById(R.id.toolbarImageHelp);
        this.recyclerViewMenu = findViewById(R.id.recyclerViewMenu);
        this.shimmerViewContainer = findViewById(R.id.shimmerViewContainer);
    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        this.toolbarImageHelp.setVisibility(View.INVISIBLE);
        this.toolbarTitle.setText(pvScreenTitle);
        this.toolbarImage.setImageResource(R.drawable.ic_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {

        if (! cUser.currentUser.pGetAutorisationsBln()){
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_autorisations_failed), cUser.currentUser.getUsernameStr(), true, true );
            this.mStopShimmering();
            return;
        }

        if (cUser.currentUser.autorisationObl.size() == 0 ) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_no_autorisations_available), cUser.currentUser.getUsernameStr(), true, true );
            this.mStopShimmering();
            return;
        }

        if (! cUser.currentUser.currentBranch.pGetWorkplacesBln()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_workplaces_failed), cUser.currentUser.currentBranch.getBranchNameStr(), true, true );
            this.mStopShimmering();
            return;
        }

        if (cUser.currentUser.currentBranch.workplacesObl().size() == 0 && cSetting.PICK_SALES_ASK_WORKPLACE()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_no_workplaces_available), cUser.currentUser.currentBranch.getBranchNameStr(), true, true );
            this.mStopShimmering();
            return;
        }

        this.mSetAuthorisationRecycler();
        this.mStopShimmering();
    }

    @Override
    public void mSetListeners() {

    }

    @Override
    public void mInitScreen() {

    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public static void pAuthorisationSelected() {

        ViewGroup container = cAppExtension.activity.findViewById(R.id.container);

        final Intent intent;
        final View clickedImage;
        final View clickedText;
        final ActivityOptionsCompat activityOptions;

        if (cUser.currentUser.currentAuthorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.PICK) {

            cLicense.currentLicenseEnu = cLicense.LicenseEnu.Pick;
            if (!  cLicense.pGetLicenseViaWebserviceBln()) {
                cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_license_error), "",true,true);
                return;
            }

            intent = new Intent(cAppExtension.context, PickorderSelectActivity.class);
            clickedImage = container.findViewWithTag(cAuthorisation.TAG_IMAGE_PICK);
            clickedText= container.findViewWithTag(cAuthorisation.TAG_TEXT_PICK);
            activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new androidx.core.util.Pair<>(clickedImage, PickorderSelectActivity.VIEW_NAME_HEADER_IMAGE), new androidx.core.util.Pair<>(clickedText, PickorderSelectActivity.VIEW_NAME_HEADER_TEXT));
            ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
            return;
        }

        if (cUser.currentUser.currentAuthorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.SORTING) {

            cLicense.currentLicenseEnu = cLicense.LicenseEnu.Pick;

            cLicense.currentLicenseEnu = cLicense.LicenseEnu.Pick;
            if (!  cLicense.pGetLicenseViaWebserviceBln()) {
                cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_license_error), "",true,true);
                return;
            }


            intent  = new Intent(cAppExtension.context, SortorderSelectActivity.class);
            clickedImage = container.findViewWithTag(cAuthorisation.TAG_IMAGE_SORT);
            clickedText = container.findViewWithTag(cAuthorisation.TAG_TEXT_SORT);
            activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new Pair<>(clickedImage, SortorderSelectActivity.VIEW_NAME_HEADER_IMAGE), new Pair<>(clickedText, SortorderSelectActivity.VIEW_NAME_HEADER_TEXT));
            ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
            return;
        }

        if (cUser.currentUser.currentAuthorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.SHIPPING) {

            cLicense.currentLicenseEnu = cLicense.LicenseEnu.Pick;

            cLicense.currentLicenseEnu = cLicense.LicenseEnu.Pick;
            if (!  cLicense.pGetLicenseViaWebserviceBln()) {
                cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_license_error), "",true,true);
                return;
            }


            intent = new Intent(cAppExtension.context, ShiporderSelectActivity.class);
            clickedImage = container.findViewWithTag(cAuthorisation.TAG_IMAGE_SHIP);
            clickedText = container.findViewWithTag(cAuthorisation.TAG_TEXT_SHIP);
            activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new Pair<>(clickedImage, ShiporderSelectActivity.VIEW_NAME_HEADER_IMAGE), new Pair<>(clickedText, ShiporderSelectActivity.VIEW_NAME_HEADER_TEXT));
            ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
            return;
        }


        if (cUser.currentUser.currentAuthorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.INVENTORY) {

            cLicense.currentLicenseEnu = cLicense.LicenseEnu.Inventory;
            if (!  cLicense.pGetLicenseViaWebserviceBln()) {
                cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_license_error), "",true,true);
                return;
            }

            intent = new Intent(cAppExtension.context, InventoryorderSelectActivity.class);
            clickedImage = container.findViewWithTag(cAuthorisation.TAG_IMAGE_INVENTORY);
            clickedText= container.findViewWithTag(cAuthorisation.TAG_TEXT_INVENTORY);
            activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new androidx.core.util.Pair<>(clickedImage, PickorderSelectActivity.VIEW_NAME_HEADER_IMAGE), new androidx.core.util.Pair<>(clickedText, PickorderSelectActivity.VIEW_NAME_HEADER_TEXT));
            ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
            return;
        }

    }

    public static void pHandleScan(cBarcodeScan pvScannedBarcode){

     cArticle article =   cArticle.pGetArticleByBarcodeViaWebservice(pvScannedBarcode);
     article.pGetBarcodesViaWebserviceBln();
     article.pGetStockViaWebserviceBln();
    }

    //End Region Public Methods

    //Region Private Methods
    private void mSetAuthorisationRecycler() {

        this.recyclerViewMenu.setHasFixedSize(false);
        this.recyclerViewMenu.setAdapter(cAuthorisation.getAuthorisationAdapter());
        this.recyclerViewMenu.setLayoutManager(new LinearLayoutManager(this));
        cAuthorisation.getAuthorisationAdapter().setAuthorisations();
    }

    private void mStartShimmering(){
        //Start Shimmer Effect's animation until data is loaded
        this.  shimmerViewContainer.startShimmerAnimation();
}

    private void mStopShimmering(){
        //Stopping Shimmer Effect's animation after data is loaded
        this.shimmerViewContainer.stopShimmerAnimation();
        this.shimmerViewContainer.setVisibility(View.GONE);
    }

    //End Region Private Methods







}
