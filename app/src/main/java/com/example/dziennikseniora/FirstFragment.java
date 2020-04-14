package com.example.dziennikseniora;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView logintext = view.findViewById(R.id.textView);
        logintext.setText("Wpisz swój login");
        TextView passtext = view.findViewById(R.id.textView2);
        passtext.setText("Wpisz swoje hasło");

        final EditText loginedit = view.findViewById(R.id.editText3);
        final EditText passedit = view.findViewById(R.id.editText4);

        Button loginbutt = view.findViewById(R.id.button);


        loginbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject logindata = new JSONObject();
                String user_login = loginedit.getText().toString();
                String user_pass = passedit.getText().toString();
                if (user_login != "" && user_pass != "") {
                    try {
                        logindata.put("login", user_login);
                        logindata.put("password", user_pass);
                        Log.e("TAG", logindata.toString());
                        new SendJSONtoServer().execute("http://192.168.0.66:8080/telematyka-serwer/servletdata", logindata.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                }
        });
    }
}
