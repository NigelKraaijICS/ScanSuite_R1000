package SSU_WHS.Intake.IntakeorderMATLineSummary;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLineAdapter;

public class cIntakeorderMATSummaryLineRecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback{

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private cIntakeorderMATSummaryLineRecyclerItemTouchHelper.RecyclerItemTouchHelperListener listener;

    //End Region Private Properties

    //Region Constructor

    public cIntakeorderMATSummaryLineRecyclerItemTouchHelper(int pvDragDirsInt, int pvSwipeDirsInt, cIntakeorderMATSummaryLineRecyclerItemTouchHelper.RecyclerItemTouchHelperListener pvListener) {
        super(pvDragDirsInt, pvSwipeDirsInt);
        this.listener = pvListener;
    }


    //End Region Constructor

    //Region Default Methods


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((cIntakeorderMATSummaryLineAdapter.IntakeorderMATSummaryLineViewHolder) viewHolder).viewForeground;

            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((cIntakeorderMATSummaryLineAdapter.IntakeorderMATSummaryLineViewHolder) viewHolder).viewForeground;
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((cIntakeorderMATSummaryLineAdapter.IntakeorderMATSummaryLineViewHolder) viewHolder).viewForeground;
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((cIntakeorderMATSummaryLineAdapter.IntakeorderMATSummaryLineViewHolder) viewHolder).viewForeground;

        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        this.listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }

    //End Region Default Methods




}
