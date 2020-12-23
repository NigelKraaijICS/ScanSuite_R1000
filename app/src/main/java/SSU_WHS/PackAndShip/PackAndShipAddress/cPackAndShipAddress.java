package SSU_WHS.PackAndShip.PackAndShipAddress;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.cAppExtension;

public class cPackAndShipAddress {

    private cPackAndShipAddressEntity packAndShipAddressEntity;

    public static List<cPackAndShipAddress> allAddressesObl;

    //Region Public Properties

    public String addressCodeStr;
    public String getAddressCodeStr() {
        return this.addressCodeStr;
    }

    public String addressNameStr;
    public String getAddressNameStr() {
        return this.addressNameStr;
    }

    public String addressTypeStr;
    public String getAddressTypeStr() {
        return this.addressTypeStr;
    }

    public String nameAdditionStr;
    public String getNameAdditionStr() {
        return this.nameAdditionStr;
    }

    public String addressStr;
    public String getAddressStr() {

        if (!this.addressStr.isEmpty()) {
            return this.addressStr;
        }
        else
        {
            return  this.getStreetStr() + " " + this.getAddressNumberInt() + " " + this.getAddressNumberAdditionStr();
        }
    }

    public String addressAdditionStr;
    public String getAddressAdditionStr() {
        return this.addressAdditionStr;
    }

    public String streetStr;
    public String getStreetStr() {
        return this.streetStr;
    }

    public int addressNumberInt;
    public int getAddressNumberInt() {
        return this.addressNumberInt;
    }

    public String addressNumberAdditionStr;
    public String getAddressNumberAdditionStr() {
        return this.addressNumberAdditionStr;
    }

    public String zipcodeStr;
    public String getZipcodeStr() {
        return this.zipcodeStr;
    }

    public String regionStr;
    public String getRegionStr() {
        return this.regionStr;
    }

    public String cityStr;
    public String getCityStr() {
        return this.cityStr;
    }

    public String countryStr;
    public String getCountryStr() {
        return this.countryStr;
    }

    public String emailStr;
    public String getEmailStr() {
        return this.emailStr;
    }

    public String phoneStr;
    public String getPhoneStr() {
        return this.phoneStr;
    }

    public String contactPersonStr;
    public String getContactPersonStr() {
        return this.contactPersonStr;
    }


    private cPackAndShipAddressViewModel getPackAndShipAddressViewModel () {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPackAndShipAddressViewModel.class);
    }

    //Region Constructor
    public cPackAndShipAddress(JSONObject pvJsonObject,  boolean pvViaDocumentBln) {
        this.packAndShipAddressEntity = new cPackAndShipAddressEntity(pvJsonObject, pvViaDocumentBln);
        this.addressCodeStr = this.packAndShipAddressEntity.getAddressCodeStr();
        this.addressNameStr = this.packAndShipAddressEntity.getAddressNameStr();
        this.addressTypeStr =  this.packAndShipAddressEntity.getAddressTypeStr();
        this.nameAdditionStr = this.packAndShipAddressEntity.getNameAdditionStr();
        this.addressStr = this.packAndShipAddressEntity.getAddressStr();
        this.addressAdditionStr = this.packAndShipAddressEntity.getAddressAdditionStr();
        this.streetStr = this.packAndShipAddressEntity.getStreetStr();
        this.addressNumberInt = this.packAndShipAddressEntity.getAddressNumberInt();
        this.addressNumberAdditionStr = this.packAndShipAddressEntity.getAddressNumberAdditionStr();
        this.zipcodeStr = this.packAndShipAddressEntity.getZipcodeStr();
        this.regionStr = this.packAndShipAddressEntity.getRegionStr();
        this.cityStr = this.packAndShipAddressEntity.getCityStr();
        this.countryStr = this.packAndShipAddressEntity.getCountryStr();
        this.emailStr = this.packAndShipAddressEntity.getEmailStr();
        this.phoneStr = this.packAndShipAddressEntity.getPhoneStr();
        this.contactPersonStr = this.packAndShipAddressEntity.getContactPersonStr();
    }

    //End Region Constructor


    public static boolean pTruncateTableBln(){

        cPackAndShipAddressViewModel packAndShipAddressViewModel=   new ViewModelProvider(cAppExtension.fragmentActivity).get(cPackAndShipAddressViewModel.class);
        packAndShipAddressViewModel.deleteAll();
        return true;
    }

    public boolean pInsertInDatabaseBln() {
        this.getPackAndShipAddressViewModel().insert(this.packAndShipAddressEntity);

        if (cPackAndShipAddress.allAddressesObl == null){
            cPackAndShipAddress.allAddressesObl = new ArrayList<>();
        }
        cPackAndShipAddress.allAddressesObl.add(this);
        return  true;
    }

}