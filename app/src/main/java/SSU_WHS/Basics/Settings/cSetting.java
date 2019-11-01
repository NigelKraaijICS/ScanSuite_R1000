package SSU_WHS.Basics.Settings;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cSetting {

    //region Public Properties
    public String nameStr;
        public String getNameStr() {
        return nameStr;
    }
    public settingEnu getSettingEnu(){

        settingEnu settingEnu;

        try {
            settingEnu =   cSetting.settingEnu.valueOf(this.getNameStr().toUpperCase());
        }
        catch(Exception e) {
            settingEnu = cSetting.settingEnu.UNKNOWN;
        }

             return settingEnu ;

    }

    public String valueStr;
    public String getValueStr() {
        return valueStr;
    }

    public cSettingsEntity settingsEntity;
     public boolean indatabaseBln;

    public static cSettingsViewModel gSettingViewModel;
    public static cSettingsViewModel getSettingViewModel() {
        if (gSettingViewModel == null) {
            gSettingViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cSettingsViewModel.class);
        }
        return gSettingViewModel;
    }

    public static cSettingsAdapter gSettingsAdapter;
    public static cSettingsAdapter getSettingsAdapter() {
        if (gSettingsAdapter == null) {
            gSettingsAdapter = new cSettingsAdapter();
        }
        return gSettingsAdapter;
    }

    public static List<cSetting> allSettingsObl;
    public  static boolean settingsAvailableBln;

    public enum settingEnu {
        //from the resultobl
        SCANNERFIXEDBRANCH,
        SCANNERDESCRIPTION,
        SCANNERREQUIREDVERSION,
        SCANNERAPPLICATIONENVIRONMENT,
        SCANNERCULTURE,
        SCANNERREQUIREDCONFIGURATION,
        SCANNERREAPPLYCONFIGURATION,
        SCANNERVERSIONCONFIGVERSION,
        SCANNERREQUIREDSCANNERVERSIONCONFIGVERSION,

        //settings
        ARTIKELMASK,
        CACHED_DATA_MAX_AGE_ARTICLE,
        CACHED_DATA_MAX_AGE_ARTICLE_IMAGE,
        CACHED_DATA_MAX_AGE_BARCODE,
        CACHED_DATA_MAX_AGE_BIN_ARTICLE,
        CACHED_DATA_MAX_AGE_PROPERTY,
        CACHED_DATA_MAX_AGE_STOCK,
        EXTRET_AMOUNT_MANUAL,
        EXTRET_AUTO_ACCEPT_AT_NEW_BIN,
        EXTRET_AUTO_ACCEPT_AT_NEW_ITEM,
        EXTRET_AUTO_CREATE_ORDER,
        EXTRET_BARCODE_CHECK,
        EXTRET_NEW_WORKFLOWS,
        EXTRET_REASON_REQUIRED,
        GENERIC_PRINT_BINLABEL,
        GENERIC_PRINT_ITEMLABEL,
        GENERIC_SHOW_BIN_DESCRIPTION,
        GENERIC_STATUS_UPDATE,
        GENERIC_STATUS_UPDATE_START_SYNCHRONOUS,
        GENERIC_UNLOCK_BUSY_ORDERS_ALLOWED,
        GENERIC_WAKE_SERVER_AT_STARTUP,
        GENERIC_WEBSERVICE_TIMEOUT_ERP_IN_S,
        GENERIC_WEBSERVICE_TIMEOUT_SSU_IN_S,
        INTERFACE_RESULT_METHOD,
        INV_ADD_EXTRA_BIN,
        INV_ADD_EXTRA_LINES,
        INV_AMOUNT_MANUAL,
        INV_AUTO_CLOSE_BIN,
        INV_AUTO_CREATE_ORDER,
        INV_BARCODE_CHECK,
        INV_BIN_MANUAL,
        INV_NEW_WORKFLOWS,
        INV_PRECOUNT,
        INV_SHOW_EXT_REF,
        MOVE_AMOUNT_MANUAL,
        MOVE_AUTO_ACCEPT_AT_NEW_BIN,
        MOVE_AUTO_ACCEPT_AT_NEW_ITEM,
        MOVE_AUTO_ACCEPT_AT_REQUESTED,
        MOVE_AUTO_ACCEPT_VALIDATION_MESSAGE,
        MOVE_AUTO_CREATE_ORDER,
        MOVE_AUTO_CREATE_ORDER_MI,
        MOVE_AUTO_CREATE_ORDER_MO,
        MOVE_AUTO_CREATE_ORDER_MV,
        MOVE_BARCODE_CHECK,
        MOVE_CONTAINER_REQUIRED,
        MOVE_MI_BATCH_TAKE_AVAILABLE,
        MOVE_MI_BATCH_TAKE_DEFAULT,
        MOVE_MI_BATCH_TAKE_REQUIRED,
        MOVE_MO_BATCH_PLACE_AVAILABLE,
        MOVE_MO_BATCH_PLACE_DEFAULT,
        MOVE_MO_BATCH_PLACE_REQUIRED,
        MOVE_MT_PLACE_AMOUNT_MANUAL,
        MOVE_MT_PLACE_AUTO_ITEM,
        MOVE_MT_TAKE_ALLOW_END,
        MOVE_MT_TAKE_AMOUNT_MANUAL,
        MOVE_MT_TAKE_AUTO_ACCEPT_SINGLE_PIECE,
        MOVE_MT_TAKE_AUTO_ITEM,
        MOVE_MT_TAKE_AUTO_ITEM_CONTAINER_ONCE,
        MOVE_MT_TAKE_IGNORE_UNDERTAKE,
        MOVE_MT_TRACK_CURRENT_LOCATION,
        MOVE_MV_PLACE_AFTER_TAKES_DONE,
        MOVE_NEW_WORKFLOWS,
        MOVE_NO_EXTRA_BINS,
        MOVE_NO_EXTRA_ITEMS,
        MOVE_NO_EXTRA_PIECES,
        MOVE_ORDER_BIN_REQUIRED,
        MOVE_VALIDATE_STOCK,
        MOVE_VALIDATE_STOCK_ENFORCE,
        MOVEITEM_AMOUNT_MANUAL,
        MOVEITEM_WITH_AMOUNT,
        OVERPICK_PERCENTAGE,
        OVERPICKEN,
        PICK_AUTO_ACCEPT,
        PICK_AUTO_ACCEPT_AT_NEW_BIN,
        PICK_AUTO_ACCEPT_AT_NEW_ITEM,
        PICK_AUTO_ACCEPT_AT_NEW_LOCATION,
        PICK_AUTO_CREATE_ORDER,
        PICK_AUTO_CREATE_ORDER_SALES,
        PICK_AUTO_CREATE_ORDER_TRANSFER,
        PICK_AUTO_NEXT,
        PICK_AUTO_START_PICKEN,
        PICK_BARCODE_CHECK,
        PICK_BIN_IS_ITEM,
        PICK_BIN_MANUAL,
        PICK_CONTAINER_IS_COLLI,
        PICK_FINISH_PACK_FASE_AVAILABLE,
        PICK_FINISH_PACK_SINGLE_PIECE,
        PICK_FINISH_PACK_SINGLE_PIECE_MANUAL,
        PICK_INCOMPLETE_AUTO_STORAGE,
        PICK_INCOMPLETE_STORE,
        PICK_INCOMPLETE_TO_NEW_PICKORDER,
        PICK_LOCATION_MANUAL,
        PICK_NEW_WORKFLOWS,
        PICK_NOTIFY_NO_NETWORK,
        PICK_PACK_AND_SHIP_AUTO_START,
        PICK_PACK_AND_SHIP_FASE_AVAILABLE,
        PICK_PACKING_SALES,
        PICK_PACKING_TABLE_IS_BIN,
        PICK_PACKING_TABLE_SCAN_REQUIRED,
        PICK_PACKING_TRANSFER,
        PICK_PER_SCAN,
        PICK_PICK_PV_VKO_EACH_PIECE,
        PICK_PICK_TO_CONTAINER,
        PICK_PICK_TO_CONTAINER_TYPE,
        PICK_PRINT_ADDRESSLABEL,
        PICK_PRINT_ADDRESSLABEL_QUANTITY,
        PICK_PRINT_ADDRESSLABEL_TEMPLATE,
        PICK_PRINT_CONTENTLABEL,
        PICK_PRINT_CONTENTLABEL_QUANTITY,
        PICK_PRINT_CONTENTLABEL_TEMPLATE,
        PICK_PRINT_CONTENTLABEL_TEMPLATE_NOCONTENT,
        PICK_QC_CHECK_REJECT,
        PICK_QC_FASE_AVAILABLE,
        PICK_REJECT_DURING_PICK,
        PICK_REJECT_DURING_SORT,
        PICK_REMEMBER_LINE,
        PICK_SALES_ASK_WORKPLACE,
        PICK_SELECTEREN_BARCODE,
        PICK_SEPARATE_QC,
        PICK_SHIPPING_AGENT_OVERRIDABLE,
        PICK_SHIPPING_BATCH_PRINT,
        PICK_SHIPPING_METHOD_OVERRIDABLE,
        PICK_SHIPPING_PACKAGE_CONTENT_COUNT_REQUIRED,
        PICK_SHIPPING_PACKAGE_OVERRIDABLE,
        PICK_SHIPPING_PACKAGE_REQUIRED,
        PICK_SHIPPING_PACKAGE_WEIGHT_REQUIRED,
        PICK_SHIPPING_SALES,
        PICK_SHIPPING_SERVICE_OVERRIDABLE,
        PICK_SHIPPING_TRANSFER,
        PICK_SHOW_EXT_REF,
        PICK_SORT_FASE_AVAILABLE,
        PICK_SOURCE_MANUAL,
        PICK_STORE_FASE_AVAILABLE,
        PICK_TRANSFER_ASK_WORKPLACE,
        PICK_WITH_PICTURE,
        PICK_WITH_PICTURE_AUTO_OPEN,
        PICK_WITH_PICTURE_PREFETCH,
        REALTIME_BARCODE_CHECK,
        RECEIVE_AMOUNT_MANUAL_EO,
        RECEIVE_AMOUNT_MANUAL_MA,
        RECEIVE_AMOUNT_MANUAL_OM,
        RECEIVE_AUTO_CREATE_ORDER,
        RECEIVE_AUTO_CREATE_ORDER_EO,
        RECEIVE_AUTO_CREATE_ORDER_MA,
        RECEIVE_AUTO_CREATE_ORDER_OM,
        RECEIVE_BARCODE_CHECK,
        RECEIVE_EXPORT_PART,
        RECEIVE_INTAKE_EO_AUTO_ACCEPT_AT_NEW_ITEM,
        RECEIVE_INTAKE_OM_AUTO_ACCEPT_AT_NEW_ITEM,
        RECEIVE_NEW_WORKFLOWS,
        RECEIVE_NO_EXTRA_BINS,
        RECEIVE_NO_EXTRA_ITEMS,
        RECEIVE_NO_EXTRA_PIECES,
        RECEIVE_STORE_AUTO_ACCEPT_AT_NEW_BIN,
        RECEIVE_STORE_AUTO_ACCEPT_AT_NEW_ITEM,
        RECEIVE_STORE_AUTO_ACCEPT_AT_REQUESTED,
        RECEIVE_STORE_AUTO_ACCEPT_VALIDATION_MESSAGE,
        RETOUR_AMOUNT_MANUAL,
        RETOUR_AUTO_ACCEPT_AT_NEW_ITEM,
        RETOUR_AUTO_CREATE_ORDER_RV,
        RETOUR_BARCODE_CHECK,
        RETOUR_DOCUMENT_BARCODELAYOUT,
        RETOUR_DOCUMENT_EACH_ITEM,
        RETOUR_MULTI_DOCUMENT,
        RETOUR_MULTI_DOCUMENT_POSSIBLE,
        RETOUR_NEW_WORKFLOWS,
        RETOUR_REASON_REQUIRED,
        SELFPICK_AMOUNT_MANUAL,
        SELFPICK_EXTERNAL_REFERENCES,
        SELFPICK_LOGIN,
        SELFPICK_LOGOUT,
        SELFPICK_SHOW_PRICE,
        SOFTWARE_DIRECTORY,
        PICK_SORT_AUTO_START,
        PICK_SORT_LOCATION_ADVICE,
        PICK_SORT_LOCATION_ADVICE_MANDATORY,
        GENERIC_ITEM_EXTRA_FIELD1,
        GENERIC_ITEM_EXTRA_FIELD2,
        GENERIC_ITEM_EXTRA_FIELD3,
        GENERIC_ITEM_EXTRA_FIELD4,
        GENERIC_ITEM_EXTRA_FIELD5,
        GENERIC_ITEM_EXTRA_FIELD6,
        GENERIC_ITEM_EXTRA_FIELD7,
        GENERIC_ITEM_EXTRA_FIELD8,
        UPDATE_PACKAGE_URL,
        UNKNOWN
    }

    //end region Public Properties

     //Region Constructor
    cSetting(JSONObject pvJsonObject) {
        this.settingsEntity = new cSettingsEntity(pvJsonObject);
        this.nameStr = this.settingsEntity.getNameStr();
        this.valueStr = this.settingsEntity.getValueStr();
    }
    //End Region Constructor

    //Region Public Methods
    public boolean pInsertInDatabaseBln() {
        cSetting.getSettingViewModel().insert(this.settingsEntity);
        this.indatabaseBln = true;

        if (cSetting.allSettingsObl == null){
            cSetting.allSettingsObl = new ArrayList<>();
        }
        cSetting.allSettingsObl.add(this);
        return  true;
    }

    private static cSetting mGetSettingByEnu(settingEnu pvSettingEnu){
        if(cSetting.allSettingsObl == null){
            return null;
        }

        for (cSetting setting : cSetting.allSettingsObl)
        {
            if(setting.getSettingEnu() == pvSettingEnu){
                return  setting;
            }
        }
        return null;
    }

    public static boolean pTruncateTableBln(){
        cSetting.getSettingViewModel().deleteAll();
        return true;
            }

    public static boolean pGetSettingsViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cSetting.allSettingsObl = null;
            cSetting.pTruncateTableBln();
        }

        if (cSetting.allSettingsObl != null) {
            return  true;
        }

        cWebresult WebResult;
        WebResult =  cSetting.getSettingViewModel().pGetSettingsFromWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){


            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cSetting Setting = new cSetting(jsonObject);
                Setting.pInsertInDatabaseBln();
            }

            cSetting.settingsAvailableBln = true;
            return  true;
        }
        else {
                cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETSETTINGS);
                return  false;
        }
    }
    //End Region Public Methods

    //Region Settings

    public static boolean PICK_SORT_FASE_AVAILABLE(){

            cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_SORT_FASE_AVAILABLE);
            if (Setting == null) {
                return  false;
            }

            return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean PICK_AUTO_ACCEPT(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_AUTO_ACCEPT);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean PICK_PACK_AND_SHIP_FASE_AVAILABLE(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_PACK_AND_SHIP_FASE_AVAILABLE);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean PICK_SHIPPING_SALES(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_SHIPPING_SALES);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean PICK_SALES_ASK_WORKPLACE(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_SALES_ASK_WORKPLACE);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean PICK_WITH_PICTURE_PREFETCH(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_WITH_PICTURE_PREFETCH);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean PICK_WITH_PICTURE_AUTO_OPEN(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_WITH_PICTURE_AUTO_OPEN);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean PICK_WITH_PICTURE(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_WITH_PICTURE);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean PICK_BIN_IS_ITEM(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_BIN_IS_ITEM);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean PICK_BIN_MANUAL(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_BIN_MANUAL);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static Boolean PICK_SORT_AUTO_START(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_SORT_AUTO_START);
        if (Setting == null) {
            return  false;
        }

        if (Setting.valueStr.equalsIgnoreCase( "")) {
            return  null;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static String PICK_SORT_LOCATION_ADVICE(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_SORT_LOCATION_ADVICE);
        if (Setting == null) {
            return "";
        }

        return Setting.valueStr;
    }

    public static Boolean PICK_SORT_LOCATION_ADVICE_MANDATORY(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_SORT_LOCATION_ADVICE_MANDATORY);
        if (Setting == null) {
            return  false;
        }

        if (Setting.valueStr.equalsIgnoreCase( "")) {
            return  null;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static Boolean PICK_PACK_AND_SHIP_AUTO_START(){

        cSetting Setting =  mGetSettingByEnu(settingEnu.PICK_PACK_AND_SHIP_AUTO_START);
        if (Setting == null) {
            return  false;
        }
        if (Setting.valueStr.equalsIgnoreCase( "")) {
            return  null;
        }


        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static Boolean GENERIC_UNLOCK_BUSY_ORDERS_ALLOWED(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.GENERIC_UNLOCK_BUSY_ORDERS_ALLOWED);
        if (Setting == null) {
            return  false;
        }
        if (Setting.valueStr.equalsIgnoreCase( "")) {
            return  null;
        }


        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static String GENERIC_ITEM_EXTRA_FIELD1(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.GENERIC_ITEM_EXTRA_FIELD1);
        if (Setting == null) {
            return "";
        }

        return Setting.valueStr;
    }

    public static String GENERIC_ITEM_EXTRA_FIELD2(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.GENERIC_ITEM_EXTRA_FIELD2);
        if (Setting == null) {
            return "";
        }

        return Setting.valueStr;
    }

    public static String GENERIC_ITEM_EXTRA_FIELD3(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.GENERIC_ITEM_EXTRA_FIELD3);
        if (Setting == null) {
            return "";
        }

        return Setting.valueStr;
    }

    public static String GENERIC_ITEM_EXTRA_FIELD4(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.GENERIC_ITEM_EXTRA_FIELD4);
        if (Setting == null) {
            return "";
        }

        return Setting.valueStr;
    }

    public static String GENERIC_ITEM_EXTRA_FIELD5(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.GENERIC_ITEM_EXTRA_FIELD5);
        if (Setting == null) {
            return "";
        }

        return Setting.valueStr;
    }

    public static String GENERIC_ITEM_EXTRA_FIELD6(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.GENERIC_ITEM_EXTRA_FIELD6);
        if (Setting == null) {
            return "";
        }

        return Setting.valueStr;
    }

    public static String GENERIC_ITEM_EXTRA_FIELD7(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.GENERIC_ITEM_EXTRA_FIELD7);
        if (Setting == null) {
            return "";
        }

        return Setting.valueStr;
    }

    public static String GENERIC_ITEM_EXTRA_FIELD8(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.GENERIC_ITEM_EXTRA_FIELD8);
        if (Setting == null) {
            return "";
        }

        return Setting.valueStr;
    }

    public static boolean INV_AMOUNT_MANUAL(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.INV_AMOUNT_MANUAL);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean PICK_PER_SCAN(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_PER_SCAN);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean INV_BARCODE_CHECK(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.INV_BARCODE_CHECK);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }
    public static String INV_NEW_WORKFLOWS(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.INV_NEW_WORKFLOWS);
        if (Setting == null) {
            return "";
        }

        return Setting.valueStr;
    }

    public static boolean INV_ADD_EXTRA_BIN(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.INV_ADD_EXTRA_BIN);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean INV_ADD_EXTRA_LINES(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.INV_ADD_EXTRA_LINES);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean PICK_SELECTEREN_BARCODE(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_SELECTEREN_BARCODE);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }




    //End Region Settings


}
