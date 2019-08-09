package SSU_WHS.PickorderAddresses;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.cDatabase;

    @Entity(tableName = cDatabase.TABLENAME_PICKORDERADDRESS, primaryKeys = {cDatabase.ADDRESSCODE_NAMESTR})
    public class cPickorderAddressEntity {
        @NonNull
        @ColumnInfo(name = cDatabase.ADDRESSCODE_NAMESTR)
        public String addrescode;
        @ColumnInfo(name = cDatabase.NAME_NAMESTR)
        public String name;
        @ColumnInfo(name = cDatabase.NAMEADDITION_NAMESTR)
        public String name_addition;
        @ColumnInfo(name = cDatabase.ADDRESS_NAMESTR)
        public String address;
        @ColumnInfo(name = cDatabase.ADDRESSADDITION_NAMESTR)
        public String address_addition;
        @ColumnInfo(name = cDatabase.STREET_NAMESTR)
        public String street;
        @ColumnInfo(name = cDatabase.ADDRESSNUMBER_NAMESTR)
        public String address_number;
        @ColumnInfo(name = cDatabase.ADDRESSNUMBERADDITION_NAMESTR)
        public String address_number_addition;
        @ColumnInfo(name = cDatabase.ZIPCODE_NAMESTR)
        public String zipcode;
        @ColumnInfo(name = cDatabase.CITY_NAMESTR)
        public String city;
        @ColumnInfo(name = cDatabase.COUNTRY_NAMESTR)
        public String country;

        //empty constructor
        public cPickorderAddressEntity() {

        }

        public cPickorderAddressEntity(JSONObject jsonObject) {
            try {
                addrescode = jsonObject.getString(cDatabase.ADDRESSCODE_NAMESTR);
                name = jsonObject.getString(cDatabase.NAME_NAMESTR);
                name_addition = jsonObject.getString(cDatabase.NAMEADDITION_NAMESTR);
                address = jsonObject.getString(cDatabase.ADDRESS_NAMESTR);
                address_addition = jsonObject.getString(cDatabase.ADDRESSADDITION_NAMESTR);
                street = jsonObject.getString(cDatabase.STREET_NAMESTR);
                address_number = jsonObject.getString(cDatabase.ADDRESSNUMBER_NAMESTR);
                address_number_addition = jsonObject.getString(cDatabase.ADDRESSNUMBERADDITION_NAMESTR);
                zipcode = jsonObject.getString(cDatabase.ZIPCODE_NAMESTR);
                city = jsonObject.getString(cDatabase.CITY_NAMESTR);
                country = jsonObject.getString(cDatabase.COUNTRY_NAMESTR);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @NonNull
        public String getAddrescode() {
            return addrescode;
        }

        public void setAddrescode(@NonNull String addrescode) {
            this.addrescode = addrescode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName_addition() {
            return name_addition;
        }

        public void setName_addition(String name_addition) {
            this.name_addition = name_addition;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddress_addition() {
            return address_addition;
        }

        public void setAddress_addition(String address_addition) {
            this.address_addition = address_addition;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getAddress_number() {
            return address_number;
        }

        public void setAddress_number(String address_number) {
            this.address_number = address_number;
        }

        public String getAddress_number_addition() {
            return address_number_addition;
        }

        public void setAddress_number_addition(String address_number_addition) {
            this.address_number_addition = address_number_addition;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

    }


