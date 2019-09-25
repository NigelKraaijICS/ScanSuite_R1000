package SSU_WHS.Basics.ItemProperty;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cRegex;
import ICS.Weberror.cWeberror;
import nl.icsvertex.scansuite.cAppExtension;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cItemProperty {

    public String propertyStr;
    public String getPropertyStr() { return propertyStr; }

    public String layoutStr;
    public String getLayoutStr() { return layoutStr; }

    public String omschrijvingStr;
    public String getOmschrijvingStr() { return omschrijvingStr; }

    public Boolean isUniqueBln;
    public Boolean getUniqueBln() { return isUniqueBln; }

    public String uniquenessStr;
    public String getUniquenessStr() { return uniquenessStr; }

    public Boolean rememberValueBln;
    public Boolean getRememberValueBln() { return rememberValueBln; }

    public String valueTypeStr;
    public String getValueTypeStr() { return valueTypeStr; }

    public boolean indatabaseBln;
    public static Boolean itemPropertiesAvaliableBln;

    public static cItemPropertyViewModel itemPropertyViewModel;
    public static cItemPropertyViewModel getItemPropertyViewModel() {
        if (itemPropertyViewModel == null) {
            itemPropertyViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity).get(cItemPropertyViewModel.class);
        }
        return itemPropertyViewModel;
    }

    public cItemPropertyEntity itemPropertyEntity;
    public  static List<cItemProperty> allItemPropertiesObl;

    //Region Constructor
    cItemProperty(JSONObject pvJsonObject) {
        this.itemPropertyEntity = new cItemPropertyEntity(pvJsonObject);
        this.propertyStr = this.itemPropertyEntity.getPropertyStr();
        this.layoutStr = this.itemPropertyEntity.getLayoutStr();
        this.omschrijvingStr = this.itemPropertyEntity.getOmschrijvingStr();
        this.isUniqueBln = this.itemPropertyEntity.getIsUniqueBln();
        this.uniquenessStr = this.itemPropertyEntity.getUniquenessStr();
        this.rememberValueBln = this.itemPropertyEntity.getRememberValueBln();
        this.valueTypeStr = this.itemPropertyEntity.getValueTypeStr();
    }

    //End Region Constructor

    //Region Public Methods
    public boolean pInsertInDatabaseBln() {
        cItemProperty.getItemPropertyViewModel().insert(this.itemPropertyEntity);
        this.indatabaseBln = true;

        if (cItemProperty.allItemPropertiesObl == null) ;
        { cItemProperty.allItemPropertiesObl = new ArrayList<>();
        }
        cItemProperty.allItemPropertiesObl.add(this);
        return true;
    }

    public static boolean pTruncateTableBln(){
        cItemProperty.getItemPropertyViewModel().deleteAll();
        return true;
    }

    public static boolean pGetItemPropertiesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln == true) {
            cItemProperty.allItemPropertiesObl = null;
            cItemProperty.pTruncateTableBln();
        }

        if (cItemProperty.allItemPropertiesObl != null) {
            return  true;
        }

        cWebresult WebResult;
        WebResult =  cItemProperty.getItemPropertyViewModel().pGetItemPropertyFromWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){

            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cItemProperty itemProperty = new cItemProperty(jsonObject);
                itemProperty.pInsertInDatabaseBln();
            }
            cItemProperty.itemPropertiesAvaliableBln = true;
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETITEMPROPERTY);
            return  false;
        }
    }

    public static cItemProperty pCheckItemProperty(String pvPropertyStr){

        if(cItemProperty.allItemPropertiesObl == null || cItemProperty.allItemPropertiesObl.size()==0 ){
            return null;
        }

        for (cItemProperty property :  cItemProperty.allItemPropertiesObl)
        {
            if(property.propertyStr.equalsIgnoreCase(pvPropertyStr) == true){
                return  property;
            }
        }
        return  null;
    }

    //End Region Public Methods

    //Region Private Methods

    private static ArrayList<cItemProperty> mGetPropertyStrObl(String pvBarcodeStr){
        if(cItemProperty.allItemPropertiesObl == null){
            return null;
        }

        ArrayList<cItemProperty> resultObl;
        resultObl = new ArrayList<>();

        for (cItemProperty itemProperty : cItemProperty.allItemPropertiesObl)
        {
            if (cRegex.pCheckRegexBln(itemProperty.getLayoutStr(),pvBarcodeStr) == true) {
                resultObl.add(itemProperty);
            }
        }
        return resultObl;
    }

    //End Region Private Methods
}
