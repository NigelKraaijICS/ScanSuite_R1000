package SSU_WHS.Basics.ArticlePropertyStock;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_ARTICLEPROPERTYSTOCK, primaryKeys = {"Location", "BinCode","ItemNo","VariantCode","Quantity"})
public class cArticlePropertyStockEntity {

    //Region Public Properties

    @NonNull
    @ColumnInfo(name= cDatabase.WAREHOUSELOCATION_NAMESTR )
    public String location = "";
    public String getLocationStr() {return this.location;}

    @NonNull
    @ColumnInfo(name= cDatabase.BINCODE_NAMESTR)
    public String binCode = "";
    public String getBinCodeStr() {return this.binCode;}

    @NonNull
    @ColumnInfo(name= cDatabase.ITEMNO_NAMESTR)
    public String itemno = "'";
    public String getItemnoStr() {return this.itemno;}

    @NonNull
    @ColumnInfo(name= cDatabase.VARIANTCODE_NAMESTR)
    public String variantcode = "";
    public String getVariantCodeStr() {return this.variantcode;}

    @ColumnInfo(name= cDatabase.QUANTITY_NAMESTR)
    public Double quantity;
    public Double getQuantityDbl() {
        return quantity;
    }

    @ColumnInfo(name = cDatabase.PROPERTYCODE_NAMESTR )
    public String propertyCodeStr;
    public String getPropertyCodeStr() {return this.propertyCodeStr;}

    @ColumnInfo(name = cDatabase.VALUE_NAMESTR )
    public String valueStr;
    public String getValueStr() {return this.valueStr;}

    @ColumnInfo(name= cDatabase.DATATIMESTAMP_NAMESTR)
    private String dataTimeStamp;
    public String getDataTimeStamp() {
        return dataTimeStamp;
    }

    //End Region Public Properies

    public cArticlePropertyStockEntity(JSONObject pvJsonObject) {
        try {
            this.location = cUser.currentUser.currentBranch.getBranchStr();
            this.itemno = pvJsonObject.getString(cDatabase.ITEMNO_NAMESTR);
            this.variantcode = pvJsonObject.getString(cDatabase.VARIANTCODE_NAMESTR);
            this.binCode = pvJsonObject.getString(cDatabase.BINCODENL_NAMESTR);
            this.quantity = pvJsonObject.getDouble(cDatabase.QUANTITYAVAILABLE_NAMESTR);
            this.dataTimeStamp = pvJsonObject.getString(cDatabase.DATATIMESTAMP_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //End Region Constructor


}
