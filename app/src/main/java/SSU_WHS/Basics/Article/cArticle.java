package SSU_WHS.Basics.Article;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;
import SSU_WHS.Basics.ArticleStock.cArticleStock;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cArticle {

    //Region Public Properties

    public String itemNoStr;
    public String getItemNoStr() {
        return itemNoStr;
    }

    public String variantCodeStr;
    public String getVariantCodeStr() {
        return variantCodeStr;
    }

    public String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    public String description2Str;
    public String getDescription2Str() {
        return description2Str;
    }

    public String itemInfoCodeStr;
    public String getItemInfoCodeStr() { return itemInfoCodeStr; }

    public String vendorItemNoStr;
    public String getVendorItemNoStr() { return vendorItemNoStr; }

    public String vendorItemDescriptionStr;
    public String getItemDescriptionStr() { return vendorItemDescriptionStr; }

    public String component10Str;
    public String getComponent10Str() { return component10Str; }

    public Double priceDbl;
    public Double getPriceDbl() { return priceDbl; }

    public cArticleEntity articleEntity;
    public  List<cArticleBarcode> barcodesObl;
    public  List<cArticleStock> stockObl;

    public static cArticleViewModel gArticleViewModel;
    public static cArticleViewModel getArticleViewModel() {
        if (gArticleViewModel == null) {
            gArticleViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cArticleViewModel.class);
        }
        return gArticleViewModel;
    }

    public static List<cArticle> allArticles;
    public  static cArticle currentArticle;

    //End Region Public Properties

    //Region Constructor
    public cArticle(JSONObject pvJsonObject) {
        this.articleEntity = new cArticleEntity(pvJsonObject);
        this.itemNoStr = this.articleEntity.getItemnoStr();
        this.variantCodeStr =  this.articleEntity.getVariantCodeStr();
        this.descriptionStr = this.articleEntity.getDescriptionStr();
        this.description2Str = this.articleEntity.getDescription2Str();
        this.itemInfoCodeStr = this.articleEntity.getItemInfoCodeStr();
        this.vendorItemNoStr = this.articleEntity.getVendorItemNoStr();
        this.vendorItemDescriptionStr = this.articleEntity.getVendorItemDescriptionStr();
        this.component10Str = this.articleEntity.getComponent10Str();
        this.priceDbl = this.articleEntity.getPriceDbl();
    }

    //End Region Constructor

    //Region Public Methods

    public static cArticle pGetArticleByBarcodeViaWebservice(cBarcodeScan pvBarcodescan){

        cWebresult webresult;
        webresult = cArticle.getArticleViewModel().pGetArticleByBarcodeViaWebserviceWrs(pvBarcodescan);
        if (webresult.getResultBln() == true && webresult.getSuccessBln() == true ) {


            List<JSONObject> myList = webresult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);
                cArticle article = new cArticle(jsonObject);
                return  article;
            }
            return null;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETARTICLEVIAOWNERBARCODE);
            return null;
        }
    }

    public boolean pGetBarcodesViaWebserviceBln(){
        cWebresult webresult;

        webresult = cArticle.getArticleViewModel().pGetBarcodesViaWebserviceWrs(this);
        if (webresult.getResultBln() == true && webresult.getSuccessBln() == true ) {

            List<JSONObject> myList = webresult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);
                cArticleBarcode articleBarcode = new cArticleBarcode(jsonObject, this);

                if (this.barcodesObl == null) {
                    this.barcodesObl = new ArrayList<>();
                }

                this.barcodesObl.add(articleBarcode);

            }
            return true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETARTICLEBARCODES);
            return false;
        }
    }

    public boolean pGetStockViaWebserviceBln(){
        cWebresult webresult;

        webresult = cArticle.getArticleViewModel().pGetStockViaWebserviceWrs(this);
        if (webresult.getResultBln() == true && webresult.getSuccessBln() == true ) {

            List<JSONObject> myList = webresult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);
                cArticleStock articleStock = new cArticleStock(jsonObject, this);

                if (this.stockObl == null) {
                    this.stockObl = new ArrayList<>();
                }

                this.stockObl.add(articleStock);

            }
            return true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETARTICLEBARCODES);
            return false;
        }
    }


    //End Region Public Methods
}
