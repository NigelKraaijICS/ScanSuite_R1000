package SSU_WHS.General;

public class cDatabase {
    //region Tables

    public static final String TABLENAME_USERS = "Users";
    public static final String TABLENAME_SETTINGS = "Settings";
    public static final String TABLENAME_SCANNERLOGON = "ScannerLogon";
    public static final String TABLENAME_BARCODELAYOUTS = "BarcodeLayouts";
    public static final String TABLENAME_WORKPLACE = "Workplaces";
    public static final String TABLENAME_BRANCH = "Branches";
    public static final String TABLENAME_WAREHOUSELOCATIONS = "WarehouseLocations";
    public static final String TABLENAME_ITEMPROPERTY = "ItemProperties";
    public static final String TABLENAME_PICKORDERBARCODE = "PickorderBarcode";
    public static final String TABLENAME_PICKORDERLINEBARCODE = "PickorderLineBarcode";
    public static final String TABLENAME_ARTICLEIMAGE = "ArticleImage";
    public static final String TABLENAME_COMMENT = "Comment";
    public static final String TABLENAME_AUTHORISATIONS = "Authorisations";
    public static final String TABLENAME_SALESORDERPACKINGTABLE = "SalesOrderPackingTable";
    public static final String TABLENAME_PICKORDERADDRESS = "PickorderAddress";
    public static final String TABLENAME_SHIPPINGAGENTS = "ShippingAgents";
    public static final String TABLENAME_SHIPPINGAGENTSERVICES = "ShippingAgentServices";
    public static final String TABLENAME_SHIPPINGAGENTSERVICESHIPPINGUNITS = "ShippingAgentServiceShippingUnits";
    public static final String TABLENAME_SHIPPINGAGENTSERVICESHIPMETHODS = "ShippingAgentServiceShipMethods";
    public static final String TABLENAME_PICKORDERSHIPMETHODS = "PickorderShipMethods";
    public static final String TABLENAME_PICKORDERSHIPPACKAGES = "PickorderShipPackages";
    public static final String TABLENAME_PICKORDERLINEPACKANDSHIP = "PickorderLinePackAndShip";
    //endregion Tables

    //region localtables
    public static final String TABLENAME_ENVIRONMENTS = "Environments";
    //endregion localtables


    //region A
    public static final String ASSIGNEDUSERID_NAMESTR = "AssignedUserId";
    public static final String AUTHORISATION_NAMESTR = "Autorisatie";
    public static final String ADDRESSCODE_NAMESTR = "Adrescode";
    public static final String ADDRESS_NAMESTR = "Adres";
    public static final String ADDRESSADDITION_NAMESTR = "Adrestoevoeging";
    public static final String ADDRESSNUMBER_NAMESTR = "Huisnummer";
    public static final String ADDRESSNUMBERADDITION_NAMESTR = "Huisnummertoevoeging";
    public static final String ACTUALSHIPPINGAGENTCODE_NAMESTR = "ActualShippingAgentCode";
    public static final String ACTUALSHIPPINGAGENTSERVICECODE_NAMESTR = "ActualShippingAgentServiceCode";
    //endregion A

    //region B
    public static final String BARCODE_NAMESTR = "Barcode";
    public static final String BARCODELAYOUT_NAMESTR = "Barcodelayout";
    public static final String BARCODETYPE_NAMESTR = "BarCodeType";

    public static final String BINCODE_NAMESTR = "BinCode";
    public static final String BINMANDATORY_NAMESTR = "BinMandatory";
    public static final String BINTYPE_NAMESTR = "BinType";
    public static final String BRANCH_NAMESTR = "Vestiging";
    public static final String BRANCHTYPE_NAMESTR = "Vestigingtype";
    public static final String BRANCHNAME_NAMESTR = "Name";
    //endregion B

