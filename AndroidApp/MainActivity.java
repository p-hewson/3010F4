package com.example.tiltlogger.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/*
*****Class MainActivity*****
Author: Paul Hewson
Version: 2.0

This Class Is the "Data" screen in the application. Gets data values from database and displays them.
*/
public class MainActivity extends AppCompatActivity {
    private ArrayList<ClassListData> dataArrayList;  //List of data items
    private MyAppAdapter myAppAdapter; //Array Adapter
    private ListView listView; // ListView
    private boolean success = false; // boolean

    //set up credentials for database connection
    private static final String DB_URL = "jdbc:mysql://192.168.0.26/sysc3010";
    private static final String USER = "root";
    private static final String PASS = "sysc3010";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //initialize view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        dataArrayList = new ArrayList<ClassListData>();

        // Calling Async Task
        SyncData orderData = new SyncData();
        orderData.execute("");

        //button listener
        Button toInfo=(Button)findViewById(R.id.buttonToInfo);
        toInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //on click, start InfoActivity
                Intent myIntent = new Intent(MainActivity.this, InfoActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
    }

    // Async Task has three overrided methods,
    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            //show loading message while running background
            progress = ProgressDialog.show(MainActivity.this, "Synchronising",
                    "Loading, Please Wait...", true);
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            //try to connect to database
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); //Connection Object
                //if connection fails, set success to false
                if (conn == null) {
                    success = false;
                } else {
                    //query all columns from collected_data table
                    String query = "SELECT * FROM collected_data";
                    Statement stmt = conn.createStatement();
                    ResultSet results = stmt.executeQuery(query);
                    if (results != null) // if results not null, add items to dataArraylist
                    {
                        while (results.next()) {
                            try {
                                //add each row as an ArrayList element
                                dataArrayList.add(new ClassListData(results.getString("date"), results.getString("time"), results.getString("sensor_id"), results.getString("tilt"), results.getString("temperature")));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        //set success variables
                        msg = "Found";
                        success = true;
                    } else {
                        //set failure variables
                        msg = "No Data found!";
                        success = false;
                    }
                }
            } catch (Exception e) {
                //catch exception, set error msg
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();
                success = false;
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) // disimissing progress dialoge, showing error and setting up my ListView
        {
            progress.dismiss();
            Toast.makeText(MainActivity.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false) {
            } else {
                try {
                    myAppAdapter = new MyAppAdapter(dataArrayList, MainActivity.this);
                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    listView.setAdapter(myAppAdapter);
                } catch (Exception ex) {

                }

            }
        }
    }

    public class MyAppAdapter extends BaseAdapter
    {
        public class ViewHolder {
            TextView text_Date;
            TextView text_Time;
            TextView text_ID;
            TextView text_Tilt;
            TextView text_Temp;
        }

        public List<ClassListData> dataList;

        public Context context;
        ArrayList<ClassListData> arraylist;

        private MyAppAdapter(List<ClassListData> apps, Context context) {
            this.dataList = apps;
            this.context = context;
            arraylist = new ArrayList<ClassListData>();
            arraylist.addAll(dataList);
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) // inflating the layout and initializing widgets
        {

            View rowView = convertView;
            ViewHolder viewHolder = null;
            if (rowView == null) {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.list_content, parent, false);
                viewHolder = new ViewHolder();
                //grab all fields to update
                viewHolder.text_Date = (TextView) rowView.findViewById(R.id.textDate);
                viewHolder.text_Time = (TextView) rowView.findViewById(R.id.textTime);
                viewHolder.text_ID = (TextView) rowView.findViewById(R.id.textID);
                viewHolder.text_Tilt = (TextView) rowView.findViewById(R.id.textTilt);
                viewHolder.text_Temp = (TextView) rowView.findViewById(R.id.textTemp);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //update table fields
            viewHolder.text_Date.setText(dataList.get(position).getDate() + "");
            viewHolder.text_Time.setText(dataList.get(position).getTime() + "");
            viewHolder.text_ID.setText(dataList.get(position).getID() + "");
            viewHolder.text_Tilt.setText(dataList.get(position).getTilt() + "");
            viewHolder.text_Temp.setText(dataList.get(position).getTemp() + "");

            return rowView;
        }
    }
}