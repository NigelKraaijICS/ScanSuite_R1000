package SSU_WHS.General;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import SSU_WHS.Basics.ArticleImages.cArticleImageEntity;
import SSU_WHS.Basics.ArticleImages.iArticleImageDao;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayoutEntity;
import SSU_WHS.Basics.BarcodeLayouts.iBarcodeLayoutDao;
import SSU_WHS.Basics.Branches.cBranchEntity;
import SSU_WHS.Basics.Branches.iBranchDao;

import SSU_WHS.Basics.ItemProperty.cItemPropertyEntity;
import SSU_WHS.Basics.ItemProperty.iItemPropertyDao;
import SSU_WHS.General.Comments.cCommentEntity;
import SSU_WHS.General.Comments.iCommentDao;
import SSU_WHS.Picken.PickorderAddresses.cPickorderAddressEntity;
import SSU_WHS.Picken.PickorderAddresses.iPickorderAddressDao;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcodeEntity;
import SSU_WHS.Picken.PickorderBarcodes.iPickorderBarcodeDao;
import SSU_WHS.Picken.PickorderLineBarcodes.cPickorderLineBarcodeEntity;
import SSU_WHS.Picken.PickorderLineBarcodes.iPickorderLineBarcodeDao;

import SSU_WHS.Picken.PickorderLines.cPickorderLineEntity;
import SSU_WHS.Picken.PickorderLines.iPickorderLineDao;
import SSU_WHS.Picken.PickorderShipMethods.cPickorderShipMethodEntity;
import SSU_WHS.Picken.PickorderShipMethods.iPickorderShipMethodDao;
import SSU_WHS.Picken.PickorderShipPackages.cPickorderShipPackageEntity;
import SSU_WHS.Picken.PickorderShipPackages.iPickorderShipPackageDao;
import SSU_WHS.Picken.Pickorders.cPickorderEntity;
import SSU_WHS.Picken.Pickorders.iPickorderDao;
import SSU_WHS.Picken.SalesOrderPackingTable.cSalesOrderPackingTableEntity;
import SSU_WHS.Picken.SalesOrderPackingTable.iSalesOrderPackingTableDao;
import SSU_WHS.ScannerLogon.cScannerLogonEntity;
import SSU_WHS.ScannerLogon.iScannerLogonDao;
import SSU_WHS.Basics.Settings.iSettingsDao;
import SSU_WHS.Basics.Settings.cSettingsEntity;

import SSU_WHS.Basics.Authorisations.iAuthorisationDao;
import SSU_WHS.Basics.Authorisations.cAuthorisationEntity;

import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnitEntity;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.iShippingAgentServiceShippingUnitDao;
import SSU_WHS.Basics.ShippingAgentServices.cShippingAgentServiceEntity;
import SSU_WHS.Basics.ShippingAgentServices.iShippingAgentServiceDao;
import SSU_WHS.Basics.ShippingAgents.cShippingAgentEntity;
import SSU_WHS.Basics.ShippingAgents.iShippingAgentDao;
import SSU_WHS.Basics.ShippingAgentsServiceShipMethods.cShippingAgentServiceShipMethodEntity;
import SSU_WHS.Basics.ShippingAgentsServiceShipMethods.iShippingAgentServiceShipMethodDao;
import SSU_WHS.Basics.Users.iUserDao;
import SSU_WHS.Basics.Users.cUserEntity;
import SSU_WHS.Basics.Workplaces.cWorkplaceEntity;
import SSU_WHS.Basics.Workplaces.iWorkplaceDao;
import SSU_WHS.Picken.WarehouseLocations.cWarehouseLocationEntity;
import SSU_WHS.Picken.WarehouseLocations.iWarehouseLocationDao;

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
        cItemPropertyEntity.class
        },version = 66)


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
    public abstract iItemPropertyDao itemPropertyDao();

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
