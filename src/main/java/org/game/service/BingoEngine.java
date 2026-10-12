package org.game.service;
import java.util.*;

public class BingoEngine {
	
	
	//generate 5*5 board for whom ever call this method
	public static int[][] generateBoard() {
		List<Integer> pool = new ArrayList<>();

		for (int i = 1; i <= 25; i++) {
			pool.add(i);
		}
		Collections.shuffle(pool); // shuffle the ascending order into unknown

		int[][] board = new int[5][5];
		int idx = 0; // to access every element in pool
		for (int j = 0; j < 5; j++) {
			for (int k = 0; k < 5; k++) {
				board[j][k] = pool.get(idx++);
			}
		}
		return board;
	}
	
	
	//converting matrix to string to store in database as a string
	public static String matrixToString(int[][] matrix) {
		StringBuilder sb = new StringBuilder();
		for(int j=0;j<5;j++) {
			for(int k=0;k<5;k++) {
				sb.append(matrix[j][k]).append(","); // adding coma(,) inbetween every number (1,2,3)
			}
		}
		return sb.substring(0,sb.length()-1); // converts stringbuilder into string
	}
	
	
	//converting string to matrix for the crossing out numbers in game
	public static int[][] stringToMatrix(String str){
		String[] temp = str.split(",");
		int [][] matrix = new int[5][5];
		for(int i=0;i<25;i++) {
			matrix[i/5][i%5] = Integer.parseInt(temp[i]); // converts string of character into integer
		}
		return matrix;
	}
	
	public static boolean checkWinCondition(boolean[][] marked) {

		int completedlines = 0;
		
		//checking the rows whether it is fully marked or not
		for (int r = 0; r < 5; r++) {
			boolean full = true;
			for (int c = 0; c < 5; c++) {
				if (!marked[r][c]) {
					full = false;
				}
			}
			if (full) {
				completedlines++;
			}

		}
		
		//checking columns 
		for (int c = 0; c < 5; c++) {
			boolean full = true;
			for (int r = 0; r < 5; r++) {
				if (!marked[r][c]) {
					full = false;
				}
			}
			if (full) {
				completedlines++;
			}
		}
		
		//left to right diagonal
		boolean diagonal = true;
		for(int i=0;i<5;i++) {
			if(!marked[i][i]) {
				diagonal = false;
			}
		}
		if(diagonal) {
			completedlines++;
		}
		
		//right to left diagonal
		boolean antidiagonal = true;
		for(int i=0;i<5;i++) {
			if(!marked[i][4-i]) {
				antidiagonal = false;
			}
		}
		if(antidiagonal) {
			completedlines++;
		}

		return completedlines>=5;
	}
	

}
