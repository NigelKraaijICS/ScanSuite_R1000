package SSU_WHS.Webservice;

public class cWebserviceDefinitions {
    //region Methods
    public static String WEBMETHOD_SERVICEAVAILABLE = "p_WebserviceBeschikbaarJsonStr";
    public static String WEBMETHOD_SCANNERLOGON = "p_ScannerLogonJsonStr";
    public static String WEBMETHOD_GETSETTINGS = "p_InstellingenGetJsonStr";
    public static String WEBMETHOD_GET14SETTINGS = "p_Instellingen14GetJsonStr";
    public static String WEBMETHOD_GETBARCODELAYOUTS = "p_BarcodelayoutGetJsonStr";
    public static String WEBMETHOD_GETAUTHORISATIONS = "p_GebruikerVestigingAutorisatieGetJsonStr";
    //Used to be different for Android
    //public static String WEBMETHOD_GETUSERS = "p_AndroidGebruikersGetJsonStr";
    public static String WEBMETHOD_GETUSERS = "p_GebruikersGetJsonStr";
    public static String WEBMETHOD_GETWORKPLACES = "p_MagazijnWerkplekkenGetJsonStr";
    public static String WEBMETHOD_GETBRANCHESFORUSER = "p_GebruikerVestigingenGetJsonStr";
    public static String WEBMETHOD_GETPROCESSINGORPARKEDORDERS = "p_PickordersInBehandlingOfInWachtJsonStr";
    public static String WEBMETHOD_GETPICKORDERS = "p_PickordersGetJsonStr";
    public static String WEBMETHOD_GETPICKORDERLINES = "p_PickorderRegelsGetJsonStr";
    public static String WEBMETHOD_WAREHOUSEORDERLOCK = "p_WarehouseopdrachtLockJsonStr";
    public static String WEBMETHOD_WAREHOUSEORDERUNLOCK = "p_WarehouseopdrachtUnlockJsonStr";
    public static String WEBMETHOD_WAREHOUSEORDERLOCKRELEASE = "p_WarehouseopdrachtLockReleaseJsonStr";
    public static String WEBMETHOD_GETWAREHOUSELOCATIONS = "p_MagazijnlocatieGetSubsetJsonStr";
    public static String WEBMETHOD_GETPICKORDERBARCODES = "p_PickorderBarcodesGetJsonStr";
    public static String WEBMETHOD_GETPICKORDERLINEBARCODES = "p_PickorderRegelBarcodesGetJsonStr";
    public static String WEBMETHOD_GETARTICLEIMAGES = "p_ArtikelImageGetJsonStr";
    public static String WEBMETHOD_PICKORDERLINE_HANDLED = "p_PickorderRegelTakeHandledJsonStr";
    public static String WEBMETHOD_GETPICKORDERCOMMENTS = "p_PickorderCommentsGetJsonStr";
    public static String WEBMETHOD_PICKORDERSTEPHANDLED = "p_PickorderStapHandledJsonStr";
    public static String WEBMETHOD_PICKORDERLINERESET = "p_PickorderRegelResetPickenJsonStr";
    public static String WEBMETHOD_UPDATECURRENTORDERLOCATION = "p_PickOrderUpdateCurrentOrderLocationJsonStr";
    public static String WEBMETHOD_GETPICKORDERSSEQUELSTEP = "p_PickordersVervolgstapGetJsonStr";
    public static String WEBMETHOD_SORTORDERLINE_HANDLED = "p_PickorderRegelPlaceSortedJsonStr";
    public static String WEBMETHOD_GETPICKORDERADDRESSES = "p_PickorderAdresGetJsonStr";
    public static String WEBMETHOD_GETSHIPPINGAGENTS = "p_ExpediteursGetJsonStr";
    public static String WEBMETHOD_GETSHIPPINGAGENTSERVICES = "p_ExpediteurServicesGetJsonStr";
    public static String WEBMETHOD_GETSHIPPINGAGENTSERVICESHPPINGUNITS = "p_ExpediteurServiceVerzendeenhedenGetJsonStr";
    public static String WEBMETHOD_GETSHIPPINGAGENTSERVICESHIPMETHODS = "p_ExpediteurServiceVerzendmethodsGetJsonStr";
    public static String WEBMETHOD_GETPICKORDERSHIPMETHODS = "p_PickorderShippingmethodGetJsonStr";
    public static String WEBMETHOD_GETPICKORDERLINESPACKANDSHIP = "p_PickorderRegelsPackAndShipGetJsonStr";
    public static String WEBMETHOD_GETPICKORDERSHIPPACKAGES = "p_PickorderShippingPackageGetJsonStr";
    public static String WEBMETHOD_PICKORDERSOURCEDOCUMENTSHIPPED = "p_PickorderSourceDocumentShippedJsonStr";
    public static String WEBMETHOD_PICKORDERUPDATEWORKPLACE = "p_PickorderUpdateWorkplaceJsonStr";
    public static String WEBMETHOD_GETSORTLOCATIONADVICE = "p_WarehouseopdrachtSortLocationAdviceGetJsonStr";
    public static String WEBMETHOD_GETARTICLEIMAGESMULTIPLE = "p_ArtikelenImageGetJsonStr";
    public static String WEBMETHOD_USERLOGIN = "p_GebruikerLoginJsonStr";
    //endregion Methods

