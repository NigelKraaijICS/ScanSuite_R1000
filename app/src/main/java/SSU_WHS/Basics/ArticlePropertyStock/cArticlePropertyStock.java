package SSU_WHS.Basics.ArticlePropertyStock;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.util.Date;

import ICS.Utils.cText;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.Users.cUser;

public class cArticlePropertyStock {

    //Region Public Properties

    private String locationStr;
    public String getLocationStr() {
        return this.locationStr;
    }

    public String bincodeStr;
    public String getBincodeStr() {
        return this.bincodeStr;
    }

    public String itemNoStr;
    public String getItemNoStr() {
        return this.itemNoStr;
    }

    public String variantCodeStr;
    public String getVariantCodeStr() {
        return this.variantCodeStr;
    }

    public Double quantityDbl;
    public Double getQuantityDbl() {
        return this.quantityDbl;
    }

    private Date dataTimeStampDat;
    public Date getDataTimeStampDat() {
        return this.dataTimeStampDat;
    }

    private cArticlePropertyStockEntity articlePropertyStockEntity;


    //End Region Public Properties

    //Region Constructor
    public cArticlePropertyStock(JSONObject pvJsonObject) {
        this.articlePropertyStockEntity = new cArticlePropertyStockEntity(pvJsonObject);
        this.itemNoStr = this.articlePropertyStockEntity.getItemnoStr();
        this.variantCodeStr = this.articlePropertyStockEntity.getVariantCodeStr();
        this.locationStr = cUser.currentUser.currentBranch.getBranchStr();
        this.bincodeStr =  this.articlePropertyStockEntity.getBinCodeStr();
        this.quantityDbl =  this.articlePropertyStockEntity.getQuantityDbl();
        this.dataTimeStampDat = cText.pStringToDateStr(this.articlePropertyStockEntity.getDataTimeStamp(),"YYYY-MM-dd");
    }

    //End Region Constructor

    //Region Public Methods


    //End Region Public Methods
}