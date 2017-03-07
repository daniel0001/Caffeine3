package com.example.daniel.berkeleyguessinggame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private int num1;
    private int num2;
    private int points;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pickNumbers();
        points = 0;
    }

    public void pickNumbers(){
        Button lbutton = (Button) findViewById(R.id.LeftButton);
        Button rbutton = (Button) findViewById(R.id.RightButton);
        Random randy = new Random();
        num1 = 0;
        num2 = 0;
        while (num1 == num2){
            num1 = randy.nextInt(10);
            num2 = randy.nextInt(10);
        }
        lbutton.setText(String.valueOf(num1));
        rbutton.setText(String.valueOf(num2));
    }

    public void leftButtonClick(View view) {
        if (num1 > num2){
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
            points ++;
        } else {
            Toast.makeText(this, "Wrong dumbo!", Toast.LENGTH_SHORT).show();
            points --;
        }
        TextView pointsView = (TextView) findViewById(R.id.pointsView);
        pointsView.setText("Points:" + points);

        pickNumbers();
    }

    public void rightButtonClick(View view) {
        if (num2 > num1) {
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
            points++;
        } else {
            Toast.makeText(this, "Wrong dumbo!", Toast.LENGTH_SHORT).show();
            points--;
        }
        TextView pointsView = (TextView) findViewById(R.id.pointsView);
        pointsView.setText("Points:" + points);

        pickNumbers();
    }
}
