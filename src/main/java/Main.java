import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
	   public static void main(String[] args) throws Exception{
	       System.out.println("Sudoku application by KARAV team - Karolina & Rafał Warno");
	       if (args.length == 0){
	           System.out.println("Please provide input data file name.");
	           return;	    	   
	       }
	    	   	
	       File f = new File(args[0]);
//	       System.out.println("current path->" + (new File(".")).getAbsolutePath() );	       
	       if (! f.exists()){
	           System.out.println("Sudoku input data file not found: " + args[0]);
	           return;
	       }

	       String[][] input = readData(args[0]);	         
	       SudokuTiny sudoku = new SudokuTiny(input);
	       sudoku.startStoper();
	       sudoku.startSearching();
	       sudoku.stopStoper();
	       System.out.println("Total solutions: " + sudoku.getSolutionCounter());
	    }
	   
	   static String[][] readData(String aFileName) throws IOException{
	       String[][] input = new String[9][];
	       BufferedReader br = new BufferedReader(new FileReader(aFileName));	       
	       String line = null;
       	   int i = 0;	       
	        do {
	            line= br.readLine();
	            String[] row = new String[9];
	            if (line != null){
	               StringTokenizer st = new StringTokenizer(line,",",false);
	               List rowList = new LinkedList();
	               while (st.hasMoreTokens()) {
	                   String el =  st.nextToken();	                  
	                   rowList.add(el);		                   
	               }	               	
	               rowList.toArray(row);
		           input[i++] = row;		                 
	            }	            
	         }while(line!= null);	       		        
	        return input;
	   }
}
