package com.example.lamiaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SudokuActivity extends AppCompatActivity {
    private SudokuTableHandler handler = new SudokuTableHandler();
    private TimerText timerHandler;
    private Thread t;

    private int numersOfHints = 2; //numero di consigli che può ottenere al massimo il gioccatore
    private ProgressDatabase database = new ProgressDatabase(this);
    private boolean submited = false;
    int[] indexButtonPressedNow = {0,0};

    private Button[][] sudokuMatrix;

    private String[] selection = {"Easy", "Normal", "Hard", "Extreme"};
    private String[] backSelection = {"Quit", "Continue playing"};
    private String[] againSelection = {"Reset table", "Continue playing"};
    private String[] colorSelection = {"White", "Black", "Orange", "Red", "Yellow"};
    private TextView txtTimer;
    private int seconds = 0, minutes = 0, hour = 0;

    private int numbersForDifficulty[]= {40, 35, 29, 23, 19};                   //numeri generati sulla tabella sudoku in base alla difficoltà
    private String difficulties[] = {"Easy", "Normal", "Hard", "Extreme"};      //array delle diverse difficoltà di gioco

    private String pasteNumber = "0"; //equivale al numero che verrà messo nella tabella sudoku
    private String difficulty;

    private Button btnColor;    //bottone per la scelta del colore del testo
    private Button btnConfirm;
    private Button btnNew;
    private Button btnAgain;
    private Button btnHint;

    private static final int idPasteNumbers []= {R.id.number0, R.id.number1, R.id.number2, R.id.number3, R.id.number4, R.id.number5, R.id.number6, R.id.number7, R.id.number8, R.id.number9};
    private int selectedIdColor = 0;
    private int indexPasteButton = 0; //indice del bottone selezionato

    int colors []= {R.color.white, R.color.Black, R.color.Orange, R.color.Red, R.color.Yellow};
    private Button pasteButtons[];

    private ProgressDatabase tablesDB; //database per il prelievo di tabelle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();getSupportActionBar().hide();
        Intent intent = getIntent();
        difficulty = intent.getStringExtra("mode");         //prelevo da intent la modalità di gioco
        txtTimer = (TextView) findViewById(R.id.lblTimer);
        GenerateButtons();                                      //genero i bottoni ausigliari

        handler = new SudokuTableHandler(GetNumbersToGenerate(), SudokuActivity.this);    //passo il numero di valori da creare
        sudokuMatrix = handler.GetButtonsMatrix();                                              //prelevo la tabella di bottoni
        SetOnClickListener();//setto il Listener
        StartTimer();
    }

    //setto gli OnClickListener
    public void SetOnClickListener() {
        for(int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(sudokuMatrix[i][j].getText().equals("")) {
                    int auxi = i, auxj = j;
                    sudokuMatrix[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (pasteNumber.equals("0"))
                                sudokuMatrix[auxi][auxj].setText("");
                            else {
                                sudokuMatrix[auxi][auxj].setTextColor(getResources().getColor(colors[selectedIdColor])); //prendo il colore selezionato sul bottone del colore
                                sudokuMatrix[auxi][auxj].setText(pasteNumber);
                            }
                            sudokuMatrix[indexButtonPressedNow[0]][indexButtonPressedNow[1]].setBackgroundColor(getResources().getColor((R.color.purple_500)));
                            sudokuMatrix[auxi][auxi].setBackgroundColor((getResources().getColor(R.color.purple_200)));
                            indexButtonPressedNow[0] = auxi;
                            indexButtonPressedNow[1] = auxj;
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Log.d("Buttons:", "Premuto indietro");
        AlertDialog.Builder alert = new AlertDialog.Builder(SudokuActivity.this);
        alert.setTitle("Are you sure?").setItems(backSelection, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if(item==0) {
                    Intent intent = new Intent(SudokuActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        alert.show();
    }

    private Button[] CreatePasteButtons(){ //creo i bottoni destinati a fare copia e incolla
        Button myButtons[] = new Button[10];
        for(int i = 0; i < 10; i++) {
            myButtons[i] = (Button)findViewById(idPasteNumbers[i]);
            int auxi = i;
            myButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //al click salvo il valore numerico del bottone che servirà per pastarlo nel sudoku table
                    //myButtons[indexPasteButton].setBackgroundColor(getResources().getColor(R.color.purple_500));
                    //myButtons[auxi].setBackgroundColor(getResources().getColor(R.color.Red));
                    myButtons[auxi].setTextColor(getResources().getColor(colors[selectedIdColor]));
                    myButtons[Integer.parseInt(pasteNumber)].setTextColor(Color.WHITE);
                    pasteNumber = auxi +"";
                    indexPasteButton = Integer.parseInt(pasteNumber);
                }
            });
        }
        return myButtons;
    }

    //necessario per capire in base alla difficoltà i numeri da generare
    private int GetNumbersToGenerate() {
        for(int i = 0; i < difficulties.length; i++) {
            if(difficulties[i].equals(difficulty))
                return numbersForDifficulty[i];
        }
        return 0;
    }

    private void GenerateButtons() {
        pasteButtons = CreatePasteButtons();

        btnConfirm = (Button)findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] confirmSelection = {"Submit answer", "Keep playing"};
                AlertDialog.Builder alert1 = new AlertDialog.Builder(SudokuActivity.this);
                alert1.setTitle("Do you want to submit the table?").setItems(confirmSelection, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        String[] confirmSelection = {"New Game", "Quit"};
                        AlertDialog.Builder alert2 = new AlertDialog.Builder(SudokuActivity.this);
                        if(item == 0 && !submited){
                            submited = true;
                            timerHandler.StopTimer();
                            String title = "";
                            if(!handler.ControlTable(sudokuMatrix)) {
                                sudokuMatrix = handler.GenerateButtonsMatrix(handler.GetSolvedSudoku());
                                database.insertSudokuTable(database.numberOfRows()+1, MatrixToString(handler.GetSudokuTable()), MatrixToString(handler.GetSolvedSudoku()), difficulty, "Lost", (String) txtTimer.getText());
                                txtTimer.setText("You LOST!!!");
                                txtTimer.setTextColor(Color.RED);
                            }
                            else { //se il controllo della tabella compilata dall'utente da esito positivo allora mi congratulo
                                timerHandler.StopTimer();
                                database.insertSudokuTable(database.numberOfRows()+1, MatrixToString(handler.GetSudokuTable()), MatrixToString(handler.GetSolvedSudoku()), difficulty, "Won", (String) txtTimer.getText());
                                txtTimer.setText("You WON!!!");
                                txtTimer.setTextColor(Color.GREEN);
                            }
                            alert2.setTitle(title).setItems(confirmSelection, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    if (item == 0)
                                        btnAgain.callOnClick();
                                }
                            });
                        }
                        else
                            Toast.makeText(SudokuActivity.this,"Sorry you can't submit the table again",Toast.LENGTH_LONG).show();
                    }
                });
                alert1.show();
            }
        });

        btnNew = (Button)findViewById(R.id.btnNew); //creo una nuova partita
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(SudokuActivity.this);
                alert.setTitle("Sudoku difficulty").setItems(selection, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        submited = false;
                        handler.CleanButtons();
                        difficulty = selection[item];
                        handler = new SudokuTableHandler(GetNumbersToGenerate(), SudokuActivity.this);
                        sudokuMatrix = handler.GetButtonsMatrix();
                        SetOnClickListener();
                        StartTimer();
                        txtTimer.setTextColor(Color.WHITE);
                        numersOfHints=2;
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for CANCEL button here, or leave in blank
                    }
                });
                alert.show();

            }
        });

        btnAgain = (Button)findViewById(R.id.btnAgain);
        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(SudokuActivity.this);
                alert.setTitle("Are you sure?").setItems(againSelection, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if(item == 0 && !submited) { //se viene scelto dall'utente di ricreare la tabella inziale allora lo ricrea
                            handler.CleanButtons();
                            sudokuMatrix = handler.GenerateButtonsMatrix(handler.GetSudokuTable());
                            numersOfHints = 2;
                        }
                        else{
                            Toast.makeText(SudokuActivity.this,"Sorry you can't play again this table ",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                alert.show();
            }
        });

        btnColor = (Button) findViewById(R.id.btnColor);
        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(SudokuActivity.this);
                alert.setTitle("Do you want to change the text color?").setItems(colorSelection, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        selectedIdColor = item;
                        pasteButtons[indexPasteButton].setTextColor(getResources().getColor(colors[selectedIdColor]));
                    }
                });
                alert.show();
            }
        });

        btnHint = (Button) findViewById(R.id.btnHint);
        btnHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numersOfHints > 0 && !submited){
                    numersOfHints --;
                    int values[] = handler.GetHint(sudokuMatrix);
                    sudokuMatrix[values[0]][values[1]].setText(values[2]+"");
                    sudokuMatrix[values[0]][values[1]].setTextColor(Color.GREEN);
                    sudokuMatrix[values[0]][values[1]].setOnClickListener(null);        //rimuovo il listener al click
                    Toast.makeText(SudokuActivity.this,"You still have " + numersOfHints + " hints",Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(SudokuActivity.this,"No more hints remaining",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void StartTimer() {
        if(timerHandler != null)
            timerHandler.StopTimer();
        timerHandler = new TimerText(SudokuActivity.this, txtTimer);
        t = new Thread(timerHandler);
        t.start();
    }

    private String MatrixToString(int[][] matrix){
        String table = "";
        for(int i=0; i < 9; i++){
            for(int j=0; j<9; j++){
                table += matrix[i][j];
            }
        }
        return table;
    }
}