package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import DAO.BettingDAO;
import DAO.MemberDAO;
import DTO.BettingDTO;
import DTO.MemberDTO;

public class RecommendPanel extends JPanel {

	private BettingDAO bDAO;
	private MemberDAO mDAO;
	
	private ButtonListener btnL;
	
	private JLabel[] lblUsers;
	private JPanel[] userPanels;
	private ArrayList<MemberDTO> userList;
	private ArrayList<BettingDTO> curBetList;
	private JScrollPane[] scrolls;
	
	private JButton btnPrev, btnNext;
	
	private int nowUserNum;
	
	public RecommendPanel(MemberDAO mDAO, BettingDAO bDAO) {
		setLayout(null);
		setBackground(Color.white);
		setPreferredSize(new Dimension(540, 470));
		
		this.bDAO = bDAO;
		this.mDAO = mDAO;
		
		btnL = new ButtonListener();
		
		userPanels = new JPanel[5];
		lblUsers = new JLabel[5];
		scrolls = new JScrollPane[5];
		for (int i = 0; i < 5; i++) {
			userPanels[i] = new JPanel();
			userPanels[i].setLayout(new BoxLayout(userPanels[i], BoxLayout.Y_AXIS));
			
			scrolls[i] = new JScrollPane(userPanels[i], JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrolls[i].setBounds(50,50,440,410);
			add(scrolls[i]);
			
			lblUsers[i] = new JLabel("user" + i);
			lblUsers[i].setFont(new Font("굴림", Font.BOLD, 16));
			userPanels[i].add(lblUsers[i]);
			
			//userPanels[i].setVisible(false);
		}
		scrolls[0].setVisible(true);
		
		userList = new ArrayList<MemberDTO>();
		curBetList = new ArrayList<BettingDTO>();
		
		btnPrev = new JButton("Prev");
		btnPrev.addActionListener(btnL);
		btnPrev.setBounds(160, 10, 70, 30);
		add(btnPrev);
		
		btnNext = new JButton("Next");
		btnNext.addActionListener(btnL);
		btnNext.setBounds(280, 10, 70, 30);
		add(btnNext);
		
		//refreshRecommend();
	}
	
	public void refreshRecommend() {
		int size, bcode = 0;
		float off = 0.0f;

		userList = mDAO.getUsersByDesc();

		// 총 5명의 유저의 정보 표시
		for (int i = 0; i < 5; i++) {
			off = 0.0f;
			bcode = 0;
			// 전체 지워줌
			userPanels[i].removeAll();

			String userId;
			// 가입 유저가 5명 미만일 때 에외처리
			if (userList.size() < i + 1) {

				userId = "no data";

				lblUsers[i].setText((i + 1) + "위 : " + userId);
				userPanels[i].add(lblUsers[i]);
			} else {
				// 해당 유저 ID 받아옴
				userId = userList.get(i).id;

				// 유저 Id Label 패널에 add
				lblUsers[i].setText((i + 1) + "위 : " + userId);
				userPanels[i].add(lblUsers[i]);

				// 해당 ID 유저의 현재 배팅 정보 받아옴
				curBetList = bDAO.getUserBettingList(userId, false);
				// 해당 ID 유저의 현재 배팅 정보 패널에 add
				for (BettingDTO betting : curBetList) {
					if (betting.bcode != bcode) {
						JPanel pnlEmpty = new JPanel();
						pnlEmpty.setLayout(null);
						pnlEmpty.setPreferredSize(new Dimension(430, 30));
						pnlEmpty.setBackground(Color.lightGray);
						userPanels[i].add(pnlEmpty);

						JLabel lblPnt = new JLabel("Batting Points : " + betting.pnt);
						lblPnt.setBounds(280, 5, 140, 30);
						pnlEmpty.add(lblPnt);
						off += 0.5;
					}
					bcode = betting.bcode;
					UserBetPanel userBetPanel = new UserBetPanel(betting, 1);
					userPanels[i].add(userBetPanel);
				}

				size = curBetList.size() + (int) off;
				if (size == 0) {
					JLabel lblNoData = new JLabel("The user did not bat today.");
					lblNoData.setFont(new Font("돋음", Font.PLAIN, 16));
					userPanels[i].add(lblNoData);
				}
				while (size < 6) { // BoxLayout로 발생하는 빈 공간 빈 Panel로 채워줌
					JPanel pnlEmpty = new JPanel();
					pnlEmpty.setPreferredSize(new Dimension(430, 60));
					pnlEmpty.setBackground(Color.lightGray);
					// pnlEmpty.setBorder(new TitledBorder(new LineBorder(Color.black), "1"));
					userPanels[i].add(pnlEmpty);
					size++;
				}
			}
		}
		scrolls[0].setVisible(true);
		nowUserNum = 0;
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			
			Object obj = event.getSource();
			
			// prev 클릭 이벤트
			if (obj == btnPrev && nowUserNum > 0) {
				scrolls[nowUserNum].setVisible(false);
				scrolls[nowUserNum - 1].setVisible(true);
				nowUserNum--;
			}
			// next 클릭 이벤트
			else if (obj == btnNext && nowUserNum < 4) {
				scrolls[nowUserNum].setVisible(false);
				scrolls[nowUserNum + 1].setVisible(true);
				nowUserNum++;
			}
		}
	}
}
