package SSU_WHS.Move.MoveorderLines;

import android.graphics.Canvas;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class cMoveorderLineRecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback{

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private cMoveorderLineRecyclerItemTouchHelper.RecyclerItemTouchHelperListener listener;

    //End Region Private Properties

    //Region Constructor

    public cMoveorderLineRecyclerItemTouchHelper(int pvDragDirsInt, int pvSwipeDirsInt, cMoveorderLineRecyclerItemTouchHelper.RecyclerItemTouchHelperListener pvListener) {
        super(pvDragDirsInt, pvSwipeDirsInt);
        this.listener = pvListener;
    }


    //End Region Constructor

    //Region Default Methods


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((cMoveorderLineAdapter.MoveorderLineViewHolder) viewHolder).viewForeground;

            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((cMoveorderLineAdapter.MoveorderLineViewHolder) viewHolder).viewForeground;
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((cMoveorderLineAdapter.MoveorderLineViewHolder) viewHolder).viewForeground;
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((cMoveorderLineAdapter.MoveorderLineViewHolder) viewHolder).viewForeground;

        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
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
