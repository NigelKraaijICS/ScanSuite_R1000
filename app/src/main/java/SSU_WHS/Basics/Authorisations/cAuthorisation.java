package SSU_WHS.Basics.Authorisations;

import android.graphics.Bitmap;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import ICS.Utils.cImages;
import ICS.cAppExtension;
import SSU_WHS.Basics.CustomAuthorisations.cCustomAuthorisation;

public class cAuthorisation {

    //region Public Properties

    private String authorisationStr;
    public String getAuthorisationStr() {
        return authorisationStr;
    }

    public AutorisationEnu getAutorisationEnu(){

        String autorisationStr = this.getAuthorisationStr();

        if (this.getCustomAuthorisation() != null) {
            autorisationStr = this.getCustomAuthorisation().getAuthorisationBaseStr();
        }

        switch(autorisationStr.toUpperCase()) {
            case "EXTERNAL_RETURN":
                return  AutorisationEnu.EXTERNAL_RETURN;

            case "INTAKE":
                return  AutorisationEnu.INTAKE;

            case "INTAKE_EO":
                return  AutorisationEnu.INTAKE_EO;

            case "INTAKE_MA":
                return  AutorisationEnu.INTAKE_MA;

            case "INTAKE_OM":
                return  AutorisationEnu.INTAKE_OM;

            case "INTAKECLOSE":
                return  AutorisationEnu.INTAKECLOSE;

            case "INVENTORY":
                return  AutorisationEnu.INVENTORY;

            case "INVENTORYCLOSE":
                return  AutorisationEnu.INVENTORYCLOSE;

            case "MOVE":
                return  AutorisationEnu.MOVE;

            case "MOVE_MI":
                return  AutorisationEnu.MOVE_MI;

            case "MOVE_MI_SINGLEPIECE":
                return  AutorisationEnu.MOVE_MI_SINGLEPIECE;

            case "MOVE_MO":
                return  AutorisationEnu.MOVE_MO;

            case "MOVE_MV":
                return  AutorisationEnu.MOVE_MV;

            case "MOVEITEM":
                return  AutorisationEnu.MOVEITEM;

            case "PICK":
                return  AutorisationEnu.PICK;

            case "PICK_PF":
                return  AutorisationEnu.PICK_PF;

            case "PICK_PV":
                return  AutorisationEnu.PICK_PV;

            case "PICK_MERGE":
                return  AutorisationEnu.PICK_MERGE;

            case "RETURN":
                return  AutorisationEnu.RETURN;

            case "SELFPICK":
                return  AutorisationEnu.SELFPICK;

            case "SORTING":
                return  AutorisationEnu.SORTING;

            case "SHIPPING":
                return  AutorisationEnu.SHIPPING;

            case "STORAGE":
                return  AutorisationEnu.STORAGE;

            case "FINISH_SHIPPING":
                return  AutorisationEnu.FINISH_SHIPPING;

            case "QC":
                return  AutorisationEnu.QC;

            case "PACK_AND_SHIP":
                return  AutorisationEnu.PACK_AND_SHIP;

            case "PACK_AND_SHIP_MERGE":
                return  AutorisationEnu.PACK_AND_SHIP_MERGE;

            default:
                return AutorisationEnu.DEMO;
        }
    }

    private cCustomAuthorisation customAuthorisation;
    public cCustomAuthorisation getCustomAuthorisation() {

        if (this.customAuthorisation != null) {
            return  this.customAuthorisation;
        }

        if (cCustomAuthorisation.allCustomAutorisations == null || cCustomAuthorisation.allCustomAutorisations.size() == 0 ) {
            return  null;
        }

        for (cCustomAuthorisation loopCustomAuthorisation : cCustomAuthorisation.allCustomAutorisations) {

            if (loopCustomAuthorisation.getAuthorisationStr().equalsIgnoreCase(this.getAuthorisationStr())) {
                customAuthorisation = loopCustomAuthorisation;
                return  customAuthorisation;
            }

        }

        return null;
    }

    public  Bitmap customImageBmp() {

        if (customAuthorisation == null) {
            return  null;
        }

        return  cImages.pBase64ToImage(customAuthorisation.getImagebase64Str());

    }




    private Integer orderInt;
    public Integer getOrderInt() {
        return orderInt;
    }

    private String licenseStr;
    public String getLicenseStr() {
        return licenseStr;
    }

