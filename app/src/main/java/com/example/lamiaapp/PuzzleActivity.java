package com.example.lamiaapp;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PuzzleActivity extends AppCompatActivity {
    private static final int[][] idArray = { //matrice di R.id che mi serve per la dichiarazione dei bottoni
            {R.id.bp01, R.id.bp02, R.id.bp03, R.id.bp04},
            {R.id.bp05, R.id.bp06, R.id.bp07, R.id.bp08},
            {R.id.bp09, R.id.bp10, R.id.bp11, R.id.bp12},
            {R.id.bp13, R.id.bp14, R.id.bp15, R.id.bp16},
    };

    private static final int[][] Correct = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 0},
    };

    private PuzzleGenerator puzzleGenerator = new PuzzleGenerator();
    private Button table[][] = new Button[4][4];
    private String[][]numbersOfPuzzle;

    private Button btnAgain, btnNew;
    private String[] selectionAgain = {"Restore puzzle", "Continue playing"};
    private String[] selectionNew = {"New puzzle", "Continue playing"};
    private String[] backSelection = {"Quit", "Continue playing"};
    //per capire dove si trova la casella vuota
    private int vuotoI;
    private int vuotoJ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_puzzelle);
        table = ButtonsOfTable();
        puzzleGenerator.CreateTable();
        numbersOfPuzzle = puzzleGenerator.ToString();
        CreateTable(numbersOfPuzzle);
        SetOnClick();

        btnAgain = (Button)findViewById(R.id.btnAgainPuzzle);
        btnNew = (Button)findViewById(R.id.btnNewPuzzle);


        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(PuzzleActivity.this);
                alert.setTitle("Do you want to start a new game?").setItems(selectionNew, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if(item == 0){
                            puzzleGenerator.CreateTable();
                            numbersOfPuzzle = puzzleGenerator.ToString();
                            CreateTable(numbersOfPuzzle);
                        }
                    }
                });
                alert.show();
            }
        });
        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(PuzzleActivity.this);
                alert.setTitle("Do you want to start a new game?").setItems(selectionAgain, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if(item == 0)
                            CreateTable(numbersOfPuzzle);
                    }
                });
                alert.show();
            }
        });
    }

    private Button[][] ButtonsOfTable()
    {
        Button myButtons[][] = new Button[4][4];
        for(int i=0; i < 4; i++) {
            for(int j=0; j<4; j++) {
                Button aux = (Button)findViewById(idArray[i][j]);
                myButtons[i][j]=aux;
            }
        }
        return myButtons;
    }

    private void SetOnClick()
    {
        for(int i=0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int auxi= i, auxj= j;
                table[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (auxi)           //in base all'indice della riga
                        {
                            //RIGA 0
                            case 0:
                                switch (auxj){  //in base all'indice della colonna
                                    //COLONNA 0
                                    case 0:
                                        Swap(0, 1, auxi, auxj); //Scambio i valori delle celle
                                        Swap(1, 0, auxi, auxj);
                                        break;
                                    case 1:
                                        Swap(0, 0, auxi, auxj);
                                        Swap(0, 2, auxi, auxj);
                                        Swap(1, 1, auxi, auxj);
                                        break;
                                    case 2:
                                        Swap(0, 1, auxi, auxj);
                                        Swap(0, 3, auxi, auxj);
                                        Swap(1, 2, auxi, auxj);
                                        break;
                                    case 3:
                                        Swap(0, 2, auxi, auxj);
                                        Swap(1, 3, auxi, auxj);
                                        break;
                                }
                                break;
                            case 1:
                                switch (auxj){
                                    case 0:
                                        Swap(0, 0, auxi, auxj);
                                        Swap(1, 1, auxi, auxj);
                                        Swap(2, 0, auxi, auxj);
                                        break;
                                    case 1:
                                        Swap(0, 1, auxi, auxj);
                                        Swap(2, 1, auxi, auxj);
                                        Swap(1, 0, auxi, auxj);
                                        Swap(1, 2, auxi, auxj);
                                        break;
                                    case 2:
                                        Swap(0, 2, auxi, auxj);
                                        Swap(2, 2, auxi, auxj);
                                        Swap(1, 1, auxi, auxj);
                                        Swap(1, 3, auxi, auxj);
                                        break;
                                    case 3:
                                        Swap(0, 3, auxi, auxj);
                                        Swap(2, 3, auxi, auxj);
                                        Swap(1, 2, auxi, auxj);
                                        break;
                                }
                                break;
                            case 2:
                                switch (auxj){
                                    case 0:
                                        Swap(1, 0, auxi, auxj);
                                        Swap(2, 1, auxi, auxj);
                                        Swap(3, 0, auxi, auxj);
                                        break;
                                    case 1:
                                        Swap(1, 1, auxi, auxj);
                                        Swap(3, 1, auxi, auxj);
                                        Swap(2, 0, auxi, auxj);
                                        Swap(2, 2, auxi, auxj);
                                        break;
                                    case 2:
                                        Swap(1, 2, auxi, auxj);
                                        Swap(3, 2, auxi, auxj);
                                        Swap(2, 1, auxi, auxj);
                                        Swap(2, 3, auxi, auxj);
                                        break;
                                    case 3:
                                        Swap(1, 3, auxi, auxj);
                                        Swap(3, 3, auxi, auxj);
                                        Swap(2, 2, auxi, auxj);
                                        break;
                                }
                                break;
                            case 3:
                                switch (auxj){
                                    case 0:
                                        Swap(3, 1, auxi, auxj);
                                        Swap(2, 0, auxi, auxj);
                                        break;
                                    case 1:
                                        Swap(3, 0, auxi, auxj);
                                        Swap(3, 2, auxi, auxj);
                                        Swap(2, 1, auxi, auxj);
                                        break;
                                    case 2:
                                        Swap(3, 1, auxi, auxj);
                                        Swap(3, 3, auxi, auxj);
                                        Swap(2, 2, auxi, auxj);
                                        break;
                                    case 3:
                                        Swap(3, 2, auxi, auxj);
                                        Swap(2, 3, auxi, auxj);
                                        break;
                                }
                                break;
                        }
                    }
                });
            }
        }
    }

    private void CreateTable(String numbers[][]) {
        for(int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if(numbers[i][j].equals("0")) {
                    table[i][j].setText(" ");
                    vuotoI = i;
                    vuotoJ = j;
                }
                else
                    table[i][j].setText(numbers[i][j]);
            }
        }
    }

    private void Swap(int i, int j, int auxi, int auxj) {
        if(vuotoI == i && vuotoJ == j) {
            table[i][j].setText(table[auxi][auxj].getText());
            table[auxi][auxj].setText("");
            vuotoI = auxi;
            vuotoJ = auxj;
        }
        if(ControlloTabella())                  //se la tabella è completata in maniera corretta
            CreateToast("Hai Vintoo!!!!");
    }

    private boolean ControlloTabella()
    {
        //scorro tutta la matrice, basta un numero sbagliato per non dare la vittoria
        for(int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if(i == 3 && j == 3) {
                    if(!table[i][j].getText().equals(""))
                        return false;
                }
                else {
                    if (!table[i][j].getText().equals(Correct[i][j] + ""))
                        return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Log.d("Buttons:", "Premuto indietro");
        AlertDialog.Builder alert = new AlertDialog.Builder(PuzzleActivity.this);
        alert.setTitle("Are you sure?").setItems(backSelection, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if(item==0) {
                    Intent intent = new Intent(PuzzleActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        alert.show();
    }

    private void CreateToast(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

}