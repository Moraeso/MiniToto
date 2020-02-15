package View;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import DAO.BettingDAO;
import DAO.MemberDAO;
import DTO.BettingDTO;
import DTO.MemberDTO;

public class MyInfoPanel extends JPanel {
	
	private MemberDAO mDAO;
	private BettingDAO bDAO;
	private String userId;
	private MemberDTO user;
	
	private JLabel lblCurBet, lblRecBet, lblUserInfo;
	private JPanel curBetPanel, recBetPanel, userInfoPanel;
	
	private JLabel lblId, lblName, lblSex, lblLvl, lblPnt, lblMatchCnt, lblWin, lblLose, lblHitRate;
	
	private ArrayList<BettingDTO> curBetList;
	private ArrayList<BettingDTO> recBetList;
	
	public MyInfoPanel(MemberDAO mDAO, BettingDAO bDAO) {
		setLayout(null);
		setSize(1180, 580);
		setBackground(Color.gray);
		
		this.mDAO = mDAO;
		this.bDAO = bDAO;
		
		curBetList = new ArrayList<BettingDTO>();		
		recBetList = new ArrayList<BettingDTO>();
		
		initCurBetUI();
		initRecBetUI();
		initUserInfoUI();
	}
	
	public void initCurBetUI() {
		lblCurBet = new JLabel("Now Betting");
		lblCurBet.setBounds(20,15,300,40);
		lblCurBet.setFont(new Font("³ª´®°íµñ", Font.BOLD, 32));
		add(lblCurBet);
		
		curBetPanel = new JPanel();
		curBetPanel.setLayout(new BoxLayout(curBetPanel, BoxLayout.Y_AXIS));
		curBetPanel.setBackground(Color.lightGray);
		JScrollPane scroll = new JScrollPane(curBetPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBounds(10,70,430,500);
		add(scroll);
		
	}
	public void initRecBetUI() {
		lblRecBet = new JLabel("Record");
		lblRecBet.setBounds(460,15,300,40);
		lblRecBet.setFont(new Font("³ª´®°íµñ", Font.BOLD, 32));
		add(lblRecBet);
		
		recBetPanel = new JPanel();
		recBetPanel.setLayout(new BoxLayout(recBetPanel, BoxLayout.Y_AXIS));
		recBetPanel.setBackground(Color.lightGray);
		JScrollPane scroll = new JScrollPane(recBetPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBounds(450,70,430,500);
		add(scroll);
	}
	public void initUserInfoUI() {
		lblUserInfo = new JLabel("My Information");
		lblUserInfo.setBounds(900,15,270,40);
		lblUserInfo.setFont(new Font("³ª´®°íµñ", Font.BOLD, 32));
		add(lblUserInfo);
		
		userInfoPanel = new JPanel();
		userInfoPanel.setBackground(Color.lightGray);
		userInfoPanel.setBounds(890,70,280,500);
		userInfoPanel.setLayout(null);
		add(userInfoPanel);
		
		lblId = new JLabel("ID : ");
		lblId.setBounds(10,10,250,40);
		lblId.setFont(new Font("µ¸À½", Font.BOLD, 18));
		userInfoPanel.add(lblId);
		
		lblName = new JLabel("Name : ");
		lblName.setBounds(10,50,250,40);
		lblName.setFont(new Font("µ¸À½", Font.BOLD, 18));
		userInfoPanel.add(lblName);
		
		lblSex = new JLabel("Gender : ");
		lblSex.setBounds(10,90,250,40);
		lblSex.setFont(new Font("µ¸À½", Font.BOLD, 18));
		userInfoPanel.add(lblSex);
		
		lblLvl = new JLabel("Level : ");
		lblLvl.setBounds(10,130,250,40);
		lblLvl.setFont(new Font("µ¸À½", Font.BOLD, 18));
		userInfoPanel.add(lblLvl);
		
		lblPnt = new JLabel("Points : ");
		lblPnt.setBounds(10,170,250,40);
		lblPnt.setFont(new Font("µ¸À½", Font.BOLD, 18));
		userInfoPanel.add(lblPnt);
		
		lblMatchCnt = new JLabel("Total bet : ");
		lblMatchCnt.setBounds(10,210,250,40);
		lblMatchCnt.setFont(new Font("µ¸À½", Font.BOLD, 18));
		userInfoPanel.add(lblMatchCnt);
		
		lblWin = new JLabel("Hit : ");
		lblWin.setBounds(10,250,250,40);
		lblWin.setFont(new Font("µ¸À½", Font.BOLD, 18));
		userInfoPanel.add(lblWin);
		
		lblLose = new JLabel("Failure : ");
		lblLose.setBounds(10,290,250,40);
		lblLose.setFont(new Font("µ¸À½", Font.BOLD, 18));
		userInfoPanel.add(lblLose);
		
		lblHitRate = new JLabel("Hit rate : ");
		lblHitRate.setBounds(10,330,250,40);
		lblHitRate.setFont(new Font("µ¸À½", Font.BOLD, 18));
		userInfoPanel.add(lblHitRate);
	}
	public void refreshInfo(String id) {
		
		int size, bcode = 0;
		float off = 0.0f;
		
		this.userId = id;
		
		// ÇöÀç ¹èÆÃ refresh
		curBetPanel.removeAll();
		
		curBetList = bDAO.getUserBettingList(userId, false);
		for (BettingDTO betting : curBetList) {
			if (betting.bcode != bcode) {
				JPanel pnlEmpty = new JPanel();
				pnlEmpty.setLayout(null);
				pnlEmpty.setPreferredSize(new Dimension(430, 30));
				pnlEmpty.setBackground(Color.lightGray);
				curBetPanel.add(pnlEmpty);
				
				JLabel lblPnt = new JLabel("Bating point : " + betting.pnt);
				lblPnt.setBounds(280, 5, 140, 30);
				pnlEmpty.add(lblPnt);
				off += 0.5;
			}
			bcode = betting.bcode;
			UserBetPanel userBetPanel = new UserBetPanel(betting, 1);
			curBetPanel.add(userBetPanel);
		}
		
		size = curBetList.size() + (int)off;
		while (size != 0 && size < 8) {
			JPanel pnlEmpty = new JPanel();
			pnlEmpty.setPreferredSize(new Dimension(430, 60));
			pnlEmpty.setBackground(Color.lightGray);
			//pnlEmpty.setBorder(new TitledBorder(new LineBorder(Color.black), "1"));
			curBetPanel.add(pnlEmpty);
			size++;
		}
		
		// ÀüÃ¼ ÀüÀû refresh
		recBetPanel.removeAll();
		
		recBetList = bDAO.getUserBettingList(userId, true);
		for (BettingDTO betting : recBetList) {
			UserBetPanel userBetPanel = new UserBetPanel(betting, 2);
			recBetPanel.add(userBetPanel);
		}
		
		size = recBetList.size();
		
		while (size != 0 && size < 8) {
			JPanel pnlEmpty = new JPanel();
			pnlEmpty.setPreferredSize(new Dimension(430, 60));
			pnlEmpty.setBackground(Color.lightGray);
			//pnlEmpty.setBorder(new TitledBorder(new LineBorder(Color.black), "1"));
			recBetPanel.add(pnlEmpty);
			size++;
		}
		
		// À¯Àú Á¤º¸ refresh
		user = mDAO.getUser(userId);
		lblId.setText("ID : " + user.id);
		lblName.setText("Name : " + user.name);
		lblSex.setText("Gender : " + user.sex);
		lblLvl.setText("Level : " + user.lvl);
		lblPnt.setText("Points : " + user.pnt);
		lblMatchCnt.setText("Total bet : " + user.matchCnt);
		lblWin.setText("Hit : " + user.win);
		lblLose.setText("Failure : " + user.lose);
		lblHitRate.setText("Hit rate : " + Float.toString(user.hitRate) + "%");
	}
	
}