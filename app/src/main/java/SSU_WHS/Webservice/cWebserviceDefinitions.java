package SSU_WHS.Webservice;

public class cWebserviceDefinitions {
    //region Methods
    public static String WEBMETHOD_SERVICEAVAILABLE = "p_WebserviceBeschikbaarJsonStr";
    public static String WEBMETHOD_SCANNERLOGON = "p_ScannerLogonJsonStr";
    public static String WEBMETHOD_GETSETTINGS = "p_InstellingenGetJsonStr";
    public static String WEBMETHOD_GET14SETTINGS = "p_Instellingen14GetJsonStr";
    public static String WEBMETHOD_GETBARCODELAYOUTS = "p_BarcodelayoutGetJsonStr";
    public static String WEBMETHOD_GETPACKAGING = "p_EmballageGetJsonStr";
    public static String WEBMETHOD_GETAUTHORISATIONSFORUSERINLOCATION = "p_GebruikerVestigingAutorisatieGetJsonStr";
    //Used to be different for Android

    public static String WEBMETHOD_GETLICENSE = "p_LicentieGetJsonStr";
    public static String WEBMETHOD_RELEASELICENSE = "p_LicentieReleaseJsonStr";

    public static String WEBMETHOD_GETUSERS = "p_GebruikersGetJsonStr";
    public static String WEBMETHOD_GETSCANNERS = "p_ScannersGetJsonStr";
    public static String WEBMETHOD_GETWORKPLACES = "p_MagazijnWerkplekkenGetJsonStr";
    public static String WEBMETHOD_GETLABELTEMPLATES = "p_LabelTemplateGetJsonStr";
    public static String WEBMETHOD_GETTRANSLATIONS = "p_VertalingGetJsonStr";
    public static String WEBMETHOD_GETIDENTIFIERWITHDESTINATION = "p_IdentifierWithDestinationGetJsonStr";
    public static String WEBMETHOD_GETBINARTICLES = "p_MagazijnlocatieArtikelenGetJsonStr";
    public static String WEBMETHOD_GETBRANCHES = "p_VestigingGetJsonStr";
    public static String WEBMETHOD_GETBRANCHESFORUSER = "p_GebruikerVestigingenGetJsonStr";
    public static String WEBMETHOD_GETSTOCKOWNERFORUSER = "p_GebruikerVestigingenStockOwnersGetJsonStr";
    public static String WEBMETHOD_GETPROCESSINGORPARKEDORDERS = "p_PickordersInBehandlingOfInWachtJsonStr";
    public static String WEBMETHOD_GETPICKORDERS = "p_PickordersGetJsonStr";
    public static String WEBMETHOD_GETPICKORDERLINES = "p_PickorderRegelsGetJsonStr";
    public static String WEBMETHOD_GETPICKHANDLEDCONTAINERS = "p_PickorderContainersHandledGetJsonStr";
    public static String WEBMETHOD_GETQCPICKORDERLINES = "p_WarehouseopdrachtLinesQcCheckGetJsonStr";


    public static String WEBMETHOD_WAREHOUSEORDERLOCK = "p_WarehouseopdrachtLockJsonStr";
    public static String WEBMETHOD_WAREHOUSEORDERUNLOCK = "p_WarehouseopdrachtUnlockJsonStr";
    public static String WEBMETHOD_WAREHOUSEORDERLOCKRELEASE = "p_WarehouseopdrachtLockReleaseJsonStr";
    public static String WEBMETHOD_GETWAREHOUSELOCATIONS = "p_MagazijnlocatieGetSubsetJsonStr";
    public static String WEBMETHOD_GETWAREHOUSEORDERCOMPOSITEBARCODES = "p_WarehouseopdrachtCompositeBarcodesGetJsonStr";
    public static String WEBMETHOD_GETPICKORDERBARCODES = "p_PickorderBarcodesGetJsonStr";
    public static String WEBMETHOD_GETSHIPPINGMETHODS = "p_PickorderShippingmethodGetJsonStr";
    public static String WEBMETHOD_GETPICKORDERLINEBARCODES = "p_PickorderRegelBarcodesGetJsonStr";
    public static String WEBMETHOD_GETARTICLEIMAGE = "p_ArtikelImageGetJsonStr";
    public static String WEBMETHOD_PICKORDERLINE_HANDLED = "p_PickorderRegelTakeHandledJsonStr";
    public static String WEBMETHOD_PICKORDERLINECHECKED= "p_WarehouseopdrachtLineQcCheckCheckedJsonStr";
    public static String WEBMETHOD_PICKORDERPRINTDOCUMENTS= "p_PickorderPrintDocumentsJsonStr";
    public static String WEBMETHOD_PICKORDERLINECORRECT= "p_PickorderRegelCorrectQcJsonStr";
    public static String WEBMETHOD_GETPICKORDERCOMMENTS = "p_PickorderCommentsGetJsonStr";
    public static String WEBMETHOD_PICKORDERSTEPHANDLED = "p_PickorderStapHandledJsonStr";
    public static String WEBMETHOD_PICKORDERLINERESET = "p_PickorderRegelResetPickenJsonStr";
    public static String WEBMETHOD_PICKORDERLINERESETGENERATED = "p_PickorderRegelResetGeneratedJsonStr";
    public static String WEBMETHOD_UPDATECURRENTORDERLOCATION = "p_PickOrderUpdateCurrentOrderLocationJsonStr";
    public static String WEBMETHOD_GETPICKORDERSSEQUELSTEP = "p_PickordersVervolgstapGetJsonStr";
    public static String WEBMETHOD_LINE_PROPERTIES_HANDLED = "p_WarehouseopdrachtLinePropertiesHandledJsonStr";
    public static String WEBMETHOD_PICKORDERLINEGENERATEDTAKEHANDLED = "p_PickorderTakeItemJsonStr";
    public static String WEBMETHOD_PICKORDERITEMVARIANTCREATE = "p_PickorderItemVariantCreateJsonStr";
    public static String WEBMETHOD_PICKORDERHANDLEDGENERATED = "p_PickorderOrderHandledGeneratedJsonStr";


