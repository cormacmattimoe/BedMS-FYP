package com.example.bedms.Model;




import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;


import com.example.bedms.R;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.MyViewHolder> {
    private ArrayList<Employee> viewingAllEmployees;
    public static final String MESSAGE_KEY1 = "text";
    public static final String MESSAGE_KEY2 = "position";

    public void addItemDecoration(DividerItemDecoration dividerItemDecoration) {
    }

    // Provide a reference to the views for each data item
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvViewName, tvRole;



        public MyViewHolder(View itemView) {
            super(itemView);
            tvViewName = (TextView) itemView.findViewById(R.id.empName);
            tvRole = (TextView) itemView.findViewById(R.id.empRole);

        }

        @Override
        public void onClick(View view) {
            int position = this.getLayoutPosition();
            String name = viewingAllEmployees.get(position).getEmployeeName();
            //Intent intent = new Intent(view.getContext(), UpdateActivity.class );
            //intent.putExtra(MESSAGE_KEY1 ,name);
            //intent.putExtra(MESSAGE_KEY2, position);
            //view.getContext().startActivity(intent); //start activity from another activity, here we are in MyAdapter class,
            // need to call start from the activity within that viewholder

        }

    }


    // Provide the dataset to the Adapter
    public EmployeeAdapter(ArrayList<Employee> myDataset) {
        viewingAllEmployees = myDataset;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public EmployeeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.employeerow, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String name = viewingAllEmployees.get(position).getEmployeeName();
        final String role = viewingAllEmployees.get(position).getEmployeeRole();

        holder.tvViewName.setText(name);
        holder.tvRole.setText(role);

    }





    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return viewingAllEmployees.size();
    }

}

