import com.mysql.cj.jdbc.exceptions.*;
import java.sql.*;

import java.awt.Image;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.awt.Font;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

public class Login extends JFrame implements ActionListener{

    JLabel lIDnumber, lPasscode, lLogo, lDate, lTime, lTrack;
	JLabel gapLogo, gapTime1, gapTime2, gapTimeNorth, gapLogoTrack1, gapLogoTrack2; //-> labels for gaps
	JTextField tfIDnumber;
	JPasswordField tfPasscode;
    JButton buttonSignIn;
    JPanel panelMain, panelIDPassSign, panelIDnum, panelPasscode, panelDateTime, panelTime, panelLogo, panelLogoTrack;
	ImageIcon logo = new ImageIcon("JAVAwookiesLOGO.png"); // load the image to a imageIcon
	Image image = logo.getImage(); // transform it 
	Image newimg = image.getScaledInstance(150, 90, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
	
	//DateFormat dateFormat = new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss");
	SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
	SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a"); // not in military time. If you want to use military time use HH then take out a
	
	Connection dbConn; // This is the container for database connection driver, host name, db name, user name, password
	Statement sqlStmnt; // This is where most MySQL statements (ex., INSERT INTO, SELECT) are stored
	String sqlQuery, sqlUpdate; // sqlQuery is for searching desired records while sqlUpdate is for data manipulation (save, delete)
	ResultSet sqlRS; // sqlRS is the Result Set that allows us to obtain data from db table based on the MySQL statement that the admin/user entered
	
	private boolean _clickMeMode = true;
	
	Login()
	{
        lIDnumber = new JLabel("ID number: ");
		lPasscode = new JLabel("Passcode:  ");
		lLogo = new JLabel();
		logo = new ImageIcon(newimg);lLogo.setIcon(logo);
		lTrack = new JLabel("* Time Tracking System *");
		Font f1 = new Font("Arial Bold", Font.ITALIC,12);
		lTrack.setFont(f1);
		
		/* ========= GAP creation ========= */
		gapLogo = new JLabel(" ");
		gapTimeNorth = new JLabel(" ");
		gapTime1 = new JLabel("         ");
		gapTime2 = new JLabel("         ");
		gapLogoTrack1 = new JLabel("           ");
		gapLogoTrack2 = new JLabel("           ");
		/* ========= GAP creation ========= */
		
		lDate = new JLabel(dateFormat.format(new Date()));
		lTime = new JLabel(timeFormat.format(new Date()));
		Font f2 = new Font("Arial", Font.BOLD,25);
		lTime.setFont(f2);
		lTime.setForeground(Color.BLUE);
		//lDate.setText(dateFormat.format(new Date())); // -> long process of setting the text value of lDate
		//lTime.setText(timeFormat.format(new Date())); // -> long process of setting the text value of lTime
		        
        tfIDnumber = new JTextField(5) // as agreed, should compose of 5 digits only
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
		tfIDnumber.addKeyListener(new KeyAdapter()
		{	@Override
			public void keyTyped(KeyEvent e)
			{	if (tfIDnumber.getText().length() >= 5 ) // limit to 5 characters
					e.consume();
			}
		});
        tfIDnumber.addActionListener(this);
		
		tfPasscode = new JPasswordField(8);
        tfPasscode.addActionListener(this);

        buttonSignIn = new JButton("Sign In");
        buttonSignIn.addActionListener(this);
		
		/* ========= PANEL creation ========= */
		panelMain = new JPanel(); // instantiate ang panel, basta i-equate gani nimo ang variable ug "new JPanel()" bah ron, meaning ana nagInstantiate ka
		panelMain.setLayout(new BorderLayout(1,1));
        panelMain.setBackground(Color.lightGray);
		
		panelIDnum = new JPanel();
		panelIDnum.setLayout(new BorderLayout(1,1));
        panelIDnum.setBackground(Color.lightGray);
		
		panelPasscode = new JPanel();
		panelPasscode.setLayout(new BorderLayout(1,1));
        panelPasscode.setBackground(Color.lightGray);
		
		panelIDPassSign = new JPanel();
		panelIDPassSign.setLayout(new BorderLayout(1,1));
        panelIDPassSign.setBackground(Color.lightGray);
		
		panelDateTime = new JPanel();
		panelDateTime.setLayout(new BorderLayout(1,1));
        panelDateTime.setBackground(Color.lightGray);
		
		panelTime = new JPanel();
		panelTime.setLayout(new BorderLayout(1,1));
        panelTime.setBackground(Color.lightGray);
		
		panelLogo = new JPanel();
		panelLogo.setLayout(new BorderLayout(1,1));
        panelLogo.setBackground(Color.lightGray);
		
		panelLogoTrack = new JPanel();
		panelLogoTrack.setLayout(new BorderLayout(1,1));
        panelLogoTrack.setBackground(Color.lightGray);
		/* ========= PANEL creation ========= */
		
		
		/* ========= layouting the PANELS ========= */
		panelIDnum.add(BorderLayout.WEST, lIDnumber);
		panelIDnum.add(BorderLayout.CENTER, tfIDnumber);
		
		panelPasscode.add(BorderLayout.WEST, lPasscode);
		panelPasscode.add(BorderLayout.CENTER, tfPasscode);
		
		panelIDPassSign.add(BorderLayout.NORTH, panelIDnum);
		panelIDPassSign.add(BorderLayout.CENTER, panelPasscode);
		panelIDPassSign.add(BorderLayout.SOUTH, buttonSignIn);
		panelIDPassSign.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder()," ",2,1));
		
		panelTime.add(BorderLayout.WEST, gapTime1);
		panelTime.add(BorderLayout.CENTER, lTime);
		panelTime.add(BorderLayout.EAST, gapTime2);
		panelTime.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),dateFormat.format(new Date()),2,0));
		
		panelDateTime.add(BorderLayout.NORTH, gapTimeNorth);
		panelDateTime.add(BorderLayout.CENTER, panelTime);
				
		panelLogo.add(BorderLayout.NORTH, lLogo);
		panelLogo.add(BorderLayout.CENTER, lTrack);
		panelLogo.add(BorderLayout.SOUTH, gapLogo);
		
		panelLogoTrack.add(BorderLayout.WEST, gapLogoTrack1);
		panelLogoTrack.add(BorderLayout.CENTER, panelLogo);
		panelLogoTrack.add(BorderLayout.EAST, gapLogoTrack2);
		
		panelMain.add(BorderLayout.NORTH, panelLogoTrack);
        panelMain.add(BorderLayout.CENTER, panelDateTime);
        panelMain.add(BorderLayout.SOUTH, panelIDPassSign);
		/* ========= layouting the PANELS ========= */
			
		/* ang pinakaMAIN panel kai gibutang sulod sa ContentPane */
        getContentPane().add(panelMain); // diri, gigamit na ang panel kai humana man siya instantiate sa taas (see line 31)
		/* ang pinakaMAIN panel kai gibutang sulod sa ContentPane */
		
		/* ========= code in making the clock ticks ========= */
		Timer timer;
		timer = new Timer(1000, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//lDate.setText(dateFormat.format(new Date(System.currentTimeMillis())));
				lTime.setText(timeFormat.format(new Date(System.currentTimeMillis())));
				panelTime.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),dateFormat.format(new Date(System.currentTimeMillis())),2,0));
			}
		}
		);
		timer.setRepeats(true);
		timer.start();
		/* ========= code in making the clock ticks ========= */
		
		this.getRootPane().setDefaultButton(buttonSignIn); // making Sign In as the default button and enable "Enter" key to function
		
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
		Object source = event.getSource();
		
		if (_clickMeMode)
		{
			try
			{
				String strPassword = new String(tfPasscode.getPassword());
				
				dbConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/timetracker", "root", "");
				sqlStmnt = dbConn.createStatement();
				sqlQuery = "SELECT * FROM employees WHERE emp_id = '" + tfIDnumber.getText() + "' AND passcode = '" + strPassword + "'";
				sqlRS = sqlStmnt.executeQuery(sqlQuery);
				if(sqlRS.next())
				{	sqlRS.close();
					sqlStmnt.close();
					dbConn.close();
				}
				else
				{	JOptionPane.showMessageDialog(null, "NO RECORD FOUND!", "ALERT", JOptionPane.ERROR_MESSAGE); // error dialog box NOTE: need better error message
					tfIDnumber.setText(""); tfIDnumber.requestFocus(); // set focus on tfIDnumber after error message prompted
					tfPasscode.setText("");
					return;
				}
			}
			catch(Exception error) { error.printStackTrace(); return; }
            
			if(tfIDnumber.getText().equals("0"))
			{
				JOptionPane.showMessageDialog(null, "ADMIN page: Under Repair.");
				tfIDnumber.setText(""); tfIDnumber.requestFocus();
				tfPasscode.setText("");
				return;
			}
			
			Employee eui = new Employee(tfIDnumber.getText());
			eui.pack();
			eui.setLocationRelativeTo(null);
			eui.setResizable(false);
			eui.setVisible(true);
			eui.setTitle("JavaWookies Time Tracking System");
			
			dispose();			
		}
    }

    public static void main(String[] args)
	{	Login frame = new Login();
		frame = new Login();

        WindowListener l = new WindowAdapter()
		{	public void windowClosing(WindowEvent e)
			{	System.exit(0);		}
        };

        frame.addWindowListener(l);
        frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
        frame.setVisible(true);
		frame.setTitle("JavaWookies Time Tracking System");
    }
}
