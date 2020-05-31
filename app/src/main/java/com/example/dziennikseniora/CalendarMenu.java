package com.example.dziennikseniora;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CalendarView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalendarMenu extends AppCompatActivity {
    private ExpandableListView listview1;
    ArrayList<String> allheadings;
    private HashMap<String, ArrayList<String>> allGroups = new HashMap<String, ArrayList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calen);
        CalendarView CV = findViewById(R.id.calendarView);
        listview1 = (ExpandableListView) findViewById(R.id.expandableListView);

        // Toast item title when ListView item is clicked
        listview1.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){
            @Override
            public boolean onChildClick(ExpandableListView listview, View child, int groupposition, int childposition, long position){
                Toast.makeText(getApplicationContext(), allGroups.get(allheadings.get(groupposition)).get(childposition), Toast.LENGTH_LONG).show();
                return true;
            }
        });
        CV.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // make 2020-05-17 12:31:57
                JSONObject logindata = new JSONObject();
                try {
                    logindata.put("login", FirstFragment.user_login);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    logindata.put("password", FirstFragment.user_pass);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String choosen_date;
                if (month == 10 || month == 11) {
                    choosen_date = year + "-" + (month + 1) + "-" + dayOfMonth;
                    Log.e("TAG", choosen_date);
                } else {
                    choosen_date = year + "-0" + (month + 1) + "-" + dayOfMonth;
                    Log.e("TAG", choosen_date);
                }
                try {
                    logindata.put("choosen_date", choosen_date);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String server_url = "http://192.168.0.66:8080/telematyka-serwer/raport";
                new RaportOperation().execute(server_url, logindata.toString());
            }
        });
    }

    private class RaportOperation extends AsyncTask<String, Void, String> {

        private String jsonResponse;
        private ProgressDialog dialog = new ProgressDialog(CalendarMenu.this);

        protected void onPreExecute() {
            dialog.setMessage("Please wait..");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes("PostData=" + params[1]);
                wr.flush();
                wr.close();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
                receivedata(httpURLConnection);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return data;
        }

        private void receivedata(HttpURLConnection connection) throws Exception {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String returnString = "";
            StringBuilder allData = new StringBuilder("");

            while ((returnString = in.readLine()) != null) {
                allData.append(returnString);
            }
            in.close();

            Log.e("TAG", allData.toString());
        }

        protected void onPostExecute(String result) {
            dialog.dismiss();
            allGroups.clear();
            result = result.replace("=", ":");
            JSONArray jArray = null;
            try {
                jArray = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                JSONObject jObject = null;
                String cisnienie_s = null;
                String cisnienie_r = null;
                String tetno = null;
                String waga = null;
                String cukier = null;
                String data = null;
                for (int i = 0; i < jArray.length(); i++) {
                    ArrayList<String> rap = new ArrayList<String>();
                    jObject = jArray.getJSONObject(i);
                    tetno = (String) jObject.get("tetno");
                    rap.add("Tętno " + tetno);
                    cisnienie_s = (String) jObject.get("cisnienie_s");
                    rap.add("Ciśnienie skurczowe " + cisnienie_s);
                    cisnienie_r = (String) jObject.get("cisnienie_r");
                    rap.add("Ciśnienie rozkurczowe " + cisnienie_r);
                    cukier = (String) jObject.get("cukier");
                    rap.add("Poziom cukru " + cukier);
                    data = (String) jObject.get("data");
                    allGroups.put("Raport " + (i+1) + " " + data, rap);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            allheadings = new ArrayList<String>(allGroups.keySet());
            CustomListAdapter adapter = new CustomListAdapter(getApplicationContext(), allGroups, allheadings);
            listview1.setAdapter(adapter);
            ((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();

        }
    }
    private class CustomListAdapter extends BaseExpandableListAdapter{

        private Context context;
        HashMap<String, ArrayList<String>> groups;
        ArrayList<String> headings;

        // Public constructor
        public CustomListAdapter(Context context, HashMap<String, ArrayList<String>> groups, ArrayList<String> headings) {
            this.context = context;
            this.groups = groups;
            this.headings = headings;
        }

        @Override
        public int getGroupCount() {
            // Return total number of groups
            return headings.size();
        }

        @Override
        public int getChildrenCount(int position) {
            // Return total items in each group
            return groups.get(headings.get(position)).size();
        }

        @Override
        public Object getGroup(int position) {
            // Return group heading
            return headings.get(position);
        }

        @Override
        public Object getChild(int groupposition, int childposition) {
            // Return child from specified group at the specified position
            return groups.get(headings.get(groupposition)).get(childposition);
        }

        @Override
        public long getGroupId(int position) {
            return position;
        }

        @Override
        public long getChildId(int groupposition, int childposition) {
            return childposition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent){
            View v = convertView;
            // Inflate the layout for each list row
            LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (v == null) {
                v = _inflater.inflate(R.layout.header, null);
            }
            // Set Width of ListView to MATCH_PARENT
            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            // Get the TextView from CustomView for displaying group headings
            TextView txtview = (TextView) v.findViewById(R.id.listitemTextView1);
            // Set the text and image for current item using data from map list
            txtview.setText(headings.get(groupPosition));
            // Return the view for the current row
            return v;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent){
            View v = convertView;
            // Inflate the layout for each list row
            LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (v == null) {
                v = _inflater.inflate(R.layout.child, null);
            }
            // Set Width of ListView to MATCH_PARENT
            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            // Get the TextView from CustomView for displaying children
            TextView txtview = (TextView) v.findViewById(R.id.childitemTextView1);
            // Set the text for current item using data from map list
            txtview.setText((groups.get(headings.get(groupPosition))).get(childPosition));
            // Return the view for the current row
            return v;
        }

        @Override
        public boolean isChildSelectable(int groupposition, int childposition)
        {
            return true;
        }

    }
}


