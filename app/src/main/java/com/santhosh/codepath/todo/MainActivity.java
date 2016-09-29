package com.santhosh.codepath.todo;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.santhosh.codepath.todo.data.ToDoContract;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<TodoItem>> {
    private ListView mListView;
    private TextView mEmptyView;
    private TodoAdapter mTodoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(getString(R.string.todo_list));

        mListView = (ListView) findViewById(R.id.todo_list);
        mEmptyView = (TextView) findViewById(R.id.empty_view);
        mTodoAdapter = new TodoAdapter(this, new ArrayList<TodoItem>());

        mListView.setAdapter(mTodoAdapter);
        mListView.setEmptyView(mEmptyView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TodoItem todoItem = (TodoItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("PARCELABLE", todoItem);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(0, null, this);

    }


    @Override
    public Loader<List<TodoItem>> onCreateLoader(int id, Bundle args) {
        return new FetchTodoItems(this);
    }

    @Override
    public void onLoadFinished(Loader<List<TodoItem>> loader, List<TodoItem> data) {
        mTodoAdapter.clear();
        if (data != null && !data.isEmpty()) {
            mTodoAdapter.addAll(data);
            mEmptyView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyView.setText(getString(R.string.no_items_add_new));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<TodoItem>> loader) {
        mTodoAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        getLoaderManager().restartLoader(0, null, this);
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_task:
                Intent intent = new Intent(this, EditActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class FetchTodoItems extends AsyncTaskLoader<List<TodoItem>> {
        private Context fetchContext;

        public FetchTodoItems(Context context) {
            super(context);
            fetchContext = context;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public List<TodoItem> loadInBackground() {
            List<TodoItem> items = new ArrayList<>();

            Cursor cursor = fetchContext.getContentResolver().query(ToDoContract.ListEntry.CONTENT_URI,
                    null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    String title = cursor.getString(cursor.getColumnIndex(ToDoContract.ListEntry.COLUMN_LIST_TITLE));
                    String date = cursor.getString(cursor.getColumnIndex(ToDoContract.ListEntry.COLUMN_LIST_DATE));
                    String note = cursor.getString(cursor.getColumnIndex(ToDoContract.ListEntry.COLUMN_LIST_NOTE));
                    String priority = cursor.getString(cursor.getColumnIndex(ToDoContract.ListEntry.COLUMN_LIST_PRIORITY));
                    String status = cursor.getString(cursor.getColumnIndex(ToDoContract.ListEntry.COLUMN_LIST_STATUS));

                    items.add(new TodoItem(title, date, note, priority, status));
                } while (cursor.moveToNext());
            }

            cursor.close();

            return items;
        }
    }
}
