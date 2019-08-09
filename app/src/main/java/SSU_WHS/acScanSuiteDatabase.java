package SSU_WHS;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import SSU_WHS.ArticleImages.cArticleImageEntity;
import SSU_WHS.ArticleImages.iArticleImageDao;
import SSU_WHS.BarcodeLayouts.cBarcodeLayoutEntity;
import SSU_WHS.BarcodeLayouts.iBarcodeLayoutDao;
import SSU_WHS.Branches.cBranchEntity;
import SSU_WHS.Branches.iBranchDao;
import SSU_WHS.Comments.cCommentEntity;
import SSU_WHS.Comments.iCommentDao;
import SSU_WHS.PickorderAddresses.cPickorderAddressEntity;
import SSU_WHS.PickorderAddresses.iPickorderAddressDao;
import SSU_WHS.PickorderBarcodes.cPickorderBarcodeEntity;
import SSU_WHS.PickorderBarcodes.iPickorderBarcodeDao;
import SSU_WHS.PickorderLineBarcodes.cPickorderLineBarcodeEntity;
import SSU_WHS.PickorderLineBarcodes.iPickorderLineBarcodeDao;
import SSU_WHS.PickorderLinePackAndShip.cPickorderLinePackAndShipEntity;
import SSU_WHS.PickorderLinePackAndShip.iPickorderLinePackAndShipDao;
import SSU_WHS.PickorderLines.cPickorderLineEntity;
import SSU_WHS.PickorderLines.iPickorderLineDao;
import SSU_WHS.PickorderShipMethods.cPickorderShipMethodEntity;
import SSU_WHS.PickorderShipMethods.iPickorderShipMethodDao;
import SSU_WHS.PickorderShipPackages.cPickorderShipPackageEntity;
import SSU_WHS.PickorderShipPackages.iPickorderShipPackageDao;
import SSU_WHS.Pickorders.cPickorderEntity;
import SSU_WHS.Pickorders.iPickorderDao;
import SSU_WHS.SalesOrderPackingTable.cSalesOrderPackingTableEntity;
import SSU_WHS.SalesOrderPackingTable.iSalesOrderPackingTableDao;
import SSU_WHS.ScannerLogon.cScannerLogonEntity;
import SSU_WHS.ScannerLogon.iScannerLogonDao;
import SSU_WHS.Settings.iSettingsDao;
import SSU_WHS.Settings.cSettingsEntity;

import SSU_WHS.Authorisations.iAuthorisationDao;
import SSU_WHS.Authorisations.cAuthorisationEntity;

import SSU_WHS.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnitEntity;
import SSU_WHS.ShippingAgentServiceShippingUnits.iShippingAgentServiceShippingUnitDao;
import SSU_WHS.ShippingAgentServices.cShippingAgentServiceEntity;
import SSU_WHS.ShippingAgentServices.iShippingAgentServiceDao;
import SSU_WHS.ShippingAgents.cShippingAgentEntity;
import SSU_WHS.ShippingAgents.iShippingAgentDao;
import SSU_WHS.ShippingAgentsServiceShipMethods.cShippingAgentServiceShipMethodEntity;
import SSU_WHS.ShippingAgentsServiceShipMethods.iShippingAgentServiceShipMethodDao;
import SSU_WHS.Users.iUserDao;
import SSU_WHS.Users.cUserEntity;
import SSU_WHS.Workplaces.cWorkplaceEntity;
import SSU_WHS.Workplaces.iWorkplaceDao;
import SSU_WHS.WarehouseLocations.cWarehouseLocationEntity;
import SSU_WHS.WarehouseLocations.iWarehouseLocationDao;

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
        cWarehouseLocationEntity.class,
        cPickorderLineBarcodeEntity.class,
        cArticleImageEntity.class,
        cPickorderBarcodeEntity.class,
        cCommentEntity.class,
        cSalesOrderPackingTableEntity.class,
        cPickorderAddressEntity.class,
        cShippingAgentEntity.class,
        cShippingAgentServiceEntity.class,
        cShippingAgentServiceShippingUnitEntity.class,
        cShippingAgentServiceShipMethodEntity.class,
        cPickorderShipMethodEntity.class,
        cPickorderShipPackageEntity.class,
        cPickorderLinePackAndShipEntity.class
        },version = 64)
public abstract class acScanSuiteDatabase extends RoomDatabase {
    public abstract iScannerLogonDao scannerLogonDao();
    public abstract iSettingsDao settingsDao();
    public abstract iBarcodeLayoutDao barcodeLaoutDao();
    public abstract iAuthorisationDao authorisationDao();
    public abstract iWorkplaceDao workplaceDao();
    public abstract iBranchDao branchDao();
    public abstract iUserDao userDao();
    public abstract iPickorderDao pickorderDao();
    public abstract iPickorderLineDao pickorderLineDao();
    public abstract iWarehouseLocationDao warehouseLocationDao();
    public abstract iPickorderLineBarcodeDao pickorderLineBarcodeDao();
    public abstract iArticleImageDao articleImageDao();
    public abstract iPickorderBarcodeDao pickorderBarcodeDao();
    public abstract iCommentDao commentDao();
    public abstract iSalesOrderPackingTableDao salesOrderPackingTableDao();
    public abstract iPickorderAddressDao pickorderAddressDao();
    public abstract iShippingAgentDao shippingAgentDao();
    public abstract iShippingAgentServiceDao shippingAgentServiceDao();
    public abstract iShippingAgentServiceShippingUnitDao shippingAgentServiceShippingUnitDao();
    public abstract iShippingAgentServiceShipMethodDao shippingAgentServiceShipMethodDao();
    public abstract iPickorderShipMethodDao pickorderShipMethodDao();
    public abstract iPickorderShipPackageDao pickorderShipPackageDao();
    public abstract iPickorderLinePackAndShipDao pickorderLinePackAndShipDao();
    //public abstract iEnvironmentDao environmentDao();

    private static acScanSuiteDatabase INSTANCE;

    public static acScanSuiteDatabase getDatabase(final Context context) {
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
