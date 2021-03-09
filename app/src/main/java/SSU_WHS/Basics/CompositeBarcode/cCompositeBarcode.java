package SSU_WHS.Basics.CompositeBarcode;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import ICS.Utils.Scanning.gs1utils.ApplicationIdentifier;
import ICS.Utils.Scanning.gs1utils.ElementStrings;
import ICS.Utils.Scanning.gs1utils.cGS1Definitions;
import ICS.Utils.cText;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.CompositeBarcodeProperty.cCompositeBarcodeProperty;
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

    public enum CompositeLayoutType {
        Unkown,
        GS1,
        SEPARATED,
        PREFIX,
        FIXEDLENGTH
    }

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
                    JSONArray propertysObl  = jsonObject.getJSONArray(cDatabase.PROPERTYSDUTCH_NAMESTR);
                    compositeBarcode.propertyObl = new ArrayList<>();

                    for (int i = 0, size = propertysObl.length(); i < size; i++)
                    {
                        JSONObject object =propertysObl.getJSONObject(i);
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

    public  static cCompositeBarcode pGetCompositeBarcodeByType(String pvCompositeBarcodeStr) {

        if (cCompositeBarcode.allCompositeBarcodes == null || cCompositeBarcode.allCompositeBarcodes.size() == 0) {
            return  null;
        }

        for (cCompositeBarcode compositeBarcode :cCompositeBarcode.allCompositeBarcodes) {
            if (compositeBarcode.getCompositeBarcodeStr().equalsIgnoreCase(pvCompositeBarcodeStr)) {
                return  compositeBarcode;
            }
        }

        return  null;

    }

    public LinkedHashMap<String, String> pGetBarcodeKeysAndValuesObl(String pvBarcodeStr) {

        LinkedHashMap<String, String> resultObl = new LinkedHashMap<>();


            switch (this.getLayoutTypeStr().toUpperCase()) {

                case "SEPARATED":
                    resultObl = this.mSplitBarcodeBySeparator(pvBarcodeStr);
                    break;

                case "FIXEDLENGTH":
                    resultObl = this.mSplitBarcodeByPosition(pvBarcodeStr);
                    break;

                case "PREFIX":
                    resultObl = this.mSplitByPrefix(pvBarcodeStr);
                    break;

                case "GS1":
                    resultObl = this.mSplitBarcodeAsGS1(pvBarcodeStr);
                    break;

                case "Unkown":
                    resultObl = null;
                    break;
            }

            return  resultObl;

    }


    //End Region Public Methods

    //Region Private Methods

    private  LinkedHashMap<String, String> mSplitBarcodeAsGS1(String pvBarcodeStr) {

        LinkedHashMap<String, String>  resultObl  = new LinkedHashMap<>();

        if (this.propertyObl == null || this.propertyObl.size() == 0 ) {
            return resultObl;
        }

        ElementStrings.ParseResult GS1BarcodeObject = ElementStrings.parse(pvBarcodeStr);

        for (cCompositeBarcodeProperty compositeBarcodeProperty : this.propertyObl) {

            ApplicationIdentifier applicationIdentifier = cGS1Definitions.getApplicationIdentifier(cText.pStringToIntegerInt(compositeBarcodeProperty.getFieldIdentifierStr()));
            Object GS1FieldOject = GS1BarcodeObject.getObject(applicationIdentifier);

            if (GS1FieldOject == null) {
                resultObl.put(compositeBarcodeProperty.getFieldStr(),"");
                continue;
            }

            if (!(GS1FieldOject instanceof Date)) {
                resultObl.put(compositeBarcodeProperty.getFieldStr(),this.mStripStr(compositeBarcodeProperty,
                        Objects.requireNonNull(GS1FieldOject).toString()));
            }

        }
        return resultObl;
    }

    private LinkedHashMap<String, String> mSplitBarcodeBySeparator(String pvBarcodeStr) {

        LinkedHashMap<String, String>  resultObl  = new LinkedHashMap<>();

        if (this.propertyObl == null || this.propertyObl.size() == 0 ) {
            return resultObl;
        }

        String separatorStr = "[" + this.getFieldSeperatorStr() + "]";
        String[] fieldsObl =  pvBarcodeStr.split(separatorStr);


        for (cCompositeBarcodeProperty compositeBarcodeProperty : this.propertyObl) {
            int fieldnumberInt = cText.pStringToIntegerInt(compositeBarcodeProperty.getFieldIdentifierStr())  -1;

            if (fieldnumberInt >= fieldsObl.length) {
                resultObl.put(compositeBarcodeProperty.getFieldStr(),"");

            }
            else {
                resultObl.put(compositeBarcodeProperty.getFieldStr(), this.mStripStr(compositeBarcodeProperty,fieldsObl[fieldnumberInt]));
            }
        }

        return resultObl;
    }

    private LinkedHashMap<String, String> mSplitBarcodeByPosition(String pvBarcodeStr) {

        LinkedHashMap<String, String>  resultObl  = new LinkedHashMap<>();

        if (this.propertyObl == null || this.propertyObl.size() == 0 ) {
            return resultObl;
        }

        for (cCompositeBarcodeProperty compositeBarcodeProperty : this.propertyObl) {
            int startIndex = compositeBarcodeProperty.getPositionStartInt() -1;
            int endIndex = compositeBarcodeProperty.getPositionEndInt();


            if (endIndex >= pvBarcodeStr.length()) {
                endIndex = pvBarcodeStr.length();
            }
            if (startIndex -1 >= pvBarcodeStr.length()) {
                resultObl.put(compositeBarcodeProperty.getFieldStr(),"");
            }
            else
            {
                resultObl.put(compositeBarcodeProperty.getFieldStr(), this.mStripStr(compositeBarcodeProperty, pvBarcodeStr.substring(startIndex, endIndex)));
            }
        }

        return resultObl;
    }

    private LinkedHashMap<String, String> mSplitByPrefix(String pvBarcodeStr) {

        LinkedHashMap<String, String>  resultObl  = new LinkedHashMap<>();

        if (this.propertyObl == null || this.propertyObl.size() == 0 ) {
            return resultObl;
        }

        for (cCompositeBarcodeProperty compositeBarcodeProperty : this.propertyObl) {
            String codeStr = compositeBarcodeProperty.getFieldIdentifierStr();
            String firstCharacter = codeStr.substring(0,1);

            int startIndex = pvBarcodeStr.indexOf(codeStr) + codeStr.length();
            int endIndex = pvBarcodeStr.indexOf(firstCharacter, startIndex);

            if (endIndex == -1) {
                endIndex = pvBarcodeStr.length();
            }

            String value = this.mStripStr(compositeBarcodeProperty,pvBarcodeStr.substring(startIndex, endIndex));
            resultObl.put(compositeBarcodeProperty.getFieldStr(),value);
        }

        return resultObl;
    }


    private  String mStripStr(cCompositeBarcodeProperty pvCompositeBarcodeProperty, String pvDirtyStr) {

        String trimmedResult;
        String resultStr;

        trimmedResult = pvDirtyStr.trim();


        if (!pvCompositeBarcodeProperty.getStripThisStr().isEmpty()) {
            resultStr = trimmedResult.replaceAll(pvCompositeBarcodeProperty.getStripThisStr(), "");
        }
        else {
            resultStr = trimmedResult;
        }
        return resultStr;
    }

    //End Region Private Methods
}