    public static String WEBMETHOD_GETLOCATIONARTICLEVIAOWNERBARCODE = "p_VestigingArtikelGetViaOwnerBarcodeJsonStr";
    public static String WEBMETHOD_GETARTICLEVIAOWNERBARCODE = "p_ArtikelGetViaOwnerBarcodeJsonStr";
    public static String WEBMETHOD_GETARTICLEBARCODES = "p_VestigingArtikelBarcodesGetJsonStr";
    public static String WEBMETHOD_GETARTICLEBYFORCE = "p_VestigingArtikelGetForcedJsonStr";
    public static String WEBMETHOD_GETUNIQUEBARCODES = "p_VestigingArtikelBarcodesUpdateUniqueGetJsonStr";
    public static String WEBMETHOD_GETARTICLEPROPERTIES = "p_VestigingArtikelPropertiesGetJsonStr";
    public static String WEBMETHOD_GETARTICLEPROPERTYVALUES = "p_VestigingArtikelPropertyValuesGetJsonStr";
    public static String WEBMETHOD_GETARTICLESTOCKWITHPROPERTY = "p_ItemPropertyStockGetForcedJsonStr";

    public static String WEBMETHOD_GETARTICLESTOCK = "p_ArtikelVoorraadGetJsonStr";
    public static String WEBMETHOD_SORTORDERLINE_HANDLED = "p_PickorderRegelPlaceSortedJsonStr";
    public static String WEBMETHOD_GETPICKORDERADDRESSES = "p_PickorderAdresGetJsonStr";
    public static String WEBMETHOD_GETSHIPPINGAGENTS = "p_ExpediteursGetJsonStr";
    public static String WEBMETHOD_GETSHIPPINGAGENTSERVICES = "p_ExpediteurServicesGetJsonStr";
    public static String WEBMETHOD_GETSHIPPINGAGENTSERVICESHPPINGUNITS = "p_ExpediteurServiceVerzendeenhedenGetJsonStr";
    public static String WEBMETHOD_GETSHIPPINGAGENTSERVICESHIPMETHODS = "p_ExpediteurServiceVerzendmethodsGetJsonStr";
    public static String WEBMETHOD_GETPICKORDERSHIPMETHODS = "p_PickorderShippingmethodGetJsonStr";
    public static String WEBMETHOD_GETPICKORDERLINESPACKANDSHIP = "p_PickorderRegelsPackAndShipGetJsonStr";
    public static String WEBMETHOD_GETPICKORDERLINESFINISHSINGLEPIECE = "p_PickorderRegelsFinishPackSinglePieceGetJsonStr";
    public static String WEBMETHOD_GETPICKORDERLINESSTORAGE = "p_PickorderRegelsStorageGetJsonStr";


    public static String WEBMETHOD_CREATEMERGEDPICKORDER = "p_PickorderCreateMergedJsonStr";
    public static String WEBMETHOD_ADDORDERTOMERGEDPICKORDER = "p_PickorderAddToMergedJsonStr";
    public static String WEBMETHOD_REMOVEORDERFROMMERGEDPICKORDER = "p_PickorderRemoveFromMergedJsonStr";
    public static String WEBMETHOD_REMOVEMERGEDPICKORDER = "p_PickorderVerwijderenJsonStr";
    public static String WEBMETHOD_WAREHOUSEOPDRACHTSETTINGSGET = "p_WarehouseopdrachtSettingsGetJsonStr";

    public static String WEBMETHOD_GETPICKORDERSHIPPACKAGES = "p_PickorderShippingPackageGetJsonStr";
    public static String WEBMETHOD_PICKORDERSOURCEDOCUMENTSHIPPED = "p_PickorderSourceDocumentShippedJsonStr";
    public static String WEBMETHOD_PICKORDERSOURCEDOCUMENTSTORED = "p_PickorderEenSourceDocumentOpgeslagenJsonStr";


