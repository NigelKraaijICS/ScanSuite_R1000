package SSU_WHS.Picken.PickorderAddresses;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

    @Entity(tableName = cDatabase.TABLENAME_PICKORDERADDRESS, primaryKeys = {cDatabase.ADDRESCODE_NAMESTR})





    public class cPickorderAddressEntity {

        //Region Public Properties

        @NonNull
        @ColumnInfo(name = cDatabase.ADDRESCODE_NAMESTR)
        public String addrescode = "";
        public String getAddrescodeStr() {
            return addrescode;
        }

        @ColumnInfo(name = cDatabase.NAMEDUTCH_NAMESTR)
        public String name;
        public String getNameStr() {
            return name;
        }

        @ColumnInfo(name = cDatabase.NAMEADDITION_NAMESTR)
        public String nameAddition;
        public String getNameAdditionStr() {
            return nameAddition;
        }

        @ColumnInfo(name = cDatabase.ADDRESS_NAMESTR)
        public String address;
        public String getAddressStr() {
            return address;
        }

        @ColumnInfo(name = cDatabase.ADDRESSADDITION_NAMESTR)
        public String addressAddition;
        public String getAddressAdditionStr() {
            return addressAddition;
        }

        @ColumnInfo(name = cDatabase.STREET_NAMESTR)
        public String street;
        public String getStreetStr() {
            return street;
        }

        @ColumnInfo(name = cDatabase.ADDRESSNUMBER_NAMESTR)
        public String addressNumber;
        public String getAddressNumberStr() {
            return addressNumber;
        }

        @ColumnInfo(name = cDatabase.ADDRESSNUMBERADDITION_NAMESTR)
        public String addressNumberAddition;
        public String getAddressNumberAdditionStr() {
            return addressNumberAddition;
        }

        @ColumnInfo(name = cDatabase.ZIPCODE_NAMESTR)
        public String zipcode;
        public String getZipcodeStr() {
            return zipcode;
        }

        @ColumnInfo(name = cDatabase.CITYNL_NAMESTR)
        public String city;
        public String getCityStr() {
            return city;
        }

        @ColumnInfo(name = cDatabase.COUNTRYNL_NAMESTR)
        public String country;
        public String getCountryStr() {
            return country;
        }

        //End Region Public Properties

        //Region Private Properties

        //End Region Private Properties

        //Region Constructor

        public cPickorderAddressEntity() {
            //empty constructor
        }

        public cPickorderAddressEntity(JSONObject pvJsonObject) {
            try {
                this.addrescode = pvJsonObject.getString(cDatabase.ADDRESCODE_NAMESTR);
                this.name = pvJsonObject.getString(cDatabase.NAMEDUTCH_NAMESTR);
                this.nameAddition = pvJsonObject.getString(cDatabase.NAMEADDITION_NAMESTR);
                this.address = pvJsonObject.getString(cDatabase.ADDRES_NAMESTR);
                this.addressAddition = pvJsonObject.getString(cDatabase.ADDRESSADDITION_NAMESTR);
                this.street = pvJsonObject.getString(cDatabase.STREETNL_NAMESTR);
                this.addressNumber = pvJsonObject.getString(cDatabase.ADDRESSNUMBER_NAMESTR);
                this.addressNumberAddition = pvJsonObject.getString(cDatabase.ADDRESSNUMBERADDITION_NAMESTR);
                this.zipcode = pvJsonObject.getString(cDatabase.ZIPCODENL_NAMESTR);
                this.city = pvJsonObject.getString(cDatabase.CITYNL_NAMESTR);
                this.country = pvJsonObject.getString(cDatabase.COUNTRYNL_NAMESTR);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //End Region Constructor















    }


