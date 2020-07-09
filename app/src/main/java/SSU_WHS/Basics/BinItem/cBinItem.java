package SSU_WHS.Basics.BinItem;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.ArticleStock.cArticleStock;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cBinItem {

    //region Public Properties

    private  String binCodeStr;
    public String getBinCodeStr() {
        return binCodeStr;
    }

    private String itemNoStr;
    public String getItemNoStr() {
        return itemNoStr;
    }

    private String variantCodeStr;
    public String getVariantCodeStr() {
        return variantCodeStr;
    }

    private boolean stockAvailaibleBln = false;

    private double stockDbl;
    public double getStockDbl() {

        if (stockAvailaibleBln) {
            return  stockDbl;
        }

        cArticle article = new cArticle(this.getItemNoStr(),this.getVariantCodeStr());


        cArticleStock articleStock =  article.pGetStockForBINViaWebservice(this.getBinCodeStr());
        if (articleStock == null) {
            stockAvailaibleBln = true;
            return  0;
        }

        stockAvailaibleBln = true;
        return articleStock.quantityDbl;
    }


    public  static cBinItem currentBinIteme;
    public  static List<cBinItem> allBinItemsObl;


    //end region Public Propties

     //Region Constructor
     private cBinItem(JSONObject pvJsonObject, String pvBinCodeStr) {
         cBinItemEntity binItemEntity = new cBinItemEntity(pvJsonObject);
        this.binCodeStr = pvBinCodeStr;
        this.itemNoStr = binItemEntity.getItemNoStr();
        this.variantCodeStr = binItemEntity.getVariantCodeStr();
    }
    //End Region Constructor

    //Region Public Methods



    public static boolean pGetBinItemsViaWebserviceBln(String pvBinCodeStr) {

        cWebresult WebResult;

        cBinItemViewModel binItemViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cBinItemViewModel.class);

        WebResult =  binItemViewModel.pBinItemsFromWebserviceWrs(pvBinCodeStr);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            cBinItem.allBinItemsObl = new ArrayList<>();

            for ( JSONObject jsonObject :  WebResult.getResultDtt()) {
                cBinItem binItem = new cBinItem(jsonObject, pvBinCodeStr);
                allBinItemsObl.add(binItem);
            }

            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETBINARTICLES);
            return  false;
        }
    }

    //End Region Public Methods
}
