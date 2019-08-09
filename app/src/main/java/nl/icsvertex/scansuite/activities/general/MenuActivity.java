package nl.icsvertex.scansuite.activities.general;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Weberror.cWeberrorEntity;
import ICS.Weberror.cWeberrorViewModel;
import SSU_WHS.Basics.Authorisations.cAuthorisation;
import SSU_WHS.Basics.Authorisations.cAuthorisationAdapter;
import SSU_WHS.Basics.Authorisations.cAuthorisationEntity;
import SSU_WHS.Basics.Authorisations.cAuthorisationViewModel;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Basics.Workplaces.cWorkplaceEntity;
import SSU_WHS.Basics.Workplaces.cWorkplaceViewModel;
import SSU_WHS.General.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.activities.inventory.InventoryActivity;
import nl.icsvertex.scansuite.activities.pick.PickorderSelectActivity;
import nl.icsvertex.scansuite.activities.ship.ShiporderSelectActivity;
import nl.icsvertex.scansuite.activities.sort.SortorderSelectActivity;
import nl.icsvertex.scansuite.R;

import static ICS.Weberror.cWeberror.FIREBASE_ACTIVITY;
import static ICS.Weberror.cWeberror.FIREBASE_DEVICE;
import static ICS.Weberror.cWeberror.FIREBASE_ISRESULT;
import static ICS.Weberror.cWeberror.FIREBASE_ISSUCCESS;
import static ICS.Weberror.cWeberror.FIREBASE_ITEMNAME;
import static ICS.Weberror.cWeberror.FIREBASE_METHOD;
import static ICS.Weberror.cWeberror.FIREBASE_PARAMETERS;
import static ICS.Weberror.cWeberror.FIREBASE_TIMESTAMP;
import static ICS.Weberror.cWeberror.FIREBASE_URL;

public class MenuActivity extends AppCompatActivity implements iICSDefaultActivity {
    static final String ACTIVITYNAME = "MenuActivity";

    private Context thisContext;
    private Activity thisActivity;

    cWeberrorViewModel weberrorViewModel;
    cAuthorisationViewModel authorisationViewModel;
    cWorkplaceViewModel workplaceViewModel;
    private RecyclerView recyclerViewMenu;
    cAuthorisationAdapter authorisationAdapter;

    android.support.v4.app.FragmentManager fragmentManager;

    ShimmerFrameLayout shimmerViewContainer;

    ImageView toolbarImage;
    TextView toolbarTitle;
    ImageView toolbarImageHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        thisContext = this;
        thisActivity = this;

        fragmentManager  = getSupportFragmentManager();
        weberrorViewModel = ViewModelProviders.of(this).get(cWeberrorViewModel.class);
        workplaceViewModel = ViewModelProviders.of(this).get(cWorkplaceViewModel.class);

        mActivityInitialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerViewContainer.startShimmerAnimation();
        cPublicDefinitions.CURRENT_ACTIVITY = ACTIVITYNAME;
    }

    @Override
    protected void onPause() {
        shimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

    @Override
    public void mActivityInitialize() {
        mSetAppExtensions();

        mFindViews();

        mSetViewModels();

        mSetSettings();

        mSetToolbar(getResources().getString(R.string.screentitle_menu));

        mFieldsInitialize();

        mSetListeners();

        mInitScreen();
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
        toolbarImage = findViewById(R.id.toolbarImage);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarImageHelp = findViewById(R.id.toolbarImageHelp);
        recyclerViewMenu = findViewById(R.id.recyclerViewMenu);
        shimmerViewContainer = findViewById(R.id.shimmerViewContainer);
    }

    @Override
    public void mSetViewModels() {

    }

    @Override
    public void mSetSettings() {

    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        toolbarImageHelp.setVisibility(View.INVISIBLE);
        toolbarTitle.setText(pvScreenTitle);
        toolbarImage.setImageResource(R.drawable.ic_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {
        String currentUser = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_USER, "");
        String currentBranch = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_BRANCH, "");

        workplaceViewModel.getWorkplaces(true, currentBranch).observe(this, new Observer<List<cWorkplaceEntity>>() {
            @Override
            public void onChanged(@Nullable List<cWorkplaceEntity> workplaceEntities) {
                if (workplaceEntities != null) {
                    //cUserInterface.showToastMessage(thisContext, Integer.toString(workplaceEntities.size()), null);
                }
            }
        });

        authorisationViewModel = ViewModelProviders.of(this).get(cAuthorisationViewModel.class);

        authorisationViewModel.getAuthorisations(true, currentUser, currentBranch).observe(this, new Observer<List<cAuthorisationEntity>>() {
            @Override
            public void onChanged(@Nullable final List<cAuthorisationEntity> authorisationsEntities) {
                //Stopping Shimmer Effect's animation after data is loaded
                shimmerViewContainer.stopShimmerAnimation();
                shimmerViewContainer.setVisibility(View.GONE);
                if (authorisationsEntities != null) {
                    m_setAuthorisationRecycler(authorisationsEntities);
                }
            }
        });

    }

    @Override
    public void mSetListeners() {
        mSetWeberrorOberver();
    }

    @Override
    public void mInitScreen() {

    }
    private void mSetWeberrorOberver() {
        final FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        weberrorViewModel.getAllForActivityLive(ACTIVITYNAME).observe(this, new Observer<List<cWeberrorEntity>>() {
            @Override
            public void onChanged(@Nullable List<cWeberrorEntity> cWeberrorEntities) {
                if (cWeberrorEntities != null && cWeberrorEntities.size() > 0) {
                    boolean isSuccess = true;
                    for (cWeberrorEntity weberrorEntity : cWeberrorEntities) {
                        //send to Firebase
                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, FIREBASE_ITEMNAME);
                        bundle.putString(FIREBASE_ISSUCCESS, weberrorEntity.getIssucess().toString());
                        bundle.putString(FIREBASE_ISRESULT, weberrorEntity.getIsresult().toString());
                        bundle.putString(FIREBASE_ACTIVITY, weberrorEntity.getActivity());
                        bundle.putString(FIREBASE_DEVICE, weberrorEntity.getDevice());
                        bundle.putString(FIREBASE_PARAMETERS, weberrorEntity.getParameters());
                        bundle.putString(FIREBASE_METHOD, weberrorEntity.getWebmethod());
                        bundle.putString(FIREBASE_TIMESTAMP, weberrorEntity.getDatetime());
                        bundle.putString(FIREBASE_URL, cWebservice.WEBSERVICE_URL);
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                        if (!weberrorEntity.getIssucess()) {
                            isSuccess = false;
                        }
                    }
                    if (!isSuccess) {
                        cUserInterface.doWebserviceError(cWeberrorEntities, false, false );
                    }
                }
                //all right, handled.
                weberrorViewModel.deleteAll();
            }
        });
    }
    private void m_setAuthorisationRecycler(List<cAuthorisationEntity> authorisationEntities) {
        authorisationAdapter = new cAuthorisationAdapter(thisContext);
        recyclerViewMenu.setHasFixedSize(false);
        recyclerViewMenu.setAdapter(authorisationAdapter);
        recyclerViewMenu.setLayoutManager(new LinearLayoutManager(this));

        authorisationAdapter.setAuthorisations(authorisationEntities);
    }
    public void setChosenAuthorization(cAuthorisationEntity pv_AuthorisationEntity) {
        String l_AuthorizationStr = pv_AuthorisationEntity.getAuthorisationStr();
        ViewGroup container = findViewById(R.id.container);
        if (l_AuthorizationStr.equalsIgnoreCase(cAuthorisation.g_eAuthorisation.INVENTORY.toString())) {
            Intent intent = new Intent(thisContext, InventoryActivity.class);
            View clickedImage = container.findViewWithTag(cAuthorisation.TAG_IMAGE_INVENTORY);
            View clickedText = container.findViewWithTag(cAuthorisation.TAG_TEXT_INVENTORY);
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(thisActivity, new Pair<>(clickedImage, InventoryActivity.VIEW_NAME_HEADER_IMAGE), new Pair<>(clickedText, InventoryActivity.VIEW_NAME_HEADER_TEXT));
            ActivityCompat.startActivity(thisContext,intent, activityOptions.toBundle());
        }
        else if (l_AuthorizationStr.equalsIgnoreCase(cAuthorisation.g_eAuthorisation.PICK.toString())) {
            Intent intent = new Intent(thisContext, PickorderSelectActivity.class);
            View clickedImage = container.findViewWithTag(cAuthorisation.TAG_IMAGE_PICK);
            View clickedText = container.findViewWithTag(cAuthorisation.TAG_TEXT_PICK);
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(thisActivity, new Pair<>(clickedImage, PickorderSelectActivity.VIEW_NAME_HEADER_IMAGE), new Pair<>(clickedText, PickorderSelectActivity.VIEW_NAME_HEADER_TEXT));
            ActivityCompat.startActivity(thisContext,intent, activityOptions.toBundle());
        }
        else if (l_AuthorizationStr.equalsIgnoreCase(cAuthorisation.g_eAuthorisation.SORTING.toString())) {
            Intent intent = new Intent(thisContext, SortorderSelectActivity.class);
            View clickedImage = container.findViewWithTag(cAuthorisation.TAG_IMAGE_SORT);
            View clickedText = container.findViewWithTag(cAuthorisation.TAG_TEXT_SORT);
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(thisActivity, new Pair<>(clickedImage, SortorderSelectActivity.VIEW_NAME_HEADER_IMAGE), new Pair<>(clickedText, SortorderSelectActivity.VIEW_NAME_HEADER_TEXT));
            ActivityCompat.startActivity(thisContext,intent, activityOptions.toBundle());
        }
        else if (l_AuthorizationStr.equalsIgnoreCase(cAuthorisation.g_eAuthorisation.SHIPPING.toString())) {
            Intent intent = new Intent(thisContext, ShiporderSelectActivity.class);
            View clickedImage = container.findViewWithTag(cAuthorisation.TAG_IMAGE_SHIP);
            View clickedText = container.findViewWithTag(cAuthorisation.TAG_TEXT_SHIP);
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(thisActivity, new Pair<>(clickedImage, ShiporderSelectActivity.VIEW_NAME_HEADER_IMAGE), new Pair<>(clickedText, ShiporderSelectActivity.VIEW_NAME_HEADER_TEXT));
            ActivityCompat.startActivity(thisContext,intent, activityOptions.toBundle());
        }
        else {
            cUserInterface.doNope(container, true, true);
        }

    }


}
