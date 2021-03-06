import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

// 2ndary class and extending JFrame
class UIframe extends JFrame{
 Container c; // creating an object of Container class
 
 //JLabel/JTextField/JButton and JPasswordField
 JLabel lbUN = new JLabel("ID number"); 
 JLabel lbPW = new JLabel("Passcode"); 
 JTextField IDnumber = new JTextField();
 JPasswordField passcode = new JPasswordField();
 JButton Button1 = new JButton("CANCEL");
 JButton Button2 = new JButton("LOGIN");

//constructor for UIframe
 UIframe()
 {
	 c = this.getContentPane(); 
	 c.setLayout(null);
	 c.setBackground(Color.WHITE);
	 
	 //location and size for JLabel/JTextField/JPasswordField/JButton/Banner

	 lbUN.setBounds(50,50,100,30);
	 lbPW.setBounds(50,90,100,30);
	 IDnumber.setBounds(130,50,200,25);
	 passcode.setBounds(130,90,200,25);
	 Button1.setBounds(130,140,100,25);
	 Button2.setBounds(230,140,100,25);
	 
	 //setting font style
	 Font f= new Font("Arial", Font.BOLD,12);
	 //Font f2= new Font("Arial", Font.BOLD,25);
	 //applying on JLabel/JTextField/JPasswordField/JButton/Banner
	 lbUN.setFont(f);
	 lbPW.setFont(f);
	 IDnumber.setFont(f);
	 passcode.setFont(f);
	 Button1.setFont(f);
	 Button2.setFont(f);
	
	 
	 //adding hand cursor on button
	 Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
	 //applying cursor to the Button
	 Button1.setCursor(cursor); 
	 Button2.setCursor(cursor);
	 
	 //adding to the container JLabel/JTextField/JPasswordField/JButton/Banner
	 c.add(lbUN);
	 c.add(lbPW);
	 c.add(IDnumber);
	 c.add(passcode);
	 c.add(Button1);
	 c.add(Button2);
	
 }
}

//main class and main method
public class LoginUI{
	public static void main(String args[]){
	
	
//object of UIframe class
 UIframe frame = new UIframe(); 
 frame.setVisible(true);
 frame.setTitle("JVW TimeTrackingSystem"); 
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // default close operation
 frame.setBounds(100,100,400,250); //setting location and size

 }
}