    //region C
    public static final String COMMENTLINENO_NAMESTR = "CommentLineNo";
    public static final String CURRENTLOCATION_NAMESTR = "CurrentLocation";
    public static final String COMMENTCODE_NAMESTR = "CommentCode";
    public static final String COMMENTTEXT_NAMESTR = "CommentText";
    public static final String COMPONENT10_NAMESTR = "Component10";
    public static final String CONTAINER_NAMESTR = "Container";
    public static final String CONTAINERHANDLED_NAMESTR = "ContainerHandled";
    public static final String CONTAINERINPUT_NAMESTR = "ContainerInput";
    public static final String CONTAINERTYPE_NAMESTR = "ContainerType";
    public static final String CURRENTUSERID_NAMESTR = "CurrentUserId";
    public static final String CITY_NAMESTR = "Plaats";
    public static final String COUNTRY_NAMESTR = "Land";
    public static final String CONTAINERTYPE_DUTCH_NAMESTR = "Containersoort";
    //endregion C

    //region D
    public static final String DESCRIPTION_DUTCH_NAMESTR = "Omschrijving";
    public static final String DESCRIPTION_NAMESTR = "Description";
    public static final String DESCRIPTION2_NAMESTR = "Description2";
    public static final String DESTINATIONNO_NAMESTR = "DestinationNo";
    public static final String DOCUMENT_NAMESTR = "Document";
    public static final String DOCUMENTTYPE_NAMESTR = "DocumentType";
    public static final String DOCUMENT2_NAMESTR = "Document2";
    public static final String DOCUMENTTYPE2_NAMESTR = "DocumentType2";
    public static final String DEFAULTWEIGHTINGRAMS_NAMESTR = "DefaultWeightInG";
    public static final String DEFAULTVALUE_NAMESTR = "DefaultValue";
    public static final String DELIVERYADDRESSTYPE_NAMESTR = "DeliveryAddressType";
    public static final String DELIVERYADDRESSCODE_NAMESTR = "DeliveryAddressCode";
    //endregion D


    //region E
    public static final String EXTERNALREFERENCE_NAMESTR = "ExternalReference";
    public static final String ENUMERATIONVALUES_NAMESTR = "EnumerationValues";

    public static final String ENVIRONMENT_DEFAULT = "IsDefault";
    public static final String ENVIRONMENT_NAME = "Name";
    public static final String ENVIRONMENT_DESCRIPTION = "Description";
    public static final String ENVIRONMENT_WEBSERVICEURL = "Webserviceurl";
    //endregion E

    //Region I
    public static final String IMAGE_NAMESTR = "Image";
    public static final String IMPORTFILE_NAMESTR = "Importbestand";
    public static final String INTERFACERESULTMETHOD_NAMESTR = "Interface_result_method";
    public static final String ISPARTOFMULTILINEORDER_NAMESTR = "IsPartOfMultiLineOrder";
    public static final String ISUNIQUEBARCODE_NAMESTR = "IsUniqueBarcode";
    public static final String ISUNIQUE_NAMESTR = "IsUnique";
    public static final String ITEMNO_NAMESTR = "ItemNo";
    //endregion I

    //region L
    public static final String LAYOUTVALUE_NAMESTR = "Layout";
    public static final String LICENSE_NAMESTR = "License";
    public static final String LICENSE_NL_NAMESTR = "Licentie";
    public static final String LINENO_NAMESTR = "LineNo";
    public static final String LINENOTAKE_NAMESTR = "LineNoTake";
    public static final String LOCALSTATUS_NAMESTR = "LocalStatus";
    //endregion L

    //region M
    public static final String MOVEDEFAULTBIN_NAMESTR = "Move_default_bin";
    //endregion M

    //region N
    public static final String NAME_NAMESTR = "Naam";
    public static final String NAMEADDITION_NAMESTR = "Naamtoevoeging";

    public static final String NUMBEROFBINS_NAMESTR = "Aantal_bins";
    //endregion N

