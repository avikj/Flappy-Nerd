import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class SetSearcher extends JFrame {
	static Font font= new Font("sans-serif", Font.BOLD, 25);
	static String HTMLData = "";
	static String[] newURL = new String[5];
	static GButton[] buttons = new GButton[5];
	JTextField search = new JTextField("Enter a search query or Quizlet URL.");
	GButton submit = new GButton("Submit");
	static String subject = "";
	public static void main(String[] args) {
		new SetSearcher();
	}
	public SetSearcher() {
		//setUndecorated(true);
		search.setFont(font);
		setTitle("Set Chooser");
		setBackground(Color.CYAN);
		setLayout(new GridLayout(6, 1, 20, 20));
		add(search);
		search.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				if(buttons[1]!=null){
					for (GButton button : buttons){
						remove(button);
					}
				}
				if(search.getText().contains("http"))
					new FlappyNerd(search.getText());
				else{
				subject = search.getText().replace(' ','-');
				buildURL();
				parseData();
				buildButtons();
				for (GButton button : buttons) {
					button.setPreferredSize(new Dimension(button.getWidth()-100, button.getHeight()));
					add(button);
					System.out.println(button.getText());
					revalidate();
					button.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							for (int i = 0;i < 5; i++){
								if(e.getSource() == buttons[i]){
									new FlappyNerd(newURL[i]);
								}
							}
						}
					});
				}}
				repaint();
			}
		});
		setVisible(true);
		setSize(500, 500);
	}
	public static void buildURL() {
		URL url = null;
		try {
			url = new URL("https://quizlet.com/subject/" + subject + "/");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStream is = null;
		try {
			is = (InputStream) url.getContent();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		StringBuffer sb = new StringBuffer();
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HTMLData = sb.toString();
	}

	public static void parseData() {
		for (int i = 0; i < 5; i++) {
			HTMLData = HTMLData.substring(HTMLData.indexOf("SearchResult-link"));
			HTMLData = HTMLData.substring(HTMLData.indexOf("href=\""));
			newURL[i] = "https://quizlet.com" + HTMLData.substring(HTMLData.indexOf("href=") + 6, HTMLData.indexOf(">") - 1);
		}
	}

	public static void buildButtons() {
		for (int i = 0; i < newURL.length; i++) {
			String title = newURL[i].substring(newURL[i].indexOf("m")+1);
			title = title.substring(title.indexOf("/")+1);
			title = title.substring(title.indexOf("/")+1);
			title = title.replace('-',' ');
			title = title.replace('/', ' ');
			if(title.contains("flash cards"))
				title = title.substring(0,title.indexOf("flash cards"));
			buttons[i] = new GButton(title);
		}
	}
	//public static void main(String[] args){new QuizletTest2();}
}
class GButton extends JButton{
	static Font font= new Font("lel get rekt", Font.BOLD, 25);
	public GButton(String s){
		super(s);
		setBackground(Color.RED);
		setForeground(Color.WHITE);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setFont(font);
	}
}