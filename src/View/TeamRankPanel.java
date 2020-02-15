package View;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import DTO.KBLTeamDTO;
import Enum.MatchCategory;
import Enum.MatchType;
import Util.JSoupParser;

public class TeamRankPanel extends JPanel{
	
	private Vector<KBLTeamDTO> teamVector;
	private JLabel lblTitle;
	private JPanel rankViewPanel;
	private MatchType matchType;
	private MatchCategory matchCategory;
	private JSoupParser parser;
	
	// 검색 옵션
	private JComboBox typeOptionCb;
	private JComboBox leagueOptionCb;
	private JComboBox yearOptionCb;
	private JPanel searchPanel;
	private JButton searchBtn;
	private TeamRankListener teamRankListener;
	// 검색 옵션 내용
	public static String[] types= {"Basketball"};
	public static String[] leagues= {"Korean Basketball", "NBA", "Female Basketball"};
	public static String[] years= {"2019-2020", "2018-2019","2017-2018","2016-2017","2015-2016","2014-2015",
			"2013-2014","2012-2013","2011-2012","2010-2011", "2009-2010","2008-2009","2007-2008"};
	
	private JScrollPane scroll;
	
	public TeamRankPanel(JSoupParser parser) {
		this.parser=parser;
		
		setLayout(null);
		setBackground(Color.gray);
		setPreferredSize(new Dimension(1180, 580));
		
		teamVector = new Vector<KBLTeamDTO>();
		
		// 옵션 
		teamRankListener=new TeamRankListener();
		searchPanel=new JPanel(new FlowLayout());
		searchPanel.setBounds(300,20,600,40);
		searchPanel.setBackground(Color.DARK_GRAY);
		typeOptionCb=new JComboBox(types);
		typeOptionCb.setSelectedItem("Basketball");
		leagueOptionCb=new JComboBox(leagues);
		leagueOptionCb.setSelectedItem("Korean Basketball");
		yearOptionCb=new JComboBox(years);
		yearOptionCb.setSelectedItem("2019-2020");
		searchBtn=new JButton("Search");
		
		searchBtn.addActionListener(teamRankListener);
		
		searchPanel.add(typeOptionCb);
		searchPanel.add(leagueOptionCb);
		searchPanel.add(yearOptionCb);
		searchPanel.add(searchBtn);
		add(searchPanel);
		
		lblTitle = new JLabel("Team Ranking");
		lblTitle.setBounds(20,20,300,40);
		lblTitle.setFont(new Font("나눔고딕", Font.BOLD, 32));
		add(lblTitle);
		
		rankViewPanel = new JPanel();
		rankViewPanel.setBackground(Color.lightGray);
		rankViewPanel.setLayout(new BoxLayout(rankViewPanel, BoxLayout.Y_AXIS));
		scroll=new JScrollPane(rankViewPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBounds(30,80,1120,440);
		add(scroll);
		
		showTeamRank(parser, MatchCategory.KBL, "2020");
	}
	
	// 순위에 따른 팀 정보 출력
	public void showTeamRank(JSoupParser parser, MatchCategory category, String year) {
		
		teamVector = parser.getTeamRankTableData(category, year);
		
		TeamGradePanel pnlGradeSchema = new TeamGradePanel(null);
		rankViewPanel.removeAll();
		rankViewPanel.add(pnlGradeSchema);
		
		for (KBLTeamDTO teamInfo : teamVector) {
			TeamGradePanel pnlTeam = new TeamGradePanel(teamInfo);
			rankViewPanel.add(pnlTeam);
		}
		
		rankViewPanel.repaint();
		rankViewPanel.revalidate();
	}
	
	public class TeamRankListener implements ActionListener{

		String matchCategoryStr;
		String yearStr;
		MatchCategory category;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			matchCategoryStr=leagueOptionCb.getSelectedItem().toString();
			yearStr=yearOptionCb.getSelectedItem().toString().substring(5,9);
			
			if(matchCategoryStr.equals("Korean Basketball"))
			{
				category=MatchCategory.KBL;
			}else if(matchCategoryStr.equals("NBA"))
			{
				category=MatchCategory.NBA;
			}else if(matchCategoryStr.equals("Female Basketball"))
			{
				category=MatchCategory.WKBL;
			}
			
			showTeamRank(parser, category, yearStr);
		}
		
	}
}
