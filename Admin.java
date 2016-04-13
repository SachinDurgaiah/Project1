import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Admin {

	public ResultSet rs3= null;
	public Connection connection;
	 private Statement statement = null;
	public void start() {
		System.out.println("Welcome Admin");
		System.out.println("Select the operation that you would like to perform ");
		System.out.println("1. Add/Update/Delete Book and Author\n2. Add/Update/Delete Publishers\n3. Add/Update/Delete Library Branches\n4. Add/Update/Delete Borrowers\n5. Over-ride Due Date for a Book Loan\n6.Quit");
		Scanner sc=new Scanner(System.in);
		int ch=sc.nextInt();
		switch(ch){
		case 1: edit_books(); break;
		case 2: edit_publishers(); break;
		case 3: edit_lib_branches(); break;
		case 4: edit_borrowers(); break;
		case 5: edit_bookLoans(); break;
		case 6:main.start();
		default : System.out.println("Enter a valid choice "); start();
		}
	}

	
// EDITING THE BOOKS
	
	
	private void edit_books() {
		
		System.out.println("Enter the operation that you would like to perform");
		System.out.println("1. Add a book\n2.Delete a book \n3.Update details of a book");
		Scanner sc1= new Scanner(System.in);
		int ch=sc1.nextInt();
		switch(ch){
		case 1: Add_books(); break;
		case 2: Delete_book(); break;
		case 3: Update_bookDetails(); break;
		
		default : System.out.println("Enter a valid choice "); start();
		}
	}
	
	private void Add_books() {
		
		Database ds = new Database();
		if (ds.connect()) {
            final Connection conn = ds.getConnection();
		try {
			statement = conn.createStatement();
		
			HashMap<Integer,Integer> map3 = new HashMap<Integer,Integer>();
			System.out.println("Enter the Book title");
			Scanner sc5 = new Scanner(System.in);
			String Book_title = sc5.nextLine();
			ResultSet au =null;
			String author_id=null;
			String author_name=null;
					int c=1;
					System.out.println("\nSelect the author for your book\n");
			au = statement.executeQuery("select authorId,authorName from tbl_author");
			while(au.next()){
				author_id=au.getString("authorId");
				author_name=au.getString("authorName");
				int au_id=Integer.parseInt(author_id);
				map3.put(c, au_id);
				System.out.println(c+" "+author_name);
				c++;
			}
			System.out.println("\nIf you would like to add an author then press NEW in your key board. If not then press any key\n");
			Scanner s7 = new Scanner(System.in);
			String option = s7.nextLine();
			
	//////////////////////////////adding a new author///////////////////////////////////////		
			if(option.equals("NEW")){
				System.out.println("\n Enter the new author name \n");
				String n_author=s7.nextLine();
				String new_auId=null;
				statement.executeUpdate("insert into tbl_author (authorName) values ('"+n_author+"')");//added into authors table
				System.out.println("\nNew author has been added ");
				ResultSet n_auId=statement.executeQuery("select authorId from tbl_author where authorName='"+n_author+"'");
				while(n_auId.next()){
					new_auId = n_auId.getString("authorId");
				}
				String pub_id=null;
				String pub_name=null;
				HashMap<Integer,Integer> map4 = new HashMap<Integer,Integer>();
				int c1=1;
				System.out.println("\n Choose the publisher for your book \n");
				ResultSet pub = statement.executeQuery("select publisherId,publisherName from tbl_publisher");
				while(pub.next()){
					pub_id=pub.getString("publisherId");
					pub_name=pub.getString("publisherName");
					int pu_id=Integer.parseInt(pub_id);
					map4.put(c1, pu_id);
					System.out.println(c1+" "+pub_name);
					c1++;
				}
				System.out.println("\nIf you would like to add an publisher then press NEW in your key board. If not then press any key");
				Scanner s8 = new Scanner(System.in);
				String option_pub = s8.nextLine();
				if(option_pub.equals("NEW")){
					
					System.out.println("\nEnter the new publisher name");
					String n_pubName=s8.nextLine();
					System.out.println("Enter the new publisher address");
					String n_pubAddress=s8.nextLine();
					String n_puId=null;
					statement.executeUpdate("insert into tbl_publisher (publisherName,publisherAddress) values('"+n_pubName+"','"+n_pubAddress+"')");//added into publisher
					ResultSet n_puId1=statement.executeQuery("select publisherId from tbl_publisher where publisherName='"+n_pubName+"'");
					while(n_puId1.next()){
						n_puId = n_puId1.getString("publisherId");
					}
					int n_pu_id1=Integer.parseInt(n_puId);
					System.out.println("New publisher added \n");
					statement.executeUpdate("insert into tbl_book (title,pubId) values('"+Book_title+"','"+n_pu_id1+"')");//added into books
					ResultSet rss = statement.executeQuery("select bookId from tbl_book where title='"+Book_title+"' and pubId='"+n_pu_id1+"'");
					String n_buId=null;
					while(rss.next()){
						n_buId=rss.getString("bookId");
					}
					statement.executeUpdate("insert into tbl_book_authors (bookId,authorId) values ('"+n_buId+"','"+new_auId+"')");	//added into book_authors table		
					start();
				}
				System.out.println("\nSelecting an existing publisher\n");
				System.out.println("\nPlease select the publisher that you would like to have ");
				Scanner scan = new Scanner(System.in);
				int pub_choice = scan.nextInt();
				int act_pubId=map4.get(pub_choice);
				statement.executeUpdate("insert into tbl_book (title,pubId) values('"+Book_title+"','"+act_pubId+"')");//added into books with existing publisher
				//ResultSet n_auId=statement.executeQuery("select authorId from tbl_author where authorName='"+n_author+"'");
				
				ResultSet rss1 = statement.executeQuery("select bookId from tbl_book where title='"+Book_title+"' and pubId='"+act_pubId+"'");
				String new_buId=null;
				while(rss1.next()){
					new_buId=rss1.getString("bookId");
				}
				statement.executeUpdate("insert into tbl_book_authors (bookId,authorId) values ('"+new_buId+"','"+new_auId+"')");// added into book_authors table with existing publisher
				
				System.out.println("New book Added");			
				start();
				
			}
			else{
				String new_auId=null;
				System.out.println("\nSelect the author");
				Scanner scan2 = new Scanner(System.in);
				int au_choice=scan2.nextInt();
				int au_id = map3.get(au_choice);
				//////////////have the authod ID have to get the publisherID;
				
				String pub_id=null;
				String pub_name=null;
				HashMap<Integer,Integer> map5 = new HashMap<Integer,Integer>();
				int c1=1;
				System.out.println("\n Choose the publisher for your book \n");
				ResultSet pub1 = statement.executeQuery("select publisherId,publisherName from tbl_publisher");
				while(pub1.next()){
					pub_id=pub1.getString("publisherId");
					pub_name=pub1.getString("publisherName");
					int pu_id=Integer.parseInt(pub_id);
					map5.put(c1, pu_id);
					System.out.println(c1+" "+pub_name);
					c1++;
				}
				System.out.println("\nIf you would like to add an publisher then press NEW in your key board. If not then press any key");
				Scanner s8 = new Scanner(System.in);
				String option_pub = s8.nextLine();
				if(option_pub.equals("NEW")){
					
					System.out.println("\nEnter the new publisher name");
					String n_pubName=s8.nextLine();
					System.out.println("\nEnter the new publisher address");
					String n_pubAddress=s8.nextLine();
					String n_puId=null;
					statement.executeUpdate("insert into tbl_publisher (publisherName,publisherAddress) values('"+n_pubName+"','"+n_pubAddress+"')");//added into publisher
					ResultSet n_puId1=statement.executeQuery("select publisherId from tbl_publisher where publisherName='"+n_pubName+"'");
					while(n_puId1.next()){
						n_puId = n_puId1.getString("publisherId");
					}
					int n_pu_id1=Integer.parseInt(n_puId);
					statement.executeUpdate("insert into tbl_book (title,pubId) values('"+Book_title+"','"+n_pu_id1+"')");//added into books
					ResultSet rss = statement.executeQuery("select bookId from tbl_book where title='"+Book_title+"' and pubId='"+n_pu_id1+"'");
					String n_buId=null;
					while(rss.next()){
						n_buId=rss.getString("bookId");
					}
					statement.executeUpdate("insert into tbl_book_authors (bookId,authorId) values ('"+n_buId+"','"+au_id+"')");	//added into book_authors table		
					System.out.println("New book added");
					start();
				}
				System.out.println("\nPlease select the publisher that you would like to have ");
				Scanner scan = new Scanner(System.in);
				int pub_choice = scan.nextInt();
				int act_pubId=map5.get(pub_choice);
				statement.executeUpdate("insert into tbl_book (title,pubId) values('"+Book_title+"','"+act_pubId+"')");//added into books with existing publisher
				//ResultSet n_auId=statement.executeQuery("select authorId from tbl_author where authorName='"+n_author+"'");
				
				ResultSet rss1 = statement.executeQuery("select bookId from tbl_book where title='"+Book_title+"' and pubId='"+act_pubId+"'");
				String new_buId=null;
				while(rss1.next()){
					new_buId=rss1.getString("bookId");
				}
				statement.executeUpdate("insert into tbl_book_authors (bookId,authorId) values ('"+new_buId+"','"+au_id+"')");// added into book_authors table with existing publisher
				
				System.out.println("New book Added");			
				start();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
		
	}
	

	private void Delete_book() {
		// TODO Auto-generated method stub
		System.out.println("\nSelect the book you want to delete");
		Database ds = new Database();
		if (ds.connect()) {
            final Connection conn = ds.getConnection();
		try {
			statement = conn.createStatement();
			int buk_i=0;
		int c1=1;		
		String str=null;
			HashMap<Integer,Integer> map6 = new HashMap<Integer,Integer>();
			ResultSet j= statement.executeQuery("select bookId,booktitle  from tbl_book");
			while(j.next()){
				buk_i=j.getInt("bookId");
				str=j.getString("bookTitle");
				map6.put(c1,buk_i);
				c1++;
			}
			Scanner sc5 = new Scanner(System.in);	
			
			int select=sc5.nextInt();		
			if(select>c1){
				System.out.println("Book with this ID does not exist.");
				Delete_book();
			}
			else{
				int book_id = map6.get(select);
				
				//int book_id=sc5.nextInt();
				statement.executeUpdate("delete from tbl_book where bookId='"+book_id+"'");
				System.out.println("\n");
				System.out.println("Book deleted.");
				System.out.println("\n");
				start();
			}
		
		}
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		}
		
	}
	private void Update_bookDetails() {
		System.out.println("\nSelect the book you want to delete");
		Database ds = new Database();
		if (ds.connect()) {
            final Connection conn = ds.getConnection();
		try {
			statement = conn.createStatement();
			
		int c1=1;		
		String str=null;
			HashMap<Integer,Integer> map7 = new HashMap<Integer,Integer>();
			ResultSet j= statement.executeQuery("select bookId,booktitle  from tbl_book");
			while(j.next()){
				int buk_i=j.getInt("bookId");
				str=j.getString("bookTitle");
				map7.put(c1,buk_i);
				c1++;
			}
			Scanner sc5 = new Scanner(System.in);	
			
			int select=sc5.nextInt();		
			if(select>c1){
				System.out.println("Book with this ID does not exist.");
				Delete_book();
			}
			else{
				int book_id = map7.get(select);
			
				System.out.println("Enter the new title");
				Scanner k = new Scanner(System.in);
				String name12 =k.nextLine();
				System.out.println("Enter the publisher Id ");
				int pub_id = k.nextInt();
				statement.executeUpdate("update tbl_book set title='"+name12+"',pubId='"+pub_id+"' where bookId='"+book_id+"'");
				System.out.println("\n");
				System.out.println("Book details updated");
				System.out.println("\n");
				start();
			}
		}
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		}
		
	}

	

// EDITING THE PUBLISHER

	private void edit_publishers() {
		System.out.println("Enter the operation that you would like to perform");
		System.out.println("1. Add a publisher\n2.Delete a publisher\n3.Update details of a publisher");
		Scanner sc1= new Scanner(System.in);
		int ch=sc1.nextInt();
		switch(ch){
		case 1: Add_publisher(); break;
		case 2: Delete_publishe(); break;
		case 3: Update_publisherDetails(); break;
		
		default : System.out.println("Enter a valid choice "); start(); break;
		}	
		start();
	}
	
	private void Add_publisher() {
		
		
		Database ds = new Database();
		if (ds.connect()) {
            final Connection conn = ds.getConnection();
		
		try{
			statement = conn.createStatement();
		
			System.out.println("Enter the Publisher name and Publisher Address");
			Scanner sc3 = new Scanner(System.in);
			String name =sc3.nextLine();
			String address = sc3.nextLine();
			//sc3.close();
			statement.executeUpdate("insert into tbl_publisher (publisherName,publisherAddress) values ('"+name+"','"+address+"')");		
			System.out.println("\n");
			System.out.println("New publisher added.");
			System.out.println("\n");
			start();
			
		}
		
	
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
		
	}
	
	private void Delete_publishe() {
		
		
		Database ds = new Database();
		if (ds.connect()) {
            final Connection conn = ds.getConnection();
		
		try{
			statement = conn.createStatement();
			String pub_id=null;
			String pub_name=null;
			HashMap<Integer,Integer> map7 = new HashMap<Integer,Integer>();
			int c1=1;
			System.out.println("\n Choose the publisher you want to delete \n");
			ResultSet pub1 = statement.executeQuery("select publisherId,publisherName from tbl_publisher");
			while(pub1.next()){
				pub_id=pub1.getString("publisherId");
				pub_name=pub1.getString("publisherName");
				int pu_id=Integer.parseInt(pub_id);
				map7.put(c1, pu_id);
				System.out.println(c1+" "+pub_name);
				c1++;
			}
			
			Scanner sc2 = new Scanner(System.in);
			int pp_id=sc2.nextInt();
			int p_id = map7.get(pp_id);
			statement.executeUpdate("delete from tbl_publisher where publisherId='"+p_id+"'");
			System.out.println("\n");
			System.out.println("Publisher deleted.");
			System.out.println("\n");
			start();
		}
		
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		}
	}
	
	private void Update_publisherDetails() {
		Database ds = new Database();
		
		if (ds.connect()) {
            final Connection conn = ds.getConnection();
		
		try{
			statement = conn.createStatement();
			String pub_id=null;
			String pub_name=null;
			HashMap<Integer,Integer> map8 = new HashMap<Integer,Integer>();
			int c1=1;
			System.out.println("\n Choose the publisher you want to delete \n");
			ResultSet pub1 = statement.executeQuery("select publisherId,publisherName from tbl_publisher");
			while(pub1.next()){
				pub_id=pub1.getString("publisherId");
				pub_name=pub1.getString("publisherName");
				int pu_id=Integer.parseInt(pub_id);
				map8.put(c1, pu_id);
				System.out.println(c1+" "+pub_name);
				c1++;
			}
			
			Scanner sc2 = new Scanner(System.in);
			int pp_id=sc2.nextInt();
			int p_id = map8.get(pp_id);
			
			
		
			System.out.println("Enter the new publisher name");
			Scanner k = new Scanner(System.in);
			String name12 =k.nextLine();
			System.out.println("Enter the new address ");
			String address12 = k.nextLine();
			statement.executeUpdate("update tbl_publisher set publisherName='"+name12+"',publisherAddress='"+address12+"' where publisherId='"+p_id+"'");
			System.out.println("\n");
			System.out.println("Publisher details updated");
			System.out.println("\n");
			start();
		}
		
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		}
		
	}

	
// EDITING THE LIBRARY BRANCHES
	

	private void edit_lib_branches() {
		// TODO Auto-generated method stub
		System.out.println("Enter the operation that you would like to perform");
		System.out.println("1. Add a branch\n2.Delete a branch \n3.Update details of a branch");
		Scanner sc1= new Scanner(System.in);
		int ch=sc1.nextInt();
		
		switch(ch){
		case 1: Add_branch(); break;
		case 2: Delete_branch(); break;
		case 3: Update_branchDetails(); break;
		
		default : System.out.println("Enter a valid choice "); start();
		}
		
	}
	
	private void Add_branch() {
		// TODO Auto-generated method stub
		System.out.println("Enter the new Branch name, Branch Address ");
		//Scanner sc2 = new Scanner(System.in);
		Scanner sc3 = new Scanner(System.in);
		//int b_id=sc2.nextInt();
		String name =sc3.nextLine();
		String address = sc3.nextLine();
		Database ds1 = new Database();
		if (ds1.connect()) {
            final Connection conn = ds1.getConnection();
		
		try{
			statement = conn.createStatement();
	 
		statement.executeUpdate("insert into tbl_library_branch (branchName,branchAddress) values ('"+name+"','"+address+"')");
		System.out.println("\n");
		System.out.println("New branch added.");
		System.out.println("\n");
		start();
		}
		
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		}
	}

	private void Delete_branch() {
		System.out.println("Enter the Branch that you want to delete ");
		
		HashMap<Integer,Integer> map9 = new HashMap<Integer,Integer>();
		Database ds2 = new Database();
		if (ds2.connect()) {
            final Connection conn = ds2.getConnection();
		
		try{
			statement = conn.createStatement();
		ResultSet r6= statement.executeQuery("select branchId,branchName from tbl_library_branch");
		int br_iid=0;
		int c1=1;
		String br_nname=null;
		while(r6.next()){
			br_iid=r6.getInt("branchId");
			br_nname=r6.getString("branchName");
			System.out.println(c1+" "+br_nname);
			map9.put(c1, br_iid);
			c1++;
		}	
			
		Scanner sc2 = new Scanner(System.in);
		int b_choice=sc2.nextInt();		
		int b_id=map9.get(b_choice);
		
			statement.executeUpdate("delete from tbl_library_branch where branchId='"+b_id+"'");
			System.out.println("\n");
			System.out.println("Branch deleted.");
			System.out.println("\n");
			start();			
		
		}
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		}
	}

	private void Update_branchDetails() {
		
		System.out.println("\nEnter the branch id that you want to update \n");
		
		HashMap<Integer,Integer> map10= new HashMap<Integer,Integer>();
		Database ds3 = new Database();
		if (ds3.connect()) {
            final Connection conn = ds3.getConnection();
		
		try{
			int c=1;
			statement = conn.createStatement();
			rs3=statement.executeQuery("select branchId,branchName from tbl_library_branch");
			while(rs3.next()){
				int id = rs3.getInt("branchId");
				String name1 = rs3.getString("branchName");
				//String addr = rs3.getString("branchAddress");
				map10.put(c, id);
				System.out.println(c+" "+name1);
				c++;
			}
			
			
			Scanner sc2 = new Scanner(System.in);
			Scanner sc3= new Scanner(System.in);
			int bb_id=sc2.nextInt();
			int b_id=map10.get(bb_id);
			System.out.println("Enter the new branch name and address");
			String name =sc3.nextLine();
			String address = sc3.nextLine();
			
			statement.executeUpdate("update tbl_library_branch set branchName='"+name+"',branchAddress='"+address+"' where branchId='"+b_id+"'");
			System.out.println("\n");
			System.out.println("Branch details updated");
			System.out.println("\n");
			start();
		
		}
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		}
		
	}

	
// EDITING THE BORROWER
	
	private void edit_borrowers() {
		// TODO Auto-generated method stub
		System.out.println("Enter the operation that you would like to perform");
		System.out.println("1. Add a new borrower\n2.Delete a borrower \n3.Update details of a borrower");
		Scanner sc1= new Scanner(System.in);
		int ch=sc1.nextInt();
		
		switch(ch){
		case 1: Add_borrower(); break;
		case 2: Delete_borrower(); break;
		case 3: Update_borrowerDetails(); break;
		
		default : System.out.println("Enter a valid choice "); start();
		}
	}
	
	private void Add_borrower() {
		// TODO Auto-generated method stub
		System.out.println("Enter the new name of the borrower, Address and Phone number ");
		Scanner sc2 = new Scanner(System.in);
		Scanner sc3 = new Scanner(System.in);
		//int card_no=sc2.nextInt();
		String name =sc3.nextLine();
		String address = sc3.nextLine();
		int phone=sc3.nextInt();
		
		Database ds1 = new Database();
		if (ds1.connect()) {
            final Connection conn = ds1.getConnection();
		
		try{
			statement = conn.createStatement();
			String drake=null;
		
		statement.executeUpdate("insert into tbl_borrower (name,address,phone) values ('"+name+"','"+address+"','"+phone+"')");
		System.out.println("\n");
		System.out.println("New borrower added.");
		System.out.println("\n");
		start();
		}
		
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		}
	}

	private void Delete_borrower() {
		// TODO Auto-generated method stub
		System.out.println("Enter the card number that you want to delete ");
		
		
		Database ds1 = new Database();
		if (ds1.connect()) {
            final Connection conn = ds1.getConnection();
            HashMap<Integer,Integer> map11= new HashMap<Integer,Integer>();
		try{
			statement = conn.createStatement();
			int drake=0;
			ResultSet r8 = statement.executeQuery("select cardNo,name, address,phone from tbl_borrower");
			int ca_nno=0;
			int c=1;
			String nname=null;
			String aaddress=null;
			String Phone=null;
			while(r8.next()){
				ca_nno=r8.getInt("cardNo");
				nname=r8.getString("name");
				aaddress=r8.getString("address");
				Phone=r8.getString("phone");
				map11.put(c,ca_nno);
				System.out.println(c+" "+nname+" "+aaddress+" "+Phone);
				c++;
				
			}
			Scanner sc2 = new Scanner(System.in);
			int c_no1=sc2.nextInt();	
			int c_no=map11.get(c_no1);
			ResultSet r5= statement.executeQuery("select count(cardNo) from tbl_book_loans where cardNo='"+c_no+"' and dateIn=null");
			while(r5.next()){
				drake=r5.getInt("count(cardNo)");
			}
			if(!r5.next()){
				drake=0;
			}
		if(drake==0){
			statement.executeUpdate("delete from tbl_borrower where cardNo='"+c_no+"'");
			System.out.println("Borrower deleted.");
			System.out.println("\n");
			start();			
			System.out.println("\n");
		}else{
			System.out.println("Borrower with this card number has borrowed a book.So, you cannot delete it.");
			Delete_borrower();
		}
		}
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		}
	}

	private void Update_borrowerDetails() {

		System.out.println("Select the borrower whose details you want to update");
		Database ds1 = new Database();
		if (ds1.connect()) {
            final Connection conn = ds1.getConnection();
            HashMap<Integer,Integer> map12= new HashMap<Integer,Integer>();
    		try{
    			statement = conn.createStatement();
    			int drake=0;
    			ResultSet r8 = statement.executeQuery("select cardNo,name, address,phone from tbl_borrower");
    			int ca_nno=0;
    			int c=1;
    			String nname=null;
    			String aaddress=null;
    			String Phone=null;
    			while(r8.next()){
    				ca_nno=r8.getInt("cardNo");
    				nname=r8.getString("name");
    				aaddress=r8.getString("address");
    				Phone=r8.getString("phone");
    				map12.put(c,ca_nno);
    				System.out.println(c+" "+nname+" "+aaddress+" "+Phone);
    				c++;
    				
    			}
    			Scanner sc2 = new Scanner(System.in);
    			int c_no1=sc2.nextInt();	
    			int c_no=map12.get(c_no1);
		
    			System.out.println("Enter the new  name, address and phone number of the borrower");
    			Scanner sc = new Scanner(System.in);
    			Scanner sc1 = new Scanner(System.in);
    			String name=sc.nextLine();
    			String address =sc.nextLine();
    			int phone = sc.nextInt();
			statement.executeUpdate("update tbl_borrower set name='"+name+"',address='"+address+"',phone='"+phone+"' where cardNo='"+c_no+"'");
			System.out.println("Borrower details updated");
			System.out.println("\n");
			start();
			
		
		}
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		}
	}

	
// EDITING THE BOOK LOANS
	
	private void edit_bookLoans() {
		// TODO Auto-generated method stub
		System.out.println("Enter the card number and book ID of the book that you would like to update");
		Scanner sc1= new Scanner(System.in);
		int c_no=sc1.nextInt();
		int book_id=sc1.nextInt();
		
		
		Database ds1 = new Database();
		if (ds1.connect()) {
            final Connection conn = ds1.getConnection();
		
		try{
			statement = conn.createStatement();
			ResultSet big = statement.executeQuery("select count(bookId) from tbl_book_loans where bookId ='"+book_id+"'") ;
			
			while(big.next()){
				String str = big.getString("count(bookId)");
				System.out.println("count: "+str);
				if(str.equals("1")){
					ResultSet b1 =statement.executeQuery("select dueDate, branchId from tbl_book_loans where bookId='"+book_id+"' and cardNo='"+c_no+"'");
					if(b1.next()){
						DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						Date date = new Date();
						//System.out.println(dateFormat.format(date));
						Date d1=b1.getDate("dueDate");
						System.out.println(d1);
						String branch_id=b1.getString("branchId");
						
						if(d1.equals(dateFormat.format(date)) || d1.after(date)){
							statement.executeUpdate("update tbl_book_loans set dueDate=DATE_ADD(CURDATE(),INTERVAL 7 DAY) where bookId='"+book_id+"' and cardNo='"+c_no+"'");
							System.out.println("Your book's due date has been postponed for another week.");
							//statement.executeUpdate("update tbl_book_copies set noOfCopies=noOfCopies+1 where bookId='"+book_id+"' and branchId='"+branch_id+"'");
							System.out.println("\n");
							start();
							System.out.println("\n");
						}
						else if(d1.before(date)){
							long days = date.getTime() - d1.getTime();
							statement.executeUpdate("delete from tbl_book_loans where bookId='"+book_id+"' and cardNo='"+c_no+"'");
							System.out.println("You cannot renew your book. You must return it");
							
							System.out.println("The outstanding amount of fine is :$"+(days*2)); // two dollars fine for each late date
							System.out.println("\n");
							start();
							System.out.println("\n");
						}
					}
				}
				else{
					System.out.println("Invalid book ID \n");
					
					start();
				}
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

	

	

	

	
}
