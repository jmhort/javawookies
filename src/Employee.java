import java.awt.Font;
import com.mysql.cj.jdbc.exceptions.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JTable; 

public class Employee extends JFrame implements ActionListener, MouseListener
{
	JLabel lblWelcome, lblName, lblID, lblAge, lblGender, lblAddress, lblDate, lblTime, lblGap1, lblGap2, lblCDgap;
	JButton buttonExit, buttonIn, buttonOut, buttonDelete;
	JPanel panelMain, panelWE, panelNIA, panelGPA, panelCC, panelDTT, panelWNG, panelCD, panelWC, panelDT, panelLoweredBevelBorder, panelDelete; //PANELS: WE-Welcome/Exit, NIA-Name/ID/AGE, GPA-Gender/Phone/Age, CC-Clock/out, WNG-WE/NIA/GPA, CD-CC/Jlist, WC-WNG/CD, DT-Date/Time
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("MMM. dd, yyyy"), sqldateFormat = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a"), sqltimeFormat = new SimpleDateFormat("HH:mm:ss");
	java.sql.Date sqlDate;

	DefaultTableModel tableModel = new DefaultTableModel();;
	JTable tableLogtime = new JTable(tableModel);
	String[] columnNames = {"ID Number", "Date In", "Time In", "Date Out", "Time Out"}, record = new String[5];
	String stampDate, stampTime, datein, timein, idnum;
	
	Connection dbConn;
	Statement sqlStmnt;
	PreparedStatement ps;
	String sqlQuery;
	ResultSet sqlRS;

	Employee(String nmbr)
	{	buttonExit = new JButton("Log out");
		buttonExit.addActionListener(this);
		buttonIn = new JButton("Clock in");
		buttonIn.addActionListener(this);
		buttonIn.addMouseListener(this);
		buttonOut = new JButton("Clock out");
		buttonOut.setEnabled(false);
		buttonOut.addActionListener(this);
		buttonOut.addMouseListener(this);
		buttonDelete = new JButton("Delete");
		buttonDelete.addActionListener(this);
		buttonDelete.setEnabled(false);
		idnum = nmbr;
						
		try
		{	dbConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/timetracker", "root", "");
			sqlStmnt = dbConn.createStatement();
			sqlQuery = "SELECT * FROM employees WHERE emp_id = '" + nmbr + "'";
			sqlRS = sqlStmnt.executeQuery(sqlQuery);
			sqlRS.first();
		
			lblWelcome = new JLabel ("         *  Hi, " + sqlRS.getString("emp_fname") + "!  *");
			lblWelcome.setFont(new Font("Arial Bold", Font.ITALIC,14));
			lblWelcome.setForeground(Color.BLUE);
			lblName = new JLabel (" Name: " + sqlRS.getString("emp_fname") + " " + sqlRS.getString("emp_mname") + " " + sqlRS.getString("emp_lname"));
			lblID = new JLabel (" ID#: " + sqlRS.getString("emp_id"));
			lblAge = new JLabel (" Age: " + sqlRS.getString("emp_age"));
			lblGender = new JLabel (" Gender: " + sqlRS.getString("emp_gender"));
			lblAddress = new JLabel (" Address: " + sqlRS.getString("emp_address"));
			
			sqlQuery = "SELECT * FROM timelogs WHERE emp_id = '" + nmbr + "' ORDER BY date_in DESC, time_in DESC";
			sqlRS = sqlStmnt.executeQuery(sqlQuery);
			tableModel.setColumnIdentifiers(columnNames);
			tableLogtime.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			tableLogtime.getTableHeader().setReorderingAllowed(false);
			tableLogtime.setDefaultEditor(Object.class, null); // making the table uneditable
			tableLogtime.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			while(sqlRS.next())
			{	record[0] = sqlRS.getString("emp_id");
				record[1] = dateFormat.format(sqlRS.getDate("date_in"));
				record[2] = timeFormat.format(sqlRS.getTime("time_in"));
				
				if(sqlRS.getString("date_out").equals("0000-00-00"))
				{	buttonIn.setEnabled(false);
					buttonOut.setEnabled(true);
					record[3] = "";
					record[4] = "";
				}
				else
				{	record[3] = dateFormat.format(sqlRS.getDate("date_out"));
					record[4] = timeFormat.format(sqlRS.getTime("time_out"));
				}
				
				tableModel.addRow(record);				
			}
			if(sqlRS.first())
			{	datein = sqlRS.getString("date_in");
				timein = sqlRS.getString("time_in");
				buttonDelete.setEnabled(true);
			}
		}
		catch(Exception error) { error.printStackTrace(); return; }
		
		lblTime = new JLabel(timeFormat.format(new Date()));
		lblTime.setFont(new Font("Arial",Font.BOLD,18));
		lblGap1 = new JLabel ("      ");
		lblGap2 = new JLabel ("      ");
		lblCDgap  = new JLabel(" ");

		panelMain = new JPanel();
		panelWE = new JPanel();
		panelNIA = new JPanel();
		panelGPA = new JPanel();
		panelDT = new JPanel();
		panelCC = new JPanel();
		panelWNG = new JPanel();
		panelCD = new JPanel();
		panelWC = new JPanel();
		panelLoweredBevelBorder = new JPanel();
		panelDelete = new JPanel();

		panelMain.setLayout(new BorderLayout(1,1)); panelMain.setBackground(Color.lightGray);
		panelWE.setLayout(new BorderLayout(1,1)); panelWE.setBackground(Color.lightGray);
		panelNIA.setLayout(new BorderLayout(1,1)); panelNIA.setBackground(Color.lightGray);
		panelGPA.setLayout(new BorderLayout(1,1)); panelGPA.setBackground(Color.lightGray);
		panelDT.setLayout(new BorderLayout(30,1)); panelDT.setBackground(Color.lightGray);
		panelCC.setLayout(new BorderLayout(120,1)); panelCC.setBackground(Color.lightGray);
		panelWNG.setLayout(new BorderLayout(1,1)); panelWNG.setBackground(Color.lightGray);
		panelCD.setLayout(new BorderLayout(1,1)); panelCD.setBackground(Color.lightGray);
		panelWC.setLayout(new BorderLayout(1,1)); panelWC.setBackground(Color.lightGray);
		panelLoweredBevelBorder.setLayout(new BorderLayout(1,1)); panelLoweredBevelBorder.setBackground(Color.lightGray);
		panelDelete.setLayout(new BorderLayout(1,1)); panelDelete.setBackground(Color.lightGray);
		
		panelWE.add(BorderLayout.WEST, lblWelcome);
		panelWE.add(BorderLayout.EAST, buttonExit);
		
		panelNIA.add(BorderLayout.NORTH, lblName);
		panelNIA.add(BorderLayout.CENTER, lblID);
		panelNIA.add(BorderLayout.SOUTH, lblAge);

		panelGPA.add(BorderLayout.NORTH, lblGender);
		panelGPA.add(BorderLayout.SOUTH, lblAddress);

		panelLoweredBevelBorder.add(BorderLayout.NORTH, panelNIA);
		panelLoweredBevelBorder.add(BorderLayout.SOUTH, panelGPA);
		panelLoweredBevelBorder.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),"",2,0));
		
		panelDT.add(BorderLayout.WEST, lblGap1);
		panelDT.add(BorderLayout.CENTER, lblTime);
		panelDT.add(BorderLayout.EAST, lblGap2);
		panelDT.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE,2),dateFormat.format(new Date()),1,0));

		panelCC.add(BorderLayout.WEST, buttonIn);
		panelCC.add(BorderLayout.CENTER, panelDT);
		panelCC.add(BorderLayout.EAST, buttonOut);
		
		panelWNG.add(BorderLayout.NORTH, panelWE);
		panelWNG.add(BorderLayout.SOUTH, panelLoweredBevelBorder);
				
		panelCD.add(BorderLayout.NORTH, lblCDgap);
		panelCD.add(BorderLayout.CENTER, panelCC);
		panelCD.add(BorderLayout.SOUTH, new JScrollPane(tableLogtime));
		
		panelDelete.add(BorderLayout.EAST, buttonDelete);

		panelWC.add(BorderLayout.NORTH, panelWNG);
		panelWC.add(BorderLayout.CENTER, panelCD);
		panelWC.add(BorderLayout.SOUTH, panelDelete);

		panelMain.add(BorderLayout.WEST, panelWC);
		panelMain.addMouseListener(this);

		getContentPane().add(panelMain);

		Timer timer;
		timer = new Timer(1000, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				lblTime.setText(timeFormat.format(new Date(System.currentTimeMillis())));
				panelDT.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE,2),dateFormat.format(new Date(System.currentTimeMillis())),1,0));
			}
		}
		);
		timer.setRepeats(true);
		timer.start();
		
		this.addWindowListener
		(	new WindowAdapter() 
			{	public void windowClosing(WindowEvent e)
				{	System.exit(0);		}
			}
		);
	}
	
	public void mouseClicked(MouseEvent e)
	{	tableLogtime.clearSelection();
		panelMain.requestFocusInWindow();
	}
	
	public void mouseExited(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent e) {}
	
	public void mouseReleased(MouseEvent e) {}
	
	public void mousePressed(MouseEvent e) {}

	public void actionPerformed(ActionEvent event)
	{	Object source = event.getSource();
		
		if(source == buttonExit)
		{	Login login = new Login();
			login.pack();
			login.setLocationRelativeTo(null);
			login.setResizable(false);
			login.setVisible(true);
			login.setTitle("JavaWookies Time Tracking System");
			dispose();
		}
		else if(source == buttonOut)
		{	stampDate = sqldateFormat.format(new Date(System.currentTimeMillis()));
			stampTime = sqltimeFormat.format(new Date(System.currentTimeMillis()));
			
			sqlQuery = "UPDATE timelogs SET date_out = ?, time_out = ? WHERE date_in = ? AND time_in = ? AND emp_id = ?";
			try
			{	ps = dbConn.prepareStatement(sqlQuery);
				ps.setDate(1, java.sql.Date.valueOf(stampDate));
				ps.setTime(2, new Time(sqltimeFormat.parse(stampTime).getTime()));
				ps.setDate(3, java.sql.Date.valueOf(datein));
				ps.setTime(4, new Time(sqltimeFormat.parse(timein).getTime()));
				ps.setString(5, idnum);
				ps.executeUpdate();
				
				tableModel.setValueAt(dateFormat.format(java.sql.Date.valueOf(stampDate)), 0, 3);
				tableModel.setValueAt(timeFormat.format(new Time(sqltimeFormat.parse(stampTime).getTime())), 0, 4);
				
				buttonIn.setEnabled(true);
				buttonOut.setEnabled(false);
				
				JOptionPane.showMessageDialog(null, "Time Out successful!");
			}
			catch(Exception error){ error.printStackTrace(); return; }
		}
		else if(source == buttonIn)
		{	stampDate = sqldateFormat.format(new Date(System.currentTimeMillis()));
			stampTime = sqltimeFormat.format(new Date(System.currentTimeMillis()));
			
			sqlQuery = "INSERT INTO timelogs (emp_id, date_in, time_in, date_out, time_out) VALUES (?, ?, ?, '0000-00-00', '00:00:00.000000')";
			try
			{	ps = dbConn.prepareStatement(sqlQuery);
				ps.setString(1, idnum);
				ps.setDate(2, java.sql.Date.valueOf(stampDate));
				ps.setTime(3, new Time(sqltimeFormat.parse(stampTime).getTime()));
				ps.executeUpdate();
				
				record[0] = idnum;
				record[1] = dateFormat.format(java.sql.Date.valueOf(stampDate));
				record[2] = timeFormat.format(new Time(sqltimeFormat.parse(stampTime).getTime()));
				record[3] = "";
				record[4] = "";
								
				tableModel.insertRow(0, record);
				
				datein = stampDate;
				timein = stampTime;
				
				buttonIn.setEnabled(false);
				buttonOut.setEnabled(true);
				buttonDelete.setEnabled(true);
				
				JOptionPane.showMessageDialog(null, "Time In successful!");
			}
			catch(Exception error){ error.printStackTrace(); return; }
		}
		else if(source == buttonDelete)
		{	if(tableLogtime.getSelectedRow()!=-1)
				tableLogtime.requestFocus();
			
			if((tableLogtime.getSelectedColumn()>=3) && (tableLogtime.getSelectedRow()==0))
			{	if(tableLogtime.getValueAt(0, 3) == "")
					deleteShift();
				else
				{	if(JOptionPane.showConfirmDialog(null, "Are you sure you want to delete your TIME OUT data?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
					{	tableModel.setValueAt("", 0, 3);
						tableModel.setValueAt("", 0, 4);
						tableLogtime.clearSelection();
						panelMain.requestFocusInWindow();
						buttonIn.setEnabled(false);
						buttonOut.setEnabled(true);
						
						sqlQuery = "UPDATE timelogs SET date_out = '0000-00-00', time_out = '00:00:00.000000' WHERE date_in = ? AND time_in = ? AND emp_id = ?";
						try
						{	ps = dbConn.prepareStatement(sqlQuery);
							ps.setDate(1, java.sql.Date.valueOf(sqldateFormat.format(dateFormat.parse(tableLogtime.getValueAt(0, 1).toString()))));
							ps.setTime(2, new Time(sqltimeFormat.parse(sqltimeFormat.format(timeFormat.parse(tableLogtime.getValueAt(0, 2).toString()))).getTime()));
							ps.setString(3, idnum);
							ps.executeUpdate();
							
							JOptionPane.showMessageDialog(null, "Time Out data successfully deleted!");
						}
						catch(Exception error){ error.printStackTrace(); return; }
					}
				}
			}
			else if((tableLogtime.getSelectedColumn()<=2) || (tableLogtime.getSelectedRow()>=1))
				deleteShift();
		}
	}
	
	public void deleteShift()
	{	if(tableLogtime.getSelectedRow()!=-1)
		{	if(JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this shift?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
			{	sqlQuery = "DELETE FROM timelogs WHERE emp_id = ? AND date_in = ? AND time_in = ?";
				try
				{	ps = dbConn.prepareStatement(sqlQuery);
					ps.setString(1, idnum);
					ps.setDate(2, java.sql.Date.valueOf(sqldateFormat.format(dateFormat.parse(tableLogtime.getValueAt(tableLogtime.getSelectedRow(), 1).toString()))));
					ps.setTime(3, new Time(sqltimeFormat.parse(sqltimeFormat.format(timeFormat.parse(tableLogtime.getValueAt(tableLogtime.getSelectedRow(), 2).toString()))).getTime()));
					ps.executeUpdate();
				}
				catch(Exception error){ error.printStackTrace(); return; }
			
				tableModel.removeRow(tableLogtime.getSelectedRow());
				panelMain.requestFocusInWindow();
				buttonIn.setEnabled(true);
				buttonOut.setEnabled(false);
				if(tableLogtime.getRowCount()==0)
					buttonDelete.setEnabled(false);
				
				JOptionPane.showMessageDialog(null, "Selected shift successfully deleted!");
			}
		}
	}
}