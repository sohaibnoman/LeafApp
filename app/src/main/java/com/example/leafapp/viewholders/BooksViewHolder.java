package com.example.leafapp.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leafapp.R;

import java.util.List;

public class BooksViewHolder extends RecyclerView.ViewHolder{

    ImageView mImage;
    TextView mTitle;
    TextView mSecondTitle;
    TextView mAuthors;
    TextView mIsbn;
    TextView mPublisher;
    TextView mLanguage;
    TextView mYear;

    View mView;

    public BooksViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;
        mImage = itemView.findViewById(R.id.book_list_image);
        mTitle = itemView.findViewById(R.id.book_list_title);
        mSecondTitle = itemView.findViewById(R.id.book_list_second_title);
        mAuthors = itemView.findViewById(R.id.book_list_authors);
        mIsbn = itemView.findViewById(R.id.book_list_isbn);
        mPublisher = itemView.findViewById(R.id.book_list_publisher);
        mLanguage = itemView.findViewById(R.id.book_list_language);
        mYear = itemView.findViewById(R.id.book_list_year);
    }

    public void set_values(List<String> title) {
        if (title.get(0) != null) {
            mTitle.setText(title.get(0));
        }

        if (title.size() > 1){
            mSecondTitle.setText(title.get(1));
        }
    }
}
