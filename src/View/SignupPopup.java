package View;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import DAO.MemberDAO;
import DTO.MemberDTO;
import Util.LengthRestrictedDocument;

public class SignupPopup extends JPanel {
	
	MemberDAO mDAO;
	
	JLabel lblSignup, lblName, lblId, lblPw, lblPwConfig, lblAge, lblSex, lblPhone;
	JLabel msgNameConfig, msgIdConfig, msgPwConfig, msgAgeConfig, msgPhoneConfig;
	JTextField tfName, tfId, tfAge, tfPhone;
	JPasswordField tfPw, tfPwConfig;
	JComboBox cbSex;
	JButton btnOverlap, btnSignup, btnExit;
	
	ButtonListener btnL;
	
	public SignupPopup(MemberDAO mDAO) {
		this.setSize(350, 400);
		this.setBackground(Color.white);
		this.setLayout(null);
		
		this.mDAO = mDAO;
		
		// Title Label
		lblSignup = new JLabel("Sign uP");
		lblSignup.setBounds(150,10,300,50);
		add(lblSignup);
		
		// 이름 관련
		lblName = new JLabel("Name : ");
		lblName.setBounds(30,70,100,20);
		add(lblName);
		tfName = new JTextField();
		tfName.setDocument(new LengthRestrictedDocument(10));
		tfName.setBounds(130,70,100,20);
		add(tfName);
		
		msgNameConfig = new JLabel();
		msgNameConfig.setBounds(30, 90, 200, 20);
		add(msgNameConfig);
		msgNameConfig.setVisible(false);
		
		// ID 관련
		lblId = new JLabel("ID(3~10) : ");
		lblId.setBounds(30,110,100,20);
		add(lblId);
		tfId = new JTextField();
		tfId.setDocument(new LengthRestrictedDocument(10));
		tfId.setBounds(130, 110, 100, 20);
		add(tfId);
		btnOverlap = new JButton("Double check");
		btnOverlap.setBounds(240, 105, 90, 30);
		add(btnOverlap);
		
		msgIdConfig = new JLabel();
		msgIdConfig.setBounds(30, 130, 200, 20);
		add(msgIdConfig);
		msgIdConfig.setVisible(false);
		
		// PW 관련
		lblPw = new JLabel("Password(6~8) : ");
		lblPw.setBounds(30,150,120,20);
		add(lblPw);		
		tfPw = new JPasswordField();
		tfPw.setDocument(new LengthRestrictedDocument(8));
		tfPw.setBounds(130, 150, 100, 20);
		add(tfPw);
		
		lblPwConfig = new JLabel("Confirm PW : ");
		lblPwConfig.setBounds(30,180,100,20);
		add(lblPwConfig);
		tfPwConfig = new JPasswordField();
		tfPwConfig.setDocument(new LengthRestrictedDocument(8));
		tfPwConfig.setBounds(130, 180, 100, 20);
		add(tfPwConfig);
		
		msgPwConfig = new JLabel();
		msgPwConfig.setBounds(30, 200, 220, 20);
		msgPwConfig.setForeground(Color.red);
		add(msgPwConfig);
		msgPwConfig.setVisible(false);
		
		// 나이 관련
		lblAge = new JLabel("Age : ");
		lblAge.setBounds(30,225,100,20);
		add(lblAge);
		tfAge = new JTextField();
		tfAge.setBounds(130, 225, 100, 20);
		add(tfAge);
		
		msgAgeConfig = new JLabel();
		msgAgeConfig.setBounds(30, 245, 200, 20);
		add(msgAgeConfig);
		msgAgeConfig.setVisible(false);
		
		// 성별 관련
		lblSex = new JLabel("Gender : ");
		lblSex.setBounds(30,270,100,20);
		add(lblSex);
		String strSex[] = {"Female", "Male"};
		cbSex = new JComboBox(strSex);
		cbSex.setBounds(130,270,100,20);
		add(cbSex);
		
		// 핸드폰 번호 관련
		lblPhone = new JLabel("Phone number : ");
		lblPhone.setBounds(30,310,100,20);
		add(lblPhone);
		tfPhone = new JTextField();
		tfPhone.setBounds(130, 310, 100, 20);
		add(tfPhone);
		
		msgPhoneConfig = new JLabel();
		msgPhoneConfig.setBounds(30, 330, 200, 20);
		add(msgPhoneConfig);
		msgPhoneConfig.setVisible(false);
		
		// JButton 정의
		btnSignup = new JButton("Join");
		btnSignup.setBounds(80, 350, 80, 30);
		add(btnSignup);
				
		btnExit = new JButton("Exit");
		btnExit.setBounds(200, 350, 80, 30);
		add(btnExit);
				
		// Button Listener
		btnL = new ButtonListener();
		btnOverlap.addActionListener(btnL);
		btnSignup.addActionListener(btnL);
		btnExit.addActionListener(btnL);
		
		this.setVisible(true);
	}
	
	public boolean isInteger(String input) {
	    try {
	        Integer.parseInt(input);
	        return true;
	    }
	    catch( Exception e ) {
	        return false;
	    }
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			Object obj = event.getSource();
			
			// 아이디 중복 예외처리
			if (obj == btnOverlap) { 
				if (tfId.getText().length() < 3) {
					msgIdConfig.setForeground(Color.red);
					msgIdConfig.setText("ID is too short.(3 or more characters)");
					msgIdConfig.setVisible(true);
					return;
				}
				
				msgIdConfig.setVisible(true);
				if(mDAO.isIdOverlap(tfId.getText())) {
					msgIdConfig.setForeground(Color.red);
					msgIdConfig.setText("Duplicate.");
				} else {
					msgIdConfig.setForeground(Color.green);
					msgIdConfig.setText("Available");
				}
			} else if (obj == btnSignup) {
				
				boolean isCorrect = true;
				
				// 이름 체크
				if (tfName.getText().length() < 1) {
					msgNameConfig.setForeground(Color.red);
					msgNameConfig.setText("Input Name : ");
					msgNameConfig.setVisible(true);
					isCorrect = false;
				} else {
					msgNameConfig.setForeground(Color.green);
					msgNameConfig.setText("Avilable");
					msgNameConfig.setVisible(true);
				}
				
				// ID 체크
				if (tfId.getText().length() < 3) {
					msgIdConfig.setForeground(Color.red);
					msgIdConfig.setText("ID is too short(3 or more characters)");
					msgIdConfig.setVisible(true);
					isCorrect = false;
				} else if(mDAO.isIdOverlap(tfId.getText())) { // 아이디 중복
					msgIdConfig.setForeground(Color.red);
					msgIdConfig.setText("Duplicate.");
					msgIdConfig.setVisible(true);
					isCorrect = false;
				} else {
					msgIdConfig.setForeground(Color.green);
					msgIdConfig.setText("Available.");
					msgIdConfig.setVisible(true);
				}
				
				// PW 체크
				if (tfPw.getText().length() < 6) {
					msgPwConfig.setForeground(Color.red);
					msgPwConfig.setText("PW is too short(6 or more characters)");
					msgPwConfig.setVisible(true);
					isCorrect = false;
				} else if (!tfPw.getText().equals(tfPwConfig.getText())) {
					msgPwConfig.setForeground(Color.red);
					msgPwConfig.setText("Does not match");
					msgPwConfig.setVisible(true);
					isCorrect = false;
				} else {
					msgPwConfig.setForeground(Color.green);
					msgPwConfig.setText("Available.");
					msgPwConfig.setVisible(true);
				}
				
				// 나이 체크
				if (!isInteger(tfAge.getText()) || !(Integer.parseInt(tfAge.getText()) >= 1)) {
					msgAgeConfig.setForeground(Color.red);
					msgAgeConfig.setText("Wrong input");
					msgAgeConfig.setVisible(true);
					isCorrect = false;
				} else {
					msgAgeConfig.setForeground(Color.green);
					msgAgeConfig.setText("Available.");
					msgAgeConfig.setVisible(true);
				}
				
				// 휴대폰 번호 체크
				if (!isInteger(tfPhone.getText()) || !(Integer.parseInt(tfPhone.getText()) >= 1)) {
					msgPhoneConfig.setForeground(Color.red);
					msgPhoneConfig.setText("Wrong input");
					msgPhoneConfig.setVisible(true);
					isCorrect = false;
				} else {
					msgPhoneConfig.setForeground(Color.green);
					msgPhoneConfig.setText("Available.");
					msgPhoneConfig.setVisible(true);
				}
				
				
				if(!isCorrect) { // 회원가입 포멧이 잘못됐으면 return
					return;
				}
				
				MemberDTO member = new MemberDTO();
				member.name = tfName.getText();
				member.id = tfId.getText();
				member.pw = tfPw.getText();
				member.age = Integer.parseInt(tfAge.getText());
				member.sex = cbSex.getSelectedItem().toString();
				member.phone = tfPhone.getText();
				member.lvl = 1;
				member.pnt = 1000;
				member.matchCnt = 0;
				member.win = 0;
				member.lose = 0;
				member.hitRate = 0.0f;
				
				// DB에 insert member
				mDAO.insertMember(member);

				JOptionPane.showConfirmDialog(null, "You have successfully registered!", "MINI-TOTO Sing up!",
						JOptionPane.DEFAULT_OPTION);

				msgNameConfig.setVisible(false);
				msgIdConfig.setVisible(false);
				msgPwConfig.setVisible(false);
				msgAgeConfig.setVisible(false);
				msgPhoneConfig.setVisible(false);
				tfName.setText("");
				tfId.setText("");
				tfPw.setText("");
				tfPwConfig.setText("");
				tfAge.setText("");
				tfPhone.setText("");
				setVisible(false);
				
			} else if (obj == btnExit) {
				msgNameConfig.setVisible(false);
				msgIdConfig.setVisible(false);
				msgPwConfig.setVisible(false);
				msgAgeConfig.setVisible(false);
				msgPhoneConfig.setVisible(false);
				tfName.setText("");
				tfId.setText("");
				tfPw.setText("");
				tfPwConfig.setText("");
				tfAge.setText("");
				tfPhone.setText("");
				setVisible(false);
			}
			
		} // actionPerformed();
	} // ButtonListener class
}
