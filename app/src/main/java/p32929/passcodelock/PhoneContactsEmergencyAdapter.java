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
import p32929.passcodelock.db.Contact;


public class PhoneContactsEmergencyAdapter extends RecyclerView.Adapter<PhoneContactsEmergencyAdapter.View_Holder> {
    private PhoneContactsEmergencyAdapter.OnitemClickListener mListener;

    public interface OnitemClickListener {
        void onAccept(int position);

        void onReject(int position);

    }


    LayoutInflater layoutInflater;
    List<Contact> users;
    List<Contact> addedusers;


    public PhoneContactsEmergencyAdapter(Context ctx, List<Contact> users, List<Contact> added) {
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
        Contact currentItem = users.get(position);
        holder.name.setText(users.get(position).getName());
        holder.number.setText(users.get(position).getNumber() + "");
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean resutlt=false;
                for (Contact data : addedusers) {
                    if (data.getNumber().equals(users.get(position).getNumber())) {
                        resutlt=true;
                        break;
                    }
                }
                if(resutlt){
                    Toast.makeText(holder.add.getContext(), "This is already Added", Toast.LENGTH_SHORT).show();
                }else {
                    AppDataBase database = AppDataBase.getInstance(holder.add.getContext());
                    Contact data = new Contact();
                    data.setName(users.get(position).getName());
                    data.setNumber(users.get(position).getNumber());
                    database.Dao().insert(data);
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

        public View_Holder(@NonNull View itemView, final PhoneContactsEmergencyAdapter.OnitemClickListener listener) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tvPhoneContactNames);
            number = (TextView) itemView.findViewById(R.id.tvPhoneContactNumbers);
            add = itemView.findViewById(R.id.btnAdd);

        }
    }
}


