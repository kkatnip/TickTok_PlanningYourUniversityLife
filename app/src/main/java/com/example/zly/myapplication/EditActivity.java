package com.example.zly.myapplication;


import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

enum EDIT_OR_NEW {
    EDIT,
    NEW
}

public class EditActivity extends Activity implements OnClickListener{

    private DataBase mydb;
    SQLiteDatabase sqldb;
    Button buttonSave;
    Button buttonCancel;
    EditText text;
    EDIT_OR_NEW edit_or_new;
    int editing_item_id;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        buttonCancel = (Button)findViewById(R.id.button1);
        buttonCancel.setOnClickListener(this);

        buttonSave = (Button)findViewById(R.id.button2);
        buttonSave.setOnClickListener(this);

        text = (EditText)findViewById(R.id.editText1);


        mydb = new DataBase(this);
        sqldb = mydb.getWritableDatabase();

        // get extra from caller activity
        int arg = getIntent().getIntExtra("row_id", -1);
        System.out.println(arg);
        if(-1 == arg){
            // new
            edit_or_new = EDIT_OR_NEW.NEW;

        }else{
            // edit
            edit_or_new = EDIT_OR_NEW.EDIT;


            // query the text by id from caller
            String sql = "select * from " + DataBase.TABLE_NAME + " order by " + DataBase.ID;
            Cursor c = sqldb.rawQuery(sql, null);
            c.moveToPosition(arg);
            int db_id = c.getInt(c.getColumnIndex(DataBase.ID));
            editing_item_id = db_id;
            String str = c.getString(c.getColumnIndex(DataBase.CONTENT));
            text.setText(str);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit, menu);
        return true;
    }

    /**
     * @param v
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if( R.id.button1 == v.getId()) {
            // cancel
            System.out.println("Clicked Cancel");
            finish();
        }else{
            // save
            System.out.println("clicked Save");
            String input = text.getText().toString();
            if(0 == input.length()){
                // todo: pop up a dialog " note is empty!";
                System.out.println("note is empty!");
            }else{
                String sql;
                // save it to database
                if(EDIT_OR_NEW.NEW == edit_or_new){
                    // todo: ok to ignore _id ?
                    sql = "insert into " + DataBase.TABLE_NAME + " ( "  +
                            DataBase.CONTENT + " ) " + " values ( " + "'" +
                            input + "'" + " ); ";
                }else{
                    // update existent record
                    sql = "update " + DataBase.TABLE_NAME + " set " + DataBase.CONTENT
                            + " = '" + input + "' " + " where " + DataBase.ID +
                            " == " + editing_item_id;
                    System.out.println(input);
                }
                sqldb.execSQL(sql);

                finish();
            }
        }
    }
}
