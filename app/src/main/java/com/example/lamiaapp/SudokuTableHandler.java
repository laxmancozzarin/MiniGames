package com.example.lamiaapp;

import android.app.Activity;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/*
Questa classe ha solo funzione di supporto verso il GameActivity, permette di riempire la matrice di bottoni
 */
public class SudokuTableHandler extends AppCompatActivity {
    public Activity activity;                   //necessari per ricavare gli id
    static final int SIZE = 9;
    //Viene passata alla classe una matrice di bottoni che verrà creata e riempita con gli appositi numeri
    private Button sudokuButtonsTable[][];      //tabella di bottoni
    private final int[][] idBtnArray = {        //matrice di R.id che mi serve per la dichiarazione dei bottoni
            {R.id.b01, R.id.b02, R.id.b03, R.id.b04, R.id.b05, R.id.b06, R.id.b07, R.id.b08, R.id.b09},
            {R.id.b10, R.id.b11, R.id.b12, R.id.b13, R.id.b14, R.id.b15, R.id.b16, R.id.b17, R.id.b18},
            {R.id.b19, R.id.b20, R.id.b21, R.id.b22, R.id.b23, R.id.b24, R.id.b25, R.id.b26, R.id.b27},
            {R.id.b28, R.id.b29, R.id.b30, R.id.b31, R.id.b32, R.id.b33, R.id.b34, R.id.b35, R.id.b36},
            {R.id.b37, R.id.b38, R.id.b39, R.id.b40, R.id.b41, R.id.b42, R.id.b43, R.id.b44, R.id.b45},
            {R.id.b46, R.id.b47, R.id.b48, R.id.b49, R.id.b50, R.id.b51, R.id.b52, R.id.b53, R.id.b54},
            {R.id.b55, R.id.b56, R.id.b57, R.id.b58, R.id.b59, R.id.b60, R.id.b61, R.id.b62, R.id.b63},
            {R.id.b64, R.id.b65, R.id.b66, R.id.b67, R.id.b68, R.id.b69, R.id.b70, R.id.b71, R.id.b72},
            {R.id.b73, R.id.b74, R.id.b75, R.id.b76, R.id.b77, R.id.b78, R.id.b79, R.id.b80, R.id.b81},
    };

    private int numbersToGenerate;
    private ProgressDatabase tablesDB; //database

    //medainte l'istanza della classe Sudoku è possibile generare la tabella di gioco e la relativa soluzione
    private SudokuGenerator sudokuGenerator;
    private int sudokuNumbers [][] = new int[9][9];                         //risultato della generazione della tabella da parte della classe Sudoku: tabella per il gioco
    private int solvedSudoku [][] = new int[9][9];                          //tabella generata e risolta dalla classe Sudoku

    public SudokuTableHandler() { }

    public SudokuTableHandler(Activity activity){
        this.activity = activity;
    }
    //costruttore dove dico quanti numeri generare e da che activity prendere la matrice di bottoni
    public SudokuTableHandler(int numbersToGenerate, Activity activity) {
        this.activity = activity;
        this.numbersToGenerate = numbersToGenerate;
        sudokuGenerator = new SudokuGenerator(sudokuNumbers);
        solvedSudoku = sudokuGenerator.GetSolvedSudoku();                      //prelevo la tabella sudoku già risolta
        DebugTabella(solvedSudoku, " Soluzione tabella generata");
        sudokuNumbers = RemoveNumbersFromSolvedSudoku();
        DebugTabella(sudokuNumbers, " Tabella sudoku generata");        //output per il debug
        sudokuButtonsTable = GenerateButtonsMatrix(sudokuNumbers);          //genero la tabella di bottoni con i numeri al loro interno
    }

    private int[][] RemoveNumbersFromSolvedSudoku(){
        String [][] aux = new String[SIZE][SIZE];;
        for(int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                aux[i][j] = solvedSudoku[i][j] +"";
            }
        }

        for(int i = 0; i < 81 - numbersToGenerate; i++) {
            int randRow = GenerateRandomNumber(8, 0); //riga random da 0 a 8
            int randColumn = GenerateRandomNumber(8, 0);
            if (!aux[randRow][randColumn].equals("0"))
                aux[randRow][randColumn] = 0 + "";
            else
                i--;
        }
        int [][] table = new int[SIZE][SIZE];
        for(int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                table[i][j] = Integer.parseInt(aux[i][j]);
            }
        }
        return table;
    }
    private int GenerateRandomNumber(int max, int min) //genero un numero random dal min al max
    {
        int number = min + (int)(Math.random() * ((max - min) + 1));
        return number;
    }

    //--------------- METODI DI GENERAZIONE ------------------
    //generazione dei bottoni con i numeri
    public Button[][] GenerateButtonsMatrix(int[][] sudokuNumbers) {
        Button myButtons[][] = new Button[SIZE][SIZE];
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++) {
                myButtons[i][j] = (Button)this.activity.findViewById(idBtnArray[i][j]);                   //inizializzo la tabella con l'id
                if(sudokuNumbers[i][j] != 0){                                               //controllo che nella posizione i j della tabella ci sia un valore disuguale a 0
                    myButtons[i][j].setTextColor(this.activity.getResources().getColor(R.color.Green));   //i numeri creati in partenza hanno il colore verde
                    myButtons[i][j].setText(sudokuNumbers[i][j] + "");                      //i bottoni con i numeri creati all'inizio non possono essere modificati
                    myButtons[i][j].setClickable(false);
                }
                else
                    myButtons[i][j].setText("");
                myButtons[i][j].setTextSize(15);                                            //dimensione del testo nel bottone
            }
        }
        return myButtons;
    }

    //--------------- METODI AUSIGLIARI ------------------
    public void CleanButtons() {
        for(int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                sudokuButtonsTable[i][j].setText("");
    }
    //usato per controllare la risoluzione della tabella da perte del giocatote, si effettua un semplice confronto della tabella compilata con la tabella giusta
    public boolean ControlTable(Button[][] userButtonMatrix) {
        for(int i = 0; i < SIZE; i++)    //scorro tabella e confronto i numeri della tabella compilata e tabella corretta, un solo numero non uguale e ritorna un valore false
            for (int j = 0; j < SIZE; j++)
                if(!userButtonMatrix[i][j].getText().equals(solvedSudoku[i][j]+""))
                    return false;
        return true;
    }
    public int[] GetHint(Button[][]userMatrix) {
        ArrayList<ArrayList<Integer>> rowAndCollumsWrongCells = GetWrongCells(userMatrix);
        ArrayList<Integer> indexWrongRows = rowAndCollumsWrongCells.get(0);
        ArrayList<Integer> indexWrongColums = rowAndCollumsWrongCells.get(1);
        int[] values = new int[3];
        int randIndex = ((int)(Math.random() * (indexWrongColums.size()-1) + 1));
        if(indexWrongColums.size() > 0) {
            values[0] = indexWrongRows.get(randIndex);                                  //salvo alla prima posizione la riga
            values[1] = indexWrongColums.get(randIndex);                                //salvo alla seconda posizione la colonna
            values[2] = solvedSudoku[indexWrongRows.get(randIndex)][indexWrongColums.get(randIndex)];   //salvo alla terza posizione il valore
            return values;
        }
        return null;
    }

    private ArrayList<ArrayList<Integer>> GetWrongCells(Button[][] userMatrix) {
        ArrayList<ArrayList<Integer>> rowAndCollumsWrongCells = new ArrayList<>();
        ArrayList<Integer> indexWrongRows = new ArrayList<>();
        ArrayList<Integer> indexWrongColums = new ArrayList<>();

        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++) {
                if (!userMatrix[i][j].getText().equals(solvedSudoku[i][j]+"")) {
                    indexWrongRows.add(i);
                    indexWrongColums.add(j);
                }
            }
        }
        rowAndCollumsWrongCells.add(indexWrongRows);
        rowAndCollumsWrongCells.add(indexWrongColums);
        return rowAndCollumsWrongCells;
    }

    //--------------- DEBUG --------------------
    //output delle tabelle generate dalla classe Sudoku sulla finestra di debug
    private void DebugTabella(int tabella[][], String tag) {
        String debug = "";
        for(int i=0; i<9;i++)
        {
            for (int j = 0; j < 9; j++)
                debug += tabella[i][j] + "  ";
            debug += "\n";
        }
        Log.d(""+tag, "TABLE:\n"+ debug);
    }

    //--------------- I GETTERS ------------------
    public Button[][] GetButtonsMatrix() {
        return sudokuButtonsTable;
    }
    public int[][] GetSudokuTable(){
        return sudokuNumbers;
    }
    public int[][] GetSolvedSudoku() {
        return solvedSudoku;
    }
}
