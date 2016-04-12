import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Database {

	public ResultSet rs= null;
	public Connection connection;
	 private Statement statement = null;
	public volatile HashMap<Integer, String> map = new HashMap<Integer, String>();
	 public boolean connect()  {
	       try {
	            Class.forName("com.mysql.jdbc.Driver");
	            connection= DriverManager.getConnection(
				        "jdbc:mysql://localhost:3306/library", "root", "5883");
	           
	            return true;
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	            return false;
	            // Could not find the database driver
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	            // Could not connect to the database
	        }
	    }
	 public Connection getConnection() {
	        return connection;
	    }
	 
	 public int libraryList(){
		 int i=1;
		 int r =0;
		 if (connect()) {
	            final Connection conn = getConnection();
	            try {
					statement = conn.createStatement();
					
					rs = statement.executeQuery("select branchName from tbl_library_branch");
					while(rs.next()){
						String Branches = rs.getString("branchName");
						map.put(i, Branches);
						 System.out.println(i+" " +Branches);
						 i++;					
				}
					System.out.println("Enter your choice ");
					Scanner sc= new Scanner(System.in);
					r = sc.nextInt();
	            }catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	}
		return r;
}
	 
	 
	 
	 
	 
}