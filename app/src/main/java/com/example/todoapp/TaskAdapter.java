package com.example.todoapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{
    private ClickListener clickListener;
     public interface ClickListener {
        void onClick(int pos);
        void onLongClick(int pos);
    }

    List<Task> list;

    public TaskAdapter(List<Task> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_task, viewGroup, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Task task = list.get(i);
        viewHolder.textTitle.setText(task.getTitle());
        viewHolder.textDesc.setText(task.getDescription());
        viewHolder.deadline.setText(task.getDeadline());
        viewHolder.createdTime.setText(parseDate(task.getCreatedTime()));

        switch (task.getImageImportance()) {
            case 1:
                viewHolder.image.setImageResource(R.drawable.ic_error_outline);
                break;
            case 2:
                viewHolder.image.setImageResource(R.drawable.ic_star);
                break;
            case 3:
                viewHolder.image.setImageResource(R.drawable.ic_star_border);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        TextView textDesc;
        TextView deadline;
        TextView createdTime;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_title);
            textDesc = itemView.findViewById(R.id.text_description);
            image = itemView.findViewById(R.id.image_importance);
            deadline = itemView.findViewById(R.id.deadline);
            createdTime = itemView.findViewById(R.id.created_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    clickListener.onLongClick(getAdapterPosition());
                    return true;
                }
            });

        }
    }

    public List<Task> getList() {
        return list;
    }

    public void setList(List<Task> list) {
        this.list = list;
    }

    public String parseDate(long dateLong) {
        String outputPattern = "dd-MM-yyyy";
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = new Date(dateLong);
            str = outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

}
