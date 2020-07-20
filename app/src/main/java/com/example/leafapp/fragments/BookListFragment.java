package com.example.leafapp.fragments;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.leafapp.R;
import com.example.leafapp.activities.SearchActivity;
import com.example.leafapp.models.Book;
import com.example.leafapp.viewholders.BooksViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class BookListFragment extends Fragment {

    private String TAG = "BookListFragment";

    private TextInputEditText mSearch;
    private RecyclerView mBookList;
    private FloatingActionButton mSearchBtn;

    private DatabaseReference mBookDatabase;
    private FirebaseRecyclerAdapter<Book, BooksViewHolder> adapter;

    private View mMainView;
    private int SEARCH_RESULT = 111;

    public BookListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_book_list, container, false);
        mBookList = mMainView.findViewById(R.id.book_list_list);
        mSearchBtn = mMainView.findViewById(R.id.book_list_search_btn);

        mBookDatabase = FirebaseDatabase.getInstance().getReference().child("Books");
        //mFriendsDatabse.keepSynced(true);

        mBookList.setHasFixedSize(true);
        mBookList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Click listener
        mSearchBtn.setOnClickListener(show_search_listener);

        // The "base query" is a query with no startAt/endAt/limit clauses that the adapter can use
        // to form smaller queries for each page.
        Query baseQuery = mBookDatabase;
        get_search_result(baseQuery);

        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter.startListening();
        mBookList.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    View.OnClickListener show_search_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
            startActivityForResult(searchIntent, SEARCH_RESULT);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: " + "kommer hit");
        if(requestCode == SEARCH_RESULT && resultCode == Activity.RESULT_OK) {
            String chosen_book = data.getStringExtra("result");

            Log.d(TAG, "onActivityResult: " + chosen_book);
            // Find all dinosaurs whose height is exactly 25 meters.
            Query search_query = mBookDatabase.orderByChild("title/0").equalTo(chosen_book);
            get_search_result(search_query);
        }
    }

    public void get_search_result(Query query){
        FirebaseRecyclerOptions<Book> options =
                new FirebaseRecyclerOptions.Builder<Book>()
                        .setQuery(query, Book.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Book, BooksViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BooksViewHolder holder, int position, @NonNull Book model) {
                holder.set_values(model.getTitle());
            }

            @NonNull
            @Override
            public BooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_book_list, parent, false);

                return new BooksViewHolder(view);
            }
        };

        adapter.startListening();
        mBookList.setAdapter(adapter);
    }
}