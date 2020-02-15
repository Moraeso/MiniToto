package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import DTO.BettingDTO;
import DTO.KBLTeamDTO;
import DTO.MatchDTO;
import DTO.NBATeamDTO;
import Enum.MatchCategory;
import Main.MainView;
//import Main.MainView.TimeTask;

public class MatchPanel extends JPanel {

	public JButton btnHome, btnAway, btnHomeInfo, btnAwayInfo;
	public JLabel lblTime, lblStadium, lblHomeIcon, lblAwayIcon;
	public String homePath, awayPath;
	public ImageIcon homeImage, awayImage;
	public MatchDTO match;
	public MainView view;
	Vector<KBLTeamDTO> teamKBLRanks;
	Vector<NBATeamDTO> teamNBARanks;
	MatchCategory category;

	public MatchPanel(MatchDTO match, MainView view, Vector<KBLTeamDTO> teamRanks, MatchCategory category) {
		setPreferredSize(new Dimension(580, 60));
		setBackground(Color.black);
		setBorder(BorderFactory.createLineBorder(Color.white));
		setLayout(null);

		this.category = category;
		this.match = match; // 경기 리스트
		this.view = view; // single tone pattern

		if (category != MatchCategory.NBA) {
			teamKBLRanks = teamRanks;
		} else {
			teamNBARanks = new Vector<NBATeamDTO>();
			for (int i = 0; i < teamRanks.size(); i++) {
				KBLTeamDTO kblTeam = teamRanks.get(i);
				NBATeamDTO nbaTeam = new NBATeamDTO();
				
				nbaTeam.rank=kblTeam.rank;
				nbaTeam.team=kblTeam.team;
				nbaTeam.division = kblTeam.gameCnt;
				nbaTeam.gameCnt = kblTeam.winRate;
				nbaTeam.win = kblTeam.win;
				nbaTeam.lose = kblTeam.lose;
				nbaTeam.winRate = kblTeam.winDiff;
				nbaTeam.winDiff = kblTeam.scoreAvg;
				nbaTeam.homeWin = kblTeam.assistAvg;
				nbaTeam.homeLose = kblTeam.reboundAvg;
				nbaTeam.awayWin = kblTeam.stealAvg;
				nbaTeam.awayLose = kblTeam.blockAvg;
				nbaTeam.divisionWin = kblTeam.point3Avg;
				nbaTeam.divisionLose = kblTeam.freeThrowAvg;
				nbaTeam.continueWin = kblTeam.freeThrowRate;
				nbaTeam.imgUrl = kblTeam.imgUrl;
				
				teamNBARanks.add(nbaTeam);
			}
		}

		btnHome = new JButton(match.home);
		btnHome.setBounds(10, 10, 120, 40);
		btnHome.addActionListener(new MatchListener());

		btnAway = new JButton(match.away);
		btnAway.setBounds(450, 10, 120, 40);
		btnAway.addActionListener(new MatchListener());

		btnHomeInfo = new JButton("i");
		btnHomeInfo.setBounds(130, 10, 40, 40); // 홈 팀정보
		btnHomeInfo.addActionListener(new MatchListener());

		btnAwayInfo = new JButton("i");
		btnAwayInfo.setBounds(410, 10, 40, 40); // 원정 팀정보
		btnAwayInfo.addActionListener(new MatchListener());

		homePath = "login_btn_normal.jpg";
		awayPath = "login_btn_normal.jpg";

		if (match.homeImgUrl.contentEquals("https://dthumb-phinf.pstatic.net/?src=https://imgsports.pstatic.net/images/emblem/new/kbl/33/01.png&type=f25_25&refresh=1")) {
			match.homeImgUrl = "https://dthumb-phinf.pstatic.net/?src=https://imgsports.pstatic.net/images/emblem/new/kbl/33/65.png&type=f25_25&refresh=1";
		}
		homeImage = new ImageIcon(getUrlImage(match.homeImgUrl));
		lblHomeIcon = new JLabel(homeImage);
		lblHomeIcon.setBounds(180, 10, 40, 40);

		if (match.awayImgUrl.contentEquals("https://dthumb-phinf.pstatic.net/?src=https://imgsports.pstatic.net/images/emblem/new/kbl/33/02.png&type=f25_25&refresh=1")) {
			match.awayImgUrl = "https://dthumb-phinf.pstatic.net/?src=https://imgsports.pstatic.net/images/emblem/new/kbl/33/65.png&type=f25_25&refresh=1";
		}
		awayImage = new ImageIcon(getUrlImage(match.awayImgUrl));
		lblAwayIcon = new JLabel(awayImage);
		lblAwayIcon.setBounds(360, 10, 40, 40);

		lblTime = new JLabel();

		String date = new SimpleDateFormat("MM / dd HH:mm").format(match.date.getTime());
		lblTime.setText(date);
		lblTime.setForeground(Color.cyan);
		lblTime.setBounds(240, 10, 100, 20);

		lblStadium = new JLabel(match.stadium);
		lblStadium.setForeground(Color.gray);
		lblStadium.setHorizontalAlignment(SwingConstants.CENTER);
		lblStadium.setBounds(230, 40, 120, 20);

		ArrayList<BettingDTO> userbettingList = view.bDao.getUserBettingList(view.userId);
		for (BettingDTO betting : userbettingList) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String str = dateFormat.format(match.date.getTimeInMillis());
			if (match.home.equals(betting.home) && dateFormat.format(betting.mdate.getTime()).equals(str)) {
				btnHome.setEnabled(false);
				btnHome.setBackground(Color.darkGray);
				btnAway.setEnabled(false);
				btnAway.setBackground(Color.darkGray);
			}
		} // 현재 사용자가 배팅한 경기들은 unabled 처리

		add(btnHome);
		add(btnHomeInfo);
		add(lblHomeIcon);
		add(lblTime);
		add(lblStadium);
		add(lblAwayIcon);
		add(btnAwayInfo);
		add(btnAway);
	} // MatchPanel()

	// 해당 Url의 이미지 파일을 불러와서 BufferedImage로 반환
	public Image getUrlImage(String urlStr) {
		URL url;
		try {
			url = new URL(urlStr);
			BufferedImage img = null;
			Image scaledImg = null;
			try {
				img = ImageIO.read(url);
				scaledImg = img.getScaledInstance(35, 35, Image.SCALE_DEFAULT);
			} catch (IOException e) {
				System.out.println("E: Image Load failed!");
			}
			return scaledImg;
		} catch (MalformedURLException e) {
			System.out.println("E: Connection Error!");
			return null;
		}
	} // getUrlImage()

	public class MatchListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			// TODO Auto-generated method stub

			Object obj = event.getSource();
			if (obj == btnHome || obj == btnAway) {
				if (view.bettingList.size() < 5) {
					for (MatchDTO match : view.matchList) {
						SimpleDateFormat dateFormat = new SimpleDateFormat("MM / dd HH:mm");
						String str = dateFormat.format(match.date.getTime());
						if (match.home.equals(btnHome.getText()) && lblTime.getText().equals(str)) {
							BettingDTO betting = new BettingDTO();
							betting.id = view.userId;
							betting.home = match.home;
							betting.away = match.away;
							betting.mdate = new Date(match.date.getTimeInMillis());
							betting.league = match.league;
							betting.type = match.type;
							if (obj == btnHome)
								betting.result = 1;
							else
								betting.result = 2;
							betting.correct = false;
							betting.mstatus = false;
							betting.awayURL = match.awayImgUrl;
							betting.homeURL = match.homeImgUrl;
							betting.homeScore = match.homeScore;
							betting.awayScore = match.awayScore;
							betting.homeScore = 0;
							betting.awayScore = 0;

							view.addbetting(betting); // 현재 배팅 리스트에 추가
							view.btnPredict.setEnabled(true);
							btnHome.setEnabled(false);
							btnAway.setEnabled(false);
							if (obj == btnHome)
								btnHome.setBackground(Color.yellow);
							else
								btnAway.setBackground(Color.yellow);
							break;
						}
					}
				} // 최대 5개 묶음 가능
			} // 홈

			else if (obj == btnHomeInfo) {
				JButton selectedBtn = (JButton) btnHomeInfo.getParent().getComponents()[0];
				if (category != MatchCategory.NBA)
					showKBLTeamInfoSmallBox(selectedBtn.getText());
				else
					showNBATeamInfoSmallBox(selectedBtn.getText());
			} // 홈 팀 정보
			else if (obj == btnAwayInfo) {
				JButton selectedBtn = (JButton) btnHomeInfo.getParent().getComponents()[7];
				if (category != MatchCategory.NBA)
					showKBLTeamInfoSmallBox(selectedBtn.getText());
				else
					showNBATeamInfoSmallBox(selectedBtn.getText());
			} // 원정 팀 정보

		} // actionPerformed()

		// 작은 창 띄우기
		private void showKBLTeamInfoSmallBox(String teamName) {
			for (int i = 0; i < teamKBLRanks.size(); i++) {
				if (teamKBLRanks.get(i).team.contains(teamName)) {
					new TeamInfoWindow(teamKBLRanks.get(i));
					break;
				}
			}
		}

		private void showNBATeamInfoSmallBox(String teamName) {
			for (int i = 0; i < teamNBARanks.size(); i++) {
				if (teamNBARanks.get(i).team.contains(teamName)) {
					new TeamInfoWindow(teamNBARanks.get(i));
					break;
				}
			}
		}

	} // MatchListener class

} // MatchPanel class