    public static String WEBMETHOD_PICKORDERUPDATEWORKPLACE = "p_PickorderUpdateWorkplaceJsonStr";
    public static String WEBMETHOD_GETSORTLOCATIONADVICE = "p_WarehouseopdrachtSortLocationAdviceGetJsonStr";
    public static String WEBMETHOD_GETARTICLEIMAGESMULTIPLE = "p_ArtikelenImageGetJsonStr";
    public static String WEBMETHOD_USERLOGIN = "p_GebruikerLoginJsonStr";
    public static String WEBMETHOD_GETITEMPROPERTY = "p_ItemPropertyGetJsonStr";
    public static String WEBMETHOD_GETPROPERTYGROUPS = "p_PropertiegroepenGetJsonStr";
    public static String WEBMETHOD_GETPROPERTYLINEDATA = "p_WarehouseopdrachtPropertiegroepLineDataGetJsonStr";

    public static String WEBMETHOD_GETCOMPOSITEBARCODES= "p_CompositeBarcodesGetJsonStr";



    public static String WEBMETHOD_INVENTORYCREATE = "p_InventoryOrderCreateJsonStr ";
    public static String WEBMETHOD_GETINVENTORYORDERS = "p_InventoryOrdersGetJsonStr";
    public static String WEBMETHOD_GETINVENTORYORDERLINES = "p_InventoryOrderRegelsGetJsonStr";
    public static String WEBMETHOD_GETINVENTORYORDERBINS = "p_InventoryOrderBinsGetJsonStr";
    public static String WEBMETHOD_GETINVENTORYORDERPOSSIBLEBINS = "p_InventoryOrderPossibleBinsGetJsonStr";
    public static String WEBMETHOD_GETINVENTORYORDERCOMMENTS = "p_InventoryOrderCommentsGetJsonStr";
    public static String WEBMETHOD_GETINVENTORYORDERBARCODES = "p_InventoryOrderBarcodesGetJsonStr";
    public static String WEBMETHOD_ADD_BIN = "p_InventoryOrderAddExtraBinJsonStr";
    public static String WEBMETHOD_GETINVENTORYORDERLINEBARCODES = "p_InventoryOrderRegelBarcodesGetJsonStr";
    public static String WEBMETHOD_INVENTORYBARCODECREATE = "p_InventoryBarcodeCreateJsonStr";
    public static String WEBMETHOD_INVENTORYLINECREATE = "p_InventoryRegelCreateJsonStr";
    public static String WEBMETHOD_INVENTORYLINESAVE = "p_InventoryRegelTellingOpslaanJsonStr";
    public static String WEBMETHOD_INVENTORYLINERESET = "p_InventoryRegelResetJsonStr";
    public static String WEBMETHOD_INVENTORYBINCLOSE = "p_InventoryOrderBinCloseJsonStr";
    public static String WEBMETHOD_INVENTORYHANDLED = "p_InventoryOrderHandledJsonStr";
    public static String WEBMETHOD_INVENTORYBINRESET = "p_InventoryBinResetJsonStr";
    public static String WEBMETHOD_INVENTORYBINOPEN = "p_InventoryOrderBinOpenJsonStr";
    public static String WEBMETHOD_GETINTAKEORDERS = "p_ReceiveOrdersGetJsonStr";
    public static String WEBMETHOD_GETINTAKEORDERMATLINES = "p_ReceiveMATRegelsGetJsonStr";
    public static String WEBMETHOD_GETINTAKEORDERMATLINEBARCODES = "p_ReceiveMATRegelBarcodesGetJsonStr";
    public static String WEBMETHOD_GETINTAKEARCODES = "p_ReceiveOrderBarcodesGetJsonStr";
    public static String WEBMETHOD_GETTAKEINTAKEARCODES = "p_ReceiveOrderRegelTakeBarcodesGetJsonStr";
    public static String WEBMETHOD_INTAKELINEMATRESET = "p_ReceiveMATRegelResetJsonStr";
    public static String WEBMETHOD_GETINTAKEORDERLINES = "p_ReceiveOrderScannerRegelsInTakeGetJsonStr";
    public static String WEBMETHOD_GETINTAKEORDERITEMS = "p_ReceiveOrderItemInTakeGetJsonStr";
    public static String WEBMETHOD_RECEIVEITEMVARIANTCREATE = "p_ReceiveInTakeWarehouseopdrachtItemVariantCreateJsonStr";
    public static String WEBMETHOD_RECEIVESTOREITEMVARIANTCREATE = "p_ReceiveStoreWarehouseopdrachtItemVariantCreateJsonStr";
    public static String WEBMETHOD_RECEIVEBARCODECREATE =  "p_ReceiveBarcodeCreateJsonStr";
    public static String WEBMETHOD_RECEIVEINTAKEITEM =  "p_ReceiveInTakeItemJsonStr";
    public static String WEBMETHOD_RECEIVELINRESET = "p_ReceiveInTakeRegelResetJsonStr";
    public static String WEBMETHOD_CUSTOMAUTHORISATION = "p_CustomAutorisatieGetJsonStr";

