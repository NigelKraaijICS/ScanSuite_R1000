package SSU_WHS.Basics.ContentlabelContainer;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_CONTENTLABELCONTAINER)
public class cContentlabelContainerEntity {
    //Region Public Properties

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = cDatabase.CONTAINERSEQUENCEN_NAMESTR)
    private Long containerSequenceNoLng = 0L;
    @NonNull
    public Long getContainerSequenceNoLng() {
        return this.containerSequenceNoLng;
    }

    @ColumnInfo(name = cDatabase.QUANTITY_NAMESTR)
    private double quantityDbl;
    public double getQuantityDbl() {
        return this.quantityDbl;
    }


    //End Region Public Properies

    public cContentlabelContainerEntity(JSONObject jsonObject) {
        try {
            this.containerSequenceNoLng = cText.pStringToLongLng(jsonObject.getString(cDatabase.CONTAINERSEQUENCEN_NAMESTR));
            this.quantityDbl = 0.0;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //End Region Constructor
}
