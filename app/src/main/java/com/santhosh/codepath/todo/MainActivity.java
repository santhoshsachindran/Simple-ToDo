package com.santhosh.codepath.todo;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.santhosh.codepath.todo.data.ToDoContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String[] TODO_ITEM_COLUMNS = {
            ToDoContract.ListEntry._ID,
            ToDoContract.ListEntry.COLUMN_LIST_TITLE,
            ToDoContract.ListEntry.COLUMN_LIST_DATE,
            ToDoContract.ListEntry.COLUMN_LIST_PRIORITY
    };
    private ListView mListView;
    private TextView mEmptyView;
    private ToDoCursorAdapter mTodoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(getString(R.string.todo_list));

        mListView = (ListView) findViewById(R.id.todo_list);
        mEmptyView = (TextView) findViewById(R.id.empty_view);
        mTodoAdapter = new ToDoCursorAdapter(this, null, 0);

        mListView.setAdapter(mTodoAdapter);
        mListView.setEmptyView(mEmptyView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                Uri uri = Uri.withAppendedPath(ToDoContract.ListEntry.CONTENT_URI, String.valueOf(
                        cursor.getInt(cursor.getColumnIndex(ToDoContract.ListEntry._ID))));
                intent.putExtra("URI", uri.toString());
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(0, null, this);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                ToDoContract.ListEntry.CONTENT_URI, TODO_ITEM_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            mTodoAdapter.changeCursor(data);
            mEmptyView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyView.setText(getString(R.string.no_items_add_new));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTodoAdapter.changeCursor(null);
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
}