    //region Properties
    public static String WEBPROPERTY_USER = "pv_GebruikerStr";
    public static String WEBPROPERTY_USERNAME = "pv_UsernameStr";
    public static String WEBPROPERTY_USERNAMEDUNGLISH = "pv_GebruikersnameStr";
    public static String WEBPROPERTY_USERNAMEDUTCH = "pv_GebruikersnaamStr";
    public static String WEBPROPERTY_BRANCH = "pv_VestigingStr";
    public static String WEBPROPERTY_INPROGRESS = "pv_InBehandelingBln";
    public static String WEBPROPERTY_SEARCHTEXT = "pv_ZoekTekstStr";
    public static String WEBPROPERTY_MAINTYPE = "pv_MainTypeStr";
    public static String WEBPROPERTY_ORDERNUMBER = "pv_OpdrachtnummerStr";
    public static String WEBPROPERTY_SOURCENO = "pv_SourceNoStr";

    public static String WEBPROPERTY_ACTIONTYPECODE = "pv_ActiontypecodeStr";
    public static String WEBPROPERTY_SCANNER = "pv_ScannerStr";
    public static String WEBPROPERTY_DEVICEBRAND = "pv_DeviceBrandStr";
    public static String WEBPROPERTY_DEVICETYPE = "pv_DeviceTypeStr";
    public static String WEBPROPERTY_SCANNERVERSION = "pv_ScannerVersionStr";
    public static String WEBPROPERTY_SCANNERCONFIGURATION = "pv_ScannerConfigurationStr";
    public static String WEBPROPERTY_LANGUAGEASCULTURE = "pv_LanguageAsCultureStr";
    public static String WEBPROPERTY_ORDERTYPE = "pv_OpdrachtsoortStr";
    public static String WEBPROPERTY_WORKFLOWSTEPSTR = "pv_WorkflowStapStr";
    public static String WEBPROPERTY_WORKFLOWSTEPINT = "pv_WorkflowStapInt";
    public static String WEBPROPERTY_WORKFLOWSTEPCODESTR = "pv_WorkflowStapcodeStr";
    public static String WEBPROPERTY_IGNOREBUSY = "pv_IgnoreBezigBln";
    public static String WEBPROPERTY_BINSUBSET = "pv_BinSubsetObl";
    public static String WEBPROPERTY_OWNER = "pv_OwnerStr";
    public static String WEBPROPERTY_ITEMNO = "pv_ItemNoStr";
    public static String WEBPROPERTY_VARIANTCODE = "pv_VariantCodeStr";
    public static String WEBPROPERTY_REFRESH = "Pv_RefreshBln";
    public static String WEBPROPERTY_LINENOTAKE = "pv_LineNoTakeLng";
    public static String WEBPROPERTY_LINENOPLACE = "pv_LineNoPlaceLng";
    public static String WEBPROPERTY_HANDLEDTIMESTAMP = "pv_HandledTimestampDat";
    public static String WEBPROPERTY_PICKFROMCONTAINER = "pv_PickenUitContainerStr";
    public static String WEBPROPERTY_BARCODESLIST = "pv_BarcodesObl";
    public static String WEBPROPERTY_CONTAINERSLIST = "pv_ContainersObl";
    public static String WEBPROPERTY_PROCESSINGSEQUENCE = "pv_ProcessingSequenceStr";
    public static String WEBPROPERTY_WORKPLACE = "pv_WorkplaceStr";
    public static String WEBPROPERTY_CULTURE = "pv_CultureStr";
    public static String WEBPROPERTY_CURRENTLOCATION = "pv_OrderCurrentLocationStr";
    public static String WEBPROPERTY_PICKSTEP = "pv_PickStapInt";
    public static String WEBPROPERTY_NUMBER = "pv_AantalDbl";
    public static String WEBPROPERTY_PROPERTIESHANDLED = "pv_PropertiesHandledObl";
    public static String WEBPROPERTY_LOCATION = "pv_LocationStr";
    public static String WEBPROPERTY_SHIPPINGAGENT = "pv_ShippingAgentStr";
    public static String WEBPROPERTY_SHIPPINGSERVICE = "pv_ShippingServiceStr";
    public static String WEBPROPERTY_SHIPPINGOPTIONS = "pv_ShippingOptionObl";
    public static String WEBPROPERTY_SHIPPINGPACKAGES = "pv_ShippingPackageObl";
    public static String WEBPROPERTY_ITEMSLIST = "pv_ItemsObl";
    public static String WEBPROPERTY_PASSWORD = "pv_PasswordStr";