    public static String WEBMETHOD_INTAKELINEMATHANDLED = "p_ReceiveMATLineHandledJsonStr";
    public static String WEBMETHOD_INTAKELINEMATHANDLEDPART = "p_ReceiveRegelHandledPartPlaceMATJsonStr";
    public static String WEBMETHOD_INTAKEHANLED= "p_ReceiveMATOrderHandledJsonStr";
    public static String WEBMETHOD_INTAKEITEMHANLED= "p_ReceiveMATItemHandledJsonStr";
    public static String WEBMETHOD_INTAKESTOREITEMHANLED= "p_ReceiveStoreItemJsonStr";
    public static String WEBMETHOD_RECEIVECREATE = "p_ReceiveOrderCreateJsonStr";
    public static String WEBMETHOD_RECEIVEINVALIDATE = "p_ReceiveVervallenJsonStr";
    public static String WEBMETHOD_RECEIVEHANDLED = "p_ReceiveInTakeOrderHandledJsonStr";
    public static String WEBMETHOD_PACKAGINGHANDLED = "p_WarehouseopdrachtEmballageUpdateJsonStr";
    public static String WEBMETHOD_PACKAGINGGET = "p_WarehouseopdrachtEmballageGetJsonStr";
    public static String WEBMETHOD_STOCKOWNERGET = "p_StockOwnerGetJsonStr";


    public static String WEBMETHOD_GETRECEIVEBINS = "p_MagazijnlocatieReceiveGetJsonStr";
    public static String WEBMETHOD_GETSHIPBINS = "p_MagazijnlocatieShipGetJsonStr";

    public static String WEBMETHOD_RETURNORDERSGET = "p_RetourOrdersGetJsonStr";
    public static String WEBMETHOD_RETURNLINESGET = "p_RetourOrderItemInTakeGetJsonStr";
    public static String WEBMETHOD_RETURNLINERESET = "p_RetourInTakeRegelResetJsonStr";
    public static String WEBMETHOD_RETURNBARCODEGET = "p_RetourOrderBarcodesGetJsonStr";
    public static String WEBMETHOD_RETURNORDERUPDATE = "p_RetourOrderUpdateJsonStr";
    public static String WEBMETHOD_RETURNBARCODECREATE = "p_RetourBarcodeCreateJsonStr";
    public static String WEBMETHOD_RETURNLINESAVE = "p_RetourInTakeItemJsonStr";
    public static String WEBMETHOD_RETURNORDERHANDLED = "p_RetourInTakeOrderHandledJsonStr";
    public static String WEBMETHOD_RETURNORDERCREATE = "p_RetourOrderCreateJsonStr";
    public static String WEBMETHOD_RETURNLINESCANNEDGET = "p_RetourOrderScannedRegelsInTakeGetJsonStr";
    public static String WEBMETHOD_RETURNORDERDISPOSED = "p_RetourVervallenJsonStr";
    public static String WEBMETHOD_RETURNLINESCANNEDBARCODESGET = "p_RetourOrderRegelTakeBarcodesGetJsonStr";
    public static String WEBMETHOD_RETURNORDERCOMMENTSGET = "p_RetourOrderCommentsGetJsonStr";
    public static String WEBMETHOD_RETURNCREATEITEMVARIANT = "p_RetourInTakeWarehouseopdrachtItemVariantCreateJsonStr";
    public static String WEBMETHOD_GETWAREHOUSEREASONS = "p_MagazijnRedenenGetJsonStr";

    public static String WEBMETHOD_MOVEBARCODECREATE = "p_MoveBarcodeCreateJsonStr";
    public static String WEBMETHOD_MOVEHANDLED = "p_MoveOrderHandledJsonStr";
    public static String WEBMETHOD_MOVEORDERCREATE = "p_MoveOrderCreateJsonStr";
    public static String WEBMETHOD_PICKORDERCREATE = "p_PickorderCreateJsonStr";
    public static String WEBMETHOD_PICKORDERBARCODECREATE = "p_PickorderBarcodeCreateJsonStr";
    public static String WEBMETHOD_PRINTBINLABEL = "p_MagazijnlocatieLabelPrintJsonStr";
    public static String WEBMETHOD_PRINTLINEITEMLABEL = "p_LineArtikelLabelPrintJsonStr";
    public static String WEBMETHOD_PRINTITEMLABEL = "p_ArtikelLabelPrintJsonStr";

