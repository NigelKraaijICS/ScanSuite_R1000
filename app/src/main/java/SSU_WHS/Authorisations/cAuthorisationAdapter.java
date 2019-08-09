package SSU_WHS.Authorisations;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import nl.icsvertex.scansuite.activities.general.MenuActivity;
import nl.icsvertex.scansuite.R;

public class cAuthorisationAdapter extends RecyclerView.Adapter<cAuthorisationAdapter.AuthorisationViewHolder> {
    private Context callerContext;
    public class AuthorisationViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewAuthorisation;
        private ImageView imageViewAuthorisation;
        public LinearLayout authorisationItemLinearLayout;

        public AuthorisationViewHolder(View itemView) {
            super(itemView);
            textViewAuthorisation = itemView.findViewById(R.id.textViewAuthorisationItem);
            textViewAuthorisation.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textViewAuthorisation.setSingleLine(true);
            textViewAuthorisation.setMarqueeRepeatLimit(5);
            textViewAuthorisation.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textViewAuthorisation.setSelected(true);
                }
            },1500);
            imageViewAuthorisation = itemView.findViewById(R.id.imageViewMenuItem);
            authorisationItemLinearLayout = itemView.findViewById(R.id.authorisationItemLinearLayout);
        }
    }

    private final LayoutInflater mInflater;
    public static List<cAuthorisationEntity> mAuthorisations; //cached copy of authorisations

    public cAuthorisationAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        callerContext = context;
    }

    @Override
    public cAuthorisationAdapter.AuthorisationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycler_authorisation, parent, false);
        return new cAuthorisationAdapter.AuthorisationViewHolder(itemView);
    }

    public void setAuthorisations(List<cAuthorisationEntity> authorisations) {
        mAuthorisations = authorisations;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(cAuthorisationAdapter.AuthorisationViewHolder holder, int position) {
        if (mAuthorisations != null) {
            final cAuthorisationEntity l_AuthorisationEntity = mAuthorisations.get(position);
            String l_authorisationStr = l_AuthorisationEntity.getAuthorisationStr();

            if (l_authorisationStr.equalsIgnoreCase(cAuthorisation.g_eAuthorisation.EXTERNAL_RETURN.toString())) {
                holder.textViewAuthorisation.setText(R.string.menuitem_external_return);
                holder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_external_return);
            }
            if (l_authorisationStr.equalsIgnoreCase(cAuthorisation.g_eAuthorisation.INTAKE.toString())) {
                holder.textViewAuthorisation.setText(R.string.menuitem_intake);
                holder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_intake);
            }
            if (l_authorisationStr.equalsIgnoreCase(cAuthorisation.g_eAuthorisation.INTAKE_EO.toString())) {
                holder.textViewAuthorisation.setText(R.string.menuitem_intake_eo);
                holder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_intake_eo);
            }
            if (l_authorisationStr.equalsIgnoreCase(cAuthorisation.g_eAuthorisation.INTAKE_MA.toString())) {
                holder.textViewAuthorisation.setText(R.string.menuitem_intake_ma);
                holder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_intake_ma);
            }
            if (l_authorisationStr.equalsIgnoreCase(cAuthorisation.g_eAuthorisation.INTAKE_OM.toString())) {
                holder.textViewAuthorisation.setText(R.string.menuitem_intake_om);
                holder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_intake_om);
            }
            if (l_authorisationStr.equalsIgnoreCase(cAuthorisation.g_eAuthorisation.INTAKECLOSE.toString())) {
                holder.textViewAuthorisation.setText(R.string.menuitem_intake_close);
                holder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_intakeclose);
            }
            if (l_authorisationStr.equalsIgnoreCase(cAuthorisation.g_eAuthorisation.INVENTORY.toString())) {
                holder.textViewAuthorisation.setText(R.string.menuitem_inventory);
                holder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_inventory);
                holder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_INVENTORY);
                holder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_INVENTORY);
            }
            if (l_authorisationStr.equalsIgnoreCase(cAuthorisation.g_eAuthorisation.INVENTORYCLOSE.toString())) {
                holder.textViewAuthorisation.setText(R.string.menuitem_inventoryclose);
                holder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_inventoryclose);
            }
            if (l_authorisationStr.equalsIgnoreCase(cAuthorisation.g_eAuthorisation.MOVE.toString())) {
                holder.textViewAuthorisation.setText(R.string.menuitem_move);
                holder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_move);
            }
            if (l_authorisationStr.equalsIgnoreCase(cAuthorisation.g_eAuthorisation.MOVE_MI.toString())) {
                holder.textViewAuthorisation.setText(R.string.menuitem_move_mi);
                holder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_move_mi);
            }
            if (l_authorisationStr.equalsIgnoreCase(cAuthorisation.g_eAuthorisation.MOVE_MO.toString())) {
                holder.textViewAuthorisation.setText(R.string.menuitem_move_mo);
                holder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_move_mo);
            }
            if (l_authorisationStr.equalsIgnoreCase(cAuthorisation.g_eAuthorisation.MOVE_MV.toString())) {
                holder.textViewAuthorisation.setText(R.string.menuitem_move_mv);
                holder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_move_mv);
            }
            if (l_authorisationStr.equalsIgnoreCase(cAuthorisation.g_eAuthorisation.MOVEITEM.toString())) {
                holder.textViewAuthorisation.setText(R.string.menuitem_moveitem);
                holder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_moveitem);
            }
            if (l_authorisationStr.equalsIgnoreCase(cAuthorisation.g_eAuthorisation.PICK.toString())) {
                holder.textViewAuthorisation.setText(R.string.menuitem_pick);
                holder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_pick);
                holder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_PICK);
                holder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_PICK);
            }
            if (l_authorisationStr.equalsIgnoreCase(cAuthorisation.g_eAuthorisation.SORTING.toString())) {
                holder.textViewAuthorisation.setText(R.string.menuitem_sort);
                holder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_sort);
                holder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_SORT);
                holder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_SORT);
            }
            if (l_authorisationStr.equalsIgnoreCase(cAuthorisation.g_eAuthorisation.SHIPPING.toString())) {
                holder.textViewAuthorisation.setText(R.string.menuitem_ship);
                holder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_ship);
                holder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_SHIP);
                holder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_SHIP);
            }
            if (l_authorisationStr.equalsIgnoreCase(cAuthorisation.g_eAuthorisation.PICK_PF.toString())) {
                holder.textViewAuthorisation.setText(R.string.menuitem_pick_pf);
                holder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_pick_pf);
            }
            if (l_authorisationStr.equalsIgnoreCase(cAuthorisation.g_eAuthorisation.PICK_PV.toString())) {
                holder.textViewAuthorisation.setText(R.string.menuitem_pick_pv);
                holder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_pick_pv);
            }
            if (l_authorisationStr.equalsIgnoreCase(cAuthorisation.g_eAuthorisation.RETURN.toString())) {
                holder.textViewAuthorisation.setText(R.string.menuitem_return);
                holder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_return);
            }
            if (l_authorisationStr.equalsIgnoreCase(cAuthorisation.g_eAuthorisation.SELFPICK.toString())) {
                holder.textViewAuthorisation.setText(R.string.menuitem_selfpick);
                holder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_selfpick);
            }

            holder.authorisationItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callerContext instanceof MenuActivity) {
                        ((MenuActivity)callerContext).setChosenAuthorization(l_AuthorisationEntity);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount () {
        if (mAuthorisations != null)
            return mAuthorisations.size();
        else return 0;
    }
}
