package view;

import controller.Logic;

public class Start {

	public static void main(String[] args) {
		
		System.out.println("Please wait for sometime, its working...");
		Logic logic = new Logic();
		if(args.length>0) {
		  logic.start(args[0]);
		}else {
		  logic.start(System.getProperty("user.home"));	
		}

	}

}
