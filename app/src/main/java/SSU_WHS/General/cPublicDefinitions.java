package SSU_WHS.General;


public class cPublicDefinitions {


    public static String ADDDOCUMENT_TAG = "ADDDOCUMENT_TAG";
    public static String ADDREASON_TAG = "ADDREASON_TAG";

    public static String SETTINGS_PASSWORD = "ics";
    public static String UPDATE_BASE_URL = "http://ssupdate.icsvertex.nl";
    public static String UPDATE_PACKAGE_NAME = "scansuite.apk";

    public static String INTERFACE_VERSION = "15002";

    public static String NUMBERINTENT_NUMBER = "NUMBERINTENT_NUMBER";
    public static String NUMBERINTENT_EXTRANUMBER = "NUMBERINTENT_EXTRANUMBER";

    public static String NUMBERINTENT_CURRENTQUANTITY = "NUMBERINTENT_CURRENTQUANTITY";
    public static String NUMBERINTENT_MAXQUANTITY = "NUMBERINTENT_MAXQUANTITY";


    public static String KEY_COMMENTHEADER = "KEY_COMMENTHEADER";
    public static String ADDBIN_TAG = "ADDBIN_TAG";
    public static String ADDARTICLE_TAG = "ADDARTICLE_TAG";


    public static int CHANGELANGUAGE_REQUESTCODE = 45;
    public static int CHANGEDATETIME_REQUESTCODE = 46;
    public static int CHANGEWIFI_REQUESTCODE = 47;

    public static String DATEPATTERNSHOW = "dd-MM-yyyy";

    public static String DATEPATTERNWEBSERVICE = "yyyy-MM-dd'T'hh:mm:ss'Z'";


    public static String SHAREDPREFERENCE_FILE = "SCANSUITE_PREFERENCES";

    public static String BARCODEFRAGMENT_LIST_TAG = "BARCODEFRAGMENT_LIST_TAG";
    public static String BRANCHFRAGMENT_LIST_TAG = "BRANCHFRAGMENT_LIST_TAG";

    public static String GETTING_DATA_TAG = "GETTING_DATA_TAG";

    public static String HUGEERROR_TAG = "HUGEERROR_TAG";
    public static String HUGEERROR_ERRORMESSAGE = "HUGEERROR_ERRORMESSAGE";
    public static String HUGEERROR_EXTRASTRING = "HUGEERROR_EXTRASTRING";

    public static String ORDERDONE_TAG = "ORDERDONE_TAG";
    public static String ORDERDONE_HEADER = "ORDERDONE_ERRORMESSAGE";
    public static String COMMENTFRAGMENT_TAG = "COMMENTFRAGMENT_TAG";
    public static String WORKPLACEFRAGMENT_TAG = "WORKPLACEFRAGMENT_TAG";
    public static String ARTICLEDETAILFRAGMENT_TAG = "ARTICLEDETAILFRAGMENT_TAG";
    public static String ORDERDONE_TEXT = "ORDERDONE_EXTRASTRING";
    public static String ORDERDONE_SHOWCURRENTLOCATION = "ORDERDONE_SHOWCURRENTLOCATION";

    public static String WEBSERVICEERROR_TAG = "WEBSERVICEERROR_TAG";
    public static String WEBSERVICEERROR_LIST_TAG = "WEBSERVICEERROR_LIST_TAG";

    public static String CURRENTLOCATION_TAG = "ORDERDONE_TAG";

    public static String PASSWORDFRAGMENT_TAG = "PASSWORDFRAGMENT_TAG";
    public static String PASSWORDFRAGMENT_HEADER = "PASSWORDFRAGMENT_HEADER";
    public static String PASSWORDFRAGMENT_TEXT = "PASSWORDFRAGMENT_TEXT";
    public static String PASSWORDFRAGMENT_HINT = "PASSWORDFRAGMENT_HINT";
    public static String PASSWORDFRAGMENT_ISNUMERIC = "PASSWORDFRAGMENT_ISNUMERIC";


    public static String ARTICLEFULL_TAG = "ARTICLEFULL_TAG";
    public static String ARTICLEFULL_RECORDID = "ARTICLEFULL_RECORDID";

    public static String SHOWCOMMENTS_EXTRA = "SHOWCOMMENTS_EXTRA";

    public enum Workflows {
        BC,
        BM,
        BP,
        EOM,
        EOOM,
        EOOS,
        EOR,
        EOS,
        ER,
        IVM,
        IVS,
        MAM,
        MAS,
        MAT,
        MI,
        MO,
        MT,
        MV,
        MVI,
        OMM,
        OMOM,
        OMOS,
        OMR,
        OMS,
        PA,
        PF,
        PV,
        RVR,
        RVS,
        SPV,
        UNKNOWN
    }

}
