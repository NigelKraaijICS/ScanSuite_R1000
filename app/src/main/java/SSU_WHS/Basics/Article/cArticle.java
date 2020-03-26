package SSU_WHS.Basics.Article;

import androidx.lifecycle.ViewModelProvider;

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

    private String itemInfoCodeStr;
    public String getItemInfoCodeStr() { return itemInfoCodeStr; }

    public String vendorItemNoStr;
    public String getVendorItemNoStr() { return vendorItemNoStr; }

    public String vendorItemDescriptionStr;
    public String getItemDescriptionStr() { return vendorItemDescriptionStr; }

    private String component10Str;
    public String getComponent10Str() { return component10Str; }

    private Double priceDbl;
    public Double getPriceDbl() { return priceDbl; }

    private cArticleEntity articleEntity;
    public  List<cArticleBarcode> barcodesObl;
    public  List<cArticleStock> stockObl;

    private cArticleViewModel getArticleViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cArticleViewModel.class);
    }

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

        cArticleViewModel articleViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cArticleViewModel.class);
        webresult = articleViewModel.pGetArticleByBarcodeViaWebserviceWrs(pvBarcodescan);
        if (webresult.getResultBln() && webresult.getSuccessBln()) {
            for (JSONObject jsonObject :  webresult.getResultDtt()) {
                return new cArticle(jsonObject);
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

        webresult = getArticleViewModel().pGetBarcodesViaWebserviceWrs(this);
        if (webresult.getResultBln() && webresult.getSuccessBln()) {

            for (JSONObject jsonObject :  webresult.getResultDtt()) {
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

    public void pGetStockViaWebserviceBln(){
        cWebresult webresult;

        webresult = getArticleViewModel().pGetStockViaWebserviceWrs(this);
        if (webresult.getResultBln() && webresult.getSuccessBln()) {

            for (JSONObject jsonObject :  webresult.getResultDtt()) {
                cArticleStock articleStock = new cArticleStock(jsonObject, this);

                if (this.stockObl == null) {
                    this.stockObl = new ArrayList<>();
                }

                this.stockObl.add(articleStock);

            }
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETARTICLEBARCODES);
        }
    }


    //End Region Public Methods
}
