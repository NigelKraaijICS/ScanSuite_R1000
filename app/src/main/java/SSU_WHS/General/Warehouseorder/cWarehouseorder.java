package SSU_WHS.General.Warehouseorder;

import androidx.lifecycle.ViewModelProviders;

import ICS.cAppExtension;

public class cWarehouseorder {

    public enum ActionTypeEnu {
        TAKE,
        PLACE
    }

    public enum OrderTypeEnu {
        PICKEN,
        INVENTARISATIE
    }

    public enum StepCodeEnu {
        Pick_Picking,
        Pick_Sorting,
        Pick_PackAndShip,
        Inventory
    }

    public enum PickOrderTypeEnu {
        PICK,
        SORT
    }

    public enum CommentTypeEnu {
        PICK,
        SORT,
        SHIP,
        FEEDBACK
    }

    public  enum WorkflowEnu{
        BM,
        PV,
        BC,
        BP,
        IVS
    }

    public class ItemTypeEnu {
        public static final int Unknown = 0;
        public static final int Item = 1;
        public static final int Box = 20;
        public static final int Container = 21;
        public static final int Pallet = 22;
    }



    public class WorkflowPickStepEnu {
        public static final int PickPicking = 10;
        public static final int PickSorting = 20;
        public static final int PickPackAndShip = 40;

    }

    public class WorkflowInventoryStepEnu {
        public static final int Inventory = 10;
        public static final int InventoryBusy = 11;
        public static final int InventoryPause = 12;
    }

    public class PicklineStatusEnu {
        public static final int Needed = 10;
        public static final int DONE = 11;
    }

    public class PackingAndShippingStatusEnu {
        public static final int Needed = 10;
        public static final int NotNeeded = 92;
    }

    public class PicklineLocalStatusEnu {
        public final static int LOCALSTATUS_NEW = 10;
        public final static int LOCALSTATUS_BUSY = 20;
        public final static int LOCALSTATUS_DONE_NOTSENT = 30;
        public final static int LOCALSTATUS_DONE_ERROR_SENDING = 32;
        public final static int LOCALSTATUS_DONE_SENT = 40;
    }

    public class InventoryBinStatusEnu {
        public static final int New = 10;
        public static final int InventoryPause = 20;
        public static final int InventoryDone = 30;
    }

    public enum ActivityActionEnu{
        Unknown,
        Delete,
        NoStart,
        Hold,
        Store,
        Next
    }


    public class SoureDocumentTypeEnu {
        public final static int Undefined = 0;

        public final static int Salesorder = 1;
        public final static int SalesReturnOrder = 4;

        public final static int PurchaseOrder = 5;
        public final static int PurchaseReturnOrder = 8;

        public final static int InboundTransfer = 9;
        public final static int OutboundTransfer = 10;


        public final static int ProductionConsumption = 11;
        public final static int ProductionOutput = 12;

        public final static int Generated = 99;
    }


    public static cWarehouseorderViewModel gWarehouseorderViewModel;
    public static cWarehouseorderViewModel getWarehouseorderViewModel() {
        if (gWarehouseorderViewModel == null) {
            gWarehouseorderViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cWarehouseorderViewModel.class);
        }
        return gWarehouseorderViewModel;
    }

    public static ActivityActionEnu  pGetActivityActionEnu (int pvActionInt) {

        ActivityActionEnu resultEnu = ActivityActionEnu.Unknown;

        if (pvActionInt == 1) {
            resultEnu  =  ActivityActionEnu.Delete;
        }

        if (pvActionInt == 2) {
            resultEnu  =   ActivityActionEnu.NoStart;
        }

        if (pvActionInt == 3) {
            resultEnu  =   ActivityActionEnu.Hold;
        }

        if (pvActionInt == 4) {
            resultEnu  =   ActivityActionEnu.Store;
        }

        if (pvActionInt == 5) {
            resultEnu  =   ActivityActionEnu.Next;
        }

        return  resultEnu;


    }

}
