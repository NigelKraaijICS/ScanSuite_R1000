package SSU_WHS.General.Warehouseorder;

import androidx.lifecycle.ViewModelProviders;

import ICS.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.R;

public class cWarehouseorder {

    public enum ActionTypeEnu {
        TAKE,
        PLACE
    }

    public enum OrderTypeEnu {
        PICKEN,
        INVENTARISATIE,
        ONTVANGST,
        VERPLAATS
    }

    public enum StepCodeEnu {
        Pick_Picking,
        Pick_Sorting,
        Pick_PackAndShip,
        Inventory,
        Receive_Store,
        Move
    }

    public enum PickOrderTypeEnu {
        PICK,
        SORT
    }

    public enum CommentTypeEnu {
        PICK,
        SORT,
        SHIP,
        INVENTORY,
        RECEIVE,
        FEEDBACK
    }

    public  enum WorkflowEnu{
        BM,
        PV,
        BC,
        BP,
        IVS,
        MV
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
        public static final int InventoryHandled = 13;
    }

    public class WorkflowReceiveStoreStepEnu {
        public static final int Receive_Store = 40;
        public static final int Receive_StoreBezig = 41;
        public static final int Receive_StoreWacht = 42;
    }

    public class WorkflowMoveStepEnu {
        public static final int Move = 10;
        public static final int MoveBusy = 11;
        public static final int MovePause = 12;
        public static final int MoveHandled = 13;
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

    public class IntakeMATLineLocalStatusEnu {
        public final static int LOCALSTATUS_NEW = 10;
        public final static int LOCALSTATUS_BUSY = 20;
        public final static int LOCALSTATUS_DONE_NOTSENT = 30;
        public final static int LOCALSTATUS_DONE_ERROR_SENDING = 32;
        public final static int LOCALSTATUS_DONE_SENT = 40;
    }

    public class MovelineLocalStatusEnu {
        public final static int LOCALSTATUS_NEW = 10;
        public final static int LOCALSTATUS_BUSY = 20;
        public final static int LOCALSTATUS_DONE_NOTSENT = 30;
        public final static int LOCALSTATUS_DONE_ERROR_SENDING = 32;
        public final static int LOCALSTATUS_DONE_SENT = 40;
    }



    public class InventoryBinStatusEnu {
        public static final int New = 10;
        public static final int InventoryDoneOnServer = 20;
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

        public final static int GeneratedLine = 0;

        public final static int Salesorder = 1;
        public final static int SalesReturnOrder = 4;

        public final static int PurchaseOrder = 5;
        public final static int PurchaseReturnOrder = 8;

        public final static int InboundTransfer = 9;
        public final static int OutboundTransfer = 10;

        public final static int ProductionConsumption = 11;
        public final static int ProductionOutput = 12;

        public final static int PurchaseLine = 39;


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

    public static String getWorkflowDescription(String pvWorkflowCodeStr) {
        String result = "";
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.BC.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_bc);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.BM.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_bm);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.BP.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_bp);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.EOM.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_eom);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.EOOM.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_eoom);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.EOOS.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_eoos);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.EOR.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_eor);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.EOS.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_eos);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.ER.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_er);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.IVM.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_ivm);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.IVS.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_ivs);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.MAM.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_mam);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.MAS.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_mas);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.MAT.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_mat);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.MI.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_mi);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.MO.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_mo);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.MT.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_mt);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.MV.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_mv);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.MVI.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_mvi);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.OMM.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_omm);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.OMOM.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_omom);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.OMOS.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_omos);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.OMR.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_omr);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.OMS.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_oms);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.PA.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_pa);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.PF.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_pf);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.PV.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_pv);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.RVR.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_rvr);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.RVS.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_rvs);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.SPV.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_spv);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.UNKNOWN.toString())) {
            result = cAppExtension.context.getString(R.string.ordertype_unknown);
        }
        return result;
    }

}
