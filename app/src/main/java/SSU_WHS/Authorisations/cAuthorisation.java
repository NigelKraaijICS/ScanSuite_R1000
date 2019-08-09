package SSU_WHS.Authorisations;

public class cAuthorisation {
    public enum g_eAuthorisation {
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

}
