package com.example.dziennikseniora;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.WIFI_SERVICE;
import static android.text.format.Formatter.formatIpAddress;
import static android.widget.Toast.LENGTH_SHORT;

public class FirstFragment extends Fragment {
    public static String user_login;
    public static String user_pass;
    private static Object SavedState;
    public boolean accepted = false;
    private Activity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        setRetainInstance(true);
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText loginedit = view.findViewById(R.id.editText5);
        final EditText passedit = view.findViewById(R.id.editText6);

        Button loginbutt = view.findViewById(R.id.button);
        Button registerbutt = view.findViewById(R.id.sendRegisterDatabutt);



        loginbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject logindata = new JSONObject();
                user_login = loginedit.getText().toString();
                user_pass = passedit.getText().toString();
                if (user_login != "" && user_pass != "") {
                    try {
                        SendJSONtoServer JS = new SendJSONtoServer();
                        logindata.put("login", user_login);
                        logindata.put("password", user_pass);
                        logindata.put("type", "log");
                        Log.e("TAG", logindata.toString());
                        logindata.put("adres", getIPaddr());
                        JS.execute("http://192.168.0.66:8080/telematyka-serwer/logowanie", logindata.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (SendJSONtoServer.logowany == true) {
                    Intent intent = new Intent(getActivity(), MainMenu.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(activity.getApplicationContext(),"Błędne hasło lub brak!", LENGTH_SHORT).show();
                }
            }
        });

        registerbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(getActivity(), Register.class);
                startActivity(intent2);
            }
        });
    }
    public String getIPaddr() {
        WifiManager wifiMgr = (WifiManager) getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ip);
        return ipAddress;
    }

}

