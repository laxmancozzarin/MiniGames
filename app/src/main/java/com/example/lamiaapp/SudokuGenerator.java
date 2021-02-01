package com.example.lamiaapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SudokuGenerator {
    int [][]table;
    final int SIZE = 9;

    public SudokuGenerator(){ }
    public SudokuGenerator(int[][] table){
        this.table = table;
    }

    public int[][]GetSolvedSudoku(){
        do { }while(!SolveSudokuTable());       //faccio finchè non ho una tabella sudoku completa e corretta
        return table;
    }

    //----------Generatore di numeri--------------
    public boolean SolveSudokuTable() {
        ArrayList<Integer> possibleNumbers = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));     //i possibili numeri immettibili nella tabella
        Collections.shuffle(possibleNumbers);                                                       //mescolo l'array di numeri immettibili per una maggiore casualità dei numeri immessi
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (table[row][col] == 0) {                                                         //se la posizione non contiene numeri li immetto
                    for (int i = 0; i < SIZE; i++) {                                                //provo tutti numeri
                        if (CanBeInserted(row, col, possibleNumbers.get(i))) {                      //controllo se può essere inseribile, se tru metto altrimenti nulla
                            table[row][col] = possibleNumbers.get(i);                               //immetto alla posizione i,j il valore
                            if (SolveSudokuTable())                                                 //se la cella è una soluzione continuo
                                return true;
                            else                                                                    //altrimenti resetto la casella
                                table[row][col] = 0;
                        }
                    }
                    return false;                                                                   //altrimenti ritorno falso
                }
            }
        }
        return true; // sudoku SolveSudokuTabled
    }

    //---------Metodi per il controllo delle righe, colonne e 3x3----------
    //controllo se aggiungibile alla tabella
    private boolean CanBeInserted(int row, int col, int number) {
        return !CanBeAddInRow(row, number)  &&  !CanBeAddInColl(col, number)  &&  !CanBeAddIn3x3(row, col, number);
    }

    //controllo se già presente nella riga
    private boolean CanBeAddInRow(int row, int number) {
        for (int i = 0; i < SIZE; i++)
            if (table[row][i] == number)
                return true;                //già presente
        return false;                       //non presente
    }

    //Controllo se già presente nella colonna
    private boolean CanBeAddInColl(int col, int number) {
        for (int i = 0; i < SIZE; i++)
            if (table[i][col] == number)
                return true;                //già presente
        return false;                       //non presente
    }

    //Controllo se già presente nella matrice 3x3
    private boolean CanBeAddIn3x3(int row, int col, int number) {
        int r = row - row % 3;
        int c = col - col % 3;
        for (int i = r; i < r + 3; i++)
            for (int j = c; j < c + 3; j++)
                if (table[i][j] == number)
                    return true;            //già presente
        return false;                       //non presente
    }
}
