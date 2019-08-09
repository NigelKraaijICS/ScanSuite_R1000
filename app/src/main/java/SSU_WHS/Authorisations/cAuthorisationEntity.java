package SSU_WHS.Authorisations;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.cDatabase;

@Entity(tableName="Authorisations")
    public class cAuthorisationEntity {
    @PrimaryKey @NonNull
    @ColumnInfo(name="Authorisation")
    public String authorisation;
    @ColumnInfo(name="Order")
    public Integer order;
    @ColumnInfo(name="License")
    public String license;

    //empty constructor
        public cAuthorisationEntity() {

        }

        public cAuthorisationEntity(JSONObject jsonObject) {
            try {
                authorisation = jsonObject.getString(cDatabase.AUTHORISATION_NAMESTR);
                order = jsonObject.getInt(cDatabase.ORDER_NAMESTR);
                license = jsonObject.getString(cDatabase.LICENSE_NAMESTR);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        public String getAuthorisationStr() {return this.authorisation;}
        public Integer getOrderInt() {return this.order;}
        public String getLicenseStr() {return this.authorisation;}

        @Ignore
        public cAuthorisationEntity(String authorisation, Integer order, String license) {
            this.authorisation = authorisation;
            this.order = order;
            this.license = license;
        }
    }

