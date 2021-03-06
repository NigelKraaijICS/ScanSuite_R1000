package SSU_WHS.Basics.Settings;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ICS.Utils.cText;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.Fragments.Packaging.PackagingUsedFragment;

public class cSetting {

    //region Public Properties
    private String nameStr;
    public String getNameStr() {
        return nameStr;
    }

    private settingEnu getSettingEnu(){

        settingEnu settingEnu;

        try {
            settingEnu =   cSetting.settingEnu.valueOf(this.getNameStr().toUpperCase());
        }
        catch(Exception e) {
            settingEnu = cSetting.settingEnu.UNKNOWN;
        }

             return settingEnu ;

    }

    private String valueStr;
    public String getValueStr() {
        return valueStr;
    }

    private cSettingsEntity settingsEntity;

    private cSettingsViewModel getSettingsViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cSettingsViewModel.class);
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
        INV_RESET_PASSWORD,
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
        MOVE_WITH_PICTURE,
        MOVE_WITH_PICTURE_AUTO_OPEN,
        MOVE_WITH_PICTURE_PREFETCH,
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
        PICK_STORAGE_AUTO_START,
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
        PICK_SHIPPING_QC_CHECK_COUNT,
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
        RECEIVE_STORE_DEVIATIONS_PASSWORD,
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
        RETOUR_ORDER_BIN_NO_CHECK,
        RETOUR_VALIDATE_CURRENT_LOCATION,
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
        RECEIVE_INTAKE_EO_CREATE_EXTRA_ITEM_VALIDATION,
        RECEIVE_EXTRA_PIECES_PERCENTAGE,
        RECEIVE_EXTRA_PIECES_PERCENTAGE_MANDATORY,
        RECEIVE_RESET_PASSWORD,
        RECEIVE_DEVIATIONS_PASSWORD,
        PACK_AND_SHIP_NEW_WORKFLOWS,
        PACK_AND_SHIP_AUTO_CREATE_ORDER,
        UNKNOWN
    }

    //end region Public Properties

     //Region Constructor
    private cSetting(JSONObject pvJsonObject) {
        this.settingsEntity = new cSettingsEntity(pvJsonObject);
        this.nameStr = this.settingsEntity.getNameStr();
        this.valueStr = this.settingsEntity.getValueStr();
    }
    //End Region Constructor

    //Region Public Methods
    public boolean pInsertInDatabaseBln() {
        this.getSettingsViewModel().insert(this.settingsEntity);

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

        cSettingsViewModel settingsViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cSettingsViewModel.class);
        settingsViewModel.deleteAll();
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
        cSettingsViewModel settingsViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cSettingsViewModel.class);
        WebResult =  settingsViewModel.pGetSettingsFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            for (JSONObject jsonObject : WebResult.getResultDtt()) {
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

    // GENERIC Settings

    public  static boolean REALTIME_BARCODE_CHECK() {

        cSetting Setting =  mGetSettingByEnu(settingEnu.REALTIME_BARCODE_CHECK);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean GENERIC_UNLOCK_BUSY_ORDERS_ALLOWED(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.GENERIC_UNLOCK_BUSY_ORDERS_ALLOWED);
        if (Setting == null) {
            return  false;
        }
        if (Setting.valueStr.equalsIgnoreCase( "")) {
            return  false;
        }


        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean GENERIC_PRINT_BINLABEL(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.GENERIC_PRINT_BINLABEL);
        if (Setting == null) {
            return  false;
        }
        if (Setting.valueStr.equalsIgnoreCase( "")) {
            return  false;
        }


        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean GENERIC_PRINT_ITEMLABEL(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.GENERIC_PRINT_ITEMLABEL);
        if (Setting == null) {
            return  false;
        }
        if (Setting.valueStr.equalsIgnoreCase( "")) {
            return  false;
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

    // PICK Settings
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

    public static boolean PICK_FINISH_PACK_FASE_AVAILABLE(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_FINISH_PACK_FASE_AVAILABLE);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean PICK_STORE_FASE_AVAILABLE(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_STORE_FASE_AVAILABLE);
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

    public static boolean PICK_SORT_AUTO_START(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_SORT_AUTO_START);
        if (Setting == null) {
            return  false;
        }

        if (Setting.valueStr.equalsIgnoreCase( "")) {
            return  false;
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

    public static boolean PICK_SORT_LOCATION_ADVICE_MANDATORY(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_SORT_LOCATION_ADVICE_MANDATORY);
        if (Setting == null) {
            return  false;
        }

        if (Setting.valueStr.equalsIgnoreCase( "")) {
            return  false;
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

    public static Boolean PICK_PICK_TO_CONTAINER(){

        cSetting Setting =  mGetSettingByEnu(settingEnu.PICK_PICK_TO_CONTAINER);
        if (Setting == null) {
            return  false;
        }
        if (Setting.valueStr.equalsIgnoreCase( "")) {
            return  false;
        }


        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static Boolean PICK_STORAGE_AUTO_START(){

        cSetting Setting =  mGetSettingByEnu(settingEnu.PICK_STORAGE_AUTO_START);
        if (Setting == null) {
            return  false;
        }
        if (Setting.valueStr.equalsIgnoreCase( "")) {
            return  null;
        }


        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean PICK_BARCODE_CHECK(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_BARCODE_CHECK);
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

    public static boolean PICK_SELECTEREN_BARCODE(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_SELECTEREN_BARCODE);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static List<String> PICK_NEW_WORKFLOWS(){

        List<String> resultObl = new ArrayList<>();

        cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_NEW_WORKFLOWS);
        if (Setting == null || Setting.getValueStr().isEmpty()) {
            return resultObl;
        }

        if (!Setting.getValueStr().contains(";")) {
            resultObl.add(Setting.getValueStr());
            return resultObl;
        }

        resultObl = new ArrayList<>(Arrays.asList(Setting.valueStr.split(";")));

        return resultObl ;
    }

    public static boolean PICK_AUTO_CREATE_ORDER(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_AUTO_CREATE_ORDER);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean PICK_AUTO_CREATE_ORDER_SALES(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_AUTO_CREATE_ORDER_SALES);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean PICK_AUTO_CREATE_ORDER_TRANSFER(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.PICK_AUTO_CREATE_ORDER_TRANSFER);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }


    // RECEIVE Settings
    public static boolean RECEIVE_BARCODE_CHECK(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.RECEIVE_BARCODE_CHECK);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static List<String> RECEIVE_NEW_WORKFLOWS(){

        List<String> resultObl = new  ArrayList();

        cSetting Setting =   mGetSettingByEnu(settingEnu.RECEIVE_NEW_WORKFLOWS);
        if (Setting == null || Setting.getValueStr().isEmpty()) {
            return resultObl;
        }

        if (!Setting.getValueStr().contains(";")) {
            resultObl.add(Setting.getValueStr());
            return resultObl;
        }

        resultObl = new ArrayList<>(Arrays.asList(Setting.valueStr.split(";")));

        return resultObl ;
    }

    public static List<String> RECEIVE_EO_NEW_WORKFLOWS(){

        List<String> resultObl = new  ArrayList();

        for (String workflowStr : RECEIVE_NEW_WORKFLOWS() ) {

            if (workflowStr.equalsIgnoreCase("EOR") || workflowStr.equalsIgnoreCase("EOS") )     {
                resultObl.add((workflowStr));
            }

        }

        return resultObl ;
    }

    public static List<String> RECEIVE_MA_NEW_WORKFLOWS(){

        List<String> resultObl = new  ArrayList();

        for (String workflowStr : RECEIVE_NEW_WORKFLOWS() ) {

            if (workflowStr.equalsIgnoreCase("MAS")  )     {
                resultObl.add((workflowStr));
            }

        }

        return resultObl ;
    }

    public static boolean RECEIVE_AMOUNT_MANUAL_MA(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.RECEIVE_AMOUNT_MANUAL_MA);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }


    public static boolean RECEIVE_AUTO_CREATE_ORDER(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.RECEIVE_AUTO_CREATE_ORDER);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean RECEIVE_AUTO_CREATE_ORDER_EO(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.RECEIVE_AUTO_CREATE_ORDER_EO);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean RECEIVE_AUTO_CREATE_ORDER_MA(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.RECEIVE_AUTO_CREATE_ORDER_MA);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean RECEIVE_NO_EXTRA_ITEMS(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.RECEIVE_NO_EXTRA_ITEMS);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean RECEIVE_NO_EXTRA_PIECES(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.RECEIVE_NO_EXTRA_PIECES);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public  static String RECEIVE_INTAKE_EO_CREATE_EXTRA_ITEM_VALIDATION(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.RECEIVE_INTAKE_EO_CREATE_EXTRA_ITEM_VALIDATION);
        if (Setting == null) {
            return "";
        }

        return Setting.valueStr;

    }

    public static String RECEIVE_STORE_DEVIATIONS_PASSWORD(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.RECEIVE_STORE_DEVIATIONS_PASSWORD);
        if (Setting == null) {
            return "";
        }

        return Setting.valueStr;
    }

    public static int RECEIVE_EXTRA_PIECES_PERCENTAGE(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.RECEIVE_EXTRA_PIECES_PERCENTAGE);
        if (Setting == null) {
            return 0;
        }

        return  cText.pStringToIntegerInt(Setting.valueStr);
    }

    public static boolean RECEIVE_EXTRA_PIECES_PERCENTAGE_MANDATORY(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.RECEIVE_EXTRA_PIECES_PERCENTAGE_MANDATORY);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static String RECEIVE_RESET_PASSWORD(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.RECEIVE_RESET_PASSWORD);
        if (Setting == null) {
            return "";
        }

        return Setting.valueStr;
    }

    public static String RECEIVE_DEVIATIONS_PASSWORD(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.RECEIVE_DEVIATIONS_PASSWORD);
        if (Setting == null) {
            return "";
        }

        return Setting.valueStr;
    }


    // INVENTORY Settings

    public static boolean INV_AUTO_CREATE_ORDER(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.INV_AUTO_CREATE_ORDER);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean INV_AMOUNT_MANUAL(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.INV_AMOUNT_MANUAL);
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

    public static String INV_RESET_PASSWORD(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.INV_RESET_PASSWORD);
        if (Setting == null) {
            return "";
        }

        return Setting.valueStr;
    }

    public static List<String>  INV_NEW_WORKFLOWS(){

        List<String> resultObl = new ArrayList<>();

        cSetting Setting =   mGetSettingByEnu(settingEnu.INV_NEW_WORKFLOWS);
        if (Setting == null || Setting.getValueStr().isEmpty()) {
            return resultObl;
        }

        if (!Setting.getValueStr().contains(";")) {
            resultObl.add(Setting.getValueStr());
            return resultObl;
        }

        resultObl = new ArrayList<>(Arrays.asList(Setting.valueStr.split(";")));

        return resultObl ;
    }

    public static boolean INV_ADD_EXTRA_LINES(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.INV_ADD_EXTRA_LINES);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }


    // MOVE Settings
    public static List<String> MOVE_NEW_WORKFLOWS(){

        List<String> resultObl = new ArrayList<>();

        cSetting Setting =   mGetSettingByEnu(settingEnu.MOVE_NEW_WORKFLOWS);
        if (Setting == null || Setting.getValueStr().isEmpty()) {
            return resultObl;
        }

        if (!Setting.getValueStr().contains(";")) {
            resultObl.add(Setting.getValueStr());
            return resultObl;
        }

        resultObl = new ArrayList<>(Arrays.asList(Setting.valueStr.split(";")));

        return resultObl ;
    }

    public static boolean MOVE_BARCODE_CHECK(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.MOVE_BARCODE_CHECK);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean MOVE_MT_TAKE_ALLOW_END(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.MOVE_MT_TAKE_ALLOW_END);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean MOVE_AUTO_CREATE_ORDER(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.MOVE_AUTO_CREATE_ORDER);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean MOVE_WITH_PICTURE(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.MOVE_WITH_PICTURE);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean MOVE_WITH_PICTURE_AUTO_OPEN(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.MOVE_WITH_PICTURE_AUTO_OPEN);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean MOVE_AUTO_ACCEPT_AT_REQUESTED(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.MOVE_AUTO_ACCEPT_AT_REQUESTED);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean MOVE_AMOUNT_MANUAL(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.MOVE_AMOUNT_MANUAL);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    // RETURN Settings
    public static boolean RETOUR_BARCODE_CHECK(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.RETOUR_BARCODE_CHECK);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean RETOUR_AMOUNT_MANUAL(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.RETOUR_AMOUNT_MANUAL);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static List<String> RETOUR_NEW_WORKFLOWS(){

        List<String> resultObl = new ArrayList<>();


        cSetting Setting =   mGetSettingByEnu(settingEnu.RETOUR_NEW_WORKFLOWS);
        if (Setting == null || Setting.getValueStr().isEmpty()) {
            return resultObl;
        }

        if (!Setting.getValueStr().contains(";")) {
            resultObl.add(Setting.getValueStr());
            return resultObl;
        }

        resultObl = new ArrayList<>(Arrays.asList(Setting.valueStr.split(";")));

        return resultObl ;
    }

    public static boolean RETOUR_ORDER_BIN_NO_CHECK(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.RETOUR_ORDER_BIN_NO_CHECK);
        if (Setting == null) {
            return false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    public static boolean RETOUR_VALIDATE_CURRENT_LOCATION(){
        cSetting Setting =   mGetSettingByEnu(settingEnu.RETOUR_VALIDATE_CURRENT_LOCATION);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    //PACK AND SHIP Settings

    public static List<String> PACK_AND_SHIP_NEW_WORKFLOWS(){

        List<String> resultObl = new ArrayList<>();

        cSetting Setting =   mGetSettingByEnu(settingEnu.PACK_AND_SHIP_NEW_WORKFLOWS);
        if (Setting == null) {
            return resultObl;
        }

        if (!Setting.getValueStr().contains(";")) {
            resultObl.add(Setting.getValueStr());
            return resultObl;
        }

        resultObl = new ArrayList<>(Arrays.asList(Setting.valueStr.split(";")));

        return resultObl ;
    }

    public static boolean PACK_AND_SHIP_AUTO_CREATE_ORDER(){

        cSetting Setting =   mGetSettingByEnu(settingEnu.PACK_AND_SHIP_AUTO_CREATE_ORDER);
        if (Setting == null) {
            return  false;
        }

        return cText.pStringToBooleanBln(Setting.valueStr,false);
    }

    //End Region Settings


}
