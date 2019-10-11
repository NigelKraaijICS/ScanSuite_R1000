package SSU_WHS.General.Warehouseorder;

import androidx.lifecycle.ViewModelProviders;

import ICS.cAppExtension;

public class cWarehouseorder {

    public enum ActionTypeEnu {
        TAKE,
        PLACE
    }

    public enum OrderTypeEnu {
        PICKEN
    }

    public enum StepCodeEnu {
        Pick_Picking,
        Pick_Sorting,
        Pick_PackAndShip

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
        BP

    }

    public class WorkflowPickStepEnu {

        public static final int PickPicking = 10;
        public static final int PickSorting = 20;
        public static final int PickPackAndShip = 40;

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

    public enum ActivityActionEnu{

        Unknown,
        Delete,
        NoStart,
        Hold,
        Store,
        Next

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
