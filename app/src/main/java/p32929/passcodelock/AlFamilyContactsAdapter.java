package p32929.passcodelock;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import p32929.passcodelock.db.AppDataBase;
import p32929.passcodelock.db.FamilyContact;


public class AlFamilyContactsAdapter extends RecyclerView.Adapter<AlFamilyContactsAdapter.View_Holder> {
    private AlFamilyContactsAdapter.OnitemClickListener mListener;

    public interface OnitemClickListener {
        void onAccept(int position);

        void onReject(int position);

    }


    LayoutInflater layoutInflater;
    List<FamilyContact> users;


    public AlFamilyContactsAdapter(Context ctx, List<FamilyContact> users) {
        this.layoutInflater = LayoutInflater.from(ctx);
        this.users = users;
    }

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_all_contacts, parent, false);
        return new View_Holder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {
        FamilyContact currentItem = users.get(position);
        holder.name.setText(users.get(position).getName());
        holder.number.setText(users.get(position).getNumber() + "");
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(holder.edit.getContext(), ActivityUpdateFamilyContact.class);
                i.putExtra("name", users.get(position).getName());
                i.putExtra("number", users.get(position).getNumber());
                i.putExtra("id", users.get(position).getId());
//                holder.delete.setImageResource(R.drawable.ic_baseline_delete_24);
                holder.edit.getContext().startActivity(i);

            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppDataBase database = AppDataBase.getInstance(holder.delete.getContext());
                FamilyContact data = new FamilyContact();
                data.setName(users.get(position).getName());
                data.setNumber(users.get(position).getNumber());
                data.setId(users.get(position).getId());
                database.Dao().deleteFamily(data);
                Toast.makeText(holder.delete.getContext(), "Deleted succefully", Toast.LENGTH_SHORT).show();
                holder.delete.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class View_Holder extends RecyclerView.ViewHolder {
        TextView name, number;
        TextView edit;
        TextView delete;

        public View_Holder(@NonNull View itemView, final AlFamilyContactsAdapter.OnitemClickListener listener) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tvPhoneContactName);
            number = (TextView) itemView.findViewById(R.id.tvPhoneContactNumber);
            edit = itemView.findViewById(R.id.btnEdit);
            delete = itemView.findViewById(R.id.btnDelete);


        }
    }
}


