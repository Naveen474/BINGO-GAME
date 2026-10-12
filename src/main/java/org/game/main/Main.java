package org.game.main;

import java.util.*;

import org.game.DAO.BingoCardDAO;
import org.game.DAO.GameSessionDAO;
import org.game.DAO.UserDAO;
import org.game.entity.BingoCard;
import org.game.entity.GameSession;
import org.game.entity.User;
import org.game.service.BingoEngine;

public class Main {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		UserDAO userDAO = new UserDAO();
		GameSessionDAO sessionDAO = new GameSessionDAO();
		BingoCardDAO cardDAO = new BingoCardDAO();

		System.out.println("========================================");
		System.out.println("     WELCOME TO JAVA CONSOLE BINGO      ");
		System.out.println("========================================");

		
		System.out.print("Enter your username: ");
		String username = scanner.nextLine().trim();
		
		//getting username which is unique and sending to the user.java
		User user = new User();
		user.setUsername(username);
		user.setGamesWon(0);
		userDAO.saveUser(user);
		System.out.println("Welcome, "+user.getUsername()+ "profile loaded.");
		
		//creating bot as competitor
		User cpuUser = new User();
		cpuUser.setUsername("CPU_Bot");
		cpuUser.setGamesWon(0);
		userDAO.saveUser(cpuUser);
		
		//setting gamesession to check whether is game is finished or not
		GameSession gameSession = new GameSession();
		gameSession.setStatus("IN_PROGRESS");
		sessionDAO.saveGameSession(gameSession);
		System.out.println("Game Session #"+gameSession.getId()+"started!");
		
		//creating a board using bingoengine where we have generate board method
		int[][] boardMatrix = BingoEngine.generateBoard();
		//this is for storing the matrix into string 
		String numberGridStr = BingoEngine.matrixToString(boardMatrix);
		
		//setting the card for the user , IN_PROGRESS or FINISHED , converted matrix to string
		BingoCard bingoCard = new BingoCard();
		bingoCard.setPlayer(user);
		bingoCard.setGamesession(gameSession);
		bingoCard.setNumbersGrid(numberGridStr);
		
		//creating boolean matrix for the number crossed out
		boolean[][] markedMatrix = new boolean[5][5];
		bingoCard.setMarkedGrid(matrixToMarkedString(markedMatrix));
		
		cardDAO.saveBingoCard(bingoCard);
		
		int[][] cpuBoard = BingoEngine.generateBoard();
		String cpuNumberGridStr = BingoEngine.matrixToString(cpuBoard);
		
		//same done for the bot
		BingoCard cpuCard = new BingoCard();
		cpuCard.setPlayer(cpuUser);
		cpuCard.setGamesession(gameSession);
		cpuCard.setNumbersGrid(cpuNumberGridStr);
		
		
		boolean[][] cpuMarked = new boolean[5][5];
		cpuCard.setMarkedGrid(matrixToMarkedString(cpuMarked));
		cardDAO.saveBingoCard(cpuCard);
		
		System.out.println("Your 5x5 Bingo Card has been generated and saved to MySQL");
		
		
		List<Integer> drawnNumbers = new ArrayList<>();
		Random random = new Random();
		boolean hasWon = false;
		
		while(!hasWon) {
			printBoard("your bingo board",boardMatrix,markedMatrix);
			
			System.out.println("\nOptions:");
            System.out.println("1. Call/Draw Next Number");
            System.out.println("2. Claim BINGO");
            System.out.println("3. Exit Game");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine().trim();
            
            switch(choice) {
            case "1":
            	int drawnNum = drawRandomNumber(drawnNumbers,random);
            	if(drawnNum == -1) {
            		System.out.println("All numbers from 1 to 25 have been drawned");
            		break;
            	}
            	
            	markBoard(boardMatrix, markedMatrix, drawnNum);
            	bingoCard.setMarkedGrid(matrixToMarkedString(markedMatrix));
            	
            	
            	markBoard(cpuBoard, cpuMarked, drawnNum);
            	cpuCard.setMarkedGrid(matrixToMarkedString(cpuMarked));
            	
            	
            	if (BingoEngine.checkWinCondition(cpuMarked)) {
            		System.out.println("CPU_Bot completed 5 lines and got BINGO first! You lost!");
            		printBoard("CPU's BOARD", cpuBoard, cpuMarked);
            		gameSession.setStatus("FINISHED");
            		sessionDAO.saveGameSession(gameSession);
            		hasWon = true;
            	}
            	break;
            	
            case "2":
            	hasWon = BingoEngine.checkWinCondition(markedMatrix);
            	if(hasWon) {
            		System.out.println("BINGO Congratulatuinons"+ user.getUsername());
            		gameSession.setStatus("FINISHED");
            		sessionDAO.saveGameSession(gameSession);
            	}else {
            		System.out.println("Not a BINGO yet!");
            	}
            	break;
            	
            case "3":
            	System.out.println("Exiting game. Goodbye!");
            	scanner.close();
            	return;
            	
            default:
            	System.out.println("Invalid option. plaese choose 1,2,3.");
            }
		}
		
		scanner.close();
	}
	
	private static void markBoard(int[][] board, boolean[][] marked, int drawnNum) {
		for(int r = 0; r < 5; r++) {
			for(int c = 0; c < 5; c++) {
				if(board[r][c] == drawnNum) {
					marked[r][c] = true;
				}
			}
		}
	}
	
	private static int drawRandomNumber(List<Integer> drawnNumbers,Random random) {
		if(drawnNumbers.size() >= 25) {
			return -1;
		}
		int num;
		do {
			num = random.nextInt(25)+1;
		}while (drawnNumbers.contains(num));
		drawnNumbers.add(num);
		return num;
	}
	
	private static void printBoard(String title , int[][] board , boolean[][] marked) {
		System.out.println("\n================ BINGO BOARD =============");
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                if (marked[r][c]) {
                    System.out.printf("%5s", "[X]");
                } else {
                    System.out.printf("%5d", board[r][c]);
                }
            }
            System.out.println();
        }
        System.out.println("=================================");
	}
	
	private static String matrixToMarkedString(boolean[][] marked) {
		StringBuilder sb = new StringBuilder();
		for(int r=0;r<5;r++) {
			for(int c=0;c<5;c++) {
				sb.append(marked[r][c] ? "1" : "0").append(",");
			}
		}
		return sb.substring(0, sb.length()-1);
	}

}
