package SSU_WHS.Basics.ItemProperty;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cRegex;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cItemProperty {

    private String propertyStr;
    public String getPropertyStr() { return propertyStr; }

    private String layoutStr;
    public String getLayoutStr() { return layoutStr; }

    private String omschrijvingStr;
    public String getOmschrijvingStr() { return omschrijvingStr; }

    private Boolean isUniqueBln;
    public Boolean getUniqueBln() { return isUniqueBln; }

    private String uniquenessStr;
    public String getUniquenessStr() { return uniquenessStr; }

    private Boolean rememberValueBln;
    public Boolean getRememberValueBln() { return rememberValueBln; }

    private String valueTypeStr;
    public String getValueTypeStr() { return valueTypeStr; }

    public static Boolean itemPropertiesAvaliableBln;

    private cItemPropertyViewModel getItemPropertyViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cItemPropertyViewModel.class);
    }

    private cItemPropertyEntity itemPropertyEntity;
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
        this.getItemPropertyViewModel().insert(this.itemPropertyEntity);

        if (cItemProperty.allItemPropertiesObl == null) {
            cItemProperty.allItemPropertiesObl = new ArrayList<>();
        }
        cItemProperty.allItemPropertiesObl.add(this);
        return true;
    }

    public static boolean pTruncateTableBln(){
       cItemPropertyViewModel itemPropertyViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cItemPropertyViewModel.class);
        itemPropertyViewModel.deleteAll();
        return true;
    }

    public static boolean pGetItemPropertiesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cItemProperty.allItemPropertiesObl = null;
            cItemProperty.pTruncateTableBln();
        }

        if (cItemProperty.allItemPropertiesObl != null) {
            return  true;
        }

        cWebresult WebResult;
        cItemPropertyViewModel itemPropertyViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cItemPropertyViewModel.class);
        WebResult =  itemPropertyViewModel.pGetItemPropertyFromWebserviceWrs();


        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            for (JSONObject jsonObject : WebResult.getResultDtt()) {
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


    //End Region Public Methods

    //Region Private Methods



    //End Region Private Methods
}
