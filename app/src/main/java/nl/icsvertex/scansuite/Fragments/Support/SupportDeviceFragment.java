package nl.icsvertex.scansuite.Fragments.Support;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.R;

public class SupportDeviceFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private TextView textViewDeviceManufacturer;
    private  TextView textViewDeviceBrand;
    private TextView textViewDeviceModel;
    private  TextView textViewSerialnumber;
    private TextView textViewAndroidversion;
    private TextView textViewBatterypercent;
    private TextView textViewBatteryCharging;
    private TextView textViewBatterypercentOverImage;
    private ImageView imageViewBattery;
    private ImageView imageIsCharging;
    private TextView textChargeState;
    private ImageView imageViewBatteryRefresh;
    private  int batteryChargePercent;
    private  AnimationDrawable batteryAnimation;

    //End Region Private Properties

    //Region Constructor

    public SupportDeviceFragment() {

    }

    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_support_device, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cAppExtension.dialogFragment = this;
       this.mFragmentInitialize();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                cAppExtension.dialogFragment.dismiss();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //End Region Default Methods

    //Region iICSDefaultFragment defaults


    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        cUserInterface.pEnableScanner();
    }

    @Override
    public void mFindViews() {
        this.textViewDeviceManufacturer = getView().findViewById(R.id.textViewDeviceManufacturer);
        this.textViewDeviceBrand = getView().findViewById(R.id.textViewDeviceBrand);
        this.textViewDeviceModel = getView().findViewById(R.id.textViewDeviceModel);
        this.textViewSerialnumber = getView().findViewById(R.id.textViewSerialnumber);
        this.textViewAndroidversion = getView().findViewById(R.id.textViewAndroidversion);
        this.textViewBatterypercent = getView().findViewById(R.id.textViewBatterypercent);
        this.textViewBatteryCharging = getView().findViewById(R.id.textViewBatteryCharging);
        this.textViewBatterypercentOverImage = getView().findViewById(R.id.textViewBatterypercentOverImage);
        this.imageViewBattery = getView().findViewById(R.id.imageViewBattery);
        this.imageIsCharging = getView().findViewById(R.id.imageIsCharging);
        this.textChargeState = getView().findViewById(R.id.textChargeState);
        this.imageViewBatteryRefresh = getView().findViewById(R.id.imageViewBatteryRefresh);
    }

    @Override
    public void mFieldsInitialize() {
        this.textViewDeviceManufacturer.setText(cDeviceInfo.getDeviceManufacturer());
        this.textViewDeviceBrand.setText(cDeviceInfo.getDeviceBrand());
        this.textViewDeviceModel.setText(cDeviceInfo.getDeviceModel());
        this.textViewSerialnumber.setText(cDeviceInfo.getSerialnumberStr());
        this.textViewAndroidversion.setText(cDeviceInfo.getAndroidBuildVersion() + " - " + cDeviceInfo.getFriendlyVersionName());
        this.mSetBatteryInfo();
    }

    @Override
    public void mSetListeners() {
        this.mSetBatteryRefreshListener();
    }

    //End Region iICSDefaultFragment defaults

    //Region Private Methods

    private void mSetBatteryRefreshListener() {
        this.imageViewBatteryRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cUserInterface.pDoRotate(imageViewBatteryRefresh,0);
                mSetBatteryInfo();
            }
        });
    }

    private void mSetBatteryInfo() {
        this.batteryChargePercent = cDeviceInfo.getBatteryChargePct();
        this.textChargeState.setText(cDeviceInfo.getChargingStatusString());
        this.textViewBatteryCharging.setText(cDeviceInfo.getChargingStatusString());
        this.textViewBatteryCharging.setVisibility(View.GONE);
        this.textViewBatterypercent.setText(getString(R.string.battery_at_parameter1_percent, cText.pIntToStringStr(cDeviceInfo.getBatteryChargePct())));
        this.textViewBatterypercent.setVisibility(View.GONE);
        this.textViewBatterypercentOverImage.setText( cDeviceInfo.getBatteryChargePct() + "%");
        this.mSetBatteryIcon();
    }

    private void mSetBatteryIcon() {

        if (cDeviceInfo.isCharging()) {
            this.imageIsCharging.setVisibility(View.VISIBLE);
            this.mAnimateCharging();
            return;
        }

        this.imageIsCharging.setVisibility(View.INVISIBLE);
        if (cDeviceInfo.getBatteryChargePct() < 40) {
            this.imageViewBattery.setImageResource(R.drawable.ic_battery25);
            return;
        }

        if (cDeviceInfo.getBatteryChargePct() < 65) {
            this.imageViewBattery.setImageResource(R.drawable.ic_battery50);
            return;
        }
        if (cDeviceInfo.getBatteryChargePct() < 90) {
            this.imageViewBattery.setImageResource(R.drawable.ic_battery75);
            return;
        }

        this.imageViewBattery.setImageResource(R.drawable.ic_battery100);
    }

    private void mAnimateCharging() {
        this.imageViewBattery.setImageResource(R.drawable.battery_charging);
        this.batteryAnimation = (AnimationDrawable) imageViewBattery.getDrawable();
        this.batteryAnimation.start();
    }


    //End Region private Methods



}
