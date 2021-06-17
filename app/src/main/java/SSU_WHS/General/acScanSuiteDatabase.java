package SSU_WHS.General;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import SSU_WHS.Basics.ArticleImages.cArticleImageEntity;
import SSU_WHS.Basics.ArticleImages.iArticleImageDao;
import SSU_WHS.Basics.Authorisations.cAuthorisationEntity;
import SSU_WHS.Basics.Authorisations.iAuthorisationDao;
import SSU_WHS.Basics.AuthorizedStockOwners.cAuthorizedStockOwnerEntity;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayoutEntity;
import SSU_WHS.Basics.BarcodeLayouts.iBarcodeLayoutDao;
import SSU_WHS.Basics.Branches.cBranchEntity;
import SSU_WHS.Basics.Branches.iBranchDao;
import SSU_WHS.Basics.CompositeBarcode.cCompositeBarcodeEntity;
import SSU_WHS.Basics.CompositeBarcode.iCompositeBarcodeDao;
import SSU_WHS.Basics.CompositeBarcodeProperty.cCompositeBarcodePropertyEntity;
import SSU_WHS.Basics.CompositeBarcodeProperty.iCompositeBarcodePropertyDao;
import SSU_WHS.Basics.CustomAuthorisations.cCustomAuthorisationEntity;
import SSU_WHS.Basics.CustomAuthorisations.iCustomAuthorisationDao;
import SSU_WHS.Basics.ItemProperty.cItemPropertyEntity;
import SSU_WHS.Basics.ItemProperty.iItemPropertyDao;
import SSU_WHS.Basics.LabelTemplate.cLabelTemplateEntity;
import SSU_WHS.Basics.LabelTemplate.iLabelTemplateDao;
import SSU_WHS.Basics.Packaging.cPackagingEntity;
import SSU_WHS.Basics.Packaging.iPackagingDao;
import SSU_WHS.Basics.PropertyGroup.cPropertyGroupEntity;
import SSU_WHS.Basics.PropertyGroup.iPropertyGroupDao;
import SSU_WHS.Basics.PropertyGroupProperty.cPropertyGroupPropertyEntity;
import SSU_WHS.Basics.PropertyGroupProperty.iPropertyGroupPropertyDao;
import SSU_WHS.Basics.Scanners.cScannerEntity;
import SSU_WHS.Basics.Scanners.iScannerDao;
import SSU_WHS.Basics.Settings.cSettingsEntity;
import SSU_WHS.Basics.Settings.iSettingsDao;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnitEntity;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.iShippingAgentServiceShippingUnitDao;
import SSU_WHS.Basics.ShippingAgentServices.cShippingAgentServiceEntity;
import SSU_WHS.Basics.ShippingAgentServices.iShippingAgentServiceDao;
import SSU_WHS.Basics.ShippingAgents.cShippingAgentEntity;
import SSU_WHS.Basics.ShippingAgents.iShippingAgentDao;
import SSU_WHS.Basics.ShippingAgentsServiceShipMethods.cShippingAgentServiceShipMethodEntity;
import SSU_WHS.Basics.ShippingAgentsServiceShipMethods.iShippingAgentServiceShipMethodDao;
import SSU_WHS.Basics.StockOwner.cStockOwnerEntity;
import SSU_WHS.Basics.StockOwner.iStockOwnerDao;
import SSU_WHS.Basics.Translations.cTranslationEntity;
import SSU_WHS.Basics.Translations.iTranslationDao;
import SSU_WHS.Basics.Users.cUserEntity;
import SSU_WHS.Basics.Users.iUserDao;
import SSU_WHS.Basics.Workplaces.cWorkplaceEntity;
import SSU_WHS.Basics.Workplaces.iWorkplaceDao;
import SSU_WHS.General.Comments.cCommentEntity;
import SSU_WHS.General.Comments.iCommentDao;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcodeEntity;
import SSU_WHS.Intake.IntakeorderBarcodes.iIntakeorderBarcodeDao;
import SSU_WHS.Intake.IntakeorderMATLineBarcodes.cIntakeorderMATLineBarcodeEntity;
import SSU_WHS.Intake.IntakeorderMATLineBarcodes.iIntakeorderMATLineBarcodeDao;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLineEntity;
import SSU_WHS.Intake.IntakeorderMATLines.iIntakeorderMATLineDao;
import SSU_WHS.Intake.Intakeorders.cIntakeorderEntity;
import SSU_WHS.Intake.Intakeorders.iIntakeorderDao;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorderEntity;
import SSU_WHS.Inventory.InventoryOrders.iInventoryorderDao;
import SSU_WHS.Inventory.InventoryorderBarcodes.cInventoryorderBarcodeEntity;
import SSU_WHS.Inventory.InventoryorderBarcodes.iInventoryorderBarcodeDao;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBinEntity;
import SSU_WHS.Inventory.InventoryorderBins.iInventoryorderBinDao;
import SSU_WHS.Inventory.InventoryorderLineBarcodes.cInventoryorderLineBarcodeEntity;
import SSU_WHS.Inventory.InventoryorderLineBarcodes.iInventoryorderLineBarcodeDao;
import SSU_WHS.Inventory.InventoryorderLines.cInventoryorderLineEntity;
import SSU_WHS.Inventory.InventoryorderLines.iInventoryorderLineDao;
import SSU_WHS.LineItemProperty.LineProperty.cLinePropertyEntity;
import SSU_WHS.LineItemProperty.LineProperty.iLinePropertyDao;
import SSU_WHS.LineItemProperty.LinePropertyValue.cLinePropertyValueEntity;
import SSU_WHS.LineItemProperty.LinePropertyValue.iLinePropertyValueDao;
import SSU_WHS.Move.Moveorders.cMoveorderEntity;
import SSU_WHS.Move.Moveorders.iMoveorderDao;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcodeEntity;
import SSU_WHS.Move.MoveorderBarcodes.iMoveorderBarcodeDao;
import SSU_WHS.Move.MoveorderLineBarcode.cMoveorderLineBarcodeEntity;
import SSU_WHS.Move.MoveorderLineBarcode.iMoveorderLineBarcodeDao;
import SSU_WHS.Move.MoveorderLines.cMoveorderLineEntity;
import SSU_WHS.Move.MoveorderLines.iMoveorderLineDao;
import SSU_WHS.PackAndShip.PackAndShipAddress.cPackAndShipAddressEntity;
import SSU_WHS.PackAndShip.PackAndShipAddress.iPackAndShipAddressDao;
import SSU_WHS.PackAndShip.PackAndShipBarcode.cPackAndShipBarcodeEntity;
import SSU_WHS.PackAndShip.PackAndShipBarcode.iPackAndShipBarcodeDao;
import SSU_WHS.PackAndShip.PackAndShipLines.cPackAndShipOrderLineEntity;
import SSU_WHS.PackAndShip.PackAndShipLines.iPackAndShipOrderLineDao;
import SSU_WHS.PackAndShip.PackAndShipOrders.cPackAndShipOrderEntity;
import SSU_WHS.PackAndShip.PackAndShipOrders.iPackAndShipOrderDao;
import SSU_WHS.PackAndShip.PackAndShipSetting.cPackAndShipSettingEntity;
import SSU_WHS.PackAndShip.PackAndShipSetting.iPackAndShipSettingDao;
import SSU_WHS.PackAndShip.PackAndShipShipment.cPackAndShipShipmentEntity;
import SSU_WHS.PackAndShip.PackAndShipShipment.iPackAndShipShipmentDao;
import SSU_WHS.PackAndShip.PackAndShipShippingMethod.cPackAndShipShippingMethodEntity;
import SSU_WHS.PackAndShip.PackAndShipShippingMethod.iPackAndShipShippingMethodDao;
import SSU_WHS.PackAndShip.PackAndShipShippingPackage.cPackAndShipShippingPackageEntity;
import SSU_WHS.PackAndShip.PackAndShipShippingPackage.iPackAndShipShippingPackageDao;
import SSU_WHS.Picken.FinishSinglePieceLine.cPickorderLineFinishSinglePieceEntity;
import SSU_WHS.Picken.PickorderAddresses.cPickorderAddressEntity;
import SSU_WHS.Picken.PickorderAddresses.iPickorderAddressDao;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcodeEntity;
import SSU_WHS.Picken.PickorderBarcodes.iPickorderBarcodeDao;
import SSU_WHS.Picken.PickorderCompositeBarcode.cPickorderCompositeBarcodeEntity;
import SSU_WHS.Picken.PickorderCompositeBarcode.iPickorderCompositeBarcodeDao;
import SSU_WHS.Picken.PickorderLineBarcodes.cPickorderLineBarcodeEntity;
import SSU_WHS.Picken.PickorderLineBarcodes.iPickorderLineBarcodeDao;
import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShipEntity;
import SSU_WHS.Picken.PickorderLines.cPickorderLineEntity;
import SSU_WHS.Picken.PickorderLines.iPickorderLineDao;
import SSU_WHS.Picken.PickorderSetting.cPickorderSettingEntity;
import SSU_WHS.Picken.PickorderSetting.iPickorderSettingDao;
import SSU_WHS.Picken.PickorderShipPackages.cPickorderShipPackageEntity;
import SSU_WHS.Picken.PickorderShipPackages.iPickorderShipPackageDao;
import SSU_WHS.Picken.Pickorders.cPickorderEntity;
import SSU_WHS.Picken.Pickorders.iPickorderDao;
import SSU_WHS.Picken.SalesOrderPackingTable.cSalesOrderPackingTableEntity;
import SSU_WHS.Picken.SalesOrderPackingTable.iSalesOrderPackingTableDao;
import SSU_WHS.Receive.ReceiveLines.cReceiveorderLineEntity;
import SSU_WHS.Receive.ReceiveLines.iReceiveorderLineDao;
import SSU_WHS.Return.ReturnOrder.cReturnorderEntity;
import SSU_WHS.Return.ReturnOrder.iReturnorderDao;
import SSU_WHS.Return.ReturnorderBarcode.cReturnorderBarcodeEntity;
import SSU_WHS.Return.ReturnorderBarcode.iReturnorderBarcodeDao;
import SSU_WHS.Return.ReturnorderDocument.cReturnorderDocumentEntity;
import SSU_WHS.Return.ReturnorderDocument.iReturnorderDocumentDao;
import SSU_WHS.Return.ReturnorderLine.cReturnorderLineEntity;
import SSU_WHS.Return.ReturnorderLine.iReturnorderLineDao;
import SSU_WHS.Return.ReturnorderLineBarcode.cReturnorderLineBarcodeEntity;
import SSU_WHS.Return.ReturnorderLineBarcode.iReturnorderLineBarcodeDao;
import SSU_WHS.ScannerLogon.cScannerLogonEntity;
import SSU_WHS.ScannerLogon.iScannerLogonDao;

