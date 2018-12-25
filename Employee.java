import java.awt.Font;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JTable; 
import java.io.*;

public class Employee extends JFrame implements ActionListener{

	JLabel lblWelcome, lblName, lblID, lblAge, lblGender, lblPhone, lblAddress, lblDate, lblTime, lblGap1, lblGap2, lblCDgap;
	JButton buttonExit, buttonIn, buttonOut;
	JPanel panelMain, panelWE, panelNIA, panelGPA, panelCC, panelDTT, panelWNG, panelCD, panelWC, panelDT; //PANELS: WE-Welcome/Exit, NIA-Name/ID/AGE, GPA-Gender/Phone/Age, CC-Clock/out, WNG-WE/NIA/GPA, CD-CC/Jlist, WC-WNG/CD, DT-Date/Time
	JTable tableLogtime; 

	SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
	SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");

	String[][] data = { 
            { "255","12/20/2018", "7:55AM", "5:01PM" }, 
            { "255","12/21/2018", "7:52AM", "5:21PM" },
			{ "255","12/22/2018", "7:33AM", "5:13PM" }, 
			{ "255","12/23/2018", "7:51AM", "5:17PM" },
			{ "255","12/24/2018", "7:42AM", "5:09PM" },
			{ "255","12/25/2018", "7:58AM", "5:15PM" }
			
		}; 
		
	String[] columnNames = { "ID Number", "Date", "Time in", "Time out" }; 

	private boolean _clickMeMode = true;

	Employee()
	{
		lblWelcome = new JLabel ("Hi, Juan dela Cruz!");
		lblName = new JLabel ("Name: Juan dela Cruz");
		lblID = new JLabel ("ID #: 255");
		lblAge = new JLabel ("Age: 25");
		lblGender = new JLabel ("Gender: Male");
		lblPhone = new JLabel ("Phone #: 09123456789");
		lblAddress = new JLabel ("Address: Cebu City");
		//lblDate = new JLabel(dateFormat.format(new Date()));
		lblTime = new JLabel(timeFormat.format(new Date()));
		lblTime.setFont(new Font("Arial",Font.BOLD,18));
		lblGap1 = new JLabel ("      ");
		lblGap2 = new JLabel ("      ");
		lblCDgap  = new JLabel(" ");

		tableLogtime = new JTable(data, columnNames); 

		buttonExit = new JButton("Log out");
		buttonExit.addActionListener(this);
		buttonIn = new JButton("Clock in");
		buttonIn.addActionListener(this);
		//buttonIn.setEnabled(false);
		buttonOut = new JButton("Clock out");
		buttonOut.addActionListener(this);

		panelMain = new JPanel();
		panelWE = new JPanel();
		panelNIA = new JPanel();
		panelGPA = new JPanel();
		panelDT = new JPanel();
		panelCC = new JPanel();
		panelWNG = new JPanel();
		panelCD = new JPanel();
		panelWC = new JPanel();

		panelMain.setLayout(new BorderLayout(1,1));
		panelMain.setBackground(Color.lightGray);
		panelWE.setLayout(new BorderLayout(1,1));
		panelWE.setBackground(Color.lightGray);
		panelNIA.setLayout(new BorderLayout(1,1));
		panelNIA.setBackground(Color.lightGray);
		panelGPA.setLayout(new BorderLayout(1,1));
		panelGPA.setBackground(Color.lightGray);
		panelDT.setLayout(new BorderLayout(30,1));
		panelDT.setBackground(Color.lightGray);
		panelCC.setLayout(new BorderLayout(120,1));
		panelCC.setBackground(Color.lightGray);
		panelWNG.setLayout(new BorderLayout(1,1));
		panelWNG.setBackground(Color.lightGray);
		panelCD.setLayout(new BorderLayout(1,1));
		panelCD.setBackground(Color.lightGray);
		panelWC.setLayout(new BorderLayout(1,1));
		panelWC.setBackground(Color.lightGray);

		panelWE.add(BorderLayout.WEST, lblWelcome);
		panelWE.add(BorderLayout.EAST, buttonExit);

		panelNIA.add(BorderLayout.NORTH, lblName);
		panelNIA.add(BorderLayout.CENTER, lblID);
		panelNIA.add(BorderLayout.SOUTH, lblAge);

		panelGPA.add(BorderLayout.NORTH, lblGender);
		panelGPA.add(BorderLayout.CENTER, lblPhone);
		panelGPA.add(BorderLayout.SOUTH, lblAddress);

		panelDT.add(BorderLayout.WEST, lblGap1);
		panelDT.add(BorderLayout.CENTER, lblTime);
		panelDT.add(BorderLayout.EAST, lblGap2);
		panelDT.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE,2),dateFormat.format(new Date()),1,0));

		panelCC.add(BorderLayout.WEST, buttonIn);
		panelCC.add(BorderLayout.CENTER, panelDT);
		panelCC.add(BorderLayout.EAST, buttonOut);

		panelWNG.add(BorderLayout.NORTH, panelWE);
		panelWNG.add(BorderLayout.CENTER, panelNIA);
		panelWNG.add(BorderLayout.SOUTH, panelGPA);
		
		panelCD.add(BorderLayout.NORTH, lblCDgap);
		panelCD.add(BorderLayout.CENTER, panelCC);
		panelCD.add(BorderLayout.SOUTH, new JScrollPane(tableLogtime));

		panelWC.add(BorderLayout.NORTH, panelWNG);
		panelWC.add(BorderLayout.CENTER, panelCD);

		panelMain.add(BorderLayout.WEST, panelWC);

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
		(
			new WindowAdapter() 
			{
				public void windowClosing(WindowEvent e)
				{
						System.exit(0);
				}
			}
		);
	}

	public void actionPerformed(ActionEvent event)
	{
		try
		{
			Object source = event.getSource();
			if (source == buttonExit)
			{
				Login login = new Login();
				login.pack();
				login.setLocationRelativeTo(null);
				login.setResizable(false);
				login.setVisible(true);
				login.setTitle("JavaWookies Time Tracking System");
				dispose();
			}
		}
		catch(Exception e) { e.printStackTrace(); }
	}
	
	public static void main(String[] args){
		
		Employee frame = new Employee();
        frame.setTitle("JAVAWOOKIES Employee Logtime");
        WindowListener l = new WindowAdapter() 
		{
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };

        frame.addWindowListener(l);
		frame.pack();
		frame.setLocationRelativeTo(null); // set the frame to appear at the center of the screen
		frame.setResizable(false);
        frame.setVisible(true);
    }
}