    public static String WEBMETHOD_GETMOVEORDERLINES = "p_MoveRegelsGetJsonStr";
    public static String WEBMETHOD_GETMOVEORDERBARCODES = "p_MoveOrderBarcodesGetJsonStr";
    public static String WEBMETHOD_GETMOVEORDERLINEBARCODES = "p_MoveRegelBarcodesGetJsonStr";
    public static String WEBMETHOD_GETMOVEORDERCOMMENTS = "p_MoveOrderCommentsGetJsonStr";
    public static String WEBMETHOD_GETPACKANDSHIPORDERCOMMENTS = "p_PackAndShipOrderCommentsGetJsonStr";
    public static String WEBMETHOD_GETMOVEORDERS = "p_MoveOrdersGetJsonStr";
    public static String WEBMETHOD_MOVELINERESET = "p_MoveRegelResetJsonStr";
    public static String WEBMETHOD_CREATEMOVEORDERBARCODES = "p_MoveBarcodeCreateJsonStr";
    public static String WEBMETHOD_MOVEORDERUPDATECURRENTLOCATIION = "p_MoveOrderUpdateCurrentOrderLocationJsonStr";
    public static String WEBMETHOD_GETBRANCHBINS = "p_MagazijnlocatieGetJsonStr";
    public static String WEBMETHOD_MOVEORDERLINE_HANDLEDTAKEMT = "p_MoveRegelHandledTakeMTJsonStr";
    public static String WEBMETHOD_MOVEORDERLINE_HANDLEDPLACEMT = "p_MoveRegelHandledPlaceMTJsonStr";
    public static String WEBMETHOD_MOVEITEM_HANDLED = "p_MoveItemHandledJsonStr";
    public static String WEBMETHOD_MOVEPICKHANDLED = "p_MovePickHandledJsonStr";
    public static String WEBMETHOD_GETPACKANDSHIPORDERS = "p_PackAndShipOrdersGetJsonStr";
    public static String WEBMETHOD_PACKANDSHIPORDERCREATE = "p_PackAndShipOrderCreateJsonStr";
    public static String WEBMETHOD_GETPACKANDSHIPORDERLINES = "p_PackAndShipRegelsGetJsonStr";
    public static String WEBMETHOD_GETPACKANDSHIPBARCODES = "p_PackAndShipOrderBarcodesGetJsonStr";
    public static String WEBMETHOD_GETPACKANDSHIPSHIPMENTS = "p_PackAndShipShipmentsGetJsonStr";
    public static String WEBMETHOD_GETPACKANDSHIPADDRESSES = "p_PackAndShipOrderAdresGetJsonStr";
    public static String WEBMETHOD_GETPACKANDSHIPSHIPPINGMETHODS = "p_PackAndShipOrderShippingmethodGetJsonStr";
    public static String WEBMETHOD_GETPACKANDSHIPSHIPPINPACKAGES = "p_PackAndShipOrderShippingPackageGetJsonStr";
    public static String WEBMETHOD_GETSOURCEDOCUMENTSHIPPINGDATA = "p_SourceDocShippingDataGetJsonStr";
    public static String WEBMETHOD_CREATEPACKANDSHIPSHIPMENT = "p_PackAndShipOrderCreateShipmentJsonStr";
    public static String WEBMETHOD_PACKANDSHIPSHIPMENTSHIPPED= "p_PackAndShipShipmentShippedJsonStr";
    public static String WEBMETHOD_PACKANDSHIPSHIPMENTHANDLED = "p_PackAndShipShipmentHandledJsonStr";
    public static String WEBMETHOD_PACKANDSHIPHANDLED = "p_PackAndShipOrderHandledJsonStr";
    public static String WEBMETHOD_PACKANDSHIPDELETE = "p_PackAndShipVervallenJsonStr";
    public static String WEBMETHOD_RECEIVEDELETE = "p_ReceiveVervallenJsonStr";

    public static String WEBMETHOD_PACKANDSHIPUPDATE = "p_PackAndShipOrderUpdateJsonStr";
    public static String WEBMETHOD_PACKANDSHIPSHIPMENTADDDOCUMENT = "p_PackAndShipShipmentDocumentAddJsonStr";

    public static String WEBMETHOD_WAREHOUSEOPDRACHTLINEITEMPROPERTIESGET = "p_WarehouseopdrachtLineItemPropertiesGetJsonStr";
    public static String WEBMETHOD_WAREHOUSEOPDRACHTLINEITEMPROPERTIEVALUESGET = "p_WarehouseopdrachtLineItemPropertyValuesGetJsonStr";



       //endregion Methods

