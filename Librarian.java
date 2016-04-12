import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Scanner;

public class Librarian {

	public ResultSet rs= null;
	public Connection connection;
	 private Statement statement = null;
	  
	public void start() {
		// TODO Auto-generated method stub
		
		System.out.println("1. Enter Branch you manage \n2. Go back to previous screen");
		Scanner sc1 = new Scanner(System.in);
		int choice1 = sc1.nextInt();
		if(choice1==1){
			librarySelection();
		}
		else if(choice1==2){
			main m = new main();
			main.start();
		}
		else if(choice1>2){
			System.out.println("Invalid choice");
		}
	}

	private void librarySelection() {
		// list the names of the library branches
		
		 Database db = new Database();
		int ch = db.libraryList();
		String Name = db.map.get(ch);
		Update_options(Name);
	}

	private void Update_options(String name) {
		// TODO Auto-generated method stub
		System.out.println("Choose from below options ");
		System.out.println("1. Update the details of the Library \n2. Add copies of Book to the Branch\n3. Quit to previous ");
		Scanner sc2=new Scanner(System.in);
		int choice = sc2.nextInt();
		if(choice==1){
			Update_address(name);
		}
		else if(choice==2){
			Update_books(name);
		}
		else if(choice==3){
			librarySelection();
		}
		
		
		
	}

	private void Update_address(String name) {
		// update the name of the branch
		//update the address of the branch
		Scanner sc3 =  new Scanner(System.in);
		System.out.println("Enter the new name for the branch or If you do not want to update the details press N/A");
		String n_name=sc3.nextLine();
		if (n_name.equals("N/A")){
			librarySelection();
		}
		else{
			System.out.println("Enter the new Address of the branch or If you do not want to update the details press N/A");
			String n_address=sc3.nextLine();
			if (n_address.equals("N/A")){
				librarySelection();
			}
			else{
				//update the address of the library.
				Database ds= new Database();
				 if (ds.connect()) {
			            final Connection conn = ds.getConnection();
			            try {
			            	String branch_id=null;
							statement = conn.createStatement();
							ResultSet rs7=statement.executeQuery("Select branchId from tbl_library_branch where branchName='"+name+"'");
							while(rs7.next()){
								branch_id=rs7.getString("branchId");
							}
							//System.out.println("branch id "+branch_id);
							statement.executeUpdate("update tbl_library_branch set branchName ='"+n_name+"', branchAddress='"+n_address+"' where branchId='"+branch_id+"'");
						
							System.out.println("Update successfully done.");
							start();
			            }catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			}
			}
		}
	}

	private void Update_books(String name) {
		// TODO Auto-generated method stub\
		HashMap<Integer,Integer> map1= new HashMap<Integer,Integer>();
		int id=0;
		Database d1= new Database();
		 if (d1.connect()) {
	            final Connection conn = d1.getConnection();
	            try {
					statement = conn.createStatement();
					String b_id=null;
					ResultSet rs1=statement.executeQuery("select branchId from tbl_library_branch where branchName='"+name+"'");
					while(rs1.next()){
						b_id=rs1.getString("branchId");
					}
					//System.out.println("branch id "+b_id);
					int branch_id=Integer.parseInt(b_id);
					int cu=1;
					rs=statement.executeQuery("select bookId, title from tbl_book");
					//rs = statement.executeQuery("select b.bookId, b.title, a.authorName from tbl_book as b inner join tbl_book_authors as ba on b.bookId=ba.bookId left join tbl_author as a on ba.authorId=ba.authorId ");
					while(rs.next()){
						
						String id1 = rs.getString("bookId");
						String title = rs.getString("title");
						//String au_name=rs.getString("a.authorName");
						System.out.println(cu+" "+title);
						int iid = Integer.parseInt(id1);
						map1.put(cu, iid);
						
						cu++;
						}
					System.out.println("Enter you book selection");
					Scanner sc = new Scanner(System.in);
					int b = sc.nextInt();
					int b_id1=map1.get(b);
					String no_c=null;
					
					ResultSet r4 = statement.executeQuery("select noOfCopies from tbl_book_copies where bookId='"+b_id1+"' and branchId='"+b_id+"'");
					
					while(r4.next()){
						no_c = r4.getString("noOfCopies");
					}

					if(no_c==null){
						no_c="0";
					}
					
					System.out.println("The current number of copies is :"+no_c);
					
					System.out.println("Enter the number of copies ");
					Scanner sc1 = new Scanner(System.in);
					
					int count=sc1.nextInt();
					System.out.println("\n");
					if(no_c.equals("0"))
					{
						statement.executeUpdate("insert into tbl_book_copies (bookId,branchId,noOfCopies) values('"+b_id1+"','"+branch_id+"','"+count+"') ");
						System.out.println("New book added to the branch");
						start();
						
						
					}else{
						statement.executeUpdate("update tbl_book_copies set noOfCopies='"+count+"' where bookId='"+b_id1+"' and branchId='"+b_id+"' ");
						System.out.println("Number of copies updates ");
						start();
					}
	            }
	
	            catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

		
		
	}

	}
}
