package SSU_WHS.Picken.PickorderAddresses;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

    @Entity(tableName = cDatabase.TABLENAME_PICKORDERADDRESS, primaryKeys = {cDatabase.ADDRESSCODE_NAMESTR})
    public class cPickorderAddressEntity {
        @NonNull
        @ColumnInfo(name = cDatabase.ADDRESSCODE_NAMESTR)
        public String addrescode;
        public String getAddrescodeStr() {
            return addrescode;
        }
        @ColumnInfo(name = cDatabase.NAME_NAMESTR)
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
        @ColumnInfo(name = cDatabase.CITY_NAMESTR)
        public String city;
        public String getCityStr() {
            return city;
        }
        @ColumnInfo(name = cDatabase.COUNTRY_NAMESTR)
        public String country;
        public String getCountryStr() {
            return country;
        }

        //empty constructor
        public cPickorderAddressEntity() {

        }

        public cPickorderAddressEntity(JSONObject jsonObject) {
            try {
                addrescode = jsonObject.getString(cDatabase.ADDRESSCODE_NAMESTR);
                name = jsonObject.getString(cDatabase.NAME_NAMESTR);
                nameAddition = jsonObject.getString(cDatabase.NAMEADDITION_NAMESTR);
                address = jsonObject.getString(cDatabase.ADDRESS_NAMESTR);
                addressAddition = jsonObject.getString(cDatabase.ADDRESSADDITION_NAMESTR);
                street = jsonObject.getString(cDatabase.STREET_NAMESTR);
                addressNumber = jsonObject.getString(cDatabase.ADDRESSNUMBER_NAMESTR);
                addressNumberAddition = jsonObject.getString(cDatabase.ADDRESSNUMBERADDITION_NAMESTR);
                zipcode = jsonObject.getString(cDatabase.ZIPCODE_NAMESTR);
                city = jsonObject.getString(cDatabase.CITY_NAMESTR);
                country = jsonObject.getString(cDatabase.COUNTRY_NAMESTR);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