    //region Properties
    public static String WEBPROPERTY_USER = "pv_GebruikerStr";
    public static String WEBPROPERTY_USERNAME = "pv_UsernameStr";
    public static String WEBPROPERTY_USERNAMEDUNGLISH = "pv_GebruikersnameStr";
    public static String WEBPROPERTY_USERNAMEDUTCH = "pv_GebruikersnaamStr";
    public static String WEBPROPERTY_IDENTIFIER = "pv_IdentifierStr";
    public static String WEBPROPERTY_LOCATION_NL = "pv_VestigingStr";
    public static String WEBPROPERTY_LICENSE = "pv_LicentieStr";
    public static String WEBPROPERTY_INPROGRESS = "pv_InBehandelingBln";
    public static String WEBPROPERTY_SEARCHTEXT = "pv_ZoekTekstStr";
    public static String WEBPROPERTY_MAINTYPE = "pv_MainTypeStr";
    public static String WEBPROPERTY_ORDERNUMBER = "pv_OpdrachtnummerStr";
    public static String WEBPROPERTY_ORDERNUMBERMERGED = "pv_OpdrachtnummerMergedStr";
    public static String WEBPROPERTY_ORDERNUMBERTOADD = "pv_OpdrachtnummerToAddStr";
    public static String WEBPROPERTY_ORDERNUMBERTOREMOVE = "pv_OpdrachtnummerToRemoveStr";
    public static String WEBPROPERTY_SOURCEDOCUMENT = "pv_SourceDocumentStr";
    public static String WEBPROPERTY_SOURCENO = "pv_SourceNoStr";
    public static String WEBPROPERTY_INCLUDEIMAGE = "pv_IncludeImageBln";
    public static String WEBPROPERTY_SETTINGTYPE = "pv_SettingTypeEnu";