    //region O
    public static final String ORDER_NAMESTR = "Volgorde";
    public static final String OMSCHRIJVING_NAMESTR = "Omschrijving";
    public static final String ORDERNUMBER_NAMESTR = "Opdrachtnummer";
    public static final String ORDERTYPE_NAMESTR = "Opdrachttype";
    //endregion O

    //region P
    public static final String PACKAGETYPE_NAMESTR = "PackageType";
    public static final String PACKAGESEQUENCENUMBER_NAMESTR = "PackageSequenceNumber";
    public static final String PICKDEFAULTREJECTREASON = "Pick_default_reject_reden";
    public static final String PICKDEFAULTSTORAGEBIN = "Pick_default_storage_bin";
    public static final String PICKREJECTDURINGPICK_NAMESTR = "Pick_Reject_during_pick";
    public static final String PICKREJECTDURINGSORT_NAMESTR = "Pick_Reject_during_sort";
    public static final String PICKSALESASKWORKPLACE_NAMESTR = "Pick_Sales_ask_workplace";
    public static final String PICKTRANSFERASKWORKPLACE_NAMESTR = "Pick_Transfer_ask_workplace";
    public static final String PICKBARCODECHECK_NAMESTR = "Pick_Barcode_check";
    public static final String PICKPICKPVVKKOEACHPIECE_NAMESTR = "Pick_Pick_PV_VKO_each_piece";
    public static final String PICKPICKTOCONTAINER_NAMESTR = "Pick_Pick_to_container";
    public static final String PICKPICKTOCONTAINERTYPE_NAMESTR = "Pick_Pick_to_container_type";
    public static final String PICKPRINTADDRESSLABEL_NAMESTR = "Pick_Print_adreslabel";
    public static final String PICKPRINTCONTENTLABEL_NAMESTR = "Pick_Print_contentlabel";
    public static final String PICKPRINTWITHPICTURE_NAMESTR = "Pick_with_picture";
    public static final String PICKPRINTWITHPICTURE_AUTO_OPEN_NAMESTR = "Pick_with_picture_auto_open";
    public static final String PICKPRINTWITHPICTURE_PREFETCH_NAMESTR = "Pick_with_picture_prefetch";
    public static final String PICKACTIVITYBINREQUIRED_NAMESTR = "Pick_Activity_bin_required";

    public static final String PRINTDOCUMENTS_NAMESTR = "PrintDocuments";
    public static final String PROCESSINGSEQUENCE_NAMESTR = "ProcessingSequence";
    public static final String PROPERTY_NAMESTR = "Property";
    public static final String PICKORDERLINEBARCODE_LINENO = "LineNo";
    public static final String PICKORDERLINEBARCODE_BARCODE = "Barcode";
    public static final String PICKORDERLINEBARCODE_QUANTITY = "Quantity";
    public static final String PICKORDERLINEBARCODE_ISMANUAL = "IsManual";
    //endregion P

    //region Q
    public static final String QUANTITY_NAMESTR = "Quantity";
    public static final String QUANTITYHANDLED_NAMESTR = "QuantityHandled";
    public static final String QUANTITYHANDLEDSUM_NAMESTR = "QuantityHandledSum";
    public static final String QUANTITYPERUNITOFMEASURE_NAMESTR = "QtyPerUnitOfMeasure";
    public static final String QUANTITYREJECTED_NAMESTR = "QuantityRejected";
    public static final String QUANTITYTOTAL_NAMESTR = "QuantityTotal";
    public static final String QUANTITYTAKEN_NAMESTR = "QuantityTaken";
    //endregion Q

    //region R
    public static final String RECEIVEDEFAULTBIN_NAMESTR = "Receive_default_bin";
    public static final String REQUESTENDDATETIME_NAMESTR = "RequestedEndDateTime";
    public static final String REMEMBERVALUE_NAMESTR = "RememberValue";
    public static final String RETURNDEFAULTBIN_NAMESTR = "Retour_default_bin";
    //endregion R

    //region S
    public static final String SHIPDEFAULTBIN_NAMESTR = "Ship_default_bin";
    public static final String SHIPPINGADVICE_NAMESTR = "ShippingAdvice";
    public static final String SETTING_NAMESTR = "Instelling";
    public static final String SINGLEARTICLEORDERS_NAMESTR = "SingleArticleOrders";
    public static final String STATUS_NAMESTR = "Status";
    public static final String STATUSSHIPPING_NAMESTR = "StatusShipping";
    public static final String STATUSPACKING_NAMESTR = "StatusPacking";
    public static final String STATUSPRINTATSTART_NAMESTR = "StatusPrintAtStart";
    public static final String STOCKOWNER_NAMESTR = "StockOwner";
    public static final String SORTORDER_NAMESTR = "Volgorde";
    public static final String SORTING_NAMESTR = "Sortering";
    public static final String SOURCEDOCUMENT_NAMESTR = "SourceDocument";
    public static final String SOURCENO_NAMESTR = "SourceNo";
    public static final String STORAGEBINCODE_NAMESTR = "StorageBinCode";
    public static final String STORESOURCEORDER_NAMESTR = "StoreSourceOpdracht";
    public static final String STREET_NAMESTR = "Straat";
    public static final String SEQUENCE_NAMESTR = "Order";
    public static final String SHIPPINGAGENT_NAMESTR = "Expediteur";
    public static final String SERVICE_NAMESTR = "Service";
    public static final String SERVICECOUNTRIES_NAMESTR = "ServiceCountries";
    public static final String SHIPPINGUNIT_NAMESTR = "Verzendeenheid";
    public static final String SHIPPINGMETHOD_NAMESTR = "Verzendmethode";
    public static final String SHIPPINGMETHODCODE_NAMESTR = "ShippingMethodCode";
    public static final String SHIPPINGMETHODVALUE_NAMESTR = "ShippingMethodValue";
    public static final String SHIPPINGUNITQUANTITYUSED_NAMESTR = "Verzendmethode";
    public static final String SHIPPINGAGENTCODE_NAMESTR = "ShippingAgentCode";
    public static final String SHIPPINGAGENTSERVICECODE_NAMESTR = "ShippingAgentServiceCode";
    public static final String SHOWONTERMINAL_NAMESTR = "ShowOnTerminal";
    //endregion S

    //region T
    public static final String TAKENTIMESTAMP_NAMESTR = "TakenTimestamp";
    //endregion T


    //region U
    public static final String USEFORSTORAGE_NAMESTR = "UseForStorage";
    public static final String USEFORRETURNSALES_NAMESTR = "UseForReturnSales";
    public static final String UNIQUNESS_NAMESTR = "Uniqueness";
    public static final String USERNAME_NAMESTR = "Gebruikersnaam";
    //endregion U

    //region V
    public static final String VALUE_NAMESTR = "Waarde";
    public static final String VARIANTCODE_NAMESTR = "VariantCode";
    public static final String VENDORITEMNO_NAMESTR = "VendorItemNo";
    public static final String VENDORITEMDESCRIPTION_NAMESTR = "VendorItemDescription";
    public static final String VALUETYPE_NAMESTR = "ValueType";

    //endregion V

    //region W
    public static final String WAREHOUSELOCATION_NAMESTR = "Magazijnlocatie";
    public static final String WEBSERVICETIMEOUTERPINS_NAMESTR = "Webservice_timeout_erp_in_s";
    public static final String WORKPLACE_NAMESTR = "Workplace";
    public static final String WORKPLACE_DUTCH_NAMESTR = "Werkplek";
    //endregion W

    //region Z
    public static final String ZIPCODE_NAMESTR = "Postcode";
    public static final String ZONE_NAMESTR = "Zone";
    //endregion Z
}
