package SSU_WHS.Basics.CompositeBarcode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.CompositeBarcodeProperty.cCompositeBarcodeProperty;
import SSU_WHS.Basics.PropertyGroup.cPropertyGroupEntity;
import SSU_WHS.Basics.PropertyGroup.cPropertyGroupViewModel;
import SSU_WHS.Basics.PropertyGroupProperty.cPropertyGroupProperty;
import SSU_WHS.General.cDatabase;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cCompositeBarcode {

    private String compositeBarcodeStr;
    public String getCompositeBarcodeStr() { return compositeBarcodeStr; }

    private String compositeBarcodeTypeStr;
    public String getCompositeBarcodeTypeStr() { return compositeBarcodeTypeStr; }

    private String descriptionStr;
    public String getDescriptionStr() { return descriptionStr; }

    private String fieldSeperatorStr;
    public String getFieldSeperatorStr() { return fieldSeperatorStr; }

    private String layoutRegexStr;
    public String getLayoutRegexStr() { return layoutRegexStr; }

    private String layoutTypeStr;
    public String getLayoutTypeStr() { return layoutTypeStr; }

    public List<cCompositeBarcodeProperty> propertyObl;


    public static Boolean compositeBarcodesAvailableBln;

    private cCompositeBarcodeViewModel getCompositeBarcodeViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cCompositeBarcodeViewModel.class);
    }

    private cCompositeBarcodeEntity compositeBarcodeEntity;
    public  static List<cCompositeBarcode> allCompositeBarcodes;

    //Region Constructor
    cCompositeBarcode(JSONObject pvJsonObject) {
        this.compositeBarcodeEntity = new cCompositeBarcodeEntity(pvJsonObject);
        this.compositeBarcodeStr = this.compositeBarcodeEntity.getCompositeBarcodeStr();
        this.compositeBarcodeTypeStr = this.compositeBarcodeEntity.getCompositeBarcodeTypeStr();
        this.descriptionStr = this.compositeBarcodeEntity.getDescriptionStr();
        this.fieldSeperatorStr = this.compositeBarcodeEntity.getFieldSeperatorStr();
        this.layoutRegexStr = this.compositeBarcodeEntity.getLayoutRegexStr();
        this.layoutTypeStr = this.compositeBarcodeEntity.getLayoutTypeStr();
    }

    //End Region Constructor

    //Region Public Methods
    public boolean pInsertInDatabaseBln() {
        this.getCompositeBarcodeViewModel().insert(this.compositeBarcodeEntity);

        if (cCompositeBarcode.allCompositeBarcodes == null) {
            cCompositeBarcode.allCompositeBarcodes = new ArrayList<>();
        }
        cCompositeBarcode.allCompositeBarcodes.add(this);
        return true;
    }

    public static boolean pTruncateTableBln(){
        cCompositeBarcodeViewModel compositeBarcodeViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cCompositeBarcodeViewModel.class);
        compositeBarcodeViewModel.deleteAll();
        return true;
    }

    public static boolean pGetCompositeBarcodesViaWebserviceBln(Boolean pvRefreshBln) {


        if (pvRefreshBln) {
            cCompositeBarcode.allCompositeBarcodes = null;
            cCompositeBarcode.pTruncateTableBln();
        }

        if (cCompositeBarcode.allCompositeBarcodes != null) {
            return  true;
        }

        cWebresult WebResult;
        cCompositeBarcodeViewModel compositeBarcodeViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cCompositeBarcodeViewModel.class);
        WebResult =  compositeBarcodeViewModel.pGetCompositeBarcodesFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cCompositeBarcode compositeBarcode = new cCompositeBarcode(jsonObject);
                compositeBarcode.pInsertInDatabaseBln();

                try {
                    JSONArray propertys    = jsonObject.getJSONArray(cDatabase.PROPERTYSDUTCH_NAMESTR);
                    compositeBarcode.propertyObl = new ArrayList<>();

                    for (int i = 0, size = propertys.length(); i < size; i++)
                    {
                        JSONObject object =propertys.getJSONObject(i);
                        cCompositeBarcodeProperty compositeBarcodeProperty = new cCompositeBarcodeProperty(object);
                        compositeBarcode.propertyObl.add(compositeBarcodeProperty);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            cCompositeBarcode.compositeBarcodesAvailableBln = true;
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPROPERTYGROUPS);
            return  false;
        }
    }



    //End Region Public Methods

    //Region Private Methods



    //End Region Private Methods
}
