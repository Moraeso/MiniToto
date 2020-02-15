package View;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import DAO.MemberDAO;
import Util.LengthRestrictedDocument;

public class SearchUserPopup extends JPanel {
	
	MemberDAO mDAO;
	
	JLabel lblTitle, lblClue, lblPhone;
	JLabel msgClueConfig, msgPhoneConfig;
	JTextField tfClue, tfPhone;
	JButton btnSearch, btnExit;
	
	ButtonListener btnL;
	
	String type;
	
	public SearchUserPopup(String type, MemberDAO mDAO) {
		
		this.setSize(300, 200);
		this.setBackground(Color.white);
		this.setLayout(null);
		
		this.type = type;
		
		this.mDAO = mDAO;
		
		btnL = new ButtonListener();
		
		// Title Label
		lblTitle = new JLabel();
		if(type.equals("id"))
			lblTitle.setText("Find ID");
		else if(type.equals("pw"))
			lblTitle.setText("Find PW");
		lblTitle.setBounds(110,10,300,50);
		add(lblTitle);
		
		// Clue 관련
		lblClue = new JLabel();
		if(type.equals("id"))
			lblClue.setText("Name : ");
		else if(type.equals("pw"))
			lblClue.setText("ID : ");
		lblClue.setBounds(50, 60, 100, 20);
		add(lblClue);
		
		tfClue = new JTextField();
		tfClue.setDocument(new LengthRestrictedDocument(10));
		tfClue.setBounds(140, 60, 100, 20);
		add(tfClue);
		
		msgClueConfig = new JLabel();
		msgClueConfig.setBounds(50, 80, 100, 20);
		add(msgClueConfig);
		msgClueConfig.setVisible(false);
		
		// Phone 관련
		lblPhone = new JLabel("Phone number : ");
		lblPhone.setBounds(50, 100, 100, 20);
		add(lblPhone);
		
		tfPhone = new JTextField();
		tfPhone.setDocument(new LengthRestrictedDocument(12));
		tfPhone.setBounds(140, 100, 100, 20);
		add(tfPhone);
		
		msgPhoneConfig = new JLabel();
		msgPhoneConfig.setBounds(50, 120, 100, 20);
		add(msgPhoneConfig);
		msgPhoneConfig.setVisible(false);
		
		// JButton 관련
		btnSearch = new JButton("Search");
		btnSearch.setBounds(60, 150, 80, 30);
		add(btnSearch);
		
		btnExit = new JButton("Exit");
		btnExit.setBounds(160, 150, 80, 30);
		add(btnExit);
	
		btnSearch.addActionListener(btnL);
		btnExit.addActionListener(btnL);
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			Object obj = event.getSource();
			
			if (obj == btnSearch) {
				boolean isCorrected = true;
				
				// Clue 입력 체크
				if (tfClue.getText().length() < 1) {
					msgClueConfig.setForeground(Color.red);
					msgClueConfig.setText("Please enter.");
					msgClueConfig.setVisible(true);
					isCorrected = true;
				} else {
					msgClueConfig.setVisible(false);
				}
				
				// 휴대폰 번호 체크
				if (tfPhone.getText().length() < 1) {
					msgPhoneConfig.setForeground(Color.red);
					msgPhoneConfig.setText("Please enter.");
					msgPhoneConfig.setVisible(true);
					isCorrected = false;
				} else {
					msgPhoneConfig.setVisible(false);
				}
				
				if (!isCorrected)
					return;
				
				String res = "";
				String succMsg = "";
				if (type == "id") {
					res = mDAO.getId(tfClue.getText(), tfPhone.getText());
					succMsg = "ID found!";
				} else if(type == "pw") {
					res = mDAO.getPw(tfClue.getText(), tfPhone.getText());
					succMsg = "Password found!";
				}
				
				boolean bSuccessed = false;
				if (res == null) { // 입력 정보가 일치하지 않을 시
					JOptionPane.showConfirmDialog(null, "No matching data. Please enter again.", "failed!", JOptionPane.DEFAULT_OPTION);
					bSuccessed = false;
				} else { // 입력 정보가 일치했을 시
					JOptionPane.showConfirmDialog(null, res, succMsg, JOptionPane.DEFAULT_OPTION);
					bSuccessed = true;
				}
				
				if (bSuccessed) {
					tfClue.setText("");
					tfPhone.setText("");
					msgClueConfig.setVisible(false);
					msgPhoneConfig.setVisible(false);
					setVisible(false);
				}
				
			} else if (obj == btnExit) {
				tfClue.setText("");
				tfPhone.setText("");
				msgClueConfig.setVisible(false);
				msgPhoneConfig.setVisible(false);
				setVisible(false);
			}
		}
	}

}
