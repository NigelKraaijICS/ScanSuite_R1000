package SSU_WHS.Basics.Authorisations;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.List;

import ICS.cAppExtension;
public class cAuthorisation {

    //region Public Properties

    private String authorisationStr;
    private String getAuthorisationStr() {
        return authorisationStr;
    }

    public AutorisationEnu getAutorisationEnu(){

        switch(getAuthorisationStr().toUpperCase()) {
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

            case "RETURN":
                return  AutorisationEnu.RETURN;

            case "SELFPICK":
                return  AutorisationEnu.SELFPICK;

            case "SORTING":
                return  AutorisationEnu.SORTING;

            case "SHIPPING":
                return  AutorisationEnu.SHIPPING;

            default:
                return AutorisationEnu.DEMO;
        }
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
        MOVE_MO,
        MOVE_MV,
        MOVEITEM,
        PICK,
        PICK_PF,
        PICK_PV,
        RETURN,
        SELFPICK,
        // Generated
        SORTING,
        SHIPPING
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

    public static String TAG_IMAGE_RETURN = "TAG_IMAGE_RETURN";
    public static String TAG_TEXT_RETURN = "TAG_TEXT_RETURN";

    public static String TAG_IMAGE_MOVE = "TAG_IMAGE_MOVE";
    public static String TAG_TEXT_MOVE = "TAG_TEXT_MOVE";

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