    private cAuthorisationEntity authorisationEntity;

    private cAuthorisationViewModel getAuthorisationViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cAuthorisationViewModel.class);
    }

    public enum AutorisationEnu {
        DEMO,
        EXTERNAL_RETURN,
        INTAKE,
        INTAKE_EO,
        INTAKE_MA,
        INTAKE_OM,
        INTAKECLOSE,
        INVENTORY,
        INVENTORYCLOSE,
        MOVE,
        MOVE_MI,
        MOVE_MI_SINGLEPIECE,
        MOVE_MO,
        MOVE_MV,
        MOVEITEM,
        PICK,
        PICK_PF,
        PICK_PV,
        PICK_MERGE,
        RETURN,
        SELFPICK,
        // Generated
        SORTING,
        STORAGE,
        SHIPPING,
        FINISH_SHIPPING,
        QC,
        PACK_AND_SHIP,
        PACK_AND_SHIP_MERGE
    }

    public static String TAG_IMAGE_INTAKE = "TAG_IMAGE_INTAKE";
    public static String TAG_TEXT_INTAKE = "TAG_TEXT_INTAKE";

    public static String TAG_IMAGE_INVENTORY = "TAG_IMAGE_INVENTORY";
    public static String TAG_TEXT_INVENTORY = "TAG_TEXT_INVENTORY";

    public static String TAG_IMAGE_PICK = "TAG_IMAGE_PICK";
    public static String TAG_TEXT_PICK = "TAG_TEXT_PICK";

    public static String TAG_IMAGE_SORT = "TAG_IMAGE_SORT";
    public static String TAG_TEXT_SORT = "TAG_TEXT_SORT";

    public static String TAG_IMAGE_SHIP = "TAG_IMAGE_SHIP";
    public static String TAG_TEXT_SHIP = "TAG_TEXT_SHIP";

    public static String TAG_IMAGE_STORE = "TAG_IMAGE_STORE";
    public static String TAG_TEXT_STORE = "TAG_TEXT_STORE";

    public static String TAG_IMAGE_FINISH_SHIP = "TAG_IMAGE_FINISH_SHIP";
    public static String TAG_TEXT_FINSIH_SHIP = "TAG_TEXT_FINISH_SHIP";

    public static String TAG_IMAGE_QC = "TAG_IMAGE_QC";
    public static String TAG_TEXT_QC = "TAG_TEXT_QC";

    public static String TAG_IMAGE_RETURN = "TAG_IMAGE_RETURN";
    public static String TAG_TEXT_RETURN = "TAG_TEXT_RETURN";

    public static String TAG_IMAGE_MOVE = "TAG_IMAGE_MOVE";
    public static String TAG_TEXT_MOVE = "TAG_TEXT_MOVE";

    public static String TAG_IMAGE_PACK_AND_SHIP = "TAG_IMAGE_PACK_AND_SHIP";
    public static String TAG_TEXT_PACK_AND_SHIP = "TAG_TEXT_PACK_AND_SHIP";

    public static String TAG_IMAGE_PACK_AND_SHIP_MERGE = "TAG_IMAGE_PACK_AND_SHIP_MERGE";
    public static String TAG_TEXT_PACK_AND_SHIP_MERGE = "TAG_TEXT_PACK_AND_SHIP_MERGE";

    //end region Public Propties


    //Region Constructor
    public cAuthorisation(JSONObject pvJsonObject) {
        this.authorisationEntity = new cAuthorisationEntity(pvJsonObject);
        this.authorisationStr = this.authorisationEntity.getAuthorisationStr();
        this.orderInt =  this.authorisationEntity.getOrderInt();
        this.licenseStr = this.authorisationEntity.getLicenseStr();
    }

    public cAuthorisation(String pvAuthorisationStr, int pvOrderInt){
        this.authorisationStr = pvAuthorisationStr;
        this.orderInt =  pvOrderInt;
    }
    //End Region Constructor

//Region Public Methods

    public boolean pInsertInDatabaseBln() {
        this.getAuthorisationViewModel().insert(this.authorisationEntity);
        return true;
    }

    public static boolean pTruncateTableBln(){
         cAuthorisationViewModel authorisationViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cAuthorisationViewModel.class);
        authorisationViewModel.deleteAll();
        return true;
    }

    //End Region Public Methods



}
