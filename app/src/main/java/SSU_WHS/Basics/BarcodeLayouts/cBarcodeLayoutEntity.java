package SSU_WHS.Basics.BarcodeLayouts;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_BARCODELAYOUTS, primaryKeys = {cDatabase.BARCODELAYOUT_NAMESTR, cDatabase.LAYOUT_NAMESTR})
public class cBarcodeLayoutEntity {

    //Region Public Properties
    @NonNull
    @ColumnInfo(name = cDatabase.BARCODELAYOUT_NAMESTR)
    public String barcodelayout = "'";
    public String getBarcodelayoutStr() {return barcodelayout;}

    @NonNull
    @ColumnInfo(name = cDatabase.LAYOUT_NAMESTR)
    public String layoutvalue = "";
    public String getLayoutValueStr() {return layoutvalue;}

    //End Region Public Properies

    public  cBarcodeLayoutEntity(){

    }

    public cBarcodeLayoutEntity(JSONObject jsonObject) {
        try {
            this.barcodelayout = jsonObject.getString(cDatabase.BARCODELAYOUT_NAMESTR);
            this.layoutvalue = jsonObject.getString(cDatabase.LAYOUT_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //End Region Constructor
}


