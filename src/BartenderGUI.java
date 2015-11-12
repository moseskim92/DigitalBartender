import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class BartenderGUI extends JFrame{
	public static JList liquids; //list of all ingredients
	public static JList cabinet; //list of selected ingredients
	public static JList cocktails; //list of available mixes
	public static JList liquidid; //list of all ingredients
	public static JList cocktailid; //list of available mixes
	public static String[] liquidNames = {"sad", "asd"}; //testing only, remove later
	public static String[] templist = {"aewrersfdsdf", "v", "s"}; //testing only
	public static JTextArea ingredients;
	public static JTextArea directions;
	public static JButton apply;
	public static JButton vidsearch;
	public JTextField search;
	//constructor
	public BartenderGUI(){
		//create panels		
		super("Digital Bartender");
		//menu
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmSelectUser = new JMenuItem("Load Cabinet");
		mnFile.add(mntmSelectUser);
		
		JMenuItem mntmSaveCabinet = new JMenuItem("Save Cabinet");
		mnFile.add(mntmSaveCabinet);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		//buttons defined
		apply = new JButton("Update options");
		vidsearch = new JButton("Find video");
		search = new JTextField("",10);
		//Content Pane
		JPanel embed1 = new JPanel();
		JPanel embed2 = new JPanel();
		JPanel embed3 = new JPanel();
		embed1.setPreferredSize(new Dimension(120,600));
		embed2.setPreferredSize(new Dimension(120,600));
		
		setLayout( new FlowLayout(FlowLayout.LEFT) ); // set frame layout
		add(embed1);
		add(embed2);
		add(embed3);
		//add buttons
		embed1.add(apply);
		embed2.add(vidsearch);
		
		embed2.add(search);
		//id lists
		liquidid = new JList();
		cocktailid = new JList();
		//liquids list
		liquids = new JList(liquidNames);
		embed1.add(liquids);
		liquids.setVisibleRowCount( 20 );
		JScrollPane liquidshow = new JScrollPane(liquids);
		liquidshow.setPreferredSize(new Dimension(120, 500));
		liquidshow.setMinimumSize(new Dimension(120, 500));
		embed1.add(liquidshow );
		
		//cocktail list
		cocktails = new JList();
		embed2.add (cocktails);
		cocktails.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cocktails.setVisibleRowCount(20);
		JScrollPane cocktailshow = new JScrollPane(cocktails);
		cocktailshow.setPreferredSize(new Dimension(120, 475));
		cocktailshow.setMinimumSize(new Dimension(120, 475));
		embed2.add(cocktailshow );
		
		//liquids.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		liquids.setSelectionModel(new DefaultListSelectionModel() 
		{
		    @Override
		    //This overridden method sets each click to toggle the selection of a given list item
		    public void setSelectionInterval(int index0, int index1) 
		    {
		        if(liquids.isSelectedIndex(index0)) 
		        {
		            liquids.removeSelectionInterval(index0, index1);
		        }
		        else 
		        {
		            liquids.addSelectionInterval(index0, index1);
		        }
		    }
		});
		//contents of embed3, the far right pane, for directions and ingredients
		ingredients = new JTextArea();
		//directions = new JTextArea();
		ingredients.setPreferredSize(new Dimension(250, 400));
		//directions.setPreferredSize(new Dimension(150, 250));
		ingredients.setEditable(false);
		//directions.setEditable(false);
		embed3.add(ingredients);
		//embed3.add(directions);
		//add listener to inventory list
		liquids.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				//set liquid id to select parallel with liquids
				liquidid.setSelectedIndices(liquids.getSelectedIndices());
			}
			
		});
		/*
		search.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (search.getText() : DefaultListModel<String> cocktails) {
		            if (!s.startsWith(filter)) {
		                if (model.contains(s)) {
		                    model.removeElement(s);
		                }
		            } else {
		                if (!model.contains(s)) {
		                    model.addElement(s);
		                }
		            }
		        }
				
			}
			
		
		});
		*/
		//add listener to cocktail selection list
		cocktails.addListSelectionListener(
				new ListSelectionListener(){
					//inner variables
					 Connection connection = null; // manages connection
				      Statement statement = null; // query statement
				      ResultSet resultSet = null; // manages results
					  
					public void valueChanged(ListSelectionEvent e) {
						cocktailid.setSelectedIndex(cocktails.getSelectedIndex()); //sync lists
						String selectedCockTail = cocktailid.getSelectedValue().toString();
						//make database connection 
						 try {
					          // The newInstance() call is a work around for some
					          // broken Java implementations

					          Class.forName("com.mysql.jdbc.Driver").newInstance();
					      } catch (Exception ex) {
					          // handle the error
					      }
					      // connect to database drinks and query database
						 try{
						 try {
							    Class.forName("com.mysql.jdbc.Driver").newInstance();
							} catch (Exception ex) {
							    System.err.println(ex.getMessage());
							}
						 connection = DriverManager.getConnection(DigitalBartender.DATABASE_URL, DigitalBartender.user, DigitalBartender.password);

				         // create Statement for querying database
				         statement = connection.createStatement();
				         String query = "SELECT _instructions FROM _cocktail WHERE _id = "+selectedCockTail;
				         String query2 = "SELECT _amount, _name FROM _mix  INNER JOIN _liquids ON _mix._ingredient_id = _liquids._id WHERE _cocktail_id ="+selectedCockTail;
				         String what = new String();
				         String how = new String();
				         ResultSet resultSet = statement.executeQuery(query); //run your query
				         while (resultSet.next()) //go through each row that your query returns
				         {
				        	how += resultSet.getString("_instructions");
				         }
				        //replace '|' with new line
				         how = how.replace('|', '\n');
				         //directions.setText(how);//output directions to GUI
						 //pull ingredients
				         ResultSet resultset2 = statement.executeQuery(query2);
				         while (resultset2.next()){
				        	 what += resultset2.getString("_amount");
				        	 what += "\t";
				        	 what += resultset2.getString("_name");
				        	 what += "\n";
				         }
						 //output ingredents to GUI
				         ingredients.setText(what+how);
				         //close resultsets
				         
						 }
						 
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
					}
					
				}
			);
		//Add functions to file menu
		mntmSaveCabinet.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				saveCabinet();
			}
		});
		
		mntmSelectUser.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				loadCabinet();
			}
		});
		
		mntmExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
		
		//Search button Action Listener
				vidsearch.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						try {
							 String search = (String) cocktails.getSelectedValue();
							 search = search.replaceAll("\\s",  "+")+ "+How+To";
						     String url ="https://www.youtube.com/results?search_query=" + search;
						     Desktop dt = Desktop.getDesktop();
						     URI uri = new URI(url);
						     dt.browse(uri.resolve(uri));
						 } catch (URISyntaxException ex) {
						     System.out.println("URI Syntax Exception");
						 } catch (IOException ex) {
							 System.out.println("IO Exception");
						 }
					}
				});
				//update ActionListener
				apply.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						updateInventory();
					}
				});

	}//end constructor
	
	static public void saveCabinet(){
		try{
			
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("cabinet.ser"));
			out.writeObject(liquids.getSelectedIndices());
			out.flush();
			out.close();
		}
		catch(Exception e){
			//popup error message
			System.out.println("save error");
			System.out.println(e);
		}
	}
	
	static public void loadCabinet(){
		try{
			
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("cabinet.ser"));
			int[] cab = (int[])in.readObject();
			in.close();
			//set both liquid lists to saved data
			liquids.setSelectedIndices(cab);
			liquidid.setSelectedIndices(cab);
			//update available cocktails
			updateInventory();
		}
		catch(Exception e){
			System.out.println("load error");
			System.out.println(e);
		}
	}
	
	public static void updateInventory(){
		//inner variables
		 Connection connection = null; // manages connection
	      Statement statement = null; // query statement
	      ResultSet resultSet = null; // manages results
	      Object[] selectVal = liquidid.getSelectedValues();
	      String[] selections = new String[selectVal.length];
	      String available = "SELECT _id, _name FROM _cocktail WHERE _have_ingredients(_id) = 1";
		//make database connection 
		 try {
	          // The newInstance() call is a work around for some
	          // broken Java implementations

	          Class.forName("com.mysql.jdbc.Driver").newInstance();
	      } catch (Exception ex) {
	          // handle the error
	      }
	      // connect to database drinks and query database
		 try{
		 try {
			    Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (Exception ex) {
			    System.err.println(ex.getMessage());
			}
		 connection = DriverManager.getConnection(DigitalBartender.DATABASE_URL, DigitalBartender.user, DigitalBartender.password);

         // create Statement for querying database
         statement = connection.createStatement();
         //Execute changes to _inventory
         for(int i = 0;i<selections.length;i++){
        	 selections[i] = (String) selectVal[i];
         }
         for(int i = 0;i<selections.length;i++){
        	 System.out.println(selections[i]);
         }
         	//delete previous inventory
         	statement.execute("DELETE FROM _inventory WHERE _liquid != 2"); //remove everything but water
         	//Insert new inventory
         
         	for(int i = 1;i<selections.length+1;i++){
         		statement.execute("INSERT INTO _inventory(_id, _liquid) VALUES ('"+(i+1)+"', '"+selections[i-1]+"')");
         		System.out.println("INSERT INTO _inventory(_id, _liquid) VALUES ('"+(i+1)+"', '"+selections[i-1]+"')");
         	}
         	
         //Update available cocktail selection
         	DigitalBartender.populateJListByParts(cocktails, cocktailid, available,"_name","_id", connection,2);
         
		 }
		 
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
	}//end updateInventory
	
}//end class BartenderGUI


class JLData implements Serializable{
	int items[];
	//String[] selectedItem;
}