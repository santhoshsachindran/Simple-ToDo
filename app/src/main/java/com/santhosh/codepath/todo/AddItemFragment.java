package com.santhosh.codepath.todo;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.santhosh.codepath.todo.data.ToDoContract;

import static com.santhosh.codepath.todo.R.id.toolbar;

public class AddItemFragment extends DialogFragment {
    private EditText mTitle;
    private DatePicker mDate;
    private EditText mNotes;
    private Spinner mPriority;
    private Spinner mStatus;
    private Toolbar mToolbar;

    public static AddItemFragment newInstance(String title) {
        AddItemFragment frag = new AddItemFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitle = (EditText) view.findViewById(R.id.title_input);
        mDate = (DatePicker) view.findViewById(R.id.date_input);
        mNotes = (EditText) view.findViewById(R.id.notes_input);
        mPriority = (Spinner) view.findViewById(R.id.pri_input);
        mStatus = (Spinner) view.findViewById(R.id.status_input);
        mToolbar = (Toolbar) view.findViewById(toolbar);

        mToolbar.setVisibility(View.VISIBLE);
        String title = getArguments().getString("title", "");
        mToolbar.setTitle(title);

        mToolbar.inflateMenu(R.menu.save_discard);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.save_task:
                        addItem();
                        return true;
                    case R.id.discard_task:
                        dismiss();
                        return true;
                }

                return false;
            }
        });
    }

    private void addItem() {
        if (isTitleEmpty()) {
            return;
        }

        String title = mTitle.getText().toString();
        String date = String.valueOf(mDate.getDayOfMonth()) + "/" + String.valueOf(mDate.getMonth() + 1)
                + "/" + String.valueOf(mDate.getYear());
        String notes = mNotes.getText().toString();
        String pri = (String) mPriority.getSelectedItem();
        String status = (String) mStatus.getSelectedItem();

        ContentValues contentValues = new ContentValues();

        contentValues.put(ToDoContract.ListEntry.COLUMN_LIST_TITLE, title);
        contentValues.put(ToDoContract.ListEntry.COLUMN_LIST_DATE, date);
        contentValues.put(ToDoContract.ListEntry.COLUMN_LIST_NOTE, notes);
        contentValues.put(ToDoContract.ListEntry.COLUMN_LIST_PRIORITY, pri);
        contentValues.put(ToDoContract.ListEntry.COLUMN_LIST_STATUS, status);

        getContext().getContentResolver().insert(ToDoContract.ListEntry.CONTENT_URI, contentValues);
        dismiss();
    }

    private boolean isTitleEmpty() {
        if (mTitle.getText().toString() == null || mTitle.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.cannot_insert_with_null_title), Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }
}
