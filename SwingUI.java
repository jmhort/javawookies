import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

public class SwingUI extends JFrame implements ActionListener{

    JLabel text, clicked;
    JTextField textField;
    JButton button, clickButton;
    JPanel panel;

    private boolean _clickMeMode = true;

    SwingUI() {
        text = new JLabel("I'm a Simple Program");
        clicked = new JLabel("Button Clicked");

        textField = new JTextField(20);
        textField.addActionListener(this);

        button = new JButton("Click Me");
        //Add button as an event listener
        button.addActionListener(this);

        clickButton = new JButton("Click Again");
        //Add button as an event listener
        clickButton.addActionListener(this);

			/* ... giCreate panel ... */
        panel = new JPanel(); // instantiate ang panel, basta i-equate gani nimo ang variable ug "new JPanel()" bah ron, meaning ana nagInstantiate ka
			/* ... giCreate panel ... */
        
			/* ... gibutangan ung mga dimension or vital statistics kung baga ang panel ... */
		panel.setLayout(new BorderLayout(1,1));
        panel.setBackground(Color.white);
			/* ... gibutangan ung mga dimension or vital statistics kung baga ang panel ... */
		
			/* ang pinakaMAIN panel kai gibutang sulod sa ContentPane */
        getContentPane().add(panel); // diri, gigamit na ang panel kai humana man siya instantiate sa taas (see line 31)
			/* ang pinakaMAIN panel kai gibutang sulod sa ContentPane */
			
			/* gipangSudlan ug unod ang panel; unsay sulod sa NORTH, CENTER ug SOUTH */
        panel.add(BorderLayout.NORTH, textField); // -> pwede na magamit ang "textField" kai humana man siya instantiate sa taas
        panel.add(BorderLayout.CENTER, text); // -> pwede na magamit ang "text" kai humana man siya instantiate sa taas
        panel.add(BorderLayout.SOUTH, button); // -> pwede na magamit ang "button" kai humana man siya instantiate sa taas
			/* gipangSudlan ug unod ang panel; unsay sulod sa NORTH, CENTER ug SOUTH */
			
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

    public void actionPerformed(ActionEvent event){
        Object source = event.getSource();
        String msg = textField.getText();
        if (_clickMeMode) {
            text.setText(msg);
            button.setText("Click Again");
            _clickMeMode = false;
        } else {
            text.setText("I'm a Simple Program");
            button.setText("Click Me");
            _clickMeMode = true;
        }
    }
}
