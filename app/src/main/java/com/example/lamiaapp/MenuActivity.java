package com.example.lamiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    private String[] selection = {"Easy", "Normal", "Hard", "Extreme"};
    private int valueSelected;
    ProgressDatabase myDatabase;

    Intent intent = null;
    private Button btnRight, btnLeft;
    private Button btnPlay;
    private Button btnInfo;
    private TextView lblTitolo;
    private Button btnProgress;
    int gameSelected = 0; //identifica il gioco selezionato dall'utente

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();

        btnPlay = (Button) findViewById(R.id.btn_play);
        btnRight = (Button) findViewById(R.id.btn_right);
        btnLeft = (Button)findViewById(R.id.btn_left);

        btnInfo = (Button) findViewById(R.id.btn_info);

        btnProgress = (Button) findViewById(R.id.btn_progress);
        lblTitolo = (TextView) findViewById(R.id.lblTitle);

        btnLeft.setVisibility(View.INVISIBLE);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch (gameSelected){ //starto activity in base al gioco alla quale voglio giocare
                    case 0:
                        AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);
                        alert.setTitle("Sudoku difficulty").setItems(selection, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                Log.i("Menu selezione", "Opzionescelta: " + selection[item]);

                                intent = new Intent(MenuActivity.this, SudokuActivity.class);
                                intent.putExtra("mode", selection[item]);
                                startActivity(intent);
                            }
                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Put actions for CANCEL button here, or leave in blank
                            }
                        });
                        alert.show();
                        break;
                    case 1:
                        intent = new Intent(MenuActivity.this, PuzzleActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        //intent = new Intent(MenuActivity.this, SnakeActivity.class);
                        break;
                }
            }
        });

        btnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDatabase = new ProgressDatabase(MenuActivity.this);
                if(myDatabase.numberOfRows() > 0) { //controllo se ci sono righe da leggere
                    Intent intent = new Intent(MenuActivity.this, ProgressActivity.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(MenuActivity.this, "No data to read", Toast.LENGTH_LONG).show();
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameSelected == 0)
                {
                    lblTitolo.setText(getString(R.string.Puzzle));
                    btnLeft.setVisibility(View.VISIBLE);
                    btnRight.setVisibility(View.INVISIBLE);
                    lblTitolo.setTextSize(11);
                    gameSelected = 1;
                }
            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameSelected == 1)
                {
                    lblTitolo.setText(getString(R.string.Sudoku));
                    btnRight.setVisibility(View.VISIBLE);
                    btnLeft.setVisibility(View.INVISIBLE);
                    lblTitolo.setTextSize(13);
                    gameSelected = 0;
                }
            }
        });


        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MenuActivity.this);
                alertDialogBuilder.setMessage("Sudoku application for school projetc. \nMade by Cozzarin Laxman");
                alertDialogBuilder.setPositiveButton("Go back to the menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(MenuActivity.this,"Select the game",Toast.LENGTH_LONG).show();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }
}