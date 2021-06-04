package com.amarjeet.android.teckatproject;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ArticleViewHolder extends RecyclerView.ViewHolder {
    TextView payName,payMobile,payCourse,payStatus,payAmount;
    public ArticleViewHolder(@NonNull View itemView) {
        super(itemView);
        payName=itemView.findViewById(R.id.payName);
        payMobile=itemView.findViewById(R.id.payMobile);
        payCourse=itemView.findViewById(R.id.payCourse);
        payAmount=itemView.findViewById(R.id.payAmount);
        payStatus=itemView.findViewById(R.id.payStatus);
    }
}
