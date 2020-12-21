package SSU_WHS.PackAndShip.PackAndShipAddress;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_PACKANDSHIPBARCODE)
public class cPackAndShipAddressEntity {

    //Region Public Properties
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = cDatabase.ADDRESSCODE_NAMESTR)
    public String addressCodeStr = "";
    public String getAddressCodeStr() {
        return this.addressCodeStr;
    }

    @ColumnInfo(name = cDatabase.NAMEDUTCH_NAMESTR)
    public String nameStr;
    public String getNameStr() { return this.nameStr; }

    @ColumnInfo(name = cDatabase.NAMEADDITION_NAMESTR)
    public String nameAdditionStr;
    public String getNameAdditionStr() { return this.nameAdditionStr; }

    @ColumnInfo(name = cDatabase.ADDRESS_NAMESTR)
    public String addressStr;
    public String getAddressStr() { return this.addressStr; }

    @ColumnInfo(name = cDatabase.ADDRESSADDITION_NAMESTR)
    public String addressAdditionStr;
    public String getAddressAdditionStr() {
        return this.addressAdditionStr;
    }

    @ColumnInfo(name = cDatabase.STREET_NAMESTR)
    public String streetStr;
    public String getStreetStr() {
        return this.streetStr;
    }

    @ColumnInfo(name = cDatabase.ADDRESSNUMBER_NAMESTR)
    public int addressNumberInt;
    public int getAddressNumberInt() {
        return this.addressNumberInt;
    }

    @ColumnInfo(name = cDatabase.ADDRESSNUMBERADDITION_NAMESTR)
    public String addressNumberAdditionStr;
    public String getAddressNumberAdditionStr() {
        return this.addressNumberAdditionStr;
    }

    @ColumnInfo(name = cDatabase.ZIPCODE_NAMESTR)
    public String zipcodeStr;
    public String getZipcodeStr() {
        return this.zipcodeStr;
    }

    @ColumnInfo(name = cDatabase.REGION_NAMESTR)
    public String regionStr;
    public String getRegionStr() {
        return this.regionStr;
    }

    @ColumnInfo(name = cDatabase.CITY_NAMESTR)
    public String cityStr;
    public String getCityStr() {
        return this.cityStr;
    }

    @ColumnInfo(name = cDatabase.COUNTRY_NAMESTR)
    public String countryStr;
    public String getCountryStr() {
        return this.countryStr;
    }

    @ColumnInfo(name = cDatabase.EMAIL_NAMESTR)
    public String emailStr;
    public String getEmailStr() {
        return this.emailStr;
    }

    @ColumnInfo(name = cDatabase.PHONE_NAMESTR)
    public String phoneStr;
    public String getPhoneStr() {
        return this.phoneStr;
    }

    @ColumnInfo(name = cDatabase.CONTACTPERSON_NAMESTR)
    public String contactPersonStr;
    public String getContactPersonStr() {
        return this.contactPersonStr;
    }

    //End Region Public Properties

    //Region Constructor
    public cPackAndShipAddressEntity() {

    }

    public cPackAndShipAddressEntity(JSONObject pvJsonObject) {
        try {
            this.addressCodeStr = pvJsonObject.getString(cDatabase.ADDRESSCODE_NAMESTR);
            this.nameStr = pvJsonObject.getString(cDatabase.NAMEDUTCH_NAMESTR);
            this.nameAdditionStr =  pvJsonObject.getString(cDatabase.NAMEADDITION_NAMESTR);
            this.addressStr = pvJsonObject.getString(cDatabase.ADDRESS_NAMESTR);
            this.addressAdditionStr = pvJsonObject.getString(cDatabase.ADDRESSADDITION_NAMESTR);
            this.streetStr =pvJsonObject.getString(cDatabase.STREET_NAMESTR);
            this.addressNumberInt = pvJsonObject.getInt(cDatabase.ADDRESSNUMBER_NAMESTR);
            this.addressNumberAdditionStr = pvJsonObject.getString(cDatabase.ADDRESSNUMBERADDITION_NAMESTR);
            this.zipcodeStr = pvJsonObject.getString(cDatabase.ZIPCODE_NAMESTR);
            this.regionStr = pvJsonObject.getString(cDatabase.REGION_NAMESTR);
            this.cityStr = pvJsonObject.getString(cDatabase.CITY_NAMESTR);
            this.countryStr = pvJsonObject.getString(cDatabase.COUNTRY_NAMESTR);
            this.phoneStr = pvJsonObject.getString(cDatabase.PHONE_NAMESTR);
            this.contactPersonStr = pvJsonObject.getString(cDatabase.CONTACTPERSON_NAMESTR);
            } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//End Region Constructor
}