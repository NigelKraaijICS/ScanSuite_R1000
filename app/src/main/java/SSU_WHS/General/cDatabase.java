package SSU_WHS.General;

public class cDatabase {
    //region Tables

    public static final String TABLENAME_USERS = "Users";
    public static final String TABLENAME_SETTINGS = "Settings";
    public static final String TABLENAME_SCANNERLOGON = "ScannerLogon";
    public static final String TABLENAME_BARCODELAYOUTS = "BarcodeLayouts";
    public static final String TABLENAME_BINITEM = "BinItem";
    public static final String TABLENAME_TRANSLATION = "Translations";
    public static final String TABLENAME_WORKPLACE = "Workplaces";
    public static final String TABLENAME_SCANNERS = "Scanners";
    public static final String TABLENAME_BRANCH = "Branches";
    public static final String TABLENAME_BRANCHBIN = "BranchBin";
    public static final String TABLENAME_ITEMPROPERTY = "ItemProperties";
    public static final String TABLENAME_LABELTEMPLATE  =  "Labeltemplate";
    public static final String TABLENAME_PROPERTYGROUP = "PropertyGroup";
    public static final String TABLENAME_PROPERTYGROUPPROPERTY = "PropertyGroupProperty";
    public static final String TABLENAME_COMPOSITEBARCODE = "CompositeBarcode";
    public static final String TABLENAME_COMPOSITEBARCODEPROPERTY = "CompositeBarcodeProperty";
    public static final String TABLENAME_PICKORDERBARCODE = "PickorderBarcode";
    public static final String TABLENAME_PICKORDERCOMPOSITEBARCODE = "PickorderCompositeBarcode";
    public static final String TABLENAME_PICKORDERLINEBARCODE = "PickorderLineBarcode";
    public static final String TABLENAME_ARTICLEIMAGE = "ArticleImage";
    public static final String TABLENAME_ARTICLE = "Article" ;
    public static final String TABLENAME_ARTICLEBARCODE = "ArticleBarcode" ;
    public static final String TABLENAME_ARTICLESTOCK = "ArticleStock";
    public static final String TABLENAME_ARTICLEPROPERTYSTOCK = "ArticlePropertyStock";
    public static final String TABLENAME_COMMENT = "Comment";
    public static final String TABLENAME_AUTHORISATIONS = "Authorisations";
    public static final String TABLENAME_SALESORDERPACKINGTABLE = "SalesOrderPackingTable";

    public static final String TABLENAME_PICKORDERLINES = "Pickorderlines";
    public static final String TABLENAME_PICKORDERLINEPROPERTY = "PickorderlineProperty";
    public static final String TABLENAME_PICKORDERLINEPROPERTYVALUE = "PickorderlinePropertyValue";
    public static final String TABLENAME_PICKORDERSETTING = "PickorderSetting";
    public static final String TABLENAME_PICKORDERADDRESS = "PickorderAddress";
    public static final String TABLENAME_SHIPPINGAGENTS = "ShippingAgents";
    public static final String TABLENAME_SHIPPINGAGENTSERVICES = "ShippingAgentServices";
    public static final String TABLENAME_SHIPPINGAGENTSERVICESHIPPINGUNITS = "ShippingAgentServiceShippingUnits";
    public static final String TABLENAME_SHIPPINGAGENTSERVICESHIPMETHODS = "ShippingAgentServiceShipMethods";
    public static final String TABLENAME_PICKORDERSHIPPACKAGES = "PickorderShipPackages";
    public static final String TABLENAME_PICKORDERLINEPACKANDSHIP = "PickorderLinePackAndShip";
    public static final String TABLENAME_PICKORDERLINEFINISHSINGLEPIECE = "PickorderLineFinishSinglePiece";

    public static final String TABLENAME_INVENTORYORDER = "InventoryOrder";
    public static final String TABLENAME_INVENTORYORDERBIN = "InventoryOrderBin";
    public static final String TABLENAME_INVENTORYORDERLINE = "InventoryOrderLines";
    public static final String TABLENAME_INVENTORYORDERLINEPROPERTY = "InventoryorderlineProperty";
    public static final String TABLENAME_INVENTORYORDERLINEPROPERTYVALUE = "InventoryorderlinePropertyValue";
    public static final String TABLENAME_INVENTORYORDERBARCODE = "InventoryOrderBarcodes";
    public static final String TABLENAME_INVENTORYORDERLINEBARCODE  = "InventoryOrderLineBarcodes";
    public static final String TABLENAME_INTAKEORDERMATLINES  = "IntakeLinesMAT";
    public static final String TABLENAME_INTAKEORDER  = "IntakeOrders";
    public static final String TABLENAME_INTAKEORDERBARCODE  = "IntakeOrderBarcodes";
    public static final String TABLENAME_INTAKEORDERMATLINEBARCODE  = "IntakeOrderMATLineBarcodes";
    public static final String TABLENAME_MOVEORDER = "MoveOrder";
    public static final String TABLENAME_MOVEORDERLINE = "MoveOrderLine";
    public static final String TABLENAME_MOVEORDERLINEBARCODE = "MoveOrderLineBarcode";
    public static final String TABLENAME_MOVEORDERBARCODE = "MoveOrdeBarcode";
    public static final String TABLENAME_RETURNORDER = "Returnorder";
    public static final String TABLENAME_RETURNORDERLINE = "ReturnorderLine";
    public static final String TABLENAME_RETURNORDERLINEBARCODE = "ReturnorderLineBarcode";
    public static final String TABLENAME_RETURNORDERBARCODE = "ReturnorderBarcode";
    public static final String TABLENAME_RETURNORDERDOCUMENT = "ReturnorderDocument";
    public static final String TABLENAME_BRANCHREASON = "BranchReason";
    public static final String TABLENAME_RECEIVELINES  = "ReceiveLines";
    public static final String TABLENAME_PACKAGING = "Packaging";
    public static final String TABLENAME_CUSTOMAUTHORISATIONS = "CustomAuthorisations";
    public static final String TABLENAME_IDENTIFIERWITHDESTINATION  = "IdentifierWithDestination";
    public static final String TABLENAME_PACKANDSHIPORDER = "PackAndSHipOrder";
    public static final String TABLENAME_PACKANDSHIPORDERLINE = "PackAndShipOrderLine";
    public static final String TABLENAME_PACKANDSHIPSETTING = "PackAndShipSetting";
    public static final String TABLENAME_PACKANDSHIPBARCODE = "PackAndShipBarcode";
    public static final String TABLENAME_PACKANDSHIPSHIPMENT = "PackAndShipShipment";
    public static final String TABLENAME_PACKANDSHIPADDRESS = "PackAndShipAddress";
    public static final String TABLENAME_PACKANDSHIPSHIPPINGMETHOD = "PackAndShipShippingMethod";
    public static final String TABLENAME_PACKANDSHIPSHIPPINPACKAGE= "PackAndShipShippingPackage";
    public static final String TABLENAME_STOCKOWNER= "StockOwner";
    public static final String TABLENAME_AUTHORIZEDSTOCKOWNER= "AutorizedStockOwner";
    public static final String TABLENAME_LINEPROPERTY= "LineProperty";
    public static final String TABLENAME_LINEPROPERTYVALUE= "LinePropertyValue";
    public static final String TABLENAME_ARTICLEPROPERTY = "ArticleProperty";
    public static final String TABLENAME_ARTICLEPROPERTYVALUE = "ArticlePropertyValue";
    public static final String TABLENAME_CONTENTLABELCONTAINER= "ContentlabelContainer";


    //endregion Tables

    //region localtables
    public static final String TABLENAME_ENVIRONMENTS = "Environments";
    //endregion localtables


    //region A
    public static final String ACTIONTYPECODE_NAMESTR = "ActionTypeCode";
    public static final String ACTUALSHIPPINGAGENTCODE_NAMESTR = "ActualShippingAgentCode";
    public static final String ACTUEALSHIPPINGAGENTSERVICECODE_NAMESTR = "ActualShippingAgentServiceCode";
    public static final String ASSIGNEDUSERID_NAMESTR = "AssignedUserId";
    public static final String AUTHORISATION_NAMESTR = "Autorisatie";
    public static final String ADDRESCODE_NAMESTR = "Adrescode";
    public static final String ADDRESSCODE_NAMESTR = "AddressCode";
    public static final String ADDRESSNAME_NAMESTR = "AddressName";
    public static final String ADDRESSTYPE_NAMESTR = "AddressType";
    public static final String ADDRESSNAMEREMARK_NAMESTR = "AddressNameRemark";
    public static final String ADDRES_NAMESTR = "Adres";
    public static final String ADDRESS_NAMESTR = "Address";
    public static final String ADDRESSREMARK_NAMESTR = "AddressRemark";
    public static final String ADDRESSADDITION_NAMESTR = "Adrestoevoeging";
    public static final String ADDRESSNUMBER_NAMESTR = "Huisnummer";
    public static final String ADDRESSNUMBERADDITION_NAMESTR = "Huisnummertoevoeging";
    public static final String AUTHORISATIONBASE_NAMESTR = "Autorisatie_basis";
    public static final String AREA_NAMESTR = "Area";
    //endregion A

    //region B
    public static final String BARCODE_NAMESTR = "Barcode";
    public static final String BARCODELAYOUT_NAMESTR = "Barcodelayout";
    public static final String BARCODETYPE_NAMESTR = "BarCodeType";

    public static final String BINCODE_NAMESTR = "BinCode";
    public static final String BINCODEHANDLED_NAMESTR = "BinCodeHandled";
    public static final String BINCODENL_NAMESTR = "Magazijnlocatie";
    public static final String BINMANDATORY_NAMESTR = "BinMandatory";
    public static final String BINTYPE_NAMESTR = "BinType";
    public static final String BRANCH_NAMESTR = "Vestiging";
    public static final String BRANCHTYPE_NAMESTR = "Vestigingtype";
    public static final String BRANCHNAME_NAMESTR = "Name";
    public static final String BILLINGADDRESSCODE_NAMESTR = "BillingAddressCode";
    //endregion B

    //region C
    public static final String COMPOSITEBARCODE_NAMESTR = "CompositeBarcode";
    public static final String COMMENTLINENO_NAMESTR = "CommentLineNo";
    public static final String CURRENTLOCATION_NAMESTR = "CurrentLocation";
    public static final String COMMENT_NAMESTR = "Comment";
    public static final String COMMENTCODE_NAMESTR = "CommentCode";
    public static final String COMMENTTEXT_NAMESTR = "CommentText";
    public static final String COMPONENT10_NAMESTR = "Component10";
    public static final String CURRENTUSERID_NAMESTR = "CurrentUserId";
    public static final String CITY_NAMESTR = "City";
    public static final String CITYNL_NAMESTR = "Plaats";
    public static final String COUNTRY_NAMESTR = "Country";
    public static final String COUNTRYNL_NAMESTR = "Land";
    public static final String CONTAINER_NAMESTR = "Container";
    public static final String CONTACTNAME_NAMESTR = "ContactName";
    public static final String CONTACTPERSON_NAMESTR = "Contactpersoon";
    public static final String CONTAINERTYPE_DUTCH_NAMESTR = "Containersoort";
    public static final String CONTAINERSEQUENCEN_NAMESTR = "ContainerSequenceNo";
    //endregion C

    //region D
    public static final String DATATIMESTAMP_NAMESTR = "DataTimestamp";
    public static final String DESCRIPTION_DUTCH_NAMESTR = "Omschrijving";
    public static final String DESCRIPTION_NAMESTR = "Description";
    public static final String DESCRIPTION2_NAMESTR = "Description2";
    public static final String DESTINATION_NAMESTR = "Destination";
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

    public static final String EXTRAFIELD1_NAMESTR = "ExtraField1";
    public static final String EXTRAFIELD2_NAMESTR = "ExtraField2";
    public static final String EXTRAFIELD3_NAMESTR = "ExtraField3";
    public static final String EXTRAFIELD4_NAMESTR = "ExtraField4";
    public static final String EXTRAFIELD5_NAMESTR = "ExtraField5";
    public static final String EXTRAFIELD6_NAMESTR = "ExtraField6";
    public static final String EXTRAFIELD7_NAMESTR = "ExtraField7";
    public static final String EXTRAFIELD8_NAMESTR = "ExtraField8";
    public static final String EMAIL_NAMESTR = "Email";
    public static final String EMBALLAGE_NAMESTR = "Emballage";
    //endregion E

    //region F
    public static final String FIELD_NAMESTR = "Field";
    public static final String FIELDIDENTIFIER_NAMESTR = "Field_identifier";
    public static final String FIELDTYPE_NAMESTR = "Field_type";
    public static final String FILTERFIELD_NAMESTR = "Filter_veld";
    public static final String FILTERVALUE_NAMESTR = "Filter_waarde";
    //endregion F

    //Region G
    public static final String GENERATED_NAMESTR = "Generated";
    //End Region G

    //region H
    public static final String HANDLEDTIMESTAMP_NAMESTR = "HandledTimestamp";
    public static final String HANDLEDTIMESTAMPTAKENOTEXPORTED_NAMESTR = "HandledTimestamp_take_not_exported";
    public static final String HOUSENUMBER_NAMESTR = "HouseNumber";
    public static final String HOUSENUMBERCOMPLEMENTED_NAMESTR = "HouseNumberComplemented";
    public static final String HOUSENUMBEREXTENSION_NAMESTR = "HouseNumberExtension";
    public static final String HOUSENUMBEREXTENSIONCOMPLEMENTED_NAMESTR = "HouseNumberExtensionComplemented";
    //endregion H


    //Region I
    public static final String IDENTIFIER_NAMESTR = "Identifier";
    public static final String INFOKEY_NAMESTR = "InfoKey";
    public static final String INFOVALUE_NAMESTR = "InfoValue";
    public static final String IMAGE_NAMESTR = "Image";
    public static final String INTERFACERESULTMETHOD_NAMESTR = "Interface_result_method";
    public static final String ISPARTOFMULTILINEORDER_NAMESTR = "IsPartOfMultiLineOrder";
    public static final String ISUNIQUEBARCODE_NAMESTR = "IsUniqueBarcode";
    public static final String ISUNIQUE_NAMESTR = "IsUnique";
    public static final String ITEMINFOCODE_NAMESTR = "ItemInfoCode";
    public static final String ITEMNO_NAMESTR = "ItemNo";
    public static final String ITEMTYPE_NAMESTR = "Itemsoort";

    public static final String IMAGEBASE64_NAMESTR = "Image_base64";
    public static final String INV_AUTOCLOSE_BIN_NAMESTR = "Inv_auto_close_bin";
    public static final String INV_PRECOUNT_NAMESTR = "Inv_precount";
    public static final String INV_AMOUNT_MANUAL_NAMESTR = "Inv_Amount_manual";
    public static final String INV_BARCODECHECK_NAMESTR = "Inv_barcode_check";
    public static final String INV_ADD_EXTRA_BIN_NAMESTR = "Inv_add_extra_bin";

    public static final String INVENTORYWITHPICTURE_NAMESTR = "Inv_with_picture";
    public static final String INVENTORYWITHPICTURE_AUTO_OPEN_NAMESTR = "Inv_with_picture_auto_open";
    public static final String INVENTORYWITHPICTURE_PREFETCH_NAMESTR = "Inv_with_picture_prefetch";

    public static final String ISINPUT_NAMESTR = "IsInput";
    public static final String ISREQUIRED_NAMESTR = "Isrequired";
    public static final String ISPROCESSINGORPARKED_NAMESTR = "IsProcessingOrParked";
    public static final String INPUTWORKFLOWS_NAMESTR = "InputWorkflows";


    //endregion I

    //region L
    public static final String LANGUAGEDUTCH_NAMESTR = "Taal";
    public static final String LABELCODE_NAMESTR = "Labelcode";
    public static final String LAYOUT_NAMESTR = "Layout";
    public static final String LICENSE_NAMESTR = "License";
    public static final String LICENSE_NL_NAMESTR = "Licentie";
    public static final String LINE_NAMESTR = "Line";
    public static final String LINES_NAMESTR = "Lines";
    public static final String LINENO_NAMESTR = "LineNo";
    public static final String LINENOTAKE_NAMESTR = "LineNoTake";
    public static final String LOCALSTATUS_NAMESTR = "LocalStatus";
    public static final String LOCAL_QUANTITYTAKEN_NAMESTR = "LocalQuantityTaken";
    public static final String LOCAL_QUANTITYPLACED_NAMESTR = "LocalQuantityPlaced";
    //endregion L

    //region M
    public static final String MOVEDEFAULTBIN_NAMESTR = "Move_default_bin";
    public static final String PICKDEFAULTSTORAGEBIN = "Pick_default_storage_bin";



    public static final String MOVEAMOUNTMANUAL_NAMESTR = "Move_Amount_manual";
    public static final String MOVEBARCODECHECK_NAMESTR = "Move_Barcode_check";
    public static final String MOVEVALIDATESTOCK_NAMESTR = "Move_validate_stock";
    public static final String MOVEVALIDATESTOCKENFORCE_NAMESTR = "Move_validate_stock_enforce";
    public static final String MOVE_MT_TAKEAMOUNTMANUAL_NAMESTR = "Move_MT_take_amount_manual";
    public static final String MOVE_MT_TAKEAUTOITEM_NAMESTR = "Move_MT_take_auto_item";
    public static final String MOVE_MT_TAKEAUTOITEMCONTAINERONCE_NAMESTR = "Move_MT_take_auto_item_container_once";
    public static final String MOVE_MT_TAKEALLOWEND_NAMESTR = "Move_MT_take_allow_end";
    public static final String MOVE_MT_TAKEIGNOREUNDERTAKE_NAMESTR = "Move_MT_take_ignore_undertake";
    public static final String MOVE_MT_TAKEAUTOACCEPTSINGLEPIECE_NAMESTR = "Move_MT_take_auto_accept_single_piece";
    public static final String MOVE_MT_PLACEAMOUNTMANUAL_NAMESTR = "Move_MT_place_amount_manual";
    public static final String MOVE_MT_PLACEAUTOITEM_NAMESTR = "Move_MT_place_auto_item";
    public static final String MOVE_AUTOACCEPTATREQUESTED_NAMESTR = "Move_auto_accept_at_requested";
    public static final String MOVE_NOEXTRABINS_NAMESTR = "Move_no_extra_bins";
    public static final String MOVE_NOEXTRAITEMS_NAMESTR = "Move_no_extra_items";
    public static final String MOVE_NOEXTRAPIECES_NAMESTR = "Move_no_extra_pieces";
    public static final String MOVEWITHPICTURE_NAMESTR = "Move_with_picture";
    public static final String MOVEWITHPICTURE_AUTO_OPEN_NAMESTR = "Move_with_picture_auto_open";
    public static final String MOVEWITHPICTURE_PREFETCH_NAMESTR = "Move_with_picture_prefetch";
    //endregion M

    //region N
    public static final String NAMEDUTCH_NAMESTR = "Naam";
    public static final String NAMEADDITION_NAMESTR = "Naamtoevoeging";
    public static final String NAMESHORT_NAMESTR = "Naam_kort";


    public static final String NUMBEROFBINS_NAMESTR = "Aantal_bins";
    //endregion N

    //region O
    public static final String ORDERDUTCH_NAMESTR = "Volgorde";
    public static final String OMSCHRIJVING_NAMESTR = "Omschrijving";
    public static final String ORDERNUMBER_NAMESTR = "Opdrachtnummer";
    public static final String ORDERTYPE_NAMESTR = "Opdrachttype";
    public static final String ORIGINDUTCH_NAMESTR = "Herkomstcode";
    public static final String ORIGINKEYDUTCH_NAMESTR = "Herkomstsleutel";
    //endregion O

    //region P

    public static final String PACKAGECONTAINER_NAMESTR = "PackageContainer";
    public static final String PACKAGECONTAINERTYPE_NAMESTR = "PackageContainersoort";
    public static final String PACKAGEITEMCOUNT_NAMESTR = "PackageItemCount";
    public static final String PACKAGEWEIGHTING_NAMESTR = "PackageWeightInG";
    public static final String PACKAGETYPE_NAMESTR = "PackageType";
    public static final String PACKAGESEQUENCENUMBER_NAMESTR = "PackageSequenceNumber";
    public static final String PACKINGTABLE_NAMESTR = "Packingtable";
    public static final String PHONE_NAMESTR = "PhoneNumber";
    public static final String PHONENL_NAMESTR = "Telefoon";
    public static final String PICKSALESASKWORKPLACE_NAMESTR = "Pick_Sales_ask_workplace";
    public static final String PICKTRANSFERASKWORKPLACE_NAMESTR = "Pick_Transfer_ask_workplace";
    public static final String PICKAUTONEXT_NAMESTR = "Pick_Auto_next";
    public static final String PICKBARCODECHECK_NAMESTR = "Pick_Barcode_check";
    public static final String PICKPICKPVVKKOEACHPIECE_NAMESTR = "Pick_Pick_PV_VKO_each_piece";
    public static final String PICKPICKTOCONTAINER_NAMESTR = "Pick_Pick_to_container";
    public static final String PICKPICKTOCONTAINERTYPE_NAMESTR = "Pick_Pick_to_container_type";
    public static final String PICKPRINTADDRESSLABEL_NAMESTR = "Pick_Print_adreslabel";
    public static final String PICKPRINTCONTENTLABEL_NAMESTR = "Pick_Print_contentlabel";
    public static final String PICKWITHPICTURE_NAMESTR = "Pick_with_picture";
    public static final String PICKWITHPICTURE_AUTO_OPEN_NAMESTR = "Pick_with_picture_auto_open";
    public static final String PICKWITHPICTURE_PREFETCH_NAMESTR = "Pick_with_picture_prefetch";
    public static final String PICKACTIVITYBINREQUIRED_NAMESTR = "Pick_Activity_bin_required";
    public static final String PICKQCCHECKCOUNT_NAMESTR = "Pick_Qc_check_count";

    public static final String POSITIONSTART_NAMESTR = "Position_start";
    public static final String POSITIONEND_NAMESTR = "Position_end";

    public  static final String PRIORITY_NAMESTR = "Priority";

    public static final String PRICE_NAMESTR = "Price";
    public static final String PRINTDOCUMENTS_NAMESTR = "PrintDocuments";
    public static final String PROCESSINGSEQUENCE_NAMESTR = "ProcessingSequence";
    public static final String PROPERTY_NAMESTR = "Property";
    public static final String PROPERTYCODE_NAMESTR = "Propertycode";
    public static final String PROPERTYDUTCH_NAMESTR = "Propertie";
    public static final String PROPERTYSDUTCH_NAMESTR = "Properties";
    public static final String PROPERTYGROUP_NAMESTR = "Propertiegroep";
    public static final String ADDRESSES_NAMESTR = "Addresses";
    public static final String SHIPPINGMETHODS_NAMESTR = "ShippingMethods";
    public static final String COMPOSITEBARCODESPLITTED_NAMESTR = "Composite_barcode";
    public static final String COMPOSITEBARCODETYPE_NAMESTR = "Compositebarcode_type";
    public static final String FIELDSEPERATOR_NAMESTR = "Field_separator";
    public static final String LAYOUTREGEX_NAMESTR = "Layout_regex";
    public static final String LAYOUTTYPE_NAMESTR = "Layout_type";


    public static final String PICKORDERLINEBARCODE_LINENO = "LineNo";
    public static final String PICKORDERLINEBARCODE_BARCODE = "Barcode";
    public static final String PICKORDERLINEBARCODE_QUANTITY = "Quantity";
    public static final String PICKORDERLINEBARCODE_ISMANUAL = "IsManual";
    //endregion P

    //region Q
    public static final String QUANTITY_NAMESTR = "Quantity";

    public static final String QUANTITYAVAILABLE_NAMESTR = "Voorraad_beschikbaar";
    public static final String QUANTITYHANDLED_NAMESTR = "QuantityHandled";
    public static final String QUANTITYHANDLEDALLSCANNERS_NAMESTR = "QuantityHandledAllScanners";
    public static final String QUANTITYHANDLEDSUM_NAMESTR = "QuantityHandledSum";
    public static final String QUANTITYPERUNITOFMEASURE_NAMESTR = "QtyPerUnitOfMeasure";
    public static final String QUANTITYREJECTED_NAMESTR = "QuantityRejected";
    public static final String QUANTITYCHECKED_NAMESTR = "QuantityChecked";
    public static final String QUANTITYTOTAL_NAMESTR = "QuantityTotal";
    public static final String QUANTITYTAKEN_NAMESTR = "QuantityTaken";
    public static final String QUANTITYHANDLEDTAKE_NAMESTR = "QuantityHandled_take";
    public static final String QUANTITYTAKE_NAMESTR = "Quantity_take";
    public static final String QUANTITYTAKENOTEXPORTED_NAMESTR = "QuantityHandled_take_not_exported";
    public static final String QUANTITYTAKEEXPORTED_NAMESTR = "QuantityHandled_take_exported";
    //endregion Q

    //region R
    public static final String RECEIVEDEFAULTBIN_NAMESTR = "Receive_default_bin";
    public static final String REGION_NAMESTR = "Regio";
    public static final String REJECTPICK_NAMESTR = "RejectPick";
    public static final String RECEIVEINTAKEEOPACKAGINGINTAKE_NAMESTR = "Receive_intake_eo_packaging_intake";
    public static final String RECEIVEINTAKEEOPACKAGINGSHIPPED_NAMESTR = "Receive_intake_eo_packaging_shipped";

    public static final String RECEIVEWITHPICTURE_NAMESTR = "Receive_with_picture";
    public static final String RECEIVEWITHPICTURE_AUTO_OPEN_NAMESTR = "Receive_with_picture_auto_open";
    public static final String RECEIVEWITHPICTURE_PREFETCH_NAMESTR = "Receive_with_picture_prefetch";
    public static final String REQUESTENDDATETIME_NAMESTR = "RequestedEndDateTime";
    public static final String REQUESTEDENDDATETIME_NAMESTR = "RequestedEndDateTime";
    public static final String REQUESTEDSTARTDATETIME_NAMESTR = "RequestedStartDateTime";
    public static final String REMEMBERVALUE_NAMESTR = "RememberValue";
    public static final String RETURNDEFAULTBIN_NAMESTR = "Retour_default_bin";

    public static final String RECEIVEDDATETIME_NAMESTR = "ReceivedDateTime";
    public static final String RECEIVEEXPORTPART_NAMESTR = "Receive_export_part";
    public static final String RECEIVE_AMOUNT_MANUAL_NAMESTR = "Receive_Amount_manual";
    public static final String RECEIVEAMOUNTMANUAL_EO_NAMESTR = "Receive_Amount_manual_eo";
    public static final String RECEIVEBARCODECHECK_NAMESTR = "Receive_Barcode_check";
    public static final String RECEIVESTOREAUTOACCEPTATREQUESTED_NAMESTR = "Receive_store_auto_accept_at_requested";
    public static final String RECEIVESTOREAUTOACCEPTATNEWITEM_NAMESTR = "Receive_store_auto_accept_at_new_item";
    public static final String RECEIVESTOREAUTOACCEPTVALIDATIONMESSAGE_NAMESTR = "Receive_store_auto_accept_validation_message";
    public static final String RECEIVENOEXTRABINS_NAMESTR = "Receive_no_extra_bins";
    public static final String RECEIVENOEXTRAITEMS_NAMESTR = "Receive_no_extra_items";
    public static final String RECEIVENOEXTRAPIECES_NAMESTR = "Receive_no_extra_pieces";
    public static final String RECEIVEMATAUTOSPLITINCOMPLETELINE_NAMESTR = "Receive_mat_auto_split_incomplete_line";
    public static final String REQUIREDWORKFLOWS_NAMESTR = "RequiredWorkflows";
    public static final String RETOURREDEN_NAMESTR = "Retourreden";
    public static final String RETOURAMAOUNTMANUAL_NAMESTR = "Retour_Amount_manual";
    public static final String RETOURBARCODECHECK_NAMESTR = "Retour_Barcode_check";
    public static final String RETOURMULTIDOCUMENT_NAMESTR = "Retour_multi_document";
    public static final String RETOURORDERBINNOCHECK_NAMESTR = "Retour_Order_bin_no_check";


    public static final String REASON_NAMESTR = "Reason";
    public static final String REASONNL_NAMESTR = "Reden";
    public static final String RETURN_NAMESTR = "Return";
    public static final String RETURNEXTERNAL_NAMESTR = "ReturnExternal";

    public static final String RETOURWITHPICTURE_NAMESTR = "Retour_with_picture";
    public static final String RETURNADDRESSCODE_NAMESTR = "ReturnAddressCode";
    public static final String RETURNSENDERADDRESSCODE_NAMESTR = "ReturnSenderAddressCode";
    public static final String RETURNSHIPPINGADDRESSCODE_NAMESTR = "ReturnShippingAddressCode";
    //endregion R

    //region S
    public static final String SALESORDER_NAMESTR = "Salesorder";
    public static final String SCANNER_NAMESTR = "Scanner";
    public static final String SENDERADDRESSCODE_NAMESTR = "SenderAddressCode";

    public static final String SEQUENCENOHANDLED_NAMESTR = "SequenceNoHandled";
    public static final String SETTINGCODE_NAMESTR = "SettingCode";
    public static final String SETTINGVALUE_NAMESTR = "SettingValue";
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
    public static final String SORTINGSEQUENCENO_NAMESTR = "SortingSequenceNo";
    public static final String SORTINGSEQUENCENOPLACE_NAMESTR = "SortingSequenceNo_place";
    public static final String STORAGEBINCODE_NAMESTR = "StorageBinCode";
    public static final String STOREBINCODE_NAMESTR = "StoreBinCode";
    public static final String STORESOURCEORDER_NAMESTR = "StoreSourceOpdracht";
    public static final String STREETNL_NAMESTR = "Straat";
    public static final String STREET_NAMESTR = "Street";
    public static final String STREETCOMPLEMENTERD_NAMESTR = "StreetComplemented";
    public static final String SEQUENCE_NAMESTR = "Order";
    public static final String SHIPPINGAGENT_NAMESTR = "Expediteur";
    public static final String SHIPPINGLABELS_NAMESTR = "ShippingLabels";
    public static final String SERVICE_NAMESTR = "Service";
    public static final String SHIPPINGUNIT_NAMESTR = "Verzendeenheid";
    public static final String SHIPPINGMETHOD_NAMESTR = "Verzendmethode";
    public static final String SHIPPINGMETHODCODE_NAMESTR = "ShippingMethodCode";
    public static final String SHIPPINGMETHODVALUE_NAMESTR = "ShippingMethodValue";
    public static final String SHIPPINGUNITQUANTITYUSED_NAMESTR = "Verzendmethode";
    public static final String SHIPPINGAGENTCODE_NAMESTR = "ShippingAgentCode";
    public static final String SHIPPINGAGENTSERVICECODE_NAMESTR = "ShippingAgentServiceCode";
    public static final String SHIPPINGADDRESSCODE_NAMESTR = "ShippingAddressCode";
    public static final String SHIPPINGADDRESSTYPE_NAMESTR = "ShippingAddressType";

    public static final String SHOWONTERMINAL_NAMESTR = "ShowOnTerminal";
    public static final String SOURCETYPE_NAMESTR = "SourceType";
    public static final String SORTINGSEQUENCENOTAKE_NAMESTR = "SortingSequenceNo_take";
    public static final String STRIPTHIS_NAMESTR = "Strip_this";
    //endregion S

    //region T
    public static final String TAKENTIMESTAMP_NAMESTR = "TakenTimestamp";
    public static final String TEMPLATE_NAMESTR = "Template";
    public static final String TEXTDUTCH_NAMESTR = "Tekst";
    public static final String TRANSLATIONDUTCH_NAMESTR = "Vertaling";
    //endregion T


    //region U
    public static final String USEFORSTORAGE_NAMESTR = "UseForStorage";
    public static final String USEFORRETURNSALES_NAMESTR = "UseForReturnSales";
    public static final String UNIQUNESS_NAMESTR = "Uniqueness";
    public static final String UNITOFMEASURE_NAMESTR = "UnitOfMeasure";
    public static final String USERNAME_NAMESTR = "Gebruikersnaam";
    //endregion U

    //region V
    public static final String VALUE_NAMESTR = "Value";
    public static final String VALUENL_NAMESTR = "Waarde";
    public static final String VALUEHANDLED_NAMESTR = "ValueHandled";
    public static final String VARIANTCODE_NAMESTR = "VariantCode";
    public static final String VENDORITEMNO_NAMESTR = "VendorItemNo";
    public static final String VENDORITEMDESCRIPTION_NAMESTR = "VendorItemDescription";
    public static final String VALUETYPE_NAMESTR = "ValueType";
    public static final String VERZENDLABEL_STATUS_NAMESTR = "Verzendlabel_status";


    //endregion V

    //region W
    public static final String WAREHOUSELOCATION_NAMESTR = "Magazijnlocatie";
    public static final String WEBSERVICETIMEOUTERPINS_NAMESTR = "Webservice_timeout_erp_in_s";
    public static final String WORKPLACE_NAMESTR = "Workplace";
    public static final String WORKPLACE_DUTCH_NAMESTR = "Werkplek";
    //endregion W

    //region Z
    public static final String ZIPCODENL_NAMESTR = "Postcode";
    public static final String ZIPCODE_NAMESTR = "Zipcode";
    public static final String ZONE_NAMESTR = "Zone";



    //endregion Z
}
