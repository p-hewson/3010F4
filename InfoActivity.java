package com.example.tiltlogger.myapplication;

import android.app.Activity;
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

public class InfoActivity extends AppCompatActivity {
    private ArrayList<ClassListInfo> infoArrayList;  //List items Array
    private MyAppAdapter myAppAdapter; //Array Adapter
    private ListView listView; // ListView
    private boolean success = false; // boolean

    //private static final String DB_URL = "jdbc:mysql://DATABASE_IP/DATABASE_NAME";
    private static final String DB_URL = "jdbc:mysql://192.168.0.31/sysc3010"; //"jdbc:mysql://DATABASE_IP/DATABASE_NAME";
    private static final String USER = "root";
    private static final String PASS = "sysc3010";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        listView = (ListView) findViewById(R.id.infoView); //ListView Declaration
        infoArrayList = new ArrayList<ClassListInfo>(); // Arraylist Initialization
        //infoArrayList.add(new ClassListData("Date","Time","id","tilt","temperature"));

        // Calling Async Task
        SyncData orderData = new SyncData();
        orderData.execute("");

        Button toInfo=(Button)findViewById(R.id.buttonToData);
        toInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(InfoActivity.this, MainActivity.class);
                InfoActivity.this.startActivity(myIntent);
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
            progress = ProgressDialog.show(InfoActivity.this, "Synchronising",
                    "Loading, Please Wait...", true);
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); //Connection Object
                if (conn == null) {
                    success = false;
                } else {
                    // Change below query according to your own database.
                    String query = "SELECT * FROM info";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null) // if resultset not null, I add items to infoArraylist using class created
                    {
                        while (rs.next()) {
                            try {
                                infoArrayList.add(new ClassListInfo(rs.getString("sensor_id"),rs.getString("name"),rs.getString("location"),rs.getString("time_interval")));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        msg = "Found";
                        success = true;
                    } else {
                        msg = "No Data found!";
                        success = false;
                    }
                }
            } catch (Exception e) {
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
            Toast.makeText(InfoActivity.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false) {
            } else {
                try {
                    myAppAdapter = new MyAppAdapter(infoArrayList, InfoActivity.this);
                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    listView.setAdapter(myAppAdapter);
                } catch (Exception ex) {

                }

            }
        }
    }

    public class MyAppAdapter extends BaseAdapter         //has a class viewholder which holds
    {
        public class ViewHolder {
            TextView text_Info_ID;
            TextView text_Name;
            TextView text_Location;
            TextView text_Interval;
        }

        public List<ClassListInfo> infoList;

        public Context context;
        ArrayList<ClassListInfo> arraylist;

        private MyAppAdapter(List<ClassListInfo> apps, Context context) {
            this.infoList = apps;
            this.context = context;
            arraylist = new ArrayList<ClassListInfo>();
            arraylist.addAll(infoList);
        }

        @Override
        public int getCount() {
            return infoList.size();
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
                rowView = inflater.inflate(R.layout.info_content, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.text_Info_ID = (TextView) rowView.findViewById(R.id.textInfoID);
                viewHolder.text_Name = (TextView) rowView.findViewById(R.id.textName);
                viewHolder.text_Location = (TextView) rowView.findViewById(R.id.textLocation);
                viewHolder.text_Interval = (TextView) rowView.findViewById(R.id.textInterval);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.text_Info_ID.setText(infoList.get(position).getInfoID() + "");
            viewHolder.text_Name.setText(infoList.get(position).getSensorName() + "");
            viewHolder.text_Location.setText(infoList.get(position).getLocation() + "");
            viewHolder.text_Interval.setText(infoList.get(position).getInterval() + "");

            return rowView;
        }
    }
}