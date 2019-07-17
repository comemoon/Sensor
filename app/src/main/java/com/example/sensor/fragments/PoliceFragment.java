package com.example.sensor.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sensor.R;
import com.example.sensor.star_Activity;

import java.util.Random;

public class PoliceFragment extends Fragment {
    private TextView textView1,textView2,textView3;
    private double j,z;
    private Button button;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_police, container, false);
        TextView textView1=view.findViewById(R.id.textview1);
        TextView textView2=view.findViewById(R.id.textview2);
        Button button=view.findViewById(R.id.button);
//        for(int i=0;i<10;i++){
//            j=Math.random()*100;
//            z=Math.random()*100;
//            textView1.setText(j+"");
//            textView2.setText(z+"");
//            if(j>50 || z>50){
//                Toast.makeText(getContext(),"即将自动报警",Toast.LENGTH_SHORT).show();
//                Intent intent= new Intent(getActivity(), star_Activity.class);
//                startActivity(intent);
//            }
//        }
        Toast.makeText(getContext(),"即将自动报警",Toast.LENGTH_SHORT).show();
//                Intent intent= new Intent(getActivity(), star_Activity.class);
//                startActivity(intent);
        return  view;
    }



}
