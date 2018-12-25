import com.mysql.cj.jdbc.exceptions.*;// NOTE: REQUIRES THE USE OF ONE .JAR FILE IN THE SAME DIRECTORY AS THIS .JAVA CODE. THE .JAR FILE NAME IS:  mysql-connector-java-8.0.13.jar
import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;// input/output or read/write stream
import java.sql.*;// mysql jdbc
import java.text.SimpleDateFormat;// date/time display format
import java.util.ArrayList;// part of List<RowSorter.SortKey> sortKeys
import java.util.Date;// date/time
import java.util.List;// part of DocumentListener interface implementation
import java.util.Vector;// part of DocumentListener interface implementation
import javax.sql.*;// mysql jdbc
import javax.swing.*;// swing ui
import javax.swing.RowSorter;// part of List<RowSorter.SortKey> sortKeys
import javax.swing.SortOrder;// part of List<RowSorter.SortKey> sortKeys
import javax.swing.table.*;// part of TableRowSorter<TableModel> recordSorter
import javax.swing.event.DocumentEvent;// part of DocumentListener interface implementation
import javax.swing.event.DocumentListener;// part of DocumentListener interface implementation
import javax.swing.table.DefaultTableModel;// part of tblEmployees

public class Employees extends JFrame implements ActionListener, DocumentListener//, MouseListener
{
	Object[] record;// This allows us to temporarily store pointer data obtained from dbase
	Object activeComponent;// Object activeComponent allows us to check which component (ex., textfield, button) the admin/user is currently interacting with
	SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");// Date formatting only
	SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");// Time formatting only
	JPanel panelMain, pnlHeader, pnlDateTime, pnlID, pnlTextTableFields, pnlInputFields, pnlName, pnlFname, pnlMname, pnlLname, pnlOtherInf, pnlAge, pnlGender, pnlAddress, pnlEmpList, pnlFooter, pnlButtons, pnlModify;
	JLabel lbDateTime, lbDate, lbTime, lbIDnum, lbEmpID, lbNamefull, lbFname, lbMname, lbLname, lbAge, lbGender, lbAddress, spaceIDDT, spaceLblTxtID, gapTblBtn;
	JTextField txFname, txMname, txLname, txAge, txGender, txAddress;
	JComboBox<String> cmbxGender;// Combo Box / Drop-Down for Gender
	DefaultTableModel tableModel;// Table Model
	JTable tblEmployees;// Employees Table
	TableRowSorter<TableModel> recordSorter;// This is responsible for sorting records by ascending order (for now)
	JScrollPane scrollablePane;
	JButton btnCreate, btnRead, btnUpdate, btnDelete, btnClose;// Buttons
	List<JTextField> textFields = null;// This line of code is part of the DocumentListener interface implementation, which will be discussed in later comments.
	List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);// This is responsible for sorting records by ascending order (for now)
	//Font colFont;
	Connection dbConn;// This is the container for database connection driver, host name, db name, user name, password
	Statement sqlStmnt;// This is where most MySQL statements (ex., INSERT INTO, SELECT) are stored
	String sqlQuery, sqlUpdate;// sqlQuery is for searching desired records while sqlUpdate is for data manipulation (save, delete)
	ResultSet sqlRS;// sqlRS is the Result Set that allows us to obtain data from db table based on the MySQL statement that the admin/user entered
	String[] genders = { "", "Male", "Female" }, labels = { "Employee ID", "Given Name", "Middle Name", "Family Name", "Age", "Gender", "Address" };// For combo box and jtable, respectively
	String empMI, empGender, empMorF;// Middle Initial, Shortened Gender (M or F)
	int empIDnum = 0, latestEmpID, empAge, lastRow, recordChecked, currentRowCount, rowCountDB, rowCountTbl, selectedRowInTbl, lastRowDB = 0, lastRowTbl = 0;
	private boolean buttonIsClicked = false/* a.k.a. _clickMeMode */, functionIsReadOrSearch = true;
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	Employees()
	{
		try
		{
			// Create container for currently active component (ex., text field, button, etc.)
			activeComponent = null;
			
			// Create panels
			panelMain = new JPanel(); 				panelMain.setLayout( new BorderLayout(1, 1) ); 				panelMain.setBackground(Color.white);
			pnlHeader = new JPanel(); 				pnlHeader.setLayout( new BorderLayout(1, 1) ); 				pnlHeader.setBackground(Color.white);
			pnlID = new JPanel(); 					pnlID.setLayout( new BorderLayout(1, 1) ); 					pnlID.setBackground(Color.white);
			
			pnlDateTime = new JPanel(); 			pnlDateTime.setLayout( new BorderLayout(1, 1) ); 			pnlDateTime.setBackground(Color.white);
			pnlDateTime.setBorder ( BorderFactory.createTitledBorder( BorderFactory.createLoweredBevelBorder(), dateFormat.format( new Date() ), 2, 0) ) ;
			
			pnlTextTableFields = new JPanel(); 		pnlTextTableFields.setLayout( new BorderLayout(1, 1) ); 	pnlTextTableFields.setBackground(Color.white);
			pnlInputFields = new JPanel(); 			pnlInputFields.setLayout( new BorderLayout(1, 1) ); 		pnlInputFields.setBackground(Color.white);
			pnlName = new JPanel(); 				pnlName.setLayout( new BorderLayout(1, 1) ); 				pnlName.setBackground(Color.white);
			pnlFname = new JPanel(); 				pnlFname.setLayout( new BorderLayout(1, 1) ); 				pnlFname.setBackground(Color.white);
			pnlMname = new JPanel(); 				pnlMname.setLayout( new BorderLayout(1, 1) ); 				pnlMname.setBackground(Color.white);
			pnlLname = new JPanel(); 				pnlLname.setLayout( new BorderLayout(1, 1) ); 				pnlLname.setBackground(Color.white);
			pnlOtherInf = new JPanel(); 			pnlOtherInf.setLayout( new BorderLayout(1, 1) ); 			pnlOtherInf.setBackground(Color.white);
			pnlAge = new JPanel(); 					pnlAge.setLayout( new BorderLayout(1, 1) ); 				pnlAge.setBackground(Color.white);
			pnlGender = new JPanel(); 				pnlGender.setLayout( new BorderLayout(1, 1) ); 				pnlGender.setBackground(Color.white);
			pnlAddress = new JPanel(); 				pnlAddress.setLayout( new BorderLayout(1, 1) ); 			pnlAddress.setBackground(Color.white);
			pnlEmpList = new JPanel(); 				pnlEmpList.setLayout( new BorderLayout(1, 1) ); 			pnlEmpList.setBackground(Color.white);
			pnlFooter = new JPanel(); 				pnlFooter.setLayout( new BorderLayout(1, 1) ); 				pnlFooter.setBackground(Color.white);
			pnlButtons = new JPanel(); 				pnlButtons.setLayout( new BorderLayout(1, 1) ); 			pnlButtons.setBackground(Color.white);
			pnlModify = new JPanel(); 				pnlModify.setLayout( new BorderLayout(1, 1) ); 				pnlModify.setBackground(Color.white);
			
			// Create spaces/gaps for future use
			spaceIDDT = new JLabel("     ");
			spaceLblTxtID = new JLabel("   ");
			gapTblBtn = new JLabel("                                                                                                                                                                                                                                                                                         ");
			// Create labels and textfields and add Document Listener
			// Date/Time
			lbDate = new JLabel ( dateFormat.format( new Date() ) ) ; lbTime = new JLabel ( timeFormat.format( new Date() ) ) ;
			// Employee ID Number (int / numerical in nature only)
			lbIDnum = new JLabel("Employee ID:"); lbEmpID = new JLabel( Integer.toString(empIDnum) );
			// First Name / Given Name
			lbFname = new JLabel("Given Name"); txFname = new JTextField(20); txFname.getDocument().addDocumentListener(this);
			// Middle Name
			lbMname = new JLabel("Middle Name"); txMname = new JTextField(30); txMname.getDocument().addDocumentListener(this);
			// Last Name / Family Name
			lbLname = new JLabel("Family Name"); txLname = new JTextField(21); txLname.getDocument().addDocumentListener(this);
			// Age (int / numerical in nature only)
			lbAge = new JLabel("Age"); txAge = new JTextField(3); txAge.getDocument().addDocumentListener(this);
			// Gender (a.k.a. Sex)
			lbGender = new JLabel("Gender");
			// Create Gender combo box or drop-down
			cmbxGender = new JComboBox<>(genders); cmbxGender.setSelectedIndex(0); cmbxGender.addActionListener(this);
			// Continue creating labels and textfields and adding Document Listener
			txGender = new JTextField(6); txGender.getDocument().addDocumentListener(this);/* txGender.setText ( (String) cmbxGender.getSelectedItem() );//*/
			// Address
			lbAddress = new JLabel("Address"); txAddress = new JTextField(61); txAddress.getDocument().addDocumentListener(this);
				/* NOTE: The line of code below is related to the DocumentListener interface implementation. */
			textFields = new Vector<>(); textFields.add(txFname); textFields.add(txMname); textFields.add(txLname); textFields.add(txAge); textFields.add(txGender); textFields.add(txAddress);
				/* The List<> variable - textFields - adds all the text fields in the list, to check whether one of them is empty. */
			
			/* Table is set up. */
			tableModel = new DefaultTableModel();
			tblEmployees = new JTable(tableModel);
			recordSorter = new TableRowSorter<TableModel>( tblEmployees.getModel() );
			tableModel.setColumnIdentifiers(labels);
			tblEmployees.setRowSorter(recordSorter);
			sortKeys = new ArrayList<>(25);
			sortKeys.add( new RowSorter.SortKey(0, SortOrder.ASCENDING) );
			sortKeys.add( new RowSorter.SortKey(1, SortOrder.ASCENDING) );
			sortKeys.add( new RowSorter.SortKey(2, SortOrder.ASCENDING) );
			sortKeys.add( new RowSorter.SortKey(3, SortOrder.ASCENDING) );
			sortKeys.add( new RowSorter.SortKey(4, SortOrder.ASCENDING) );
			sortKeys.add( new RowSorter.SortKey(5, SortOrder.ASCENDING) );
			sortKeys.add( new RowSorter.SortKey(6, SortOrder.ASCENDING) );
			recordSorter.setSortKeys(sortKeys);
			tblEmployees.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			tblEmployees.getTableHeader().setReorderingAllowed(false);
			scrollablePane = new JScrollPane(tblEmployees);
			scrollablePane.setSize(990, 50);
			record = new Object[7];
			/* Table setup continues...after retrieveRecordsFromDBTable(); */
			
			// Create buttons
			btnCreate = new JButton("Create"); btnCreate.setEnabled(false); btnCreate.addActionListener(this);
			btnRead = new JButton("Search"); btnRead.setEnabled(true); functionIsReadOrSearch = true; btnRead.addActionListener(this);
			btnUpdate = new JButton("Update"); btnUpdate.setEnabled(false); btnUpdate.addActionListener(this);
			btnDelete = new JButton("Delete"); btnDelete.setEnabled(false); btnDelete.addActionListener(this);
			btnClose = new JButton("Close"); btnClose.setEnabled(true); btnClose.addActionListener(this);
			
			// Create MySQL Database Connection using Connector/J | JDBC and - if applicable - show DB Table contents in JTable
			dbConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/timetracker", "root", "");
			sqlStmnt = dbConn.createStatement();
			//retrieveRecordsFromDBTable();// Search this helper function/method for more details [Ctrl]+[F] ...MySQL Database Connection creation ends
			// Yeah, supposedly, this is part of JTable setup (due to tblEmployees) but
			// I transferred this specific code block after retrieving records from database table coz of...sequence
			// addMouseListener allows you to click any Jtable record/row and display data into their respective text fields 
			tblEmployees.addMouseListener( new MouseAdapter()
			{
				@Override public void mouseClicked(MouseEvent e)
				{
					if (functionIsReadOrSearch)
					{
						activeComponent = scrollablePane;
						// selectedRowInTbl = the index of the selected row
						selectedRowInTbl = tblEmployees.getSelectedRow();
						lbEmpID.setText( tableModel.getValueAt( selectedRowInTbl, 0 ).toString() );
						txFname.setText( tableModel.getValueAt( selectedRowInTbl, 1 ).toString() );
						txMname.setText( tableModel.getValueAt( selectedRowInTbl, 2 ).toString() );
						txLname.setText( tableModel.getValueAt( selectedRowInTbl, 3 ).toString() );
						txAge.setText( tableModel.getValueAt( selectedRowInTbl, 4 ).toString() );
						if ( record[5].equals("Male") ) {  empGender = tableModel.getValueAt( selectedRowInTbl, 5 ).toString();  cmbxGender.setSelectedItem(empGender);  }
						if ( record[5].equals("Female") ) {  empGender = tableModel.getValueAt( selectedRowInTbl, 5 ).toString();  cmbxGender.setSelectedItem(empGender);  }
						txAddress.setText( tableModel.getValueAt( selectedRowInTbl, 6 ).toString() );
						disableAllInputFields();
						btnCreate.setEnabled(false);
						btnUpdate.setEnabled(true);
						btnDelete.setEnabled(true);
						functionIsReadOrSearch = false;
						btnRead.setText("Clear");
					}
					else
					{
						enableAllInputFields();
						cmbxGender.setEnabled(true);
						emptyInputFields();
						functionIsReadOrSearch = true;
						btnRead.setText("Search");
					}
				}
			} );/* Table setup ends. */
			
			// Begin component layouting, starting with employee ID number label
			pnlID.add(BorderLayout.WEST, lbIDnum); 								pnlID.add(BorderLayout.CENTER, spaceLblTxtID); 			pnlID.add(BorderLayout.EAST, lbEmpID);
			// Date/Time label
			pnlDateTime.add(BorderLayout.CENTER, spaceIDDT); 					pnlDateTime.add(BorderLayout.EAST, lbTime);
			// Header is where the employee ID number and date/time appear
			pnlHeader.add(BorderLayout.WEST, pnlID); 							pnlHeader.add(BorderLayout.EAST, pnlDateTime);
			// First Name label/textfield
			pnlFname.add(BorderLayout.WEST, lbFname); 							pnlFname.add(BorderLayout.CENTER, txFname);
			// Middle Name label/textfield
			pnlMname.add(BorderLayout.WEST, lbMname); 							pnlMname.add(BorderLayout.CENTER, txMname);
			// Last Name label/textfield
			pnlLname.add(BorderLayout.WEST, lbLname); 							pnlLname.add(BorderLayout.CENTER, txLname);
			// Name is where the full name - the first/middle/last name - appear
			pnlName.add(BorderLayout.WEST, pnlFname); 							pnlName.add(BorderLayout.CENTER, pnlMname); 			pnlName.add(BorderLayout.EAST, pnlLname);
			// Age label/textfield
			pnlAge.add(BorderLayout.WEST, lbAge); 								pnlAge.add(BorderLayout.CENTER, txAge);
			// Gender combo box drop-down
			pnlGender.add(BorderLayout.WEST, lbGender); 						pnlGender.add(BorderLayout.CENTER, cmbxGender);
			// Address label/textfield
			pnlAddress.add(BorderLayout.WEST, lbAddress); 						pnlAddress.add(BorderLayout.CENTER, txAddress);
			// Other Info panel is where the age, gender, address appear
			pnlOtherInf.add(BorderLayout.WEST, pnlAge); 						pnlOtherInf.add(BorderLayout.CENTER, pnlGender); 		pnlOtherInf.add(BorderLayout.EAST, pnlAddress);
			// Input Fields panel is where the full name, age, gender, address appear
			pnlInputFields.add(BorderLayout.NORTH, pnlName);
			pnlInputFields.add(BorderLayout.CENTER, pnlOtherInf);
			// JTable for Employee records List
			pnlEmpList.add(BorderLayout.CENTER, scrollablePane);
			// pnlTextTableFields is the Panel showing Input Fields and Employee JTable
			pnlTextTableFields.add(BorderLayout.NORTH, pnlInputFields);
			pnlTextTableFields.add(BorderLayout.CENTER, pnlEmpList);
			// pnlModify is the Panel showing buttons such as Update, Search/Read, Delete
			pnlModify.add(BorderLayout.WEST, btnUpdate);
			pnlModify.add(BorderLayout.CENTER, btnRead);
			pnlModify.add(BorderLayout.EAST, btnDelete);
			// pnlButtons is the Panel showing Create button, Modify Panel, Close button
			pnlButtons.add(BorderLayout.WEST, btnCreate);
			pnlButtons.add(BorderLayout.CENTER, pnlModify);
			pnlButtons.add(BorderLayout.EAST, btnClose);
			// Footer is where the Create,Update,Search/Read,Delete,Close buttons appear
			pnlFooter.add(BorderLayout.CENTER, pnlButtons);
			// The Main Panel arranges the Header, Input Fields and Employee JTable, and Footer, all in a vertical sequence
			panelMain.add(BorderLayout.NORTH, pnlHeader);
			panelMain.add(BorderLayout.CENTER, pnlTextTableFields);
			panelMain.add(BorderLayout.SOUTH, pnlFooter);
			// This sums up the layouting part
			getContentPane().add(panelMain);
			
			// Create real-time ticking of clock
			Timer timer;
			timer = new Timer ( 1000, new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					lbTime.setText ( timeFormat.format( new Date( System.currentTimeMillis() ) ) ) ;
					pnlDateTime.setBorder ( BorderFactory.createTitledBorder( BorderFactory.createLoweredBevelBorder(), dateFormat.format( new Date( System.currentTimeMillis() ) ), 2, 0) ) ;
				}
			} ) ;
			timer.setRepeats(true);
			timer.start();
		}
		catch(CommunicationsException errorDBConnection) { System.out.println("ERROR: This application will now close because there is no connection to the database server."); System.exit(0); }
		catch(Exception error) { error.printStackTrace(); return; }
	}// END OF CONSTRUCTOR
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// actionPerformed is responsible for component interaction (ex., clicking the combo box / drop-down, buttons, etc.)
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent event)
	{
		try
		{
			Object source = event.getSource();
			// If Gender combo box drop-down is clicked,
			if (source == cmbxGender)
			{
				// Gender combo box drop-down becomes the currently active component
				activeComponent = cmbxGender;
				setSelectedGender();
			}
			// if (source == scrollablePane)
			// {
			// 	btnUpdate.setEnabled(true);
			// 	btnDelete.setEnabled(true);
			// 	btnUpdate.grabFocus();
			// }
			// If Create button is clicked after enabling it,
			if (source == btnCreate)
			{
				// Create button becomes the currently active component, and one (1) button is clicked
				activeComponent = btnCreate;
				buttonIsClicked = true;// _clickMeMode = true;
				sqlQuery = "SELECT * FROM `employees`";
				// Obtain the set of MySQL query results to be displayed in table later on
				sqlRS = sqlStmnt.executeQuery(sqlQuery);
				// While the employees table contains records,
				while ( sqlRS.next() ) {
					if ( lbEmpID.getText().equals(rowCountDB)) {
					// if ( lbEmpID.getText().equals( sqlRS.getString("emp_id")) ) {
						JOptionPane.showMessageDialog(null, "An employee with the same ID number already exists!", "Employee Search [JavaWookies Time Tracking System]", JOptionPane.WARNING_MESSAGE); // Warning dialog box
						return; }
					else {
						//lbEmpID.setText("-1");// This is to prevent "similar empIDnum found" from occurring
						if (   (  txFname.getText().equals( sqlRS.getString("emp_fname"))  &&  txLname.getText().equals( sqlRS.getString("emp_lname"))  )  ||
							(  txFname.getText().equals( sqlRS.getString("emp_fname"))  &&  txMname.getText().equals( sqlRS.getString("emp_mname"))  )
							)  {
							lbEmpID.setText("0");
							JOptionPane.showMessageDialog(null, "An employee with the same name already exists!", "Employee Search [JavaWookies Time Tracking System]", JOptionPane.WARNING_MESSAGE); // Warning dialog box
							txFname.grabFocus();
							txFname.selectAll();
							// The keyword return is used to escape from public void actionPerformed(ActionEvent event) function generally or Create button's save function specifically.
							return; }/* Get out of public void actionPerformed(ActionEvent event) function or Create button's save function */
						else {
							lbEmpID.setText("0");
						}
					}
				}
				// Prepare the text field details for saving into the database
				record[0] = rowCountDB;/* Employee ID number */ 				empIDnum = (int) record[0];
				record[1] = txFname.getText();// Given name
				record[2] = txMname.getText();/* Middle name */ 				empMI = (String) record[2]; empMI = empMI.substring(0, 1);// Middle initial
				record[3] = txLname.getText();// Family name
				record[4] = Integer.parseInt( txAge.getText() );/* Age */ 		empAge = (int) record[4];
				record[5] = (String) cmbxGender.getSelectedItem();// Gender
				record[6] = txAddress.getText();
				// Prepare the SQL statement for record creation/insertion
				sqlUpdate = "INSERT INTO `employees` (`emp_id`, `emp_fname`, `emp_mname`, `emp_lname`, `emp_age`, `emp_gender`, `emp_address`) VALUES ('"+(int)record[0]+"', '"+(String)record[1]+"', '"+(String)record[2]+"', '"+(String)record[3]+"', '"+(int)record[4]+"', '"+(String)record[5]+"', '"+(String)record[6]+"')";
				//sqlUpdate = "INSERT INTO `employees` (`emp_id`, `emp_fname`, `emp_mname`, `emp_lname`, `emp_age`, `emp_gender`, `emp_address`) VALUES ('"+Integer.parseInt(lbEmpID.getText())+"', '"+txFname.getText()+"', '"+txMname.getText()+"', '"+txLname.getText()+"', '"+Integer.parseInt(txAge.getText())+"', '"+(String)cmbxGender.getSelectedItem()+"', '"+txAddress.getText()+"')";
				// Insert into the employees table columns the textfield values
				lastRow = sqlStmnt.executeUpdate(sqlUpdate);
				// Show the newly saved record in JTable
				tableModel.addRow(record);
				// Update the employee ID number shown at the upper-left corner back to zero (0)
				lbEmpID.setText( Integer.toString( (int) record[0] ) );
				JOptionPane.showMessageDialog(null, "New employee has been added successfully!", "Employee Search [JavaWookies Time Tracking System]", JOptionPane.INFORMATION_MESSAGE); // Info dialog box
				//JOptionPane.showMessageDialog(null, "Middle Initial is "+empMI+", Age is "+empAge+", and Gender is "+txGender.getText()+" or "+empMorF+".\n\nNew employee has been added successfully!", "Employee Search [JavaWookies Time Tracking System]", JOptionPane.INFORMATION_MESSAGE); // Info dialog box
				btnUpdate.setEnabled(true);
				btnDelete.setEnabled(true);
				emptyInputFields();
				lbEmpID.setText("0");
				sqlRS.close();
			}
			// If Search button is clicked after enabling it,
			if (source == btnRead)
			{
				if (functionIsReadOrSearch)
				{
					// Create button becomes the currently active component, and one (1) button is clicked
					activeComponent = btnRead;
					buttonIsClicked = true;// _clickMeMode = true;
					enableAllInputFields();
					// Select all from employees table where first name column's value is equal to first name textfield's value, etc.,..
					sqlQuery = "SELECT * FROM `employees` WHERE `emp_fname` = \""+txFname.getText()+"\" OR `emp_mname` = \""+txMname.getText()+"\" OR `emp_lname` = \""+txLname.getText()+"\" OR `emp_age` = '"+txAge.getText()+"' OR `emp_gender` = \""+(String)cmbxGender.getSelectedItem()+"\" OR `emp_address` = \""+txAddress.getText()+"\"";
					// Obtain the set of MySQL query results to be displayed in table later on
					sqlRS = sqlStmnt.executeQuery(sqlQuery);
					// While JTable has employee records shown even before Search button is clicked,
					while ( tableModel.getRowCount() > 0 ) {
						// Remove the last record/row in JTable so that only the latest employee result(s) will show up
						tableModel.removeRow( tableModel.getRowCount() - 1 );
					}
					// While there are records in DataBase Table / DataBase Table is not empty,
					while ( sqlRS.next() ) {
						record[0] = sqlRS.getInt("emp_id");/* Employee ID number */lbEmpID.setText(  Integer.toString( (int) record[0] )  );
						record[1] = sqlRS.getString("emp_fname");// Given name
						record[2] = sqlRS.getString("emp_mname");/* Middle name */empMI = (String) record[2]; empMI = empMI.substring(0, 1);// Middle initial *//*empMI = sqlRS.getString("emp_mname").substring(0, 1);// Middle initial */
						record[3] = sqlRS.getString("emp_lname");// Family name
						record[4] = sqlRS.getInt("emp_age");/* Age */empAge = (int) record[4];
						record[5] = sqlRS.getString("emp_gender");
						record[6] = sqlRS.getString("emp_address");// Address
						tableModel.addRow(record);
						rowCountTbl = tableModel.getRowCount();
						if ( rowCountTbl <= 0 ) {
							emptyInputFields();
							btnRead.setText("Search");
							return;
						}
						btnCreate.setEnabled(false);
						btnUpdate.setEnabled(true);
						btnDelete.setEnabled(true);
						//btnRead.setText("Clear");
					}
					functionIsReadOrSearch = false;
					btnRead.setText("Clear");
				}
				else
				{
					functionIsReadOrSearch = true;
					activeComponent = null;
					buttonIsClicked = true;// _clickMeMode = true;
					enableAllInputFields();
					emptyInputFields();
					btnCreate.setEnabled(false);
					btnUpdate.setEnabled(false);
					btnDelete.setEnabled(false);
					while ( sqlRS.next() ) {
						record[0] = sqlRS.getInt("emp_id");/* Employee ID number */lbEmpID.setText(  Integer.toString( (int) record[0] )  );
						record[1] = sqlRS.getString("emp_fname");// Given name
						record[2] = sqlRS.getString("emp_mname");/* Middle name */empMI = (String) record[2]; empMI = empMI.substring(0, 1);// Middle initial *//*empMI = sqlRS.getString("emp_mname").substring(0, 1);// Middle initial */
						record[3] = sqlRS.getString("emp_lname");// Family name
						record[4] = sqlRS.getInt("emp_age");/* Age */empAge = (int) record[4];
						record[5] = sqlRS.getString("emp_gender");
						record[6] = sqlRS.getString("emp_address");// Address
						tableModel.addRow(record);
						btnCreate.setEnabled(false);
						btnUpdate.setEnabled(false);
						btnDelete.setEnabled(false);
						//btnRead.setText("Clear");
					}
					btnRead.setText("Search");
				}
				//sqlRS.close();
			}
			// If Update button is clicked after enabling it,
			if (source == btnUpdate)
			{
				activeComponent = btnUpdate;
				buttonIsClicked = true;
				JOptionPane.showMessageDialog(null, "Coming soon...", "Employee Search [JavaWookies Time Tracking System]", JOptionPane.INFORMATION_MESSAGE); // Info dialog box
			}
			// If Delete button is clicked after enabling it,
			if (source == btnDelete)
			{
				activeComponent = btnDelete;
				buttonIsClicked = true;
				int i = tblEmployees.getSelectedRow();
				// If the table is empty or the user did not select any existing record...
				if (i < 0) {
					JOptionPane.showMessageDialog(null, "There are no (more) employee records to delete!\n\nIf applicable, select a record to proceed.\n\n", "Employee Search [JavaWookies Time Tracking System]", JOptionPane.ERROR_MESSAGE);/* Error dialog box */
					emptyInputFields(); }
				else {
					// If table contains at least one (1) record,
					tableModel.removeRow(i);
					JOptionPane.showMessageDialog(null, "Selected employee record has been deleted!", "Employee Search [JavaWookies Time Tracking System]", JOptionPane.INFORMATION_MESSAGE); // Info dialog box
					emptyInputFields();
				}
			}
			// If Close buttton is clicked, this page/window will be disposed to make room for Employee.java
			if (source == btnClose)
			{
				activeComponent = btnClose;
				buttonIsClicked = true;
				Employee eui = new Employee();
				eui.pack();
				eui.setLocationRelativeTo(null); // Set the frame to appear at the center of the screen
				eui.setResizable(false);
				eui.setVisible(true);
				dispose();
				buttonIsClicked = false;
			}
			if (buttonIsClicked) {
				buttonIsClicked = false;
			}
		}
		catch(NumberFormatException errorNumberFormat) {
			JOptionPane.showMessageDialog(null, "Age must consist of numbers only.\n\nPlease check the information that you entered.\n\n", "Employee Search [JavaWookies Time Tracking System]", JOptionPane.ERROR_MESSAGE);/* Error dialog box */
			txAge.grabFocus();
			txAge.selectAll();
			return;
		}
		catch(SQLIntegrityConstraintViolationException errorMySQLIntegrity) {
			// JOptionPane.showMessageDialog(null, "The employee ID number about to be registered already exists in the system!", "Employee Search [JavaWookies Time Tracking System]", JOptionPane.ERROR_MESSAGE);/* Error dialog box */
			// lbEmpID.setText( Integer.toString(currentRowCount) );
			// txFname.grabFocus();
			// txFname.selectAll();
			errorMySQLIntegrity.printStackTrace();
			return;
		}
		catch(Exception error) { error.printStackTrace(); return; }
	}
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/* This code block is related to the DocumentListener interface implementation,
	 * which is responsible for updating the clickability of Create button in real time
	 * once this page/window detects any empty input fields. */
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override public void insertUpdate(DocumentEvent e) {
		updateButtonEnabledStatus(btnCreate, textFields);
	}
	@Override public void removeUpdate(DocumentEvent e) {
		updateButtonEnabledStatus(btnCreate, textFields);
	}
	@Override public void changedUpdate(DocumentEvent e) {
		updateButtonEnabledStatus(btnCreate, textFields);
	}
	private void updateButtonEnabledStatus(JButton bttn, List<JTextField> fields) {
		boolean enabled = true;
		for (JTextField field : fields) {
			if ( field.getText().length() <= 0 ) {
				enabled = false;
				break;
			}
		}
		bttn.setEnabled(enabled);
	}/* This ends the DocumentListener interface implementation. */
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////// HELPER FUNCTIONS|METHODS //////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Disable input fields
	private void disableAllInputFields()
	{
		txFname.setEditable(false);
		txMname.setEditable(false);
		txLname.setEditable(false);
		txAge.setEditable(false);
		cmbxGender.setEnabled(false);
		txAddress.setEditable(false);
	}
	// Create function to clear/reset all input fields
	private void emptyInputFields()
	{
		txAddress.setText("");
		cmbxGender.setSelectedIndex(0);
		txAge.setText("");
		txLname.setText("");
		txMname.setText("");
		txFname.setText("");
		txFname.grabFocus();
		txFname.selectAll();
	}
	// Enable input fields
	private void enableAllInputFields()
	{
		txFname.setEditable(true);
		txMname.setEditable(true);
		txLname.setEditable(true);
		txAge.setEditable(true);
		cmbxGender.setEnabled(true);
		txAddress.setEditable(true);
	}
	// Retrieve data from DataBase Table
	private void retrieveRecordsFromDBTable()
	{
		try {
			sqlQuery = "SELECT * FROM `employees` WHERE `emp_id` > 0";
			sqlRS = sqlStmnt.executeQuery(sqlQuery);
			while ( sqlRS.next() ) {
				record[0] = sqlRS.getInt("emp_id");/* Employee ID number */lbEmpID.setText( Integer.toString( (int) record[0] ) );
				record[1] = sqlRS.getString("emp_fname");// Given name
				record[2] = sqlRS.getString("emp_mname");/* Middle name */empMI = (String) record[2]; empMI = empMI.substring(0, 1);// Middle initial *//*empMI = sqlRS.getString("emp_mname").substring(0, 1);// Middle initial */
				record[3] = sqlRS.getString("emp_lname");// Family name
				record[4] = sqlRS.getInt("emp_age");/* Age */empAge = (int) record[4];
				record[5] = sqlRS.getString("emp_gender");/* Gender */
				record[6] = sqlRS.getString("emp_address");/* Address */
				tableModel.addRow(record);
			}
			sqlRS = sqlStmnt.executeQuery("SELECT COUNT(*) FROM `employees`");// Check the current row count for Time Tracker DB > Employees Table
			while ( sqlRS.next() ) {
				rowCountDB = sqlRS.getInt(1);
			}
			JOptionPane.showMessageDialog(null, "The current row count for DBTable is:\n"+rowCountDB, "Employee Search [JavaWookies Time Tracking System]", JOptionPane.INFORMATION_MESSAGE); // Info dialog box
			rowCountTbl = tableModel.getRowCount();
			JOptionPane.showMessageDialog(null, "The current row count for JTable is:\n"+rowCountTbl, "Employee Search [JavaWookies Time Tracking System]", JOptionPane.INFORMATION_MESSAGE); // Info dialog box
			// If there's no Admin account in DB Table
			if (rowCountDB <= 0/* && rowCountTbl <= 0*/) {
				System.exit(0); }
			// If only the Admin account exists in DB Table and there are no employee records yet
			else if (rowCountDB == 1) {
				empIDnum = rowCountDB - 1;
				lbEmpID.setText( Integer.toString(empIDnum) ); }
			else /* If at least one (1) or more employee records are saved in Database Table */ {
				empIDnum = 0;
				lbEmpID.setText( Integer.toString(empIDnum) );
			}
		}
		catch(SQLException errorMySQL) { errorMySQL.printStackTrace(); }
	}
	// Retrieve data from JTable
	private void retrieveRecordsFromJTable()
	{
		//
	}
	private void setSelectedGender()
	{
		// temporary stuff
		txGender.setText ( (String) cmbxGender.getSelectedItem() ) ;
		// temp stuff
		empGender = (String) cmbxGender.getSelectedItem();
		// temp
		if ( txGender.getText().isEmpty() || empGender.isEmpty() ) {
			cmbxGender.grabFocus();
			return; }
		else {
			// Gender is shortened (M or F)
			empMorF = empGender.substring(0, 1);
			//temp
			txGender.setText(empMorF);
			// Transfer focus to Address field after selecting gender
			txAddress.grabFocus();
		}
	}
	// Create function to disable clicking of buttons that are not needed
	private void toggleUpdateDeleteOffOn()
	{
		if (activeComponent == btnCreate) {
			btnUpdate.setEnabled(false);
			btnDelete.setEnabled(false);
		}
		if (activeComponent == scrollablePane) {
			btnUpdate.setEnabled(true);
			btnDelete.setEnabled(true);
		}
	}
	// private void storeDBTableData()
	// {
	// 	try {
	// 		sqlQuery = "SELECT * FROM `employees` WHERE `emp_id` > 0";
	// 		sqlRS = sqlStmnt.executeQuery(sqlQuery);
	// 		while ( sqlRS.next() ) {
	// 			record[0] = sqlRS.getInt("emp_id"); 			lbEmpID.setText( Integer.toString( (int) record[0] ) );
	// 			record[1] = sqlRS.getString("emp_fname");
	// 			record[2] = sqlRS.getString("emp_mname"); 		empMI = (String) record[2]; 	empMI = empMI.substring(0, 1);
	// 			record[3] = sqlRS.getString("emp_lname");
	// 			record[4] = sqlRS.getInt("emp_age"); 			empAge = (int) record[4];
	// 			record[5] = sqlRS.getString("emp_gender");
	// 			record[6] = sqlRS.getString("emp_address");
	// 			tableModel.addRow(record);
	// 		}
	// 		sqlRS = sqlStmnt.executeQuery("SELECT COUNT(*) FROM `employees`");// Check the current row count for Time Tracker DB > Employees Table
	// 		while ( sqlRS.next() ) {
	// 			rowCountDB = sqlRS.getInt(1);
	// 		}
	// 	}
	// 	catch(SQLException errorMySQL) { errorMySQL.printStackTrace(); }
	// }
	// private void storeJTableData()
	// {
	// 	//
	// }
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/* THE MAIN ENTRY POINT FOR Employees.java PAGE/WINDOW */
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args)
	{
		try
		{
			Employees myEmployeesFrame = new Employees();
			myEmployeesFrame.setTitle("Employees [JavaWookies Time Tracking System]");// Set frame's title to:
			myEmployeesFrame.setPreferredSize( new Dimension(910, 350) );
			myEmployeesFrame.setResizable(false);// Disable the manual resizing of frame
			// this code lets you close the window
			WindowListener myWindowListener = new WindowAdapter() {
				/* the instance myWindowListener is extended to include the ff. code: //*/
				public void windowClosing(WindowEvent e) {
					myEmployeesFrame.dispose();
					//System.exit(0);
				}
			};
			myEmployeesFrame.addWindowListener(myWindowListener);
			myEmployeesFrame.pack();
			myEmployeesFrame.setVisible(true);// show frame
		}
		catch(Exception e) { e.printStackTrace(); return; }
	}
}
