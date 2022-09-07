package p32929.passcodelock;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import p32929.passcodelock.db.AppDataBase;
import p32929.passcodelock.db.FamilyContact;


public class PhoneContactsFamilyAdapter extends RecyclerView.Adapter<PhoneContactsFamilyAdapter.View_Holder> {
    private PhoneContactsFamilyAdapter.OnitemClickListener mListener;

    public interface OnitemClickListener {
        void onAccept(int position);

        void onReject(int position);

    }


    LayoutInflater layoutInflater;
    List<FamilyContact> users;
    List<FamilyContact> addedusers;


    public PhoneContactsFamilyAdapter(Context ctx, List<FamilyContact> users, List<FamilyContact> added) {
        this.layoutInflater = LayoutInflater.from(ctx);
        this.users = users;
        this.addedusers = added;
    }

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_all_phone_contacts, parent, false);
        return new View_Holder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {
        FamilyContact currentItem = users.get(position);
        holder.name.setText(users.get(position).getName());
        holder.number.setText(users.get(position).getNumber() + "");
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean resutlt=false;
                for (FamilyContact data : addedusers) {
                    if (data.getNumber().equals(users.get(position).getNumber())) {
                        resutlt=true;
                        break;
                    }
                }
                if(resutlt){
                    Toast.makeText(holder.add.getContext(), "This is already Added", Toast.LENGTH_SHORT).show();
                }else {
                    AppDataBase database = AppDataBase.getInstance(holder.add.getContext());
                    FamilyContact data = new FamilyContact();
                    data.setName(users.get(position).getName());
                    data.setNumber(users.get(position).getNumber());
                    database.Dao().insertFamily(data);
                    Toast.makeText(holder.add.getContext(), "Added succefully", Toast.LENGTH_SHORT).show();
                    holder.add.setVisibility(View.GONE);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class View_Holder extends RecyclerView.ViewHolder {
        TextView name, number;
        Button add;

        public View_Holder(@NonNull View itemView, final PhoneContactsFamilyAdapter.OnitemClickListener listener) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tvPhoneContactNames);
            number = (TextView) itemView.findViewById(R.id.tvPhoneContactNumbers);
            add = itemView.findViewById(R.id.btnAdd);

        }
    }
}