    //endregion Properties

    //region temporary Properties
    public static String WEBPROPERTY_BARCODE = "pv_BarcodeStr";
    public static String WEBPROPERTY_BARCODELIST = "pv_BarcodesObl";
    public static String WEBPROPERTY_QUANTITYHANDLED = "pv_QuantityHandledDbl";
    //endregion temporary Properties

    //region complex types
    public static String WEBPROPERTY_INTERFACESHIPPINGPACKAGE = "c_InterfaceShippingPackageIesp";
    public static String WEBPROPERTY_INTERFACESHIPPINGPACKAGE_PACKAGE = "g_PackageTypeStr";
    public static String WEBPROPERTY_INTERFACESHIPPINGPACKAGE_SEQUENCENUMBER = "g_SequenceNumberInt";
    public static String WEBPROPERTY_INTERFACESHIPPINGPACKAGE_WEIGHTING = "g_WeightinGLng";
    public static String WEBPROPERTY_INTERFACESHIPPINGPACKAGE_ITEMCOUNT = "g_ItemcountDbl";
    public static String WEBPROPERTY_INTERFACESHIPPINGPACKAGE_CONTAINERTYPE = "g_ContainersoortStr";
    public static String WEBPROPERTY_INTERFACESHIPPINGPACKAGE_CONTAINER = "g_ContainerStr";

    public static String WEBPROPERTY_BARCODEHANDLED_COMPLEX = "c_BarcodeHandledUwbh";
    public static String WEBPROPERTY_BARCODE_COMPLEX = "g_BarcodeStr";
    public static String WEBPROPERTY_QUANTITYHANDLED_COMPLEX = "g_QuantityHandledDbl";

    public static String WEBPROPERTY_ARTICLEINPUT_COMPLEX = "c_ArtikelInputUari";
    public static String WEBPROPERTY_ITEMNO_COMPLEX = "g_ItemNoStr";
    public static String WEBPROPERTY_VARIANTCODE_COMPLEX = "g_VariantCodeStr";
    //endregion complex types

    //region cWebservice defaults
    public static String SUCCESBLN_NAMESTR = "vSuccessBln";
    public static String RESULTBLN_NAMESTR = "vResultBln";
    public static String RESULTLNG_NAMESTR = "vResultLng";
    public static String RESULTOBL_NAMESTR = "vResultsObl";
    public static String RESULTDTT_NAMESTR = "vResultDtt";
    //endregion cWebservice defaults

}
