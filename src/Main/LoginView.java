package Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import DAO.MemberDAO;
import Util.LengthRestrictedDocument;
import View.SearchUserPopup;
import View.SignupPopup;

public class LoginView extends JPanel {
	
	private JPanel primary;
	
	MemberDAO mDAO;
	
	public SignupPopup signupPopup;
	public SearchUserPopup searchIdPopup;
	public SearchUserPopup searchPwPopup;
	// ¿”Ω√
	public JLabel lblLogo, lblId, lblPw, lblMsg;
	public JTextField tfId;
	public JPasswordField tfPw;
	public JButton btnLogin;
	public JButton btnSignup;
	public JButton btnSearchId;
	public JButton btnSearchPw;
	
	public LoginView() {
		mDAO = new MemberDAO();
		
		primary = new JPanel();
		primary.setLayout(null);
		primary.setPreferredSize(new Dimension(1200,700));
		primary.setBackground(Color.gray);
		this.add(primary);
		
		lblLogo = new JLabel("MINI-TOTO");
		lblLogo.setBounds(450,150,300,50);
		lblLogo.setFont(new Font("µ∏¿Ω", Font.BOLD, 50));
		primary.add(lblLogo);
		
		lblId = new JLabel("ID : ");
		
		lblId.setBounds(450,230,80,30);
		lblId.setFont(new Font("µ∏¿Ω", Font.BOLD, 28));
		primary.add(lblId);
		
		lblPw = new JLabel("PW : ");
		lblPw.setBounds(450,270,80,30);
		lblPw.setFont(new Font("µ∏¿Ω", Font.BOLD, 28));
		primary.add(lblPw);
		
		tfId = new JTextField();
		tfId.setBounds(530, 230, 100, 30);
		primary.add(tfId);
		
		tfPw = new JPasswordField();
		tfPw.setBounds(530, 270, 100, 30);
		tfPw.setDocument(new LengthRestrictedDocument(10));
		primary.add(tfPw);
		
		lblMsg = new JLabel();
		lblMsg.setBounds(450, 310, 200, 25);
		lblMsg.setFont(new Font("µ∏¿Ω", Font.PLAIN, 18));
		lblMsg.setForeground(Color.red);
		primary.add(lblMsg);
		
		btnLogin = new JButton("Login");
		btnLogin.setBounds(650, 225, 80, 80);
		
		primary.add(btnLogin);
		
		btnSignup = new JButton("Sign UP");
		btnSignup.setBounds(450, 350, 280, 50);
		
		primary.add(btnSignup);
		
		btnSearchId = new JButton("Find ID");
		btnSearchId.setBounds(450, 410, 130, 50);
		
		primary.add(btnSearchId);
		
		btnSearchPw = new JButton("Find PW");
		btnSearchPw.setBounds(600, 410, 130, 50);
		
		primary.add(btnSearchPw);
		
		signupPopup = new SignupPopup(mDAO);
		signupPopup.setLocation(50,150);
		primary.add(signupPopup);
		signupPopup.setVisible(false);
		
		searchIdPopup = new SearchUserPopup("id", mDAO);
		searchIdPopup.setLocation(800,150);
		primary.add(searchIdPopup);
		searchIdPopup.setVisible(false);
		
		searchPwPopup = new SearchUserPopup("pw", mDAO);
		searchPwPopup.setLocation(800,400);
		primary.add(searchPwPopup);
		searchPwPopup.setVisible(false);
		
//		this.pack();
	} // LoginView()
	
	public void addLoginViewListener(ActionListener btnL)
	{
		btnLogin.addActionListener(btnL);
		btnSignup.addActionListener(btnL);
		btnSearchId.addActionListener(btnL);
		btnSearchPw.addActionListener(btnL);
	}
	
	public void startView() {
		this.setVisible(true);
	} //startView()
	
}
