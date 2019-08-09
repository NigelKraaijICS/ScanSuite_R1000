package SSU_WHS.BarcodeLayouts;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_BARCODELAYOUTS, primaryKeys = {"BarcodeLayout", "LayoutValue"})
public class cBarcodeLayoutEntity {
    @NonNull
    @ColumnInfo(name = "BarcodeLayout")
    public String barcodelayout;
    @NonNull
    @ColumnInfo(name = "LayoutValue")
    public String layoutvalue;
    @ColumnInfo(name = "SortOrder")
    public String sortorder;
    @ColumnInfo(name = "ImportFile")
    public String importfile;

    //empty constructor
    public cBarcodeLayoutEntity() {

    }
    public String getBarcodelayout() {return barcodelayout;}
    public cBarcodeLayoutEntity(JSONObject jsonObject) {
        try {
            barcodelayout = jsonObject.getString(cDatabase.BARCODELAYOUT_NAMESTR);
            layoutvalue = jsonObject.getString(cDatabase.LAYOUTVALUE_NAMESTR);
            sortorder = jsonObject.getString(cDatabase.SORTORDER_NAMESTR);
            importfile = jsonObject.getString(cDatabase.IMPORTFILE_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void setBarcodelayout(String pv_barcodelayout) {this.barcodelayout = pv_barcodelayout;}
    public String getLayoutValue() {return layoutvalue;}
    public void setLayoutValue(String pv_layoutvalue) {this.layoutvalue = pv_layoutvalue;}
    public String getSortOrder() {return sortorder;}
    public void setSortOrder(String pv_sortorder) {this.sortorder = pv_sortorder;}
    public String getImportfile() {return importfile;}
    public void setImportfile(String pv_importfile) {this.importfile = pv_importfile;}
}


