package nl.icsvertex.scansuite.Activities.General;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.Authorisations.cAuthorisation;
import SSU_WHS.Basics.Authorisations.cAuthorisationAdapter;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Licenses.cLicense;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Move.MoveOrders.cMoveorder;
import nl.icsvertex.scansuite.Activities.FinishShip.FinishShiporderSelectActivity;
import nl.icsvertex.scansuite.Activities.IntakeAndReceive.IntakeAndReceiveSelectActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderSelectActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesPlaceMTActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesTakeMTActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveMISinglepieceActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveorderSelectActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderSelectActivity;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderSelectActivity;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderSelectActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderSelectActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.BinItemsFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ItemStockFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ScanArticleFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ScanBinFragment;
import nl.icsvertex.scansuite.R;

public class MenuActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties

    public static boolean refreshBln;

    //End Region Public Properties

    //Region Private Properties

    //Region views
    private  ShimmerFrameLayout shimmerViewContainer;
    private  RecyclerView recyclerViewMenu;

    private  ImageView toolbarImage;
    private  TextView toolbarTitle;
    private  TextView toolbarSubtext;
    private  TextView toolbarSubtext2;

    private DrawerLayout menuActionsDrawer;
    private NavigationView actionMenuNavigation;

    cAuthorisationAdapter authorisationAdapter;
    cAuthorisationAdapter getAuthorisationAdapter(){
        if (this.authorisationAdapter == null) {
            this.authorisationAdapter = new cAuthorisationAdapter();
        }

        return  authorisationAdapter;
    }

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
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mStopShimmering();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onBackPressed() {
        this.mLeaveActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu pvMenu) {
        getMenuInflater().inflate(R.menu.menu_stockactions,pvMenu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu pvMenu) {
        invalidateOptionsMenu();

        pvMenu.findItem(R.id.item_bin_stock).setVisible(cSetting.REALTIME_BARCODE_CHECK());
        pvMenu.findItem(R.id.item_article_stock).setVisible(cSetting.REALTIME_BARCODE_CHECK());
        pvMenu.findItem(R.id.item_barcodeinfo).setVisible(true);

        return super.onPrepareOptionsMenu(pvMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {

        DialogFragment selectedFragment = null;

        switch (pvMenuItem.getItemId()) {

            case android.R.id.home:
                this.mLeaveActivity();
                return true;

            case R.id.item_bin_stock:
                selectedFragment = new ScanBinFragment();
                break;

            case R.id.item_article_stock:
                selectedFragment = new ScanArticleFragment();
                break;

            case R.id.item_barcodeinfo:
                mShowBarcodeInfoActivity();
                return true;

            default:
                break;
        }

        // deselect everything
        int size = actionMenuNavigation.getMenu().size();
        for (int i = 0; i < size; i++) {
            actionMenuNavigation.getMenu().getItem(i).setChecked(false);
        }

        // set item as selected to persist highlight
        pvMenuItem.setChecked(true);
        // close drawer when item is tapped
        this.menuActionsDrawer.closeDrawers();
        if (selectedFragment != null) {
            selectedFragment.setCancelable(true);
            selectedFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.BINITEMSFRAGMENT_TAG);
        }

        return true;

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
        this.toolbarSubtext = findViewById(R.id.toolbarSubtext);
        this.toolbarSubtext2 = findViewById(R.id.toolbarSubtext2);
        this.recyclerViewMenu = findViewById(R.id.recyclerViewMenu);
        this.shimmerViewContainer = findViewById(R.id.shimmerViewContainer);

        this.menuActionsDrawer = findViewById(R.id.menuActionsDrawer);
        this.actionMenuNavigation = findViewById(R.id.actionMenuNavigation);

    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        this.toolbarTitle.setText(pvScreenTitle);
        this.toolbarImage.setImageResource(R.drawable.ic_menu);
        this.toolbarTitle.setSelected(true);
        this.toolbarSubtext.setSelected(true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {

        this.toolbarSubtext.setText(cUser.currentUser.getNameStr());
        this.toolbarSubtext2.setText(cUser.currentUser.currentBranch.getBranchNameStr());

        if (! cUser.currentUser.pGetAutorisationsBln(MenuActivity.refreshBln)){
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_autorisations_failed), cUser.currentUser.getUsernameStr(), true, true );
            this.mStopShimmering();
            return;
        }

        if (cUser.currentUser.autorisationObl.size() == 0 ) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_no_autorisations_available), cUser.currentUser.getUsernameStr(), true, true );
            this.mStopShimmering();
            return;
        }

        if (! cUser.currentUser.currentBranch.pGetWorkplacesBln(MenuActivity.refreshBln)) {
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

        MenuActivity.refreshBln = false;
    }

    @Override
    public void mSetListeners() {

    }

    @Override
    public void mInitScreen() {

    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public void pAuthorisationSelected() {

        ViewGroup container = cAppExtension.activity.findViewById(R.id.container);

        final Intent intent;
        final View clickedImage;
        final View clickedText;
        final ActivityOptionsCompat activityOptions;

        if (cUser.currentUser.currentAuthorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.PICK  ||
            cUser.currentUser.currentAuthorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.PICK_PF ||
            cUser.currentUser.currentAuthorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.PICK_PV){

            cLicense.currentLicenseEnu = cLicense.LicenseEnu.Pick;
            if (!  cLicense.pGetLicenseViaWebserviceBln()) {
                cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_license_error), "",true,true);
                return;
            }

            intent = new Intent(cAppExtension.context, PickorderSelectActivity.class);
            clickedImage = container.findViewWithTag(cAuthorisation.TAG_IMAGE_PICK);
            clickedText= container.findViewWithTag(cAuthorisation.TAG_TEXT_PICK);
            activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new androidx.core.util.Pair<>(clickedImage, cPublicDefinitions.VIEW_NAME_HEADER_IMAGE), new androidx.core.util.Pair<>(clickedText, cPublicDefinitions.VIEW_NAME_HEADER_TEXT));
            ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
            return;
        }

        if (cUser.currentUser.currentAuthorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.SORTING) {

            cLicense.currentLicenseEnu = cLicense.LicenseEnu.Pick;
            if (!  cLicense.pGetLicenseViaWebserviceBln()) {
                cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_license_error), "",true,true);
                return;
            }


            intent  = new Intent(cAppExtension.context, SortorderSelectActivity.class);
            clickedImage = container.findViewWithTag(cAuthorisation.TAG_IMAGE_SORT);
            clickedText = container.findViewWithTag(cAuthorisation.TAG_TEXT_SORT);
            activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new Pair<>(clickedImage, cPublicDefinitions.VIEW_NAME_HEADER_IMAGE), new Pair<>(clickedText, cPublicDefinitions.VIEW_NAME_HEADER_TEXT));
            ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
            return;
        }

        if (cUser.currentUser.currentAuthorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.SHIPPING) {

            cLicense.currentLicenseEnu = cLicense.LicenseEnu.Pick;

            if (!  cLicense.pGetLicenseViaWebserviceBln()) {
                cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_license_error), "",true,true);
                return;
            }


            intent = new Intent(cAppExtension.context, ShiporderSelectActivity.class);
            clickedImage = container.findViewWithTag(cAuthorisation.TAG_IMAGE_SHIP);
            clickedText = container.findViewWithTag(cAuthorisation.TAG_TEXT_SHIP);
            activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new Pair<>(clickedImage, cPublicDefinitions.VIEW_NAME_HEADER_IMAGE), new Pair<>(clickedText, cPublicDefinitions.VIEW_NAME_HEADER_TEXT));
            ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
            return;
        }

        if (cUser.currentUser.currentAuthorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.FINISH_SHIPPING) {

            cLicense.currentLicenseEnu = cLicense.LicenseEnu.Pick;

            if (!  cLicense.pGetLicenseViaWebserviceBln()) {
                cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_license_error), "",true,true);
                return;
            }


            intent = new Intent(cAppExtension.context, FinishShiporderSelectActivity.class);
            clickedImage = container.findViewWithTag(cAuthorisation.TAG_IMAGE_FINISH_SHIP);
            clickedText = container.findViewWithTag(cAuthorisation.TAG_TEXT_FINSIH_SHIP);
            activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new Pair<>(clickedImage, cPublicDefinitions.VIEW_NAME_HEADER_IMAGE), new Pair<>(clickedText, cPublicDefinitions.VIEW_NAME_HEADER_TEXT));
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
            activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new androidx.core.util.Pair<>(clickedImage, cPublicDefinitions.VIEW_NAME_HEADER_IMAGE), new androidx.core.util.Pair<>(clickedText, cPublicDefinitions.VIEW_NAME_HEADER_TEXT));
            ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
            return;
        }

        if (cUser.currentUser.currentAuthorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.INTAKE||
                cUser.currentUser.currentAuthorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.INTAKE_EO ||
                cUser.currentUser.currentAuthorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.INTAKE_MA) {

            cLicense.currentLicenseEnu = cLicense.LicenseEnu.Intake;
            if (!  cLicense.pGetLicenseViaWebserviceBln()) {
                cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_license_error), "",true,true);
                return;
            }

            switch (cUser.currentUser.currentAuthorisation.getAutorisationEnu()) {
                case INTAKE:
                    IntakeAndReceiveSelectActivity.currentMainTypeEnu = cWarehouseorder.ReceiveAndStoreMainTypeEnu.Unknown;
                    break;

                case INTAKE_EO:
                    IntakeAndReceiveSelectActivity.currentMainTypeEnu = cWarehouseorder.ReceiveAndStoreMainTypeEnu.External;
                    break;

                case INTAKE_MA:
                    IntakeAndReceiveSelectActivity.currentMainTypeEnu = cWarehouseorder.ReceiveAndStoreMainTypeEnu.Store;
                    break;
            }


            //Initialise the activity
            intent = new Intent(cAppExtension.context, IntakeAndReceiveSelectActivity.class);
            IntakeAndReceiveSelectActivity.startedViaMenuBln = true;

            clickedImage = container.findViewWithTag(cAuthorisation.TAG_IMAGE_INTAKE);
            clickedText= container.findViewWithTag(cAuthorisation.TAG_TEXT_INTAKE);
            activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new androidx.core.util.Pair<>(clickedImage, cPublicDefinitions.VIEW_NAME_HEADER_IMAGE), new androidx.core.util.Pair<>(clickedText, cPublicDefinitions.VIEW_NAME_HEADER_TEXT));
            ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
            return;
        }

        if (cUser.currentUser.currentAuthorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.RETURN) {

            cLicense.currentLicenseEnu = cLicense.LicenseEnu.Return;
            if (! cLicense.pGetLicenseViaWebserviceBln()) {
                cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_license_error), "",true,true);
                return;
            }

            intent = new Intent(cAppExtension.context, ReturnorderSelectActivity.class);
            ReturnorderSelectActivity.startedViaMenuBln = true;
            clickedImage = container.findViewWithTag(cAuthorisation.TAG_IMAGE_RETURN);
            clickedText= container.findViewWithTag(cAuthorisation.TAG_TEXT_RETURN);
            activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new androidx.core.util.Pair<>(clickedImage, cPublicDefinitions.VIEW_NAME_HEADER_IMAGE), new androidx.core.util.Pair<>(clickedText, cPublicDefinitions.VIEW_NAME_HEADER_TEXT));
            ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
            return;
        }

        if (cUser.currentUser.currentAuthorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.MOVE || cUser.currentUser.currentAuthorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.MOVE_MV || cUser.currentUser.currentAuthorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.MOVE_MI){
            cLicense.currentLicenseEnu = cLicense.LicenseEnu.Move;
            if (! cLicense.pGetLicenseViaWebserviceBln()) {
                cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_license_error), "",true,true);
                return;
            }

            intent = new Intent(cAppExtension.context, MoveorderSelectActivity.class);
            MoveorderSelectActivity.startedViaMenuBln = true;

            clickedImage = container.findViewWithTag(cAuthorisation.TAG_IMAGE_MOVE);
            clickedText= container.findViewWithTag(cAuthorisation.TAG_TEXT_MOVE);
            activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new androidx.core.util.Pair<>(clickedImage, cPublicDefinitions.VIEW_NAME_HEADER_IMAGE), new androidx.core.util.Pair<>(clickedText, cPublicDefinitions.VIEW_NAME_HEADER_TEXT));
            ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
            return;
        }

        if (cUser.currentUser.currentAuthorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.MOVE_MI_SINGLEPIECE){
            cLicense.currentLicenseEnu = cLicense.LicenseEnu.Move;
            if (! cLicense.pGetLicenseViaWebserviceBln()) {
                cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_license_error), "",true,true);
                return;
            }

            intent = new Intent(cAppExtension.context, MoveMISinglepieceActivity.class);

            clickedImage = container.findViewWithTag(cAuthorisation.TAG_IMAGE_MOVE);
            clickedText= container.findViewWithTag(cAuthorisation.TAG_TEXT_MOVE);
            activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new androidx.core.util.Pair<>(clickedImage, cPublicDefinitions.VIEW_NAME_HEADER_IMAGE), new androidx.core.util.Pair<>(clickedText, cPublicDefinitions.VIEW_NAME_HEADER_TEXT));
            ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
        }

    }


    public  void pHandleHandleBINScanned(String pvBinCodeStr) {

    String binCodeStr = cRegex.pStripRegexPrefixStr(pvBinCodeStr);

        cBranchBin branchBin = cUser.currentUser.currentBranch.pGetBinByCode(binCodeStr);
        if (branchBin == null) {
            this.mStepFailed(cAppExtension.activity.getString(R.string.message_bin_not_valid), pvBinCodeStr);
            return;
        }

     BinItemsFragment binItemsFragment = new BinItemsFragment(branchBin.getBinCodeStr());
        binItemsFragment.setCancelable(true);
        binItemsFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.BINITEMSFRAGMENT_TAG);

}

    public  void pHandleHandleArticleScanned(cBarcodeScan pvBarcodeScan) {

        cArticle.currentArticle=  cArticle.pGetArticleByBarcodeViaWebservice(pvBarcodeScan);
        if (cArticle.currentArticle == null) {
            this.mStepFailed(cAppExtension.activity.getString(R.string.message_unknown_article), pvBarcodeScan.getBarcodeOriginalStr());
            return;
        }


        ItemStockFragment itemStockFragment  = new ItemStockFragment();
        itemStockFragment.setCancelable(true);
        itemStockFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ARTICLESTOCKFRAGMENT_TAG);

    }

    //End Region Public Methods

    //Region Private Methods

    private void mSetAuthorisationRecycler() {

        this.recyclerViewMenu.setHasFixedSize(false);
        this.recyclerViewMenu.setAdapter(this.getAuthorisationAdapter());
        this.recyclerViewMenu.setLayoutManager(new LinearLayoutManager(this));
        this.getAuthorisationAdapter().setAuthorisations();
    }

    private void mStartShimmering(){
        //Start Shimmer Effect's animation until data is loaded
        this.shimmerViewContainer.startShimmerAnimation();
    }

    private void mStopShimmering(){
        //Stopping Shimmer Effect's animation after data is loaded
        this.shimmerViewContainer.stopShimmerAnimation();
        this.shimmerViewContainer.setVisibility(View.GONE);
    }

    private  void mStepFailed(String pvErrorMessageStr, String pvBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvBarcodeStr, true, true);
        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    private void mLeaveActivity(){

        cUserInterface.pCheckAndCloseOpenDialogs();

        Intent intent = new Intent(cAppExtension.context, LoginActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();

    }

    private void mShowBarcodeInfoActivity() {


        Intent  intent = new Intent(cAppExtension.context, BarcodeInfoActivity.class);
        ActivityCompat.startActivity(cAppExtension.context,intent, null);


    }

    //End Region Private Methods

}
