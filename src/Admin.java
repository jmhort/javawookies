import javax.swing.table.TableColumnModel;
import java.util.Arrays;

import java.awt.Image;
import java.awt.Cursor;
import javax.swing.BorderFactory;
import com.mysql.cj.jdbc.exceptions.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable; 
//import java.text.SimpleDateFormat;
//import java.util.Date;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

public class Admin extends JFrame implements ActionListener
{
	JLabel lblAdmin, lblSettings, lblSearch, lblAddEmployee, lblBack;
	JLabel lblGapMIDleft, lblGapMIDright, lblGapBottomRight;
    JTextField tfSearch;
    JButton btnTimeLogs, btnEdit, btnDeleteEmployee, btnDeleteAll;
    JPanel panelMain, panelTOP, panelSearch, panelUpperRightCorner, panelMID, panelEmpTableButtons, panelButtonLEFT, panelButtonRIGHT, panelBottom;
	
	ImageIcon logoAdmin = new ImageIcon("img/Admin logo.jpg"), logoSettings = new ImageIcon("img/settings logo.png"), logoSettingsClicked = new ImageIcon("img/settings clicked logo.png");
	ImageIcon logoSearch = new ImageIcon("img/search logo.png"), logoSearchClicked = new ImageIcon("img/search clicked logo.png"), logoAddEmployee = new ImageIcon("img/add employee logo.png"), logoAddEmployeeClicked  = new ImageIcon("img/add employee clicked logo.png");
	ImageIcon logoBack = new ImageIcon("img/back logo.png"), logoBackClicked = new ImageIcon("img/back clicked logo.png"), logoTimeLogs = new ImageIcon("img/time logs logo.png"), logoTimeLogsClicked = new ImageIcon("img/time logs clicked logo.png");
	ImageIcon logoEdit = new ImageIcon("img/edit logo.png"), logoEditClicked = new ImageIcon("img/edit clicked logo.png"), logoDeleteEmployee = new ImageIcon("img/delete user logo.png"), logoDeleteEmployeeClicked = new ImageIcon("img/delete user clicked logo.png");
	ImageIcon logoDeleteAll = new ImageIcon("img/delete all user logo.png"), logoDeleteAllClicked = new ImageIcon("img/delete all user clicked logo.png");
	Image image, newimg;
	
	//SimpleDateFormat dateFormat = new SimpleDateFormat("MMM. dd, yyyy"), sqldateFormat = new SimpleDateFormat("yyyy-MM-dd");
	//SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a"), sqltimeFormat = new SimpleDateFormat("HH:mm:ss");
	//java.sql.Date sqlDate;
	
	DefaultTableModel tableModel = new DefaultTableModel();
	JTable table = new JTable(tableModel);
	String[] columnEmployee = {"Complete Name", "ID #", "Age", "Gender", "Address"}, record = new String[5];
	TableColumnModel colModel;
	
    Connection dbConn;
	Statement sqlStmnt;
	PreparedStatement ps;
	String sqlQuery;
	ResultSet sqlRS;
	
	JLabel lblVerify = new JLabel("");
	JTextField tfFname = new JTextField(20), tfMname = new JTextField(20), tfLname = new JTextField(20), tfAge, tfAddress = new JTextField(20);
	JPasswordField pfPasscode = new JPasswordField(15), pfVerifyPasscode = new JPasswordField(15);
	JButton btnClearAll;
	String genderList[]={"-Select-","Male","Female"}, noFname; 
	JComboBox<String> cbGender=new JComboBox<>(genderList);
		
	Admin()
	{	
		tfAge = new JTextField(3)
		{	public void processKeyEvent(KeyEvent ev)
			{	char c = ev.getKeyChar();
				try
				{	if (c > 31 && c < 127)// Ignore all non-printable characters. Just check the printable ones.
					{	Integer.parseInt(c + "");	}
					super.processKeyEvent(ev);
				}
				catch (NumberFormatException nfe) {}
			}
		};
		tfAge.addKeyListener(new KeyAdapter()
		{	@Override
			public void keyTyped(KeyEvent e)
			{	if (tfAge.getText().length() >= 3 ) // limit to 3 characters
					e.consume();
			}
		});
		
		lblAdmin = new JLabel("");
		image = logoAdmin.getImage();
		newimg = image.getScaledInstance(125, 50, java.awt.Image.SCALE_SMOOTH);
		logoAdmin = new ImageIcon(newimg);lblAdmin.setIcon(logoAdmin);

/*--- SETTINGS ICON UPPER RIGHT HAND CORNER ---*/		
		lblSettings = new JLabel("  ");
		image = logoSettings.getImage();
		newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		logoSettings = new ImageIcon(newimg);lblSettings.setIcon(logoSettings);
		lblSettings.addMouseListener( new MouseAdapter()
		{	public void mouseEntered(MouseEvent e)
			{	image = logoSettingsClicked.getImage();
				newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
				logoSettingsClicked = new ImageIcon(newimg);lblSettings.setIcon(logoSettingsClicked);
			}
		});
		lblSettings.addMouseListener( new MouseAdapter()
		{	public void mouseExited(MouseEvent e)
			{	image = logoSettings.getImage();
				newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
				logoSettings = new ImageIcon(newimg);lblSettings.setIcon(logoSettings);
			}
		});
		lblSettings.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblSettings.setToolTipText("Account Settings: Reset Password");
/*--- SETTINGS ICON UPPER RIGHT HAND CORNER ---*/
		
/*--- SEARCH ICON ---*/
		lblSearch = new JLabel("");
		image = logoSearch.getImage();
		newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		logoSearch = new ImageIcon(newimg);lblSearch.setIcon(logoSearch);
		lblSearch.addMouseListener( new MouseAdapter()
		{	public void mouseEntered(MouseEvent e)
			{	if(lblSearch.isEnabled())
				{	image = logoSearchClicked.getImage();
					newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
					logoSearchClicked = new ImageIcon(newimg);lblSearch.setIcon(logoSearchClicked);
				}
			}
		});
		lblSearch.addMouseListener( new MouseAdapter()
		{	public void mouseExited(MouseEvent e)
			{	image = logoSearch.getImage();
				newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
				logoSearch = new ImageIcon(newimg);lblSearch.setIcon(logoSearch);
			}
		});
		lblSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblSearch.setToolTipText("Search");
/*--- SEARCH ICON ---*/

        tfSearch = new JTextField("Search employee", 20);
		tfSearch.setEnabled(false);
		tfSearch.addMouseListener( new MouseAdapter()
		{	public void mouseClicked(MouseEvent e)
			{	if(lblSearch.isEnabled())
				{	tfSearch.setEnabled(true);
					tfSearch.setText("");
					tfSearch.requestFocus();
				}
			}
		});
		
/*--- ADD EMPLOYEE ICON ---*/
		lblAddEmployee = new JLabel("   ");
		image = logoAddEmployee.getImage();
		newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		logoAddEmployee = new ImageIcon(newimg);lblAddEmployee.setIcon(logoAddEmployee);
		lblAddEmployee.addMouseListener( new MouseAdapter()
		{	public void mouseEntered(MouseEvent e)
			{	image = logoAddEmployeeClicked.getImage();
				newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
				logoAddEmployeeClicked = new ImageIcon(newimg);lblAddEmployee.setIcon(logoAddEmployeeClicked);
			}
		});
		lblAddEmployee.addMouseListener( new MouseAdapter()
		{	public void mouseExited(MouseEvent e)
			{	image = logoAddEmployee.getImage();
				newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
				logoAddEmployee = new ImageIcon(newimg);lblAddEmployee.setIcon(logoAddEmployee);
			}
		});
		lblAddEmployee.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblAddEmployee.setToolTipText("Add New Employee");
		lblAddEmployee.addMouseListener( new MouseAdapter()
		{	public void mouseClicked(MouseEvent e)
			{	Addemp();	}
		});
/*--- ADD EMPLOYEE ICON ---*/

/*--- BACK ICON ---*/
		lblBack = new JLabel("");
		image = logoBack.getImage();
		newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		logoBack = new ImageIcon(newimg);lblBack.setIcon(logoBack);
		lblBack.addMouseListener( new MouseAdapter()
		{	public void mouseEntered(MouseEvent e)
			{	image = logoBackClicked.getImage();
				newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
				logoBackClicked = new ImageIcon(newimg);lblBack.setIcon(logoBackClicked);
			}
		});
		lblBack.addMouseListener( new MouseAdapter()
		{	public void mouseExited(MouseEvent e)
			{	image = logoBack.getImage();
				newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
				logoBack = new ImageIcon(newimg);lblBack.setIcon(logoBack);
			}
		});
		lblBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblBack.setToolTipText("Logout");
		lblBack.addMouseListener( new MouseAdapter()
		{	public void mouseClicked(MouseEvent e)
			{	if(JOptionPane.showConfirmDialog(null, "Are you sure you want to Logout?", "Logout", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				{	Login login = new Login();
					login.pack();
					login.setLocationRelativeTo(null);
					login.setResizable(false);
					login.setVisible(true);
					login.setTitle("JavaWookies Time Tracking System");
					dispose();
				}
			}
		});
/*--- BACK ICON ---*/

/*--- TABLE TABLE TABLE ---*/
		try
		{	dbConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/timetracker", "admin", "adminuser");
			sqlStmnt = dbConn.createStatement();
			sqlQuery = "SELECT * FROM employees ORDER BY emp_lname ASC";
			sqlRS = sqlStmnt.executeQuery(sqlQuery);
			if(!(sqlRS.first()))
				disableButtons();
			tableModel.setColumnIdentifiers(columnEmployee);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			colModel = table.getColumnModel();
			colModel.getColumn(0).setPreferredWidth(120);    
			colModel.getColumn(1).setPreferredWidth(5);
			colModel.getColumn(2).setPreferredWidth(3);
			colModel.getColumn(3).setPreferredWidth(6);
			colModel.getColumn(4).setPreferredWidth(150);
			table.getTableHeader().setReorderingAllowed(false);
			table.setDefaultEditor(Object.class, null); // making the table uneditable
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			while(sqlRS.next())
			{	record[0] = sqlRS.getString("emp_lname") + ", " + sqlRS.getString("emp_fname") + " " + sqlRS.getString("emp_mname").substring(0, 1) + ".";
				record[1] = sqlRS.getString("emp_id");
				record[2] = sqlRS.getString("emp_age");
				record[3] = sqlRS.getString("emp_gender");
				record[4] = sqlRS.getString("emp_address");
				
				tableModel.addRow(record);				
			}
		}
		catch(Exception error){ error.printStackTrace(); return; }
/*--- TABLE TABLE TABLE ---*/

/*--- BUTTONS BUTTONS BUTTONS BUTTONS BUTTONS BUTTONS  BUTTONS BUTTONS BUTTONS  BUTTONS BUTTONS BUTTONS  BUTTONS BUTTONS BUTTONS  BUTTONS BUTTONS BUTTONS ---*/
	/*--- BUTTON TIME LOGS ---*/
		btnTimeLogs = new JButton("View Details");
        btnTimeLogs.addActionListener(this);
		
		image = logoTimeLogs.getImage();
		newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		logoTimeLogs = new ImageIcon(newimg);btnTimeLogs.setIcon(logoTimeLogs);
		btnTimeLogs.addMouseListener( new MouseAdapter()
		{	public void mouseEntered(MouseEvent e)
			{	if(btnTimeLogs.isEnabled())
				{	image = logoTimeLogsClicked.getImage();
					newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
					logoTimeLogsClicked = new ImageIcon(newimg);btnTimeLogs.setIcon(logoTimeLogsClicked);
					btnTimeLogs.setForeground(new Color(0, 255, 234));
				}
			}
		});
		btnTimeLogs.addMouseListener( new MouseAdapter()
		{	public void mouseExited(MouseEvent e)
			{	image = logoTimeLogs.getImage();
				newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
				logoTimeLogs = new ImageIcon(newimg);btnTimeLogs.setIcon(logoTimeLogs);
				btnTimeLogs.setForeground(Color.BLACK);
			}
		});
		btnTimeLogs.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnTimeLogs.setToolTipText("View selected employee's time logs and Account details");
	/*--- BUTTON TIME LOGS ---*/
	
	/*--- BUTTON EDIT ---*/
		btnEdit = new JButton("Edit");
		btnEdit.addActionListener(this);
	
		image = logoEdit.getImage();
		newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		logoEdit = new ImageIcon(newimg);btnEdit.setIcon(logoEdit);
		btnEdit.addMouseListener( new MouseAdapter()
		{	public void mouseEntered(MouseEvent e)
			{	if(btnEdit.isEnabled())
				{	image = logoEditClicked.getImage();
					newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
					logoEditClicked = new ImageIcon(newimg);btnEdit.setIcon(logoEditClicked);
					btnEdit.setForeground(new Color(0, 255, 234));
				}
			}
		});
		btnEdit.addMouseListener( new MouseAdapter()
		{	public void mouseExited(MouseEvent e)
			{	image = logoEdit.getImage();
				newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
				logoEdit = new ImageIcon(newimg);btnEdit.setIcon(logoEdit);
				btnEdit.setForeground(Color.BLACK);
			}
		});
		btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnEdit.setToolTipText("Edit employee's information");
	/*--- BUTTON EDIT ---*/
	
	/*--- BUTTON DELETE EMPLOYEE ---*/
		btnDeleteEmployee = new JButton("Delete");
		btnDeleteEmployee.addActionListener(this);
	
		image = logoDeleteEmployee.getImage();
		newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		logoDeleteEmployee = new ImageIcon(newimg);btnDeleteEmployee.setIcon(logoDeleteEmployee);
		btnDeleteEmployee.addMouseListener( new MouseAdapter()
		{	public void mouseEntered(MouseEvent e)
			{	if(btnDeleteEmployee.isEnabled())
				{	image = logoDeleteEmployeeClicked.getImage();
					newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
					logoDeleteEmployeeClicked = new ImageIcon(newimg);btnDeleteEmployee.setIcon(logoDeleteEmployeeClicked);
					btnDeleteEmployee.setForeground(new Color(0, 255, 234));
				}
			}
		});
		btnDeleteEmployee.addMouseListener( new MouseAdapter()
		{	public void mouseExited(MouseEvent e)
			{	image = logoDeleteEmployee.getImage();
				newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
				logoDeleteEmployee = new ImageIcon(newimg);btnDeleteEmployee.setIcon(logoDeleteEmployee);
				btnDeleteEmployee.setForeground(Color.BLACK);
			}
		});
		btnDeleteEmployee.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnDeleteEmployee.setToolTipText("Delete the selected employee / user");
	/*--- BUTTON DELETE EMPLOYEE ---*/
	
	/*--- BUTTON DELETE ALL EMPLOYEE ---*/
		btnDeleteAll = new JButton("Delete All");
		btnDeleteAll.addActionListener(this);
	
		image = logoDeleteAll.getImage();
		newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		logoDeleteAll = new ImageIcon(newimg);btnDeleteAll.setIcon(logoDeleteAll);
		btnDeleteAll.addMouseListener( new MouseAdapter()
		{	public void mouseEntered(MouseEvent e)
			{	if(btnDeleteAll.isEnabled())
				{	image = logoDeleteAllClicked.getImage();
					newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
					logoDeleteAllClicked = new ImageIcon(newimg);btnDeleteAll.setIcon(logoDeleteAllClicked);
					btnDeleteAll.setForeground(new Color(0, 255, 234));
				}
			}
		});
		btnDeleteAll.addMouseListener( new MouseAdapter()
		{	public void mouseExited(MouseEvent e)
			{	image = logoDeleteAll.getImage();
				newimg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
				logoDeleteAll = new ImageIcon(newimg);btnDeleteAll.setIcon(logoDeleteAll);
				btnDeleteAll.setForeground(Color.BLACK);
			}
		});
		btnDeleteAll.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnDeleteAll.setToolTipText("Delete all employees / users");
	/*--- BUTTON DELETE ALL EMPLOYEE ---*/
/*--- BUTTONS BUTTONS BUTTONS BUTTONS BUTTONS BUTTONS  BUTTONS BUTTONS BUTTONS  BUTTONS BUTTONS BUTTONS  BUTTONS BUTTONS BUTTONS  BUTTONS BUTTONS BUTTONS ---*/




		
		panelMain = new JPanel(); panelMain.setLayout(new BorderLayout(1,1)); panelMain.setBackground(Color.lightGray);
		panelMain.addMouseListener( new MouseAdapter()
		{	public void mouseClicked(MouseEvent e)
			{	if(tfSearch.getText().trim().isEmpty())
				{	tfSearch.setText("Search employee");
					tfSearch.setEnabled(false);
					panelMain.requestFocusInWindow();			
				}
			}
		});	
		panelTOP = new JPanel(); panelTOP.setLayout(new BorderLayout(1,1)); panelTOP.setBackground(Color.lightGray);
		panelTOP.setBorder(BorderFactory.createEtchedBorder(1));
		panelSearch = new JPanel(); panelSearch.setLayout(new BorderLayout(1,1)); panelSearch.setBackground(Color.lightGray);
		panelUpperRightCorner = new JPanel(); panelUpperRightCorner.setLayout(new BorderLayout(1,1)); panelUpperRightCorner.setBackground(Color.lightGray);
		panelMID = new JPanel(); panelMID.setLayout(new BorderLayout(1,1)); panelMID.setBackground(Color.lightGray);
		panelEmpTableButtons = new JPanel(); panelEmpTableButtons.setLayout(new BorderLayout(1,1)); panelEmpTableButtons.setBackground(Color.lightGray);
		panelButtonLEFT = new JPanel(); panelButtonLEFT.setLayout(new BorderLayout(1,1)); panelButtonLEFT.setBackground(Color.lightGray);
		panelButtonRIGHT = new JPanel(); panelButtonRIGHT.setLayout(new BorderLayout(1,1)); panelButtonRIGHT.setBackground(Color.lightGray);
		panelBottom = new JPanel(); panelBottom.setLayout(new BorderLayout(1,1)); panelBottom.setBackground(Color.lightGray);
		
		//panelUpperRightCorner.add(BorderLayout.WEST, ); // --> if you need to add another icon at the upper right hand corner of the page
		panelUpperRightCorner.add(BorderLayout.CENTER, lblSettings);
		panelUpperRightCorner.add(BorderLayout.EAST, lblBack);	
		
		panelTOP.add(BorderLayout.WEST, lblAdmin);
		//panelTOP.add(BorderLayout.CENTER, lblAdmin); // --> if you need to add another icon at the top
		panelTOP.add(BorderLayout.EAST, panelUpperRightCorner);
		
		panelSearch.add(BorderLayout.WEST, lblAddEmployee);
		panelSearch.add(BorderLayout.CENTER, tfSearch);
		panelSearch.add(BorderLayout.EAST, lblSearch);
		
		lblGapMIDleft = new JLabel("                              ");
		lblGapMIDright = new JLabel("                              ");
		panelMID.add(BorderLayout.WEST, lblGapMIDleft);
		panelMID.add(BorderLayout.CENTER, panelSearch);
		panelMID.add(BorderLayout.EAST, lblGapMIDright);
		
		panelButtonLEFT.add(BorderLayout.WEST, btnTimeLogs);
		panelButtonLEFT.add(BorderLayout.CENTER, btnEdit);
		panelButtonLEFT.add(BorderLayout.EAST, btnDeleteEmployee);
		
		lblGapBottomRight = new JLabel("                                                             ");
		panelButtonRIGHT.add(BorderLayout.WEST, lblGapBottomRight);
		//panelButtonRIGHT.add(BorderLayout.CENTER, ); // --> if you need to add another button bottom right hand corner
		panelButtonRIGHT.add(BorderLayout.EAST, btnDeleteAll);
		
		panelBottom.add(BorderLayout.WEST, panelButtonLEFT);
		//panelBottom.add(BorderLayout.CENTER, ); // --> if you need to add another button bottom right hand corner
		panelBottom.add(BorderLayout.EAST, panelButtonRIGHT);
		
        panelEmpTableButtons.add(BorderLayout.NORTH, new JScrollPane(table)); 
        //panelEmpTableButtons.add(BorderLayout.CENTER, ); // --> if you need to add another icon between the Employee Table and the buttons
        panelEmpTableButtons.add(BorderLayout.SOUTH, panelBottom);
				
		panelMain.add(BorderLayout.NORTH, panelTOP); 
        panelMain.add(BorderLayout.CENTER, panelMID); 
        panelMain.add(BorderLayout.SOUTH, panelEmpTableButtons);
			
		
        getContentPane().add(panelMain);
		
		
		this.addWindowListener
		(	new WindowAdapter() 
			{	public void windowClosing(WindowEvent e)
				{	System.exit(0);		}
			}
		);
    }
	
	public void disableButtons()
	{	btnTimeLogs.setEnabled(false);
		btnEdit.setEnabled(false);
		btnDeleteEmployee.setEnabled(false);
		btnDeleteAll.setEnabled(false);
		lblSearch.setEnabled(false);
	}
	
    public void actionPerformed(ActionEvent event)
	{	Object source = event.getSource();
        
		if(source == btnDeleteAll)
		{	if(btnDeleteAll.isEnabled())
			{	if(JOptionPane.showConfirmDialog(null, "Are you sure you want to delete all Employees?", "Delete All Employees", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				{	DefaultTableModel model = (DefaultTableModel) table.getModel();
					while( model.getRowCount() > 0 )
						model.removeRow(0);
					disableButtons();
				}
			}
		}
		else if(source == btnClearAll)
			eraseAll();
		
    }
	
	public void eraseAll()
	{
		tfFname.setText("");
		tfMname.setText("");
		tfLname.setText("");
		tfAge.setText("");
		tfAddress.setText("");
		pfPasscode.setText("");
		pfVerifyPasscode.setText("");
		cbGender.setSelectedIndex(0);
		lblVerify.setText("");
		pfPasscode.requestFocus();
	}
	
	public void Addemp()
	{
		JLabel lblEmpIDtext;
		JPanel panelAddEmployee, panelEmpPass, panelEmpID, panelPasscode, panelFname, panelMname, panelLname, panelFullName, panelAge, panelGender, panelAddress, panelAGA;
		JPanel panelFullnameAGA, panelVerifyPasscode, panelIDPasscodeGap, panelSeparatorLEFTRIGHT, panelSeparatorUPDOWN, panelFullnameSeparatorAGA, panelAgeGender;
		JPanel panelButtonClear;
		
		btnClearAll = new JButton("Clear All");btnClearAll.addActionListener(this);
		lblEmpIDtext = new JLabel("100");lblEmpIDtext.setForeground(new Color(142, 47, 53));
		
		panelAddEmployee = new JPanel(); panelAddEmployee.setLayout(new BorderLayout(1,1)); panelAddEmployee.setBackground(Color.lightGray);
		panelEmpPass = new JPanel(); panelEmpPass.setLayout(new BorderLayout(1,1)); panelEmpPass.setBackground(Color.lightGray);
		panelEmpID = new JPanel(); panelEmpID.setLayout(new BorderLayout(1,1)); panelEmpID.setBackground(Color.lightGray);
		panelPasscode = new JPanel(); panelPasscode.setLayout(new BorderLayout(1,1)); panelPasscode.setBackground(Color.lightGray);
		panelFname = new JPanel(); panelFname.setLayout(new BorderLayout(1,1)); panelFname.setBackground(Color.lightGray);
		panelMname = new JPanel(); panelMname.setLayout(new BorderLayout(1,1)); panelMname.setBackground(Color.lightGray);
		panelLname = new JPanel(); panelLname.setLayout(new BorderLayout(1,1)); panelLname.setBackground(Color.lightGray);
		panelFullName = new JPanel(); panelFullName.setLayout(new BorderLayout(1,1)); panelFullName.setBackground(Color.lightGray);
		panelAge = new JPanel(); panelAge.setLayout(new BorderLayout(1,1)); panelAge.setBackground(Color.lightGray);
		panelGender = new JPanel(); panelGender.setLayout(new BorderLayout(1,1)); panelGender.setBackground(Color.lightGray);
		panelAddress = new JPanel(); panelAddress.setLayout(new BorderLayout(1,1)); panelAddress.setBackground(Color.lightGray);
		panelAGA = new JPanel(); panelAGA.setLayout(new BorderLayout(1,1)); panelAGA.setBackground(Color.lightGray);
		panelFullnameAGA = new JPanel(); panelFullnameAGA.setLayout(new BorderLayout(1,1)); panelFullnameAGA.setBackground(Color.lightGray);
		panelVerifyPasscode = new JPanel(); panelVerifyPasscode.setLayout(new BorderLayout(1,1)); panelVerifyPasscode.setBackground(Color.lightGray);
		panelIDPasscodeGap = new JPanel(); panelIDPasscodeGap.setLayout(new BorderLayout(1,1)); panelIDPasscodeGap.setBackground(Color.lightGray);
		panelSeparatorLEFTRIGHT = new JPanel(); panelSeparatorLEFTRIGHT.setLayout(new BorderLayout(1,1)); panelSeparatorLEFTRIGHT.setBackground(Color.lightGray);
		panelSeparatorUPDOWN = new JPanel(); panelSeparatorUPDOWN.setLayout(new BorderLayout(1,1)); panelSeparatorUPDOWN.setBackground(Color.lightGray);
		panelFullnameSeparatorAGA = new JPanel(); panelFullnameSeparatorAGA.setLayout(new BorderLayout(1,1)); panelFullnameSeparatorAGA.setBackground(Color.lightGray);
		panelAgeGender = new JPanel(); panelAgeGender.setLayout(new BorderLayout(1,1)); panelAgeGender.setBackground(Color.lightGray);
		panelButtonClear = new JPanel(); panelButtonClear.setLayout(new BorderLayout(1,1)); panelButtonClear.setBackground(Color.lightGray);
		
		panelEmpID.add(BorderLayout.WEST, new JLabel("  Employee ID #:  "));
		panelEmpID.add(BorderLayout.CENTER, lblEmpIDtext);
        panelEmpID.add(BorderLayout.EAST, lblVerify);
		
		panelPasscode.add(BorderLayout.WEST, new JLabel("  Passcode:     "));
		panelPasscode.add(BorderLayout.CENTER, pfPasscode);         
        panelPasscode.add(BorderLayout.EAST, new JLabel("  "));
		
		panelVerifyPasscode.add(BorderLayout.WEST, new JLabel("  Verify Code:  "));
		panelVerifyPasscode.add(BorderLayout.CENTER, pfVerifyPasscode);         
        panelVerifyPasscode.add(BorderLayout.EAST, new JLabel("  "));
		
		panelEmpPass.add(BorderLayout.NORTH, panelEmpID);
		panelEmpPass.add(BorderLayout.CENTER, panelPasscode);
		panelEmpPass.add(BorderLayout.SOUTH, panelVerifyPasscode);
		
		panelIDPasscodeGap.add(BorderLayout.NORTH, new JLabel("  "));
		panelIDPasscodeGap.add(BorderLayout.CENTER, panelEmpPass);
		panelIDPasscodeGap.add(BorderLayout.SOUTH, new JLabel("  "));
		panelIDPasscodeGap.setBorder(BorderFactory.createLoweredBevelBorder());
		
		panelFname.add(BorderLayout.WEST, new JLabel("  Firstname:     "));
		panelFname.add(BorderLayout.CENTER, tfFname);         
        panelFname.add(BorderLayout.EAST, new JLabel("  "));
		
		panelMname.add(BorderLayout.WEST, new JLabel("  Middlename: "));
		panelMname.add(BorderLayout.CENTER, tfMname);         
        panelMname.add(BorderLayout.EAST, new JLabel("  "));
		
		panelLname.add(BorderLayout.WEST, new JLabel("  Lastname:     "));
		panelLname.add(BorderLayout.CENTER, tfLname);         
        panelLname.add(BorderLayout.EAST, new JLabel("  "));
		
		panelFullName.add(BorderLayout.NORTH, panelFname);
		panelFullName.add(BorderLayout.CENTER, panelMname);         
        panelFullName.add(BorderLayout.SOUTH, panelLname);
		
		panelSeparatorLEFTRIGHT.add(BorderLayout.WEST, new JLabel("     "));
		panelSeparatorLEFTRIGHT.add(BorderLayout.CENTER, new JSeparator());         
        panelSeparatorLEFTRIGHT.add(BorderLayout.EAST, new JLabel("     "));
		
		panelSeparatorUPDOWN.add(BorderLayout.NORTH, new JLabel("  "));
		panelSeparatorUPDOWN.add(BorderLayout.CENTER, panelSeparatorLEFTRIGHT);         
        
		panelAge.add(BorderLayout.WEST, new JLabel("  Age:          "));
		panelAge.add(BorderLayout.CENTER, tfAge);         
        
		panelGender.add(BorderLayout.WEST, new JLabel("  Gender:   "));
		panelGender.add(BorderLayout.CENTER, cbGender);         
        
		panelAgeGender.add(BorderLayout.WEST, panelAge);
		panelAgeGender.add(BorderLayout.CENTER, panelGender); 
		panelAgeGender.add(BorderLayout.EAST, new JLabel("  "));
		
		panelAddress.add(BorderLayout.WEST, new JLabel("  Address: "));
		panelAddress.add(BorderLayout.CENTER, tfAddress);         
        panelAddress.add(BorderLayout.EAST, new JLabel("  "));
		
		panelAGA.add(BorderLayout.NORTH, panelAgeGender);
		panelAGA.add(BorderLayout.SOUTH, panelAddress);
		
		panelFullnameAGA.add(BorderLayout.NORTH, panelFullName);
		panelFullnameAGA.add(BorderLayout.CENTER, panelSeparatorUPDOWN);         
		panelFullnameAGA.add(BorderLayout.SOUTH, panelAGA);
		
		panelFullnameSeparatorAGA.add(BorderLayout.NORTH, new JLabel(" "));
		panelFullnameSeparatorAGA.add(BorderLayout.CENTER, panelFullnameAGA);         
		panelFullnameSeparatorAGA.add(BorderLayout.SOUTH, new JLabel(" "));
		
		panelButtonClear.add(BorderLayout.WEST, new JLabel("                         "));
		panelButtonClear.add(BorderLayout.CENTER, btnClearAll);         
        panelButtonClear.add(BorderLayout.EAST, new JLabel("                         "));
			
		panelAddEmployee.add(BorderLayout.NORTH, panelIDPasscodeGap);
		panelAddEmployee.add(BorderLayout.CENTER, panelFullnameSeparatorAGA);         
        panelAddEmployee.add(BorderLayout.SOUTH, panelButtonClear);
		panelAddEmployee.setBorder(BorderFactory.createEtchedBorder(1));
				
		Object[] selectOptions = {"ADD", "CANCEL"};
		
		if(JOptionPane.showOptionDialog(null, panelAddEmployee, "Enter Employee Details", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, selectOptions, selectOptions[0]) == 0)
		{	if(JOptionPane.showConfirmDialog(null, "Are all entries correct?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
			{	if ((Arrays.equals(pfPasscode.getPassword(), pfVerifyPasscode.getPassword())) && ((pfPasscode.getPassword().length != 0) && (pfPasscode.getPassword().length != 0)))
				{
					JOptionPane.showMessageDialog(null, "passcodes are the same!");
					//add row to the table UI
					//start sql insert into table here
				} 
				else
				{
					lblVerify.setText("Passcode not verified  "); lblVerify.setForeground(Color.RED);
					Addemp();
				}
			}
			else
				Addemp();
		}
		eraseAll();
	}

    public static void main(String[] args)
	{	Admin frame = new Admin();
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		//frame.setSize(800,200); // (x, y)
        frame.setVisible(true);
		frame.setTitle("JavaWookies Time Tracking System");
		
	}
}