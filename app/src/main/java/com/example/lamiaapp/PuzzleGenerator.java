package com.example.lamiaapp;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;

public class PuzzleGenerator {
    private String table[][] = new String[4][4];
    private final int SIZE = 4;

    public PuzzleGenerator() { }

    public void CreateTable() {                                                 //creo la matrice per la tabella
        ArrayList<Integer> numbers = new ArrayList<>();                         //creo la lista di 16 numeri e poi la mescolo per avere tutti i numeri diversi
        for(int i = 0; i < SIZE*SIZE; i++)
            numbers.add(i);
        Collections.shuffle(numbers);
        String debug = "";
        for(int i = 0; i < SIZE;i++)                                            //Aggiungo alla mia tabella i numeri
            for(int j = 0;j < SIZE;j++) {
                table[i][j] = numbers.get((j * SIZE) + i).toString();
                debug += numbers.get((j * SIZE) + i).toString() +"|";
            }
        Log.d("Puzzle num: ", debug);
    }
    public String[][] ToString() {
        return table;
    }
}

