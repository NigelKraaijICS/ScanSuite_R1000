package SSU_WHS.Basics.Authorisations;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName=cDatabase.TABLENAME_AUTHORISATIONS)

//Region Public Properties
    public class cAuthorisationEntity {
    @PrimaryKey @NonNull
    @ColumnInfo(name=cDatabase.AUTHORISATION_NAMESTR)
    public String authorisation = "";
    public String getAuthorisationStr() {return this.authorisation;}

    @ColumnInfo(name=cDatabase.SEQUENCE_NAMESTR)
    public Integer order;
    public Integer getOrderInt() {return this.order;}

    @ColumnInfo(name=cDatabase.LICENSE_NAMESTR)
    public String license;
    public String getLicenseStr() {return this.authorisation;}

    //End Region Public Properies

    //Region Constructor
        public cAuthorisationEntity() {

        }

        public cAuthorisationEntity(JSONObject jsonObject) {
            try {
                this.authorisation = jsonObject.getString(cDatabase.AUTHORISATION_NAMESTR);
                this.order = jsonObject.getInt(cDatabase.ORDERDUTCH_NAMESTR);
                this.license = jsonObject.getString(cDatabase.LICENSE_NL_NAMESTR);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    //End Region Constructor

    }

