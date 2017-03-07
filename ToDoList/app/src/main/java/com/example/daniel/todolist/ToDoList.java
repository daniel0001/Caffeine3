package com.example.daniel.todolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class ToDoList extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayList<String> list;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        list = new ArrayList<>();
        populateList(list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        ListView myToDoList = (ListView)findViewById(R.id.myToDoList);
        myToDoList.setAdapter(adapter);
        myToDoList.setOnItemClickListener(this);
    }

    private ArrayList<String> populateList(ArrayList<String> list) {
        Scanner scan = new Scanner(getResources().openRawResource(R.raw.mytodolist));
        while(scan.hasNextLine()){
            String line = scan.nextLine();
            list.add(line);
        }
        scan.close();
        return list;

    }

    public void addToList(View view) {
        EditText entry = (EditText)findViewById(R.id.newListEntry);
        String newListItem = entry.getText().toString();
        list.add(newListItem);
        adapter.notifyDataSetChanged();
        writeToFile(list);
        entry.setText("");
    }

    private void writeToFile(ArrayList<String> list) {
        PrintStream out = null;
        try {
            out = new PrintStream(openFileOutput("mytodolist.txt", MODE_PRIVATE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < list.size(); i ++){
            String line = list.get(i);
            out.println(line);
        }
        out.close();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
        list.remove(index);
        adapter.notifyDataSetChanged();
        writeToFile(list);
    }
}
