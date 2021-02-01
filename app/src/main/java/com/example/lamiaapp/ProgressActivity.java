package com.example.lamiaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProgressActivity extends AppCompatActivity {

    private int id;
    private boolean solution;
    private Button btnBack, btnForward, btnSolution;
    private TextView lblTimer, lblMode, lblVictory;
    SudokuTableHandler sudokuTableHandler = new SudokuTableHandler(ProgressActivity.this);
    ProgressDatabase myDatabase = new ProgressDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        getSupportActionBar().hide();
        btnBack = (Button) findViewById(R.id.btn_BackTable);
        btnForward = (Button) findViewById(R.id.btn_ForwardTable);

        btnSolution = (Button) findViewById(R.id.btnShowSolution);

        lblMode = (TextView) findViewById(R.id.lblMode);
        lblTimer = (TextView) findViewById(R.id.lblTimer);
        lblVictory = (TextView) findViewById(R.id.lblVictory);

        id = myDatabase.numberOfRows();
        solution = false;
        SetNotSolvedTable(id);
        SetLables(id);

        //controllo se ci sono più tabelle da mostrare
        if(myDatabase.numberOfRows() > 1){
            btnBack.setVisibility(View.INVISIBLE);
        }
        else{
            btnBack.setVisibility(View.INVISIBLE);
            btnForward.setVisibility((View.INVISIBLE));
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solution = false;
                btnSolution.setText("Show solution");
                if (id >= 0) {
                    id++;
                    SetNotSolvedTable(id);
                    SetLables(id);
                    btnForward.setVisibility(View.VISIBLE);
                    if (id == myDatabase.numberOfRows())
                        btnBack.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solution = false;
                btnSolution.setText("Show solution");
                if (id <= myDatabase.numberOfRows()) {
                    id--;
                    SetNotSolvedTable(id);
                    SetLables(id);
                    btnBack.setVisibility(View.VISIBLE);
                    if (id == 1)
                        btnForward.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!solution) {
                    SetSolvedTable(id);
                    solution = true;
                    btnSolution.setText("Show played table");
                } else {
                    SetNotSolvedTable(id);
                    solution = false;
                    btnSolution.setText("Show solution");
                }
            }
        });
    }

    private void SetLables(int id) {
        lblTimer.setText("Execution time: ");
        lblMode.setText("Difficulty: ");
        lblVictory.setText("Result: ");
        lblTimer.setText(lblTimer.getText().toString() + myDatabase.GetTimer(id));
        lblMode.setText(lblMode.getText().toString() + myDatabase.GetMode(id));
        lblVictory.setText(lblVictory.getText().toString() + myDatabase.GetVictory(id));
    }

    public void ChangeFragment(View view) {
        Fragment sudokuTableFragment = new SudokuTableFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();   //faccio vedere fragment
        fragmentTransaction.commit();
    }

    private void SetSolvedTable(int id) {
        CleanMatrix();
        String tableString = myDatabase.getNumbersGame(id);
        int[][] matrix = StringToMatrix(tableString);
        sudokuTableHandler.GenerateButtonsMatrix(matrix);
    }

    private void SetNotSolvedTable(int id) {
        CleanMatrix();
        String tableString = myDatabase.getNumbers(id);
        int[][] matrix = StringToMatrix(tableString);
        sudokuTableHandler.GenerateButtonsMatrix(matrix);
    }

    //converto la mia striga in matrice
    private int[][] StringToMatrix(String input) {
        int[][] matrix = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                matrix[i][j] = Integer.parseInt(input.charAt((j * 9) + i) + "");
            }
        }
        return matrix;
    }

    //pulisco la tabella
    private void CleanMatrix() {
        int[][] matrix = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                matrix[i][j] = 0;
            }
        }
        sudokuTableHandler.GenerateButtonsMatrix(matrix);
    }
}