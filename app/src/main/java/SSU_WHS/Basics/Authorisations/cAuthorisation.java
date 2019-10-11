package SSU_WHS.Basics.Authorisations;

import androidx.lifecycle.ViewModelProviders;
import org.json.JSONObject;
import java.util.List;
import ICS.cAppExtension;
public class cAuthorisation {

    //region Public Properties

    public String authorisationStr;
    public String getAuthorisationStr() {
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

    public Integer orderInt;
    public Integer getOrderInt() {
        return orderInt;
    }

    public String licenseStr;
    public String getLicenseStr() {
        return licenseStr;
    }

    public List<String> errorMessagesObl;
    public List<String> getErrorMessagesObl() {
        return errorMessagesObl;
    }


    public cAuthorisationEntity authorisationEntity;
    public boolean inDatabaseBln;

    public static cAuthorisationViewModel gAutorisationViewModel;

    public static cAuthorisationViewModel getAutorisationViewModel() {
        if (gAutorisationViewModel == null) {
            gAutorisationViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cAuthorisationViewModel.class);
        }
        return gAutorisationViewModel;
    }

    public static cAuthorisationAdapter gAuthorisationAdapter;

    public static cAuthorisationAdapter getAuthorisationAdapter() {
        if (gAuthorisationAdapter == null) {
            gAuthorisationAdapter = new cAuthorisationAdapter();
        }
        return gAuthorisationAdapter;
    }

    public static List<cAuthorisation> allAutorisations;

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

    public static String TAG_IMAGE_INVENTORY = "TAG_IMAGE_INVENTORY";
    public static String TAG_TEXT_INVENTORY = "TAG_TEXT_INVENTORY";

    public static String TAG_IMAGE_PICK = "TAG_IMAGE_PICK";
    public static String TAG_TEXT_PICK = "TAG_TEXT_PICK";

    public static String TAG_IMAGE_SORT = "TAG_IMAGE_SORT";
    public static String TAG_TEXT_SORT = "TAG_TEXT_SORT";

    public static String TAG_IMAGE_SHIP = "TAG_IMAGE_SHIP";
    public static String TAG_TEXT_SHIP = "TAG_TEXT_SHIP";

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
        cAuthorisation.getAutorisationViewModel().insert(this.authorisationEntity);
        this.inDatabaseBln = true;
        return true;
    }

    private static cAuthorisation mGetAutorisationByEnumerate(AutorisationEnu pvAutorisationEnu){
        if(cAuthorisation.allAutorisations == null){
            return null;
        }

        for (cAuthorisation authorisation : cAuthorisation.allAutorisations)
        {
            if (authorisation.getAutorisationEnu() == pvAutorisationEnu) {
                return  authorisation;
            }
        }
        return null;
    }

    public static boolean pTruncateTableBln(){
        cAuthorisation.getAutorisationViewModel().deleteAll();
        return true;
    }

    //End Region Public Methods



}
