package com.example.bedms.Model;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bedms.Admin.AdminBedDetails;
import com.example.bedms.R;

import java.util.ArrayList;

public class BedAdapter extends RecyclerView.Adapter<BedAdapter.MyViewHolder> {
    private ArrayList<Bed> viewingAllBeds;
    public static final String MESSAGE_KEY1 = "text";
    public static final String MESSAGE_KEY2 = "position";

    public void addItemDecoration(DividerItemDecoration dividerItemDecoration) {
    }

    // Provide a reference to the views for each data item
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvViewName, tvViewWard, tvViewStatus;


        public MyViewHolder(View itemView) {
            super(itemView);
            tvViewName = (TextView) itemView.findViewById(R.id.bedRow);
            tvViewWard = (TextView) itemView.findViewById(R.id.wardRw);
            tvViewStatus = (TextView) itemView.findViewById(R.id.statusRw);



        }

        @Override
        public void onClick(View view) {
            int position = this.getLayoutPosition();
            String name = viewingAllBeds.get(position).getBedName();
            //Intent intent = new Intent(view.getContext(), UpdateActivity.class );
            //intent.putExtra(MESSAGE_KEY1 ,name);
            //intent.putExtra(MESSAGE_KEY2, position);
            //view.getContext().startActivity(intent); //start activity from another activity, here we are in MyAdapter class,
            // need to call start from the activity within that viewholder

        }

    }


    // Provide the dataset to the Adapter
    public BedAdapter(ArrayList<Bed> myDataset) {
        viewingAllBeds = myDataset;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public BedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String name = viewingAllBeds.get(position).getBedName();
        final String ward = viewingAllBeds.get(position).getBedWard();
        final String status = viewingAllBeds.get(position).getBedStatus();

        holder.tvViewName.setText(name);
        holder.tvViewWard.setText(ward);
        holder.tvViewStatus.setText(status);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AdminBedDetails.class);
                intent.putExtra("BedName", name);
                view.getContext().startActivity(intent); //start activity from another activity, here we are in MyAdapter class,
                // need to call start from the activity within that viewholder
            }
        });

    }

    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return viewingAllBeds.size();
    }

}