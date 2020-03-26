package SSU_WHS.Basics.ArticleStock;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.cDatabase;

@Entity(tableName=cDatabase.TABLENAME_ARTICLESTOCK, primaryKeys = {"Location", "BinCode","ItemNo","VariantCode","Quantity"})
public class cArticleStockEntity {

    //Region Public Properties

    @NonNull
    @ColumnInfo(name="Location")
    public String location = "";
    public String getLocationStr() {return this.location;}

    @NonNull
    @ColumnInfo(name="BinCode")
    public String binCode = "";
    public String getBinCodeStr() {return this.binCode;}

    @NonNull
    @ColumnInfo(name="Itemno")
    public String itemno = "'";
    public String getItemnoStr() {return this.itemno;}

    @NonNull
    @ColumnInfo(name="Variantcode")
    public String variantcode = "";
    public String getVariantCodeStr() {return this.variantcode;}

    @ColumnInfo(name="Quantity")
    public Double quantity;
    public Double getQuantityDbl() {
        return quantity;
    }

    @ColumnInfo(name="DataTimestamp")
    private String dataTimeStamp;
    public String getDataTimeStamp() {
        return dataTimeStamp;
    }

    //End Region Public Properies

    public cArticleStockEntity(JSONObject pvJsonObject, cArticle pvArticle) {
        try {
            this.location = cUser.currentUser.currentBranch.getBranchStr();
            this.itemno = pvArticle.getItemNoStr();
            this.variantcode = pvArticle.getVariantCodeStr();
            this.binCode = pvJsonObject.getString(cDatabase.BINCODENL_NAMESTR);
            this.quantity = pvJsonObject.getDouble(cDatabase.QUANTITYAVAILABLE_NAMESTR);
            this.dataTimeStamp = pvJsonObject.getString(cDatabase.DATATIMESTAMP_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //End Region Constructor


}