    public static String WEBPROPERTY_ACTIONTYPECODE = "pv_ActiontypecodeStr";
    public static String WEBPROPERTY_SCANNER = "pv_ScannerStr";
    public static String WEBPROPERTY_BATCHBINCODE = "pv_BatchBinCodeStr";
    public static String WEBPROPERTY_SCANNERID = "pv_ScannerIdStr";
    public static String WEBPROPERTY_DEVICEBRAND = "pv_DeviceBrandStr";
    public static String WEBPROPERTY_DEVICETYPE = "pv_DeviceTypeStr";
    public static String WEBPROPERTY_SCANNERVERSION = "pv_ScannerVersionStr";
    public static String WEBPROPERTY_SCANNERCONFIGURATION = "pv_ScannerConfigurationStr";
    public static String WEBPROPERTY_LANGUAGEASCULTURE = "pv_LanguageAsCultureStr";
    public static String WEBPROPERTY_ORDERTYPE = "pv_OpdrachtsoortStr";
    public static String WEBPROPERTY_WORKFLOWSTEPSTR = "pv_WorkflowStapStr";
    public static String WEBPROPERTY_WORKFLOWSTEPINT = "pv_WorkflowStapInt";
    public static String WEBPROPERTY_SHIPBINSTR = "pv_ShipBinCodeStr";
    public static String WEBPROPERTY_WORKFLOWSTEPCODESTR = "pv_WorkflowStapcodeStr";
    public static String WEBPROPERTY_IGNOREBUSY = "pv_IgnoreBezigBln";
    public static String WEBPROPERTY_BINSUBSET = "pv_BinSubsetObl";
    public static String WEBPROPERTY_OWNER = "pv_OwnerStr";
    public static String WEBPROPERTY_ITEMNO = "pv_ItemNoStr";
    public static String WEBPROPERTY_ITEMPROPERTYSEQUENCENO = "pv_ItemPropertySequenceNoLng";
    public static String WEBPROPERTY_DESTINATIONNO = "pv_DestinationNoStr";
    public static String WEBPROPERTY_VARIANTCODE = "pv_VariantCodeStr";
    public static String WEBPROPERTY_VARIANTCODETINY = "pv_VariantcodeStr";
    public static String WEBPROPERTY_REFRESH = "Pv_RefreshBln";
    public static String WEBPROPERTY_LINENO= "pv_LineNoLng";
    public static String WEBPROPERTY_LINENOTAKE = "pv_LineNoTakeLng";
    public static String WEBPROPERTY_LINENOPLACE = "pv_LineNoPlaceLng";
    public static String WEBPROPERTY_HANDLEDTIMESTAMP = "pv_HandledTimestampDat";
    public static String WEBPROPERTY_PICKFROMCONTAINER = "pv_PickenUitContainerStr";
    public static String WEBPROPERTY_BARCODESLIST = "pv_BarcodesObl";
    public static String WEBPROPERTY_PACKAGINGLIST = "pv_EmballageObl";
    public static String WEBPROPERTY_CONTAINERSLIST = "pv_ContainersObl";
    public static String WEBPROPERTY_PROCESSINGSEQUENCE = "pv_ProcessingSequenceStr";
    public static String WEBPROPERTY_WORKPLACE = "pv_WorkplaceStr";
    public static String WEBPROPERTY_WORKSPACE = "pv_WorkspaceStr";
    public static String WEBPROPERTY_CULTURE = "pv_CultureStr";
    public static String WEBPROPERTY_ORDERLOCATION = "pv_OrderLocationStr";
    public static String WEBPROPERTY_CURRENTLOCATION = "pv_OrderCurrentLocationStr";
    public static String WEBPROPERTY_PICKSTEP = "pv_PickStapInt";
    public static String WEBPROPERTY_NUMBER = "pv_AantalDbl";
    public static String WEBPROPERTY_PROPERTIESHANDLED = "pv_PropertiesHandledObl";
    public static String WEBPROPERTY_PROPERTIEGROUP = "pv_PropertiegroepStr";
    public static String WEBPROPERTY_LOCATION = "pv_LocationStr";
    public static String WEBPROPERTY_SHIPPINGAGENT = "pv_ShippingAgentStr";
    public static String WEBPROPERTY_SHIPPINGSERVICE = "pv_ShippingServiceStr";
    public static String WEBPROPERTY_SHIPPINGOPTIONS = "pv_ShippingOptionObl";
    public static String WEBPROPERTY_SHIPPINGPACKAGES = "pv_ShippingPackageObl";
    public static String WEBPROPERTY_ITEMSLIST = "pv_ItemsObl";
    public static String WEBPROPERTY_PASSWORD = "pv_PasswordStr";
    public static String WEBPROPERTY_BINCODE = "pv_BinCodeStr";
    public static String WEBPROPERTY_BINCODENL = "pv_MagazijnlocatieStr";
    public static String WEBPROPERTY_PRINTBINCODENL = "pv_LocatieStr";
    public static String WEBPROPERTY_BINCODETINY = "pv_BincodeStr";
    public static String WEBPROPERTY_BINCODEHANDLED = "pv_BincodeHandledStr";
    public static String WEBPROPERTY_STOCKOWNER = "pv_StockOwnerStr";
    public static String WEBPROPERTY_STORAGEBINCODE = "pv_StorageBinCodeStr";
    public static String WEBPROPERTY_WORKFLOW = "pv_WorkflowStr";
    public static String WEBPROPERTY_SHIPMENT = "pv_ShipmentStr";
    public static String WEBPROPERTY_DOCUMENT = "pv_DocumentStr";
    public static String WEBPROPERTY_DOCUMENT2= "pv_Document2Str";
    public static String WEBPROPERTY_EXTERNALREFERENCE = "pv_ExternalReferenceStr";
    public static String WEBPROPERTY_ORDERCONTAINER = "pv_OrderContainerStr";
    public static String WEBPROPERTY_INVENTORYBARCODECHECK = "pv_InventoryBarcodeCheckBln";
    public static String WEBPROPERTY_ADMINISTRATION = "pv_AdministrationStr";
    public static String WEBPROPERTY_BARCODETYPE = "pv_BarCodeTypeInt";
    public static String WEBPROPERTY_ISUNIQUEBARCODE = "pv_IsUniqueBarcodeBln";
    public static String WEBPROPERTY_QUANTITYPERUNITOFMEASURE = "pv_QtyPerUnitOfMeasureDbl";
    public static String WEBPROPERTY_UNITOFMEASURE = "pv_UnitOfMeasureStr";
    public static String WEBPROPERTY_ITEMTYPE = "pv_ItemsoortStr";
    public static String WEBPROPERTY_ORIGINNO = "pv_OriginNoStr";
    public static String WEBPROPERTY_RECEIVEBIN = "pv_ReceiveBinStr";
    public static String WEBPROPERTY_RECEIVEBARCODECHECK = "pv_ReceiveBarcodeCheckBln";
    public static String WEBPROPERTY_CONTAINER = "pv_ContainerStr";
    public static String WEBPROPERTY_PROPERTIESHANDLEDLIST = "pv_PropertiesHandledObl";
    public static String WEBPROPERTY_ACTIONTYPECODE_CAMELCASE = "pv_ActionTypeCodeStr";
    public static String WEBPROPERTY_CURRENTLOCATION_SHORT = "pv_CurrentLocationStr";
    public static String WEBPROPERTY_PICKBARCODECHECK = "pv_PickBarcodeCheckBln";
    public static String WEBPROPERTY_MOVEBARCODECHECK = "pv_MoveBarcodeCheckBln";
    public static String WEBPROPERTY_MIBATCHTRAKEBIN = "pv_MiBatchTakeBinStr";

    public static String WEBPROPERTY_RETURNBIN = "pv_RetourBinStr";
    public static String WEBPROPERTY_RETURNBARCODECHECK = "pv_RetourBarcodeCheckBln";
    public static String WEBPROPERTY_RETURNMULTIPLEDOCUMENTS = "pv_RetourMultipleDocumentsBln";
    public static String WEBPROPERTY_RETURNREASONNL = "pv_RetourredenStr";
    public static String WEBPROPERTY_RETURNREASONBIGNL = "pv_RetourRedenStr";
    public static String WEBPROPERTY_RECEIVEDDAT = "pv_ReceivedDat";
    public static String WEBPROPERTY_REASON = "pv_ReasonStr";

