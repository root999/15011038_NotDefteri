package com.example.semesterprojectv_0_7;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class addNoteFragment extends Fragment {
    private EditText baslikB;
    private EditText textB;
    private Button kaydetBT;
    private Button remindBT;
    private Data data;
    private Data newData;
    public addNoteFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Context context = getContext();
        View view = inflater.inflate(R.layout.fragment_add_note, container, false);

        baslikB = view.findViewById(R.id.baslikBox);
        textB = view.findViewById(R.id.TextBox);
        kaydetBT = view.findViewById(R.id.kaydetBT);
        remindBT = view.findViewById(R.id.hatirlaticiBT);
        Intent i = getActivity().getIntent();
        data = (Data) i.getSerializableExtra("deneme");
        final File file = new File(getContext().getFilesDir(),data.getBaslik());
        if(file.exists()){
            baslikB.setText( data.getBaslik() );
            textB.setText( data.getText() );
        }
        remindBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = new Data();
                data.setBaslik(baslikB.getText().toString());
                data.setText(textB.getText().toString());
                Intent intent = new Intent(getActivity(),reminderActivity.class);
                intent.putExtra("deneme",data);
                startActivity(intent);
            }
        });
        kaydetBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = new Data();
                Data tempData;
                data.setBaslik(baslikB.getText().toString());
                data.setText(textB.getText().toString());
                File file = new File(context.getFilesDir(), baslikB.getText().toString());
                writeFile();
                loadFile();
//                try {
//                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
//                    oos.writeObject(data);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                try{
//                    File newFile = new File(context.getFilesDir(),baslikB.getText().toString());
//                    ObjectInputStream ois = new ObjectInputStream(new ObjectInputStream(new FileInputStream(file)));
//                    tempData= (Data) ois.readObject();
//                    Log.d("data",tempData.getBaslik()+" " +tempData.getText()),
//                }
//                catch (FileNotFoundException e){
//                    e.printStackTrace();
//                }
//                catch (IOException e){
//                    e.printStackTrace();
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
            }
        });

        return view;
    }
    public void writeFile()
    {
        Context context = getContext();

        try
        {
            FileOutputStream fileOutput = context.openFileOutput(baslikB.getText().toString(), Context.MODE_PRIVATE);
            ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
            objectOutput.writeObject(data);
            objectOutput.close();
            fileOutput.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void loadFile()
    {
        try
        {
            FileInputStream fileInput = getContext().openFileInput("profileSaves.bin");
            ObjectInputStream objectInput = new ObjectInputStream(fileInput );
            newData = (Data) objectInput.readObject();
            Log.i("data1 ",newData.getBaslik()+" "+ newData.getText());
            objectInput.close();
            fileInput.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void getNote(String baslik, String text){
        baslikB.setText( baslik );
        textB.setText( text );

    }
}
