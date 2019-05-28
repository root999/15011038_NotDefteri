package com.example.semesterprojectv_0_7;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class mainFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Data> datalar = new ArrayList<Data>();
    private addNoteFragment addnotefragment = new addNoteFragment();
   // private mainFragment mainFragment = new mainFragment();
    public mainFragment(){

    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Data tmpData;
        View view = inflater.inflate(R.layout.main_fragment,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recylerViewID);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        String fileList[] = getContext().fileList();
        Log.i("data1","file :"+fileList.length);
        for(int i=0;i<fileList.length;i++){

            tmpData=loadFile( fileList[i]);
            Log.i("data1 ","++++"+ tmpData.getBaslik()+" "+ tmpData.getText());
            datalar.add(tmpData);
        }
        mAdapter = new myAdapter( getContext(), datalar, new myAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Data item) {
                addnotefragment.getNote( item.getBaslik(),item.getText() );
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.recylerViewID, addnotefragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        } );
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }
    public Data loadFile(String filename)
    {
        Data newData=new Data();
        try
        {
            FileInputStream fileInput = getContext().openFileInput(filename);
            ObjectInputStream objectInput = new ObjectInputStream(fileInput );
            newData = (Data) objectInput.readObject();

            objectInput.close();
            fileInput.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        Log.i("data1 ","ess "+newData.getBaslik()+" "+ newData.getText());
        return newData;
    }
}
