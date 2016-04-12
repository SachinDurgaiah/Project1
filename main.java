import java.util.*;
import java.sql.* ;  // for standard JDBC programs
import java.math.* ;

public class main {

	
	
	public static void main(String[] args){
		
		start();
		}

	public static void start() {
		// TODO Auto-generated method stub
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println("Welcome to the GCIT Library Management System. Which category of a user are you");
		System.out.println("\n");
		System.out.println("1. Librarian \n2. Administrator \n3. Borrower");
		Scanner sc = new Scanner(System.in);
		int choice = sc.nextInt();
		if(choice==1){
			Librarian l = new Librarian();
			l.start();
		}
		else if(choice==2){
			Admin a = new Admin();
			a.start();
		}
		else if(choice==3){
			Borrower b = new Borrower();
			b.start();
		}
		else if(choice>3){
			System.out.println("Invalid choice");
		}
	}
	
	
}