@Database(entities = {
        cScannerLogonEntity.class,
        cWorkplaceEntity.class,
        cBarcodeLayoutEntity.class,
        cAuthorisationEntity.class,
        cUserEntity.class,
        cBranchEntity.class,
        cSettingsEntity.class,
        cPickorderEntity.class,
        cPickorderLineEntity.class,
        cPickorderLineBarcodeEntity.class,
        cArticleImageEntity.class,
        cPickorderBarcodeEntity.class,
        cPickorderCompositeBarcodeEntity.class,
        cCommentEntity.class,
        cSalesOrderPackingTableEntity.class,
        cPickorderAddressEntity.class,
        cShippingAgentEntity.class,
        cShippingAgentServiceEntity.class,
        cShippingAgentServiceShippingUnitEntity.class,
        cShippingAgentServiceShipMethodEntity.class,
        cPickorderShipPackageEntity.class,
        cPickorderLineFinishSinglePieceEntity.class,
        cPickorderSettingEntity.class,
        cItemPropertyEntity.class,
        cPropertyGroupEntity.class,
        cPropertyGroupPropertyEntity.class,
        cPickorderLinePackAndShipEntity.class,
        cInventoryorderEntity.class,
        cInventoryorderBinEntity.class,
        cInventoryorderLineEntity.class,
        cInventoryorderBarcodeEntity.class,
        cInventoryorderLineBarcodeEntity.class,
        cIntakeorderEntity.class,
        cIntakeorderMATLineEntity.class,
        cIntakeorderBarcodeEntity.class,
        cIntakeorderMATLineBarcodeEntity.class,
        cReturnorderEntity.class,
        cReturnorderBarcodeEntity.class,
        cReturnorderDocumentEntity.class,
        cReturnorderLineEntity.class,
        cReturnorderLineBarcodeEntity.class,
        cReceiveorderLineEntity.class,
        cMoveorderEntity.class,
        cMoveorderLineEntity.class,
        cMoveorderBarcodeEntity.class,
        cMoveorderLineBarcodeEntity.class,
        cPackagingEntity.class,
        cScannerEntity.class,
        cCustomAuthorisationEntity.class,
        cTranslationEntity.class,
        cCompositeBarcodeEntity.class,
        cCompositeBarcodePropertyEntity.class,
        cPackAndShipOrderEntity.class,
        cPackAndShipOrderLineEntity.class,
        cPackAndShipBarcodeEntity.class,
        cPackAndShipShipmentEntity.class,
        cPackAndShipAddressEntity.class,
        cPackAndShipSettingEntity.class,
        cPackAndShipShippingMethodEntity.class,
        cPackAndShipShippingPackageEntity.class,
        cStockOwnerEntity.class,
        cAuthorizedStockOwnerEntity.class,
        cLabelTemplateEntity.class,
        cLinePropertyEntity.class,
        cLinePropertyValueEntity.class,
        },version = 154)


public abstract class acScanSuiteDatabase extends RoomDatabase {
    public abstract iScannerLogonDao scannerLogonDao();
    public abstract iSettingsDao settingsDao();
    public abstract iBarcodeLayoutDao barcodeLayoutDao();
    public abstract iAuthorisationDao authorisationDao();
    public abstract iScannerDao scannerDao();
    public abstract iWorkplaceDao workplaceDao();
    public abstract iBranchDao branchDao();
    public abstract iUserDao userDao();
    public abstract iPickorderDao pickorderDao();
    public abstract iPickorderLineDao pickorderLineDao();
    public abstract iPickorderLineBarcodeDao pickorderLineBarcodeDao();
    public abstract iArticleImageDao articleImageDao();
    public abstract iPickorderBarcodeDao pickorderBarcodeDao();
    public abstract iPickorderCompositeBarcodeDao pickorderCompositeBarcodeDao();
    public abstract iCommentDao commentDao();
    public abstract iSalesOrderPackingTableDao salesOrderPackingTableDao();
    public abstract iPickorderAddressDao pickorderAddressDao();
    public abstract iPickorderSettingDao pickorderSettingDao();
    public abstract iShippingAgentDao shippingAgentDao();
    public abstract iShippingAgentServiceDao shippingAgentServiceDao();
    public abstract iShippingAgentServiceShippingUnitDao shippingAgentServiceShippingUnitDao();
    public abstract iShippingAgentServiceShipMethodDao shippingAgentServiceShipMethodDao();
    public abstract iPickorderShipPackageDao pickorderShipPackageDao();
    public abstract iItemPropertyDao itemPropertyDao();
    public abstract iPropertyGroupDao propertyGroupDao();
    public abstract iPropertyGroupPropertyDao propertyGroupPropertyDao();
    public abstract iCompositeBarcodeDao compositeBarcodeDao();
    public abstract iCompositeBarcodePropertyDao compositeBarcodePropertyDao();
    public abstract iInventoryorderDao inventoryorderDao();
    public abstract iInventoryorderBinDao inventoryorderBinDao();
    public abstract iInventoryorderLineDao inventoryorderLineDao();
    public abstract iInventoryorderBarcodeDao inventoryorderBarcodeDao();
    public abstract iInventoryorderLineBarcodeDao inventoryorderLineBarcodeDao();
    public abstract iIntakeorderMATLineDao intakeorderMATLineDao();
    public abstract iIntakeorderDao intakeorderDao();
    public abstract iIntakeorderBarcodeDao intakeorderBarcodeDao();
    public abstract iIntakeorderMATLineBarcodeDao intakeorderMATLineBarcodeDao();
    public abstract iReturnorderDao returnorderDao();
    public abstract iReturnorderDocumentDao returnorderDocumentDao();
    public abstract iReturnorderLineDao returnorderLineDao();
    public abstract iReturnorderBarcodeDao returnorderBarcodeDao();
    public abstract iReturnorderLineBarcodeDao returnorderLineBarcodeDao();
    public abstract iReceiveorderLineDao receiveorderLineDao();
    public  abstract iMoveorderDao moveorderDao();
    public  abstract iMoveorderLineDao moveorderLineDao();
    public  abstract iMoveorderLineBarcodeDao moveorderLineBarcodeDao();
    public  abstract iMoveorderBarcodeDao moveorderBarcodeDao();
    public  abstract iPackagingDao packagingDao();
    public  abstract iCustomAuthorisationDao customAuthorisationDao();
    public  abstract iTranslationDao translationDao();
    public  abstract iPackAndShipOrderDao packAndShipOrderDao();
    public  abstract iPackAndShipOrderLineDao packAndShipOrderLineDao();
    public  abstract iPackAndShipSettingDao packAndShipSettingDao();
    public  abstract iPackAndShipBarcodeDao packAndShipBarcodeDao();
    public  abstract iPackAndShipShipmentDao packAndShipShipmentDao();
    public  abstract iPackAndShipAddressDao packAndShipAddressDao();
    public  abstract iPackAndShipShippingMethodDao packAndShipShippingMethodDao();
    public  abstract iPackAndShipShippingPackageDao packAndShipShippingPackageDao();
    public  abstract iStockOwnerDao stockOwnerDao();
    public  abstract iLabelTemplateDao labelTemplateDao();
    public  abstract iLinePropertyDao linePropertyDao();
    public  abstract iLinePropertyValueDao linePropertyValueDao();

    private static acScanSuiteDatabase INSTANCE;

    public static acScanSuiteDatabase pGetDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (acScanSuiteDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), acScanSuiteDatabase.class, "SSU_WHS").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
