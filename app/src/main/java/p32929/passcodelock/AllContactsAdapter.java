package p32929.passcodelock;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import p32929.passcodelock.db.Contact;


public class AllContactsAdapter extends RecyclerView.Adapter<AllContactsAdapter.View_Holder> {
    private AllContactsAdapter.OnitemClickListener mListener;

    public interface OnitemClickListener {
        void onAccept(int position);

        void onReject(int position);

    }

    public void setOnItemClick(AllContactsAdapter.OnitemClickListener listener) {
        mListener = listener;
    }

    LayoutInflater layoutInflater;
    List<Contact> users;


    public AllContactsAdapter(Context ctx, List<Contact> users) {
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
        Contact currentItem = users.get(position);
        holder.name.setText(users.get(position).getName());
        holder.number.setText(users.get(position).getNumber()+"");
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class View_Holder extends RecyclerView.ViewHolder {
        TextView name, number;


        public View_Holder(@NonNull View itemView, final AllContactsAdapter.OnitemClickListener listener) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tvContactName);
            number = (TextView) itemView.findViewById(R.id.tvContactNumber);


        }
    }
}


