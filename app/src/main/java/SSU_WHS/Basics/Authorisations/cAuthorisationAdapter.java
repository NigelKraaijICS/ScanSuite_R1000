package SSU_WHS.Basics.Authorisations;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import SSU_WHS.Basics.Users.cUser;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.Activities.general.MenuActivity;

public class cAuthorisationAdapter extends RecyclerView.Adapter<cAuthorisationAdapter.AuthorisationViewHolder> {

    //Region Public Properties
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
    //End Region Public Properties

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    //End Region Private Properties

    //Region Constructor
    public cAuthorisationAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    //Region Public Methods
    @Override
    public cAuthorisationAdapter.AuthorisationViewHolder onCreateViewHolder(ViewGroup pvViewGroup, int pvViewTypeInt) {
        View itemView = LayoutInflaterObject.inflate(R.layout.recycler_authorisation, pvViewGroup, false);
        return new cAuthorisationAdapter.AuthorisationViewHolder(itemView);
    }

    public void setAuthorisations() {
       notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(cAuthorisationAdapter.AuthorisationViewHolder pvHolder, int pvPositionInt) {
        if (cUser.currentUser.autorisationObl != null) {
            final cAuthorisation authorisation = cUser.currentUser.autorisationObl.get(pvPositionInt);


            //todo: put this back, but differently
            if (authorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.EXTERNAL_RETURN) {
                pvHolder.textViewAuthorisation.setText(R.string.menuitem_external_return);
                pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_external_return);
            }

            if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INTAKE) {
                pvHolder.textViewAuthorisation.setText(R.string.menuitem_intake);
                pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_intake);
            }

            if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INTAKE_EO) {
                pvHolder.textViewAuthorisation.setText(R.string.menuitem_intake_eo);
                pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_intake_eo);
            }

            if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INTAKE_MA) {
                pvHolder.textViewAuthorisation.setText(R.string.menuitem_intake_ma);
                pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_intake_ma);
            }

            if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INTAKE_OM) {
                pvHolder.textViewAuthorisation.setText(R.string.menuitem_intake_om);
                pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_intake_om);
            }

            if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INTAKECLOSE) {
                pvHolder.textViewAuthorisation.setText(R.string.menuitem_intake_close);
                pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_intakeclose);
            }

            if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INVENTORY) {
                pvHolder.textViewAuthorisation.setText(R.string.menuitem_inventory);
                pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_inventory);
                pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_INVENTORY);
                pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_INVENTORY);
            }

            if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INVENTORYCLOSE) {
                pvHolder.textViewAuthorisation.setText(R.string.menuitem_inventoryclose);
                pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_inventoryclose);
            }

            if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVE) {
                pvHolder.textViewAuthorisation.setText(R.string.menuitem_move);
                pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_move);
            }

            if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVE_MI) {
                pvHolder.textViewAuthorisation.setText(R.string.menuitem_move_mi);
                pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_move_mi);
            }

            if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVE_MO) {
                pvHolder.textViewAuthorisation.setText(R.string.menuitem_move_mo);
                pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_move_mo);
            }

            if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVE_MV) {
                pvHolder.textViewAuthorisation.setText(R.string.menuitem_move_mv);
                pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_move_mv);
            }
            if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVEITEM) {
                pvHolder.textViewAuthorisation.setText(R.string.menuitem_moveitem);
                pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_moveitem);
            }

            if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.PICK) {
                pvHolder.textViewAuthorisation.setText(R.string.menuitem_pick);
                pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_pick);
                pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_PICK);
                pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_PICK);
            }

            if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.SORTING) {
                pvHolder.textViewAuthorisation.setText(R.string.menuitem_sort);
                pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_sort);
                pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_SORT);
                pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_SORT);
            }

            if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.SHIPPING) {
                pvHolder.textViewAuthorisation.setText(R.string.menuitem_ship);
                pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_ship);
                pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_SHIP);
                pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_SHIP);
            }

            if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.PICK_PF) {
                pvHolder.textViewAuthorisation.setText(R.string.menuitem_pick_pf);
                pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_pick_pf);
            }

            if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.PICK_PV) {
                pvHolder.textViewAuthorisation.setText(R.string.menuitem_pick_pv);
                pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_pick_pv);
            }

            if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.RETURN) {
                pvHolder.textViewAuthorisation.setText(R.string.menuitem_return);
                pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_return);
            }

            if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.SELFPICK) {
                pvHolder.textViewAuthorisation.setText(R.string.menuitem_selfpick);
                pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_selfpick);
            }

            pvHolder.authorisationItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cAppExtension.context instanceof MenuActivity) {
                        cUser.currentUser.currentAuthorisation = authorisation;
                        MenuActivity.pAuthorisationSelected();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount () {
        if (cUser.currentUser.autorisationObl != null)
            return cUser.currentUser.autorisationObl.size();
        else return 0;
    }
    //End Region Public Methods
}
