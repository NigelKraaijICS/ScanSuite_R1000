package SSU_WHS.Basics.CompositeBarcodeProperty;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.CompositeBarcode.cCompositeBarcodeEntity;
import SSU_WHS.Basics.CompositeBarcode.cCompositeBarcodeViewModel;
import SSU_WHS.Basics.PropertyGroupProperty.cPropertyGroupProperty;
import SSU_WHS.General.cDatabase;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cCompositeBarcodeProperty {

    private String commentStr;
    public String getCommentStr() { return commentStr; }

    private String fieldStr;
    public String getFieldStr() { return fieldStr; }

    private String fieldIdentifierStr;
    public String getFieldIdentifierStr() { return fieldIdentifierStr; }

    private String fieldTypeStr;
    public String getFieldTypeStr() { return fieldTypeStr; }

    private int positionStartInt;
    public int getPositionStartInt() { return positionStartInt; }

    private int positionEndInt;
    public int getPositionEndInt() { return positionEndInt; }

    private int sortingSequenceNoInt;
    public int getSortingSequenceNoInt() { return sortingSequenceNoInt; }

    private String stripThisStr;
    public String getStripThisStr() { return stripThisStr; }

    private cCompositeBarcodePropertyViewModel getCompositeBarcodePropertyViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cCompositeBarcodePropertyViewModel.class);
    }

    private cCompositeBarcodePropertyEntity compositeBarcodePropertyEntity;
    public static List<cCompositeBarcodeProperty> allCompositeBarcodesPropertys;

    //Region Constructor
   public cCompositeBarcodeProperty(JSONObject pvJsonObject) {
        this.compositeBarcodePropertyEntity = new cCompositeBarcodePropertyEntity(pvJsonObject);
        this.commentStr = this.compositeBarcodePropertyEntity.getCommentStr();
        this.fieldStr = this.compositeBarcodePropertyEntity.getFieldStr();
        this.fieldIdentifierStr = this.compositeBarcodePropertyEntity.getFieldIdentifierStr();
        this.positionStartInt  = this.compositeBarcodePropertyEntity.getPositionStartInt();
        this.positionEndInt = this.compositeBarcodePropertyEntity.getPositionEndInt();
        this.sortingSequenceNoInt = this.compositeBarcodePropertyEntity.getSortingSequenceNoInt();
        this.stripThisStr = this.compositeBarcodePropertyEntity.getStripThisStr();
    }

    //End Region Constructor

    //Region Public Methods
    public boolean pInsertInDatabaseBln() {
        this.getCompositeBarcodePropertyViewModel().insert(this.compositeBarcodePropertyEntity);

        if (cCompositeBarcodeProperty.allCompositeBarcodesPropertys == null) {
            cCompositeBarcodeProperty.allCompositeBarcodesPropertys = new ArrayList<>();
        }
        cCompositeBarcodeProperty.allCompositeBarcodesPropertys.add(this);
        return true;
    }

    public static boolean pTruncateTableBln(){
        cCompositeBarcodePropertyViewModel compositeBarcodePropertyViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cCompositeBarcodePropertyViewModel.class);
        compositeBarcodePropertyViewModel.deleteAll();
        return true;
    }

    //End Region Public Methods

    //Region Private Methods

    //End Region Private Methods
}
