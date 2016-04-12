import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class Borrower {

	public ResultSet rs3= null;
	public Connection connection;
	 private Statement statement = null;
	 int choice1=0;
	 String n=null;
	public void start() {
		// TODO Auto-generated method stub
		System.out.println("Enter the your Card Number:");
		Scanner sc4 = new Scanner(System.in);
		choice1 = sc4.nextInt();
		
		Database ds= new Database();
		 if (ds.connect()) {
	            final Connection conn = ds.getConnection();
	            try {
					statement = conn.createStatement();
					System.out.println("ch "+choice1);
					rs3 = statement.executeQuery("select count(cardNo) from tbl_borrower where cardNo='"+choice1+"'");
					while(rs3.next()){
						 n = rs3.getString("count(cardNo)");
					//System.out.println("n "+n);
						if(n.equals("1")){
							menu1();
						}
						else {
							System.out.println("Invalid card number");
							System.out.println("\n");
							start();
						}
						
					}
					
						
				}
					
					
	            catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	}
		
	
	}

	private void menu1() {
		// TODO Auto-generated method stub
		System.out.println("1. Check out a book \n2. Return a Book \n3. Quit to previous menu");
		System.out.println("\n");
		Scanner sc = new Scanner(System.in);
		int choice = sc.nextInt();
		if(choice==1){
			book_checkOut();
		}
		else if(choice==2){
			book_return();
		}
		else if(choice==3){
			main m = new main();
			main.start();
		}
		else if(choice>3){
			System.out.println("Invalid choice");
		}
	}

	private void book_return() {
		// TODO Auto-generated method stub
		System.out.println("Enter the book id of the book that you want to return ");
		Scanner sc = new Scanner(System.in);
		int book_id=sc.nextInt();
		try {
			boolean b = statement.execute("select bookId from tbl_book_loans where bookId ='"+book_id+"'");
			if(b=true){
				Date d1=new Date();
				String branch_id=null;
				rs3 =statement.executeQuery("select dueDate, branchId from tbl_book_loans where bookId='"+book_id+"' and cardNo='"+choice1+"'");
				while(rs3.next()){
					
					d1=rs3.getDate("dueDate");
					branch_id=rs3.getString("branchId");
				}
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				//System.out.println(dateFormat.format(date));
					if(d1.equals(dateFormat.format(date)) || d1.after(date)){
						statement.executeUpdate("update tbl_book_loans set dateIn=CURDATE() where bookId='"+book_id+"' and cardNo='"+choice1+"' ");
						//statement.executeUpdate("delete from tbl_book_loans where bookId='"+book_id+"' and cardNo='"+choice1+"'");
						System.out.println("You have returned on Time.");
						statement.executeUpdate("update tbl_book_copies set noOfCopies=noOfCopies+1 where bookId='"+book_id+"' and branchId='"+branch_id+"'");
						System.out.println("\n");
						menu1();
					}
					else if(d1.before(date)){
						long days = date.getTime() - d1.getTime();
						statement.executeUpdate("update tbl_book_loans set dateIn=CURDATE() where bookId='"+book_id+"' and cardNo='"+choice1+"' ");
						//statement.executeUpdate("delete from tbl_book_loans where bookId='"+book_id+"' and cardNo='"+choice1+"'");
					
						statement.executeUpdate("update tbl_book_copies set noOfCopies=noOfCopies+1 where bookId='"+book_id+"' and branchId='"+branch_id+"'");
						System.out.println("You are late on returning the book ");
						System.out.println("The amount of fine is :$"+(days*2)); // two dollars fine for each late date
						System.out.println("\n");
						menu1();
						
					
				}
			}
			else{
				System.out.println("Invalid book ID \n");
				
				book_return();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

	private void book_checkOut() {
		// list the names of the library branches
		// query : select branchName from tbl_library_branch;
		Database db = new Database();
		int ch = db.libraryList();
		String Name = db.map.get(ch);
		Display_books(Name);
		
	}

	private void Display_books(String name) {

		HashMap<Integer,Integer> map1= new HashMap<Integer,Integer>();
		int id=0;
		Database d1= new Database();
		 if (d1.connect()) {
	            final Connection conn = d1.getConnection();
	            try {
					statement = conn.createStatement();
					String brch_id=null;
					ResultSet rs1=statement.executeQuery("select branchId from tbl_library_branch where branchName='"+name+"'");
					while(rs1.next()){
						brch_id=rs1.getString("branchId");
					}
					System.out.println("branch id "+brch_id);
					int branch_id=Integer.parseInt(brch_id);
					int cu=1;
					
					ResultSet rs9 = statement.executeQuery("select b.bookId, b.title, a.authorName from tbl_book_copies as bc inner join tbl_book as b on bc.bookId=b.bookId left join tbl_book_authors as ba on b.bookId=ba.bookId left join tbl_author as a on ba.authorId=a.authorId where bc.branchId='"+branch_id+"'");
					while(rs9.next()){
						
						String id1 = rs9.getString("b.bookId");
						String title = rs9.getString("b.title");
						String au_name=rs9.getString("a.authorName");
						System.out.println(cu+" "+title+" by "+au_name);
						int iid = Integer.parseInt(id1);
						map1.put(cu, iid);
						
						cu++;
						}
					System.out.println("Enter you selection");
					Scanner sc = new Scanner(System.in);
					int b1 = sc.nextInt();
					int b_id1=map1.get(b1);
					String ui=null;
					ResultSet r4 =  statement.executeQuery("select count(bookId) from tbl_book_loans as bl where bookId='"+b_id1+"' and branchId='"+branch_id+"'and cardNo='"+choice1+"' and dateIn<'CURDATE()'");
					while(r4.next()){
						ui = r4.getString("count(bookId)");
					}
					if(ui.equals("1")){
						System.out.println("You cannot borrow the same book twice ");
						menu1();
					}else{
					statement.executeUpdate("insert into tbl_book_loans values ('"+b_id1+"','"+branch_id+"','"+choice1+"',CURDATE(),DATE_ADD(CURDATE(),INTERVAL 7 DAY),NULL)");
					
					statement.executeUpdate("update tbl_book_copies set noOfCopies=noOfCopies-1 where bookId='"+b_id1+"' ");
					System.out.println("Book check out successful");
					System.out.println("\n");
					menu1();
					}
				}
	
	            catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

}
	}
}
