import java.awt.*;
import java.awt.event.*;
import java.io.*;// input/output or read/write stream
import java.sql.*;// mysql jdbc
import java.text.SimpleDateFormat;// date/time display format
import java.util.Date;// date/time
import java.util.List;// part of DocumentListener interface implementation
import java.util.Vector;// part of DocumentListener interface implementation
import javax.sql.*;// mysql jdbc
import javax.swing.*;// swing ui
import javax.swing.event.DocumentEvent;// part of DocumentListener interface implementation
import javax.swing.event.DocumentListener;// part of DocumentListener interface implementation
import javax.swing.table.DefaultTableModel;// part of tblEmployees

public class Employees extends JFrame implements ActionListener, DocumentListener
{
	Object[] record;
	Object componentInQuestion = null;
	SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
	SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
	JPanel panelMain, pnlHeader, pnlDateTime, pnlID, pnlTextTableFields, pnlInputFields, pnlName, pnlFname, pnlMname, pnlLname, pnlOtherInf, pnlAge, pnlGender, pnlAddress, pnlEmpList, pnlFooter, pnlButtons, pnlModify;
	JLabel lbDateTime, lbDate, lbTime, lbIDnum, lbEmpID, lbNamefull, lbFname, lbMname, lbLname, lbAge, lbGender, lbAddress, spaceIDDT, spaceLblTxtID, gapTblBtn;
	JTextField txFname, txMname, txLname, txAge, txGender, txAddress;
	JComboBox<String> cmbxGender;
	DefaultTableModel tableModel;
	JTable tblEmployees;
	JScrollPane scrollablePane;
	JButton btnCreate, btnRead, btnUpdate, btnDelete, btnClose;
	List<JTextField> textFields = null;// This line of code is part of the DocumentListener interface implementation, which will be discussed in later comments.
	Font colFont;
	String[] genders = { "", "Male", "Female" }, labels = { "Employee ID", "Given Name", "Middle Name", "Family Name", "Age", "Gender", "Address" };
	String empMI, empGender, empMorF;
	int empIDnum = 0, empAge = 0;
	private boolean buttonIsClicked = false;// a.k.a. _clickMeMode

	Employees()
	{
		try
		{
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
			// Create spaces/gaps
			spaceIDDT = new JLabel("     ");
			spaceLblTxtID = new JLabel("   ");
			gapTblBtn = new JLabel("                                                                                                                                                                                                                                                                                         ");
			// Create labels and textfields
			lbDate = new JLabel ( dateFormat.format( new Date() ) ) ;
			lbTime = new JLabel ( timeFormat.format( new Date() ) ) ;
			lbIDnum = new JLabel("Employee ID:"); lbEmpID = new JLabel( Integer.toString(empIDnum) );
			lbFname = new JLabel("Given Name");
			txFname = new JTextField(30); txFname.getDocument().addDocumentListener(this);
			lbMname = new JLabel("Middle Name");
			txMname = new JTextField(20); txMname.getDocument().addDocumentListener(this);
			lbLname = new JLabel("Family Name");
			txLname = new JTextField(20); txLname.getDocument().addDocumentListener(this);
			lbAge = new JLabel("Age");
			txAge = new JTextField(3); txAge.getDocument().addDocumentListener(this);
			lbGender = new JLabel("Gender");
			// Create Gender combo box or drop-down
			cmbxGender = new JComboBox<>(genders); cmbxGender.setSelectedIndex(0); cmbxGender.addActionListener(this);
			// Continue creating labels and textfields
			txGender = new JTextField(6); txGender.getDocument().addDocumentListener(this);/* txGender.setText ( (String) cmbxGender.getSelectedItem() );//*/
			lbAddress = new JLabel("Address");
			txAddress = new JTextField(50); txAddress.getDocument().addDocumentListener(this);
				/* NOTE: The line of code below is related to the DocumentListener interface implementation. */
			textFields = new Vector<>(); textFields.add(txFname); textFields.add(txMname); textFields.add(txLname); textFields.add(txAge); textFields.add(txGender); textFields.add(txAddress);
				/* The List<> variable textFields adds all the text fields for checking whether one of them is empty. */
			
			/* Table is set up. */
			tableModel = new DefaultTableModel();
			tblEmployees = new JTable();
			tableModel.setColumnIdentifiers(labels);
			tblEmployees.setModel(tableModel);
			tblEmployees.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			tblEmployees.getTableHeader().setReorderingAllowed(false);
			scrollablePane = new JScrollPane(tblEmployees);
			scrollablePane.setSize(990, 50);
			record = new Object[7];/* Table setup ends. */
			
			// Create buttons
			btnCreate = new JButton("Create"); btnCreate.setEnabled(false); btnCreate.addActionListener(this);
			btnRead = new JButton("Search"); btnRead.setEnabled(true); btnRead.addActionListener(this);
			btnUpdate = new JButton("Update"); btnUpdate.setEnabled(false); btnUpdate.addActionListener(this);
			btnDelete = new JButton("Delete"); btnDelete.setEnabled(false); btnDelete.addActionListener(this);
			btnClose = new JButton("Close"); btnClose.setEnabled(true); btnClose.addActionListener(this);
			
			// Start layouting the components
			pnlID.add(BorderLayout.WEST, lbIDnum); 							pnlID.add(BorderLayout.CENTER, spaceLblTxtID); 			pnlID.add(BorderLayout.EAST, lbEmpID);
			pnlDateTime.add(BorderLayout.CENTER, spaceIDDT); 				pnlDateTime.add(BorderLayout.EAST, lbTime);
			pnlHeader.add(BorderLayout.WEST, pnlID);
			pnlHeader.add(BorderLayout.EAST, pnlDateTime);
			pnlFname.add(BorderLayout.WEST, lbFname); 						pnlFname.add(BorderLayout.CENTER, txFname);
			pnlMname.add(BorderLayout.WEST, lbMname); 						pnlMname.add(BorderLayout.CENTER, txMname);
			pnlLname.add(BorderLayout.WEST, lbLname); 						pnlLname.add(BorderLayout.CENTER, txLname);
			pnlName.add(BorderLayout.WEST, pnlFname); 						pnlName.add(BorderLayout.CENTER, pnlMname); 			pnlName.add(BorderLayout.EAST, pnlLname);
			pnlAge.add(BorderLayout.WEST, lbAge); 							pnlAge.add(BorderLayout.CENTER, txAge);
			pnlGender.add(BorderLayout.WEST, lbGender); 					pnlGender.add(BorderLayout.CENTER, cmbxGender);
			pnlAddress.add(BorderLayout.WEST, lbAddress); 					pnlAddress.add(BorderLayout.CENTER, txAddress);
			pnlOtherInf.add(BorderLayout.WEST, pnlAge); 					pnlOtherInf.add(BorderLayout.CENTER, pnlGender); 		pnlOtherInf.add(BorderLayout.EAST, pnlAddress);
			pnlInputFields.add(BorderLayout.NORTH, pnlName);
			pnlInputFields.add(BorderLayout.CENTER, pnlOtherInf);
			pnlEmpList.add(BorderLayout.CENTER, scrollablePane);
			pnlTextTableFields.add(BorderLayout.NORTH, pnlInputFields);
			pnlTextTableFields.add(BorderLayout.CENTER, pnlEmpList);
			pnlModify.add(BorderLayout.WEST, btnUpdate);
			pnlModify.add(BorderLayout.CENTER, btnRead);
			pnlModify.add(BorderLayout.EAST, btnDelete);
			pnlButtons.add(BorderLayout.WEST, btnCreate);
			pnlButtons.add(BorderLayout.CENTER, pnlModify);
			pnlButtons.add(BorderLayout.EAST, btnClose);
			pnlFooter.add(BorderLayout.CENTER, pnlButtons);
			panelMain.add(BorderLayout.NORTH, pnlHeader);
			panelMain.add(BorderLayout.CENTER, pnlTextTableFields);
			panelMain.add(BorderLayout.SOUTH, pnlFooter);
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
		catch(Exception error) { error.printStackTrace(); return; }
	}
	
	// actionPerformed is responsible for component interaction (ex., clicking the combo box / drop-down, buttons, etc.)
	public void actionPerformed(ActionEvent event)
	{
		try
		{
			Object source = event.getSource();
			// If Gender combo box drop-down is clicked,
			if (source == cmbxGender) {
				txGender.setText ( (String) cmbxGender.getSelectedItem() ) ;
				empGender = (String) cmbxGender.getSelectedItem();
				if ( txGender.getText().isEmpty() || empGender.isEmpty() ) {
					// the keyword return is used to escape from Create button's save function.
					cmbxGender.grabFocus();
					return; }
				else {
					empMorF = empGender.substring(0, 1);
					txGender.setText(empMorF) ;
					txAddress.grabFocus();
				}
			}
			if (source == scrollablePane) {
				btnUpdate.setEnabled(true);
				btnDelete.setEnabled(true);
				btnUpdate.grabFocus();
			}
			// If Create button is clicked after enabling it,
			if (source == btnCreate) {
				buttonIsClicked = true;
				record[0] = Integer.toString(empIDnum+1);
				record[1] = txFname.getText();// Given name
				record[2] = txMname.getText();// Middle name
				empMI = txMname.getText().substring(0, 1);// Middle initial
				record[3] = txLname.getText();// Family name
				empAge = Integer.parseInt( txAge.getText() );
				record[4] = (int) empAge;// Age
				record[5] = (String) cmbxGender.getSelectedItem();
				record[6] = txAddress.getText();
				lbEmpID.setText( (String) record[0] );
				empIDnum = Integer.parseInt( lbEmpID.getText() );
				tableModel.addRow(record);
				JOptionPane.showMessageDialog(null, "Middle Initial is " + empMI + ", Age is " + empAge + ", and Gender is " + empMorF + ".\n\nNew employee has been added successfully!", "Employee Search [JavaWookies Time Tracking System]", JOptionPane.INFORMATION_MESSAGE); // Info dialog box
				btnUpdate.setEnabled(true);
				btnDelete.setEnabled(true);
				emptyInputFields();
			}
			// If Search button is clicked after enabling it,
			if (source == btnRead) {
				buttonIsClicked = true;
				JOptionPane.showMessageDialog(null, "Coming soon...", "Employee Search [JavaWookies Time Tracking System]", JOptionPane.INFORMATION_MESSAGE); // Info dialog box
			}
			// If Update button is clicked after enabling it,
			if (source == btnUpdate) {
				buttonIsClicked = true;
				JOptionPane.showMessageDialog(null, "Coming soon...", "Employee Search [JavaWookies Time Tracking System]", JOptionPane.INFORMATION_MESSAGE); // Info dialog box
			}
			// If Delete button is clicked after enabling it,
			if (source == btnDelete) {
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
			if (source == btnClose) {
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
		catch(Exception error) { error.printStackTrace(); return; }
	}
	
	/* This code block is related to the DocumentListener interface implementation,
	 * which is responsible for updating the clickability of buttons in real time
	 * once this employee search form detects any empty input fields. */
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
	// Create function to disable clicking of buttons that are not needed
	private void disableUpdateDelete()
	{
		btnDelete.setEnabled(false);
		btnUpdate.setEnabled(false);
	}
	
	/* THE MAIN ENTRY POINT FOR Employees.java PAGE/WINDOW */
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