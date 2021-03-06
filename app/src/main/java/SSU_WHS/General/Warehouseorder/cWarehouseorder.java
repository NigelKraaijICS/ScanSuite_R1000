package SSU_WHS.General.Warehouseorder;

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
        VERPLAATS,
        RETOUR,
        INPAKKEN_VERZENDEN
    }

    public enum StepCodeEnu {
        Unkown,
        Pick_Picking,
        Pick_Sorting,
        Pick_PackAndShip,
        Pick_Storage,
        Finish_Packing,
        Pick_QualityContol,
        Inventory,
        Receive_Store,
        Receive_InTake,
        Move_Take,
        Move_Place,
        Retour,
        PackAndShipShipping
    }

    public enum PickOrderTypeEnu {
        PICK,
        SORT,
        STORE
    }

    public  enum ReceiveAndStoreMainTypeEnu {
        Unknown,
        External,
        Internal,
        Store
    }

    public  enum PickMainTypeEnu {
        Unknown,
        PA,
        PF
    }


    public  enum MoveMainTypeEnu {
        Unknown,
        TAKE,
        PLACE,
        TAKEANDPLACE,
    }

    public  enum PackAndShipMainTypeEnu {
        Unknown,
        SINGLE,
        MULTI
    }


    public enum CommentTypeEnu {
        PICK,
        SORT,
        STORE,
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
        PA,
        PF,
        SPV,
        IVS,
        MV,
        MI,
        MO,
        MVI,
        RVS,
        RVR,
        MAT,
        MAS,
        EOS,
        EOR,
        PS1,
        PSM
    }

    public static class ItemTypeEnu {
        public static final int Unknown = 0;
        public static final int Item = 1;
        public static final int Container = 21;
    }

    public static class WorkflowPickStepEnu {
        public static final int PickPicking = 10;
        public static final int PickSorting = 20;
        public static final int PickQualityControl = 30;
        public static final int PickPackAndShip = 40;
        public static final int PickStorage = 60;
        public static final int PickFinishPacking = 70;

    }

    public static class WorkflowInventoryStepEnu {
        public static final int Inventory = 10;
        public static final int InventoryBusy = 11;
        public static final int InventoryHandled = 13;
    }

    public static class WorkflowExternalReceiveStepEnu {
        public static final int Receive_External = 10;
    }

    public static class WorkflowReceiveStoreStepEnu {
        public static final int Receive_Store = 40;
    }


    public static class MoveStatusEnu {
        public static final int Move_Take = 10;
        public static final int Move_Take_Busy = 11;
        public static final int Move_Take_Wait = 12;
        public static final int Move_Place = 40;
        public static final int Move_Place_Busy = 41;
        public static final int Move_Place_Wait = 42;
    }

    public static class PackAndShipStatusEnu {
        public static final int Pack_And_Ship = 10;
        public static final int Pack_And_Ship_Busy = 11;
        public static final int Pack_And_Ship_Wait = 12;
    }

    public static class PicklineStatusEnu {
        public static final int Needed = 10;
        public static final int DONE = 11;
        public static final int QCDONE = 20;
    }

    public static class PackingAndShippingStatusEnu {
        public static final int Needed = 10;
        public static final int NotNeeded = 92;
    }

    public static class PicklineLocalStatusEnu {
        public final static int LOCALSTATUS_NEW = 10;
        public final static int LOCALSTATUS_BUSY = 20;
        public final static int LOCALSTATUS_DONE_NOTSENT = 30;
        public final static int LOCALSTATUS_DONE_ERROR_SENDING = 32;
        public final static int LOCALSTATUS_DONE_SENT = 40;
    }

    public static class IntakeMATLineLocalStatusEnu {
        public final static int LOCALSTATUS_NEW = 10;
        public final static int LOCALSTATUS_BUSY = 20;
        public final static int LOCALSTATUS_DONE_NOTSENT = 30;
        public final static int LOCALSTATUS_DONE_SENT = 40;
    }

    public static class MovelineLocalStatusEnu {
        public final static int LOCALSTATUS_NEW = 10;
        public final static int LOCALSTATUS_BUSY = 20;
    }

    public static class InventoryBinStatusEnu {
        public static final int New = 10;
        public static final int InventoryDoneOnServer = 20;
        public static final int InventoryDone = 30;
    }

    public static class ReturnDocumentStatusEnu {
        public static final int New = 10;
        public static final int InventoryPause = 20;
        public static final int ReturnDone = 30;
    }

    public static class WorkflowReturnStepEnu {
        public static final int Return = 10;
        public static final int ReturnBusy = 1;
        public static final int ReturnPause = 12;
    }

    public enum ActivityActionEnu{
        Unknown,
        Delete,
        NoStart,
        Hold,
        Store,
        Next
    }

    public static class SourceDocumentTypeEnu {

        public final static int Salesorder = 1;
        public final static int PurchaseOrderReturn = 4;
        public final static int PurchaseOrder = 5;
        public final static int PurchaseLine = 39;
        public final static int CombinedPick = 95;
        public final static int Generated = 99;
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

    public static String pGetWorkflowDescriptionStr(String pvWorkflowCodeStr) {
        String resultStr = "";
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.BC.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_bc);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.BM.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_bm);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.BP.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_bp);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.EOM.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_eom);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.EOOM.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_eoom);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.EOOS.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_eoos);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.EOR.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_eor);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.EOS.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_eos);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.ER.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_er);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.IVM.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_ivm);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.IVS.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_ivs);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.MAM.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_mam);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.MAS.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_mas);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.MAT.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_mat);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.MI.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_mi);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.MO.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_mo);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.MT.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_mt);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.MV.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_mv);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.MVI.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_mvi);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.OMM.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_omm);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.OMOM.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_omom);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.OMOS.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_omos);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.OMR.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_omr);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.OMS.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_oms);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.PA.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_pa);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.PF.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_pf);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.PV.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_pv);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.RVR.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_rvr);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.RVS.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_rvs);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.SPV.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_spv);
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cPublicDefinitions.Workflows.UNKNOWN.toString())) {
            resultStr = cAppExtension.context.getString(R.string.ordertype_unknown);
        }
        return resultStr;
    }


    public static String pGetWorkflowByDescriptionStr(String pvWorkflowCodeStr) {

        String resultStr = "";

        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_bc))) {
            resultStr = cPublicDefinitions.Workflows.BC.toString();
        }

        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_bm))) {
            resultStr = cPublicDefinitions.Workflows.BM.toString();
        }

        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_bp))) {
            resultStr = cPublicDefinitions.Workflows.BP.toString();
        }


        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_eom))) {
            resultStr = cPublicDefinitions.Workflows.EOM.toString();
        }


        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_eoom))) {
            resultStr = cPublicDefinitions.Workflows.EOOM.toString();
        }

        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_eoos))) {
            resultStr = cPublicDefinitions.Workflows.EOOS.toString();
        }

        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_eor))) {
            resultStr = cPublicDefinitions.Workflows.EOR.toString();
        }

        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_eos))) {
            resultStr = cPublicDefinitions.Workflows.EOS.toString();
        }

        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_er))) {
            resultStr = cPublicDefinitions.Workflows.ER.toString();
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_ivm))) {
            resultStr = cPublicDefinitions.Workflows.IVM.toString();
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_ivs))) {
            resultStr = cPublicDefinitions.Workflows.IVS.toString();
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_mam))) {
            resultStr = cPublicDefinitions.Workflows.MAM.toString();
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_mas))) {
            resultStr = cPublicDefinitions.Workflows.MAS.toString();
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_mat))) {
            resultStr = cPublicDefinitions.Workflows.MAT.toString();
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_mi))) {
            resultStr = cPublicDefinitions.Workflows.MI.toString();
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_mo))) {
            resultStr = cPublicDefinitions.Workflows.MO.toString();
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_mt))) {
            resultStr = cPublicDefinitions.Workflows.MT.toString();
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_mv))) {
            resultStr = cPublicDefinitions.Workflows.MV.toString();
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_mvi))) {
            resultStr = cPublicDefinitions.Workflows.MVI.toString();
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_omm))) {
            resultStr = cPublicDefinitions.Workflows.OMM.toString();
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_omom))) {
            resultStr = cPublicDefinitions.Workflows.OMOM.toString();
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_omos))) {
            resultStr = cPublicDefinitions.Workflows.OMOS.toString();
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_omr))) {
            resultStr = cPublicDefinitions.Workflows.OMR.toString();
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_oms))) {
            resultStr = cPublicDefinitions.Workflows.OMS.toString();
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_pa))) {
            resultStr = cPublicDefinitions.Workflows.PA.toString();
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_pf))) {
            resultStr = cPublicDefinitions.Workflows.PF.toString();
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_pv))) {
            resultStr = cPublicDefinitions.Workflows.PV.toString();
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_rvr))) {
            resultStr = cPublicDefinitions.Workflows.RVR.toString();
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_rvs))) {
            resultStr = cPublicDefinitions.Workflows.RVS.toString();
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_spv))) {
            resultStr = cPublicDefinitions.Workflows.SPV.toString();
        }
        if(pvWorkflowCodeStr.equalsIgnoreCase(cAppExtension.context.getString(R.string.ordertype_unknown))) {
            resultStr = cPublicDefinitions.Workflows.UNKNOWN.toString();
        }

        return resultStr;
    }


}
