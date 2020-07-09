package SSU_WHS.Basics.ArticleStock;

import org.json.JSONObject;

import java.util.Date;

import ICS.Utils.cText;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;

public class cArticleStock implements Comparable {

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

    public  static  cArticleStock currentArticleStock;

    private cArticleStockEntity articleStockEntity;


    //End Region Public Properties

    //Region Constructor
    public cArticleStock(JSONObject pvJsonObject, cArticle pvArticle) {
        this.articleStockEntity = new cArticleStockEntity(pvJsonObject, pvArticle);
        this.itemNoStr = this.articleStockEntity.getItemnoStr();
        this.variantCodeStr = this.articleStockEntity.getVariantCodeStr();
        this.locationStr = cUser.currentUser.currentBranch.getBranchStr();
        this.bincodeStr =  this.articleStockEntity.getBinCodeStr();
        this.quantityDbl =  this.articleStockEntity.getQuantityDbl();
        this.dataTimeStampDat = cText.pStringToDateStr(this.articleStockEntity.getDataTimeStamp(),"YYYY-MM-dd");
    }

    //End Region Constructor

    //Region Public Methods

    @Override
    public int compareTo(Object o) {

        int compareint =  ((cArticleStock)o).getQuantityDbl().intValue();
        return compareint -this.getQuantityDbl().intValue();

    }

    //End Region Public Methods
}