    public static String WEBPROPERTY_BINLABELTEMPLATE = "pv_BinLabelTemplateStr";
    public static String WEBPROPERTY_ITEMLABELTEMPLATE = "pv_ItemLabelTemplateStr";
    public static String WEBPROPERTY_PRINTQUANTITY = "pv_PrintQuantityLng";

    //endregion Properties

    //region temporary Properties
    public static String WEBPROPERTY_BARCODE = "pv_BarcodeStr";
    public static String WEBPROPERTY_BARCODEORIGINAL = "pv_BarcodeOrigineelStr";
    public static String WEBPROPERTY_BARCODELIST = "pv_BarcodesObl";
    public static String WEBPROPERTY_QUANTITYHANDLED = "pv_QuantityHandledDbl";
    public static String WEBPROPERTY_QUANTITYCHECKED = "pv_QuantityCheckedDbl";
    //endregion temporary Properties

    //region complex types
    public static String WEBPROPERTY_INTERFACESHIPPINGOPTION = "c_InterfaceShippingOptionIeso";
    public static String WEBPROPERTY_INTERFACESHIPPINGPACKAGE = "c_InterfaceShippingPackageIesp";
    public static String WEBPROPERTY_INTERFACESHIPPINGPACKAGE_PACKAGE = "g_PackageTypeStr";
    public static String WEBPROPERTY_INTERFACESHIPPINGPACKAGE_SEQUENCENUMBER = "g_SequenceNumberInt";
    public static String WEBPROPERTY_INTERFACESHIPPINGPACKAGE_WEIGHTING = "g_WeightinGLng";
    public static String WEBPROPERTY_INTERFACESHIPPINGPACKAGE_ITEMCOUNT = "g_ItemcountDbl";
    public static String WEBPROPERTY_INTERFACESHIPPINGPACKAGE_CONTAINERTYPE = "g_ContainersoortStr";
    public static String WEBPROPERTY_INTERFACESHIPPINGPACKAGE_CONTAINER = "g_ContainerStr";

    public static String WEBPROPERTY_INTERFACESPROPERTY_OPTIONCODE = "g_OptionStr";
    public static String WEBPROPERTY_INTERFACESPROPERTY_VALUECODE = "g_ValueStr";
    public static String WEBPROPERTY_INTERFACESPROPERTY_PROPERTYCODE = "g_PropertyCodeStr";
    public static String WEBPROPERTY_REQUEST_PROPERTYCODE = "pv_PropertyCodeStr";
    public static String WEBPROPERTY_INTERFACESPROPERTY_PROPERTYVALUE = "pv_PropertyValueStr";
    public static String WEBPROPERTY_INTERFACESPROPERTY_SEQUENCENOHANDLED = "g_SequenceNoHandledLng";
    public static String WEBPROPERTY_INTERFACESPROPERTY_VALUEHANDLED = "g_ValueHandledStr";

    public static String WEBPROPERTY_BARCODEHANDLED_COMPLEX = "c_BarcodeHandledUwbh";
    public static String WEBPROPERTY_PACKAGINGHANDLED_COMPLEX = "c_EmballageHandledUweh";
    public static String WEBPROPERTY_PROPERTYHANDLED_COMPLEX = "c_PropertyHandledUwph";
    public static String WEBPROPERTY_BARCODE_COMPLEX = "g_BarcodeStr";
    public static String WEBPROPERTY_QUANTITYHANDLED_COMPLEX = "g_QuantityHandledDbl";
    public static String WEBPROPERTY_CONTAINERSEQUENCENO_COMPLEX = "g_ContainerSequenceNoLng";
    public static String WEBPROPERTY_CONTAINERHANDLED_COMPLEX = "c_ContainerHandledUwch";

    public static String WEBPROPERTY_ARTICLEINPUT_COMPLEX = "c_ArtikelInputUari";
    public static String WEBPROPERTY_ITEMNO_COMPLEX = "g_ItemNoStr";
    public static String WEBPROPERTY_VARIANTCODE_COMPLEX = "g_VariantCodeStr";

    public static String WEBPROPERTY_PACKAGING_COMPLEX = "g_EmballageStr";
    public static String WEBPROPERTY_QUANTITY_IN_TAKE_COMPLEX = "g_QuantityInTakeDbl";
    public static String WEBPROPERTY_QUANTITY_SHIPPED_COMPLEX = "g_QuantityShippedDbl";

    //endregion complex types

    //region cWebservice defaults
    public static String SUCCESBLN_NAMESTR = "vSuccessBln";
    public static String RESULTBLN_NAMESTR = "vResultBln";
    public static String RESULTLNG_NAMESTR = "vResultLng";
    public static String RESULTOBL_NAMESTR = "vResultsObl";
    public static String RESULTDTT_NAMESTR = "vResultDtt";
    //endregion cWebservice defaults

}
