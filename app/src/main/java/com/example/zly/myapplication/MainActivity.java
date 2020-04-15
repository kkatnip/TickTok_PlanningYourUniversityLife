package com.example.zly.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

    public final static String EXTRA_MESSAGE = "com.mayqlzu.noteapp.MESSAGE";

    private DataBase db;
    Cursor c;
    ListView listView;
    int xButtonVisible = View.INVISIBLE;

    // display 10 chars at most, or the x button will exceed the bound and missing
    ViewBinder vbinder = new ViewBinder() {

        public boolean setViewValue(View aView, Cursor aCursor, int aColumnIndex) {

            if (aColumnIndex == 1) { // the second column in table {_id, context}
                String str = aCursor.getString(aColumnIndex);
                TextView textView = (TextView) aView;
                int len = str.length() > 10 ? 10 : str.length(); // 10 chars at most
                textView.setText(str.substring(0, len));
                return true;
            }

            return false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DataBase(this);
        SQLiteDatabase d = db.getReadableDatabase();
        String sql = "SELECT * FROM " + DataBase.TABLE_NAME + " order by " + DataBase.ID;;
        c = d.rawQuery(sql, null);

        listView = (ListView) findViewById(R.id.listView1);
        String[] from = {new String("content")};
        int[] to = {R.id.entry};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this, R.layout.list_entry, c, from, to, 0);
        adapter.setViewBinder(vbinder);
        listView.setAdapter(adapter);


        Button buttonNew = (Button)findViewById(R.id.button1);
        buttonNew.setOnClickListener(this);
        Button buttonDelete = (Button)findViewById(R.id.button2);
        buttonDelete.setOnClickListener(this);

        listView.setOnItemClickListener(new  AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id){
                // todo: what do position and id mean?
                //SimpleCursorAdapter adapter = (SimpleCursorAdapter)parent.getAdapter();
                System.out.println(position);

                // notice: how i get Context :)
                Intent intent = new Intent(parent.getContext(), EditActivity.class);
                intent.putExtra("row_id", position);
                startActivity(intent);
            }
        });
    }

    protected void onRestart(){
        super.onRestart();
        System.out.println("onRestart()");

        SQLiteDatabase d = db.getReadableDatabase();
        String sql = "SELECT * FROM " + DataBase.TABLE_NAME + " order by " + DataBase.ID;;
        c = d.rawQuery(sql, null);

        String[] from = {new String("content")};
        int[] to = {R.id.entry};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this, R.layout.list_entry, c, from, to, 0);
        adapter.setViewBinder(vbinder);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void onClick(View arg0) {

        if( R.id.button1 == arg0.getId()) {
            // TODO Auto-generated method stub
            System.out.println("NewButton Clicked");

            Intent intent = new Intent(this, EditActivity.class);
            startActivity(intent);
        }else{
            // Button Delete clicked
            // show or hiden x button
            // switch the flag
            xButtonVisible = (View.VISIBLE == xButtonVisible) ?
                    View.INVISIBLE : View.VISIBLE;
            int item_count = listView.getChildCount();
            for(int i=0; i<item_count; i++){
                Button btn = (Button)listView.getChildAt(i).findViewById(R.id.entry_button);
                btn.setVisibility(xButtonVisible);
                // add button event listener
                btn.setOnClickListener(
                        new Button.OnClickListener(){
                            public void onClick(View v){
                                int index = listView.indexOfChild((View)v.getParent());
                                //System.out.println(index);

                                SQLiteDatabase d = db.getWritableDatabase();
                                String sql = "select * from " + DataBase.TABLE_NAME + " order by " + DataBase.ID;
                                Cursor c = d.rawQuery(sql, null);
                                c.moveToPosition(index); // todo: adjust?
                                int id = c.getInt(c.getColumnIndex(DataBase.ID));
                                //System.out.println(id);
                                sql = "DELETE FROM " + DataBase.TABLE_NAME + " WHERE " +
                                        DataBase.ID + "=" + id;
                                d.execSQL(sql);

                                sql = "SELECT * FROM " + DataBase.TABLE_NAME + " order by " + DataBase.ID ;
                                c = d.rawQuery(sql, null);

							        /*c.moveToFirst();
							        do {
							        	int temp_id = c.getInt(c.getColumnIndex(DataBase.ID));
							        	System.out.println(temp_id);
							        }while(c.moveToNext());
							        */

                                String[] from = {new String("content")};
                                int[] to = {R.id.entry};
                                SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                                        v.getContext(), R.layout.list_entry, c, from, to, 0);
                                adapter.setViewBinder(vbinder);
                                listView.setAdapter(adapter);

                            }
                        });
            }
        }
    }
}
