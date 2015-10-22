package com.bankonet.utils.others;

import java.util.Scanner;

public class InputSingleton {

	private static InputSingleton instance = new InputSingleton();
	private Scanner input = new Scanner(System.in);
	
	private InputSingleton() {
		super();
	}
	
	public static InputSingleton getInstance(){
		return instance;
	}
	
	public String readString(String message){
		System.out.print(message);
		String retour = input.nextLine();
		return retour;
	}
	
	public int readInt(String message, int min, int max){
		int retour = 0;
		boolean infini = (min == 0 && max == 0);
		do{
			System.out.print(message);
			retour = input.nextInt();
			input.nextLine();
		}while((retour < min || retour > max) && !infini);
		return retour;
	}
	
	public double readDouble(String message, double min, double max){
		double retour = 0.0d;
		boolean infini = (min == 0.0d && max == 0.0d);
		do{
			System.out.print(message);
			retour = input.nextDouble();
			input.nextLine();
		}while((retour < min || retour > max) && !infini);
		return retour;
	}
	
}
