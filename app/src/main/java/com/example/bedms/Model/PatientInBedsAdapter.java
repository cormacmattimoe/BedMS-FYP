package com.example.bedms.Model;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bedms.Patient.PatientListinBedsDetails;
import com.example.bedms.R;

import java.util.ArrayList;

public class PatientInBedsAdapter extends RecyclerView.Adapter<PatientInBedsAdapter.MyViewHolder> {
    private ArrayList<Patient> viewAllPatients;
    public static final String MESSAGE_KEY1 = "text";
    public static final String MESSAGE_KEY2 = "position";

    public void addItemDecoration(DividerItemDecoration dividerItemDecoration) {
    }

    // Provide a reference to the views for each data item
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvViewName, tvViewDob;


        public MyViewHolder(View itemView) {
            super(itemView);
            tvViewName = (TextView) itemView.findViewById(R.id.statusRw);
            tvViewDob = (TextView) itemView.findViewById(R.id.dobTv);


        }

        @Override
        public void onClick(View view) {
            int position = this.getLayoutPosition();
            String name = viewAllPatients.get(position).getpName();
            //Intent intent = new Intent(view.getContext(), UpdateActivity.class );
            //intent.putExtra(MESSAGE_KEY1 ,name);
            //intent.putExtra(MESSAGE_KEY2, position);
            //view.getContext().startActivity(intent); //start activity from another activity, here we are in MyAdapter class,
            // need to call start from the activity within that viewholder

        }

    }


    // Provide the dataset to the Adapter
    public PatientInBedsAdapter(ArrayList<Patient> myDataset) {
        viewAllPatients = myDataset;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public PatientInBedsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.patientrow, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String name = viewAllPatients.get(position).getpName();
        final String dob = viewAllPatients.get(position).getpDOB();

        holder.tvViewName.setText(name);
        holder.tvViewDob.setText(dob);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Item clicked", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(view.getContext(), PatientListinBedsDetails.class);
                intent.putExtra("Name", name);
                intent.putExtra("Dob", dob);
                view.getContext().startActivity(intent); //start activity from another activity, here we are in MyAdapter class,
                // need to call start from the activity within that viewholder
            }
        });

    }

    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return viewAllPatients.size();
    }

}