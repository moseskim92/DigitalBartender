// Fig. 28.23: DisplayAuthors.java
// Displaying the contents of the authors table.
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;

public class DigitalBartender
{
   // database URL                              
   static final String DATABASE_URL = "jdbc:mysql://localhost/drinks";
   static final String user = "root";
   static final String password = "root";//"!cBMT-x07!";
   // launch the application
   static final String[] names = {""};
   public static void main( String args[] )
   {
   
	   Connection connection = null; // manages connection
      Statement statement = null; // query statement
      ResultSet resultSet = null; // manages results
      try {
          // The newInstance() call is a work around for some
          // broken Java implementations

          Class.forName("com.mysql.jdbc.Driver").newInstance();
      } catch (Exception ex) {
          // handle the error
      }
      // connect to database drinks and query database
      try 
      {
         // establish connection to database                              
         //connection = DriverManager.getConnection( 
           // DATABASE_URL, "root", "!cBMT-x07!" );

try {
    Class.forName("com.mysql.jdbc.Driver").newInstance();
} catch (Exception ex) {
    System.err.println(ex.getMessage());
}
connection = DriverManager.getConnection(DATABASE_URL, user, password);

         // create Statement for querying database
         statement = connection.createStatement();
         //create GUI instance
         BartenderGUI window = new BartenderGUI();
         window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
         window.setSize( 600, 600 ); // set frame size
         window.setVisible( true ); // display frame
         
         //populate lists - testing version
        // populateJList(window.liquids, window.liquidid, "SELECT _name, _id FROM _liquids","_name","_id", connection);
         populateJListByParts(window.liquids, window.liquidid, "SELECT _name, _id FROM _liquids","_name","_id", connection,4);
         populateJListByParts(window.cocktails, window.cocktailid, "SELECT _name, _id FROM _cocktail","_name","_id", connection,20);
      }  // end try
      catch ( SQLException sqlException )                                
      {                                                                  
         sqlException.printStackTrace();
      } // end catch                                                     
      finally // ensure resultSet, statement and connection are closed
      {                                                             
         try                                                        
         {                                                          
            //resultSet.close();                                      
            statement.close();                                      
            connection.close();                                     
         } // end try                                               
         catch ( Exception exception )                              
         {                                                          
            exception.printStackTrace();                            
         } // end catch                                             
      } // end finally      
  
   } // end main
   
  
   
   public static void populateJList(JList list, JList ids, String query, String column,String idcolumn, Connection connection) throws SQLException
   {
       DefaultListModel model = new DefaultListModel(); //create a new list model
       DefaultListModel idmodel = new DefaultListModel(); //create a new list model 
       System.out.println("calling query: "+query); //for testing
       Statement statement = connection.createStatement();
       System.out.println("Created statement");
       ResultSet resultSet = statement.executeQuery(query); //run your query
       System.out.println("starting query: "+query); //for testing
       while (resultSet.next()) //go through each row that your query returns
       {
           String itemCode = resultSet.getString(column); //get the element in column "item_code"
           model.addElement(itemCode); //add each item to the model
           String itemID = resultSet.getString(idcolumn);
           idmodel.addElement(itemID);
       }
       System.out.println("ending query: "+query); //for testing
       list.setModel(model);
       ids.setModel(idmodel);
       resultSet.close();
       statement.close();
   }
   
   public static void populateJListByParts(JList list, JList ids, String query, String column,String idcolumn, Connection connection, int num) throws SQLException
   {
       DefaultListModel model = new DefaultListModel(); //create a new list model
       DefaultListModel idmodel = new DefaultListModel(); //create a new list model
       String query2 = new String();
       int MAX = 150;
       System.out.println("calling query: "+query); //for testing
       Statement statement = connection.createStatement();
       System.out.println("Created statement");
       for(int i=0;i<num;i++){
    	   //add limits to query
    	   query2 = query + " LIMIT "+(i*MAX)+","+(-1+(MAX*(i+1)));
    	   System.out.println(query2);
    	   //execute individual pull
           ResultSet resultSet = statement.executeQuery(query2); //run your query
    	   while (resultSet.next()) //go through each row that your query returns
       		{
           		String itemCode = resultSet.getString(column); //get the element in column "item_code"
           		model.addElement(itemCode); //add each item to the model
           		String itemID = resultSet.getString(idcolumn);
           		idmodel.addElement(itemID);
       		}
    	   resultSet.close();
       }
       list.setModel(model);
       ids.setModel(idmodel);
       
       statement.close();
   }
} // end class DigitalBartender


 