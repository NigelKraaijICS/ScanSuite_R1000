package SSU_WHS.Basics.Authorisations;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ICS.cAppExtension;
import SSU_WHS.Basics.Users.cUser;
import nl.icsvertex.scansuite.Activities.General.MenuActivity;
import nl.icsvertex.scansuite.R;

public class cAuthorisationAdapter extends RecyclerView.Adapter<cAuthorisationAdapter.AuthorisationViewHolder> {

    //Region Public Properties
    public class AuthorisationViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewAuthorisation;
        private ImageView imageViewAuthorisation;
        public LinearLayout authorisationItemLinearLayout;

        public AuthorisationViewHolder(View itemView) {
            super(itemView);
            this.textViewAuthorisation = itemView.findViewById(R.id.textViewAuthorisationItem);
            this.textViewAuthorisation.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewAuthorisation.setSingleLine(true);
            this.textViewAuthorisation.setMarqueeRepeatLimit(5);
            this.textViewAuthorisation.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textViewAuthorisation.setSelected(true);
                }
            },1500);
            this.imageViewAuthorisation = itemView.findViewById(R.id.imageViewBinItem);
            this.authorisationItemLinearLayout = itemView.findViewById(R.id.authorisationItemLinearLayout);
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
    @NonNull
    @Override
    public cAuthorisationAdapter.AuthorisationViewHolder onCreateViewHolder(@NonNull ViewGroup pvViewGroup, int pvViewTypeInt) {
        View itemView = LayoutInflaterObject.inflate(R.layout.recycler_authorisation, pvViewGroup, false);
        return new cAuthorisationAdapter.AuthorisationViewHolder(itemView);
    }

    public void setAuthorisations() {
       notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull cAuthorisationAdapter.AuthorisationViewHolder pvHolder, int pvPositionInt) {
        if (cUser.currentUser.autorisationWithoutMergeObl() != null) {
            final cAuthorisation authorisation = cUser.currentUser.autorisationWithoutMergeObl().get(pvPositionInt);

            this.mSetText(authorisation, pvHolder);
            this.mSetImage(authorisation, pvHolder);
            this.mSetTags(authorisation, pvHolder);

            pvHolder.authorisationItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cAppExtension.context instanceof MenuActivity) {
                        cUser.currentUser.currentAuthorisation = authorisation;
                        MenuActivity menuActivity = new MenuActivity();
                        menuActivity.pAuthorisationSelected();
                     }
                }
            });
        }
    }

    @Override
    public int getItemCount () {
        if (cUser.currentUser.autorisationWithoutMergeObl() != null)
            return cUser.currentUser.autorisationWithoutMergeObl().size();
        else return 0;
    }

    private void mSetTags(cAuthorisation pvAuthorisation, cAuthorisationAdapter.AuthorisationViewHolder pvHolder){

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INTAKE) {
            pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_INTAKE);
            pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_INTAKE);
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INTAKE_EO) {
            pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_INTAKE);
            pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_INTAKE);
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INTAKE_MA) {
            pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_INTAKE);
            pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_INTAKE);
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INVENTORY) {
            pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_INVENTORY);
            pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_INVENTORY);
        }


        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVE) {
            pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_MOVE);
            pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_MOVE);
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVE_MI) {
            pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_MOVE);
            pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_MOVE);
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVE_MI_SINGLEPIECE) {
            pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_MOVE);
            pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_MOVE);
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVE_MV) {
            pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_MOVE);
            pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_MOVE);
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.PICK) {
            pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_PICK);
            pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_PICK);
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.SORTING) {
            pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_SORT);
            pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_SORT);
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.SHIPPING) {
            pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_SHIP);
            pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_SHIP);
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.STORAGE) {
            pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_STORE);
            pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_STORE);
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.FINISH_SHIPPING) {
            pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_FINISH_SHIP);
            pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_FINSIH_SHIP);
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.QC) {
            pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_QC);
            pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_QC);
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.PICK_PF) {
            pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_PICK);
            pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_PICK);
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.PICK_PV) {
            pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_PICK);
            pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_PICK);
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.RETURN) {
            pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_RETURN);
            pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_RETURN);
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.SELFPICK) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_selfpick);
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_selfpick);
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.PACK_AND_SHIP) {
            pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_PACK_AND_SHIP);
            pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_PACK_AND_SHIP);
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.PACK_AND_SHIP_MERGE) {
            pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_PACK_AND_SHIP_MERGE);
            pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_PACK_AND_SHIP_MERGE);
        }

    }

    private void mSetText(cAuthorisation pvAuthorisation, cAuthorisationAdapter.AuthorisationViewHolder pvHolder){

        if (pvAuthorisation.getCustomAuthorisation() != null) {
            pvHolder.textViewAuthorisation.setText(pvAuthorisation.getCustomAuthorisation().getDescriptionStr());
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.EXTERNAL_RETURN) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_external_return);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INTAKE) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_intake);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INTAKE_EO) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_intake_eo);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INTAKE_MA) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_intake_ma);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INTAKE_OM) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_intake_om);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INTAKECLOSE) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_intake_close);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INVENTORY) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_inventory);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INVENTORYCLOSE) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_inventoryclose);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVE) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_move);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVE_MI) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_move_mi);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVE_MI_SINGLEPIECE) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_move_mi_singlepiece);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVE_MO) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_move_mo);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVE_MV) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_move_mv);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVEITEM) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_moveitem);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.PICK) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_pick);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.SORTING) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_sort);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.SHIPPING) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_ship);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.STORAGE) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_store);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.FINISH_SHIPPING) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_finish_ship);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.QC) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_qc);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.PICK_PF) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_pick_pf);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.PICK_PV) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_pick_pv);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.RETURN) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_return);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.SELFPICK) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_selfpick);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.PACK_AND_SHIP) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_pack_and_ship);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.PACK_AND_SHIP_MERGE) {
            pvHolder.textViewAuthorisation.setText(R.string.menuitem_pack_and_ship_merge);
            return;
        }
    }

    private void mSetImage(cAuthorisation pvAuthorisation, cAuthorisationAdapter.AuthorisationViewHolder pvHolder){

        if (pvAuthorisation.getCustomAuthorisation() != null) {
            pvHolder.imageViewAuthorisation.setImageBitmap(pvAuthorisation.customImageBmp());
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.EXTERNAL_RETURN) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_external_return);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INTAKE) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_intake);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INTAKE_EO) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_intake_eo);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INTAKE_MA) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_intake_ma);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INTAKE_OM) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_intake_om);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INTAKECLOSE) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_intakeclose);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INVENTORY) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_inventory);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INVENTORYCLOSE) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_inventoryclose);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVE) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_move);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVE_MI) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_move_mi);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVE_MI_SINGLEPIECE) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_move_mi);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVE_MO) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_move_mo);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVE_MV) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_move_mv);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVEITEM) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_moveitem);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.PICK) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_pick);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.SORTING) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_sort);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.SHIPPING) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_ship);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.STORAGE) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_intake_om);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.FINISH_SHIPPING) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_ship);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.QC) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_qualitycontrol);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.PICK_PF) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_pick_pf);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.PICK_PV) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_pick_pv);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.RETURN) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_return);
            return;
        }

        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.SELFPICK) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_selfpick);
            return;
        }
        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.PACK_AND_SHIP) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_ship);
            return;
        }
        if (pvAuthorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.PACK_AND_SHIP_MERGE) {
            pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_ship);
            return;
        }

    }


    //End Region Public Methods
}
