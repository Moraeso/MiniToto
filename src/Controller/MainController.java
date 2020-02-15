package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import DAO.BettingDAO;
import DAO.MemberDAO;
import DTO.BettingDTO;
import DTO.MatchDTO;
import DTO.MemberDTO;
import Main.LoginView;
import Main.MainView;
import Main.SceneManager;
import Util.JSoupParser;

//controller
public class MainController {

	// view
	private static SceneManager sceneManager;
	public MainView mainView;
	public LoginView loginView;
	private MemberDAO memberDAO;
	private BettingDAO bettingDAO;
	LoginViewListener loginViewListener;
	MainViewListener mainViewListener;
	JSoupParser parser;
	public Timer timer;
	public TimeTask task;

	public MainController(SceneManager sceneManager) {
		this.sceneManager = sceneManager;

		memberDAO = new MemberDAO();
		bettingDAO = new BettingDAO();

		parser = new JSoupParser();
//		Vector<MatchDTO> matchLists = parser.getMatchInfo(MatchType.BASKETBALL, MatchCategory.KBL, 2019, 1);
		this.sceneManager.startUI(parser, memberDAO, bettingDAO);

		this.mainView = this.sceneManager.mainView;
		this.loginView = this.sceneManager.loginView;

		bindListener();

		timer = new Timer();
		task = new TimeTask();
		Date date = new Date();
		date.setHours(20);
		date.setMinutes(10);
		date.setSeconds(0);
		timer.schedule(task, date, 86400000); // schedule(시간이 되면 TimeTask의 run()메소드 호출, 발생할 시간, 반복되는 시간)
		// 프로그램 시작 날짜의 23:59:59 부터 24시간 마다 task가 수행됨
	}

	// 리스너 생성
	public void bindListener() {
		loginViewListener = new LoginViewListener(this.sceneManager.loginView, memberDAO);
		this.loginView.addLoginViewListener(loginViewListener);

		mainViewListener = new MainViewListener(this.sceneManager.mainView, memberDAO, bettingDAO);
		this.mainView.addMainViewListener(mainViewListener);

	}

	// 이너 클래스 - TimerTask
	public class TimeTask extends TimerTask {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("main view run....");
			ArrayList<MemberDTO> memberList = memberDAO.getUsersByDesc(); // 전체 유저 리스트
			Iterator it = mainView.matchList.iterator();

			while (it.hasNext()) {
				MatchDTO match = (MatchDTO) it.next();
				bettingDAO.setScore(match); // 스코어 갱신
				bettingDAO.homeDecision(match); // 홈 배팅 성공 여부 경신
				bettingDAO.awayDecision(match); // 원정 배팅 성공 여부 경신
				bettingDAO.setMatchFinish(match); // 경기 종료 상태
			}

			it = memberList.listIterator();
			while (it.hasNext()) {
				MemberDTO member = (MemberDTO) it.next();
				memberDAO.updatePnt(member.id, (memberDAO.getPnt(member.id)) + CalculateID(member.id)); // 포인트 갱신
				for (int i = 0; i < bettingDAO.countCorrect(member.id, true); i++) {
					memberDAO.increaseWin(member.id); // 적중 성공
				}
				for (int i = 0; i < bettingDAO.countCorrect(member.id, false); i++) {
					memberDAO.increaseLose(member.id); // 적중 실패
				}
			}
			bettingDAO.updateByCode(bettingDAO.getBcode()); // 묶여있는 경기들의 성공 여부 재갱신
			it = mainView.matchList.iterator();

			while (it.hasNext()) {
				MatchDTO match = (MatchDTO) it.next();
				bettingDAO.setResult(match); // 실제 경기 결과로 갱신
			}
		} // run()

		public int CalculateID(String id) {
			int bettingPnt; // 사용자의 배팅금액
			int totalPnt = 0; // 획득할 금액
			double totalRatio = 1; // 배당률
			int code;
			ArrayList<BettingDTO> userCurrentList = bettingDAO.getTodayBettingList(id); // 해당 id의 현재 적중한 리스트
			ArrayList<Integer> pointList = new ArrayList<Integer>();

			if (userCurrentList.isEmpty()) {
				return 0;
			} // 없을 경우
			code = userCurrentList.get(0).bcode; // 코드 초기화
			bettingPnt = userCurrentList.get(0).pnt; // 사용자가 배팅한 포인트

			for (BettingDTO getData : userCurrentList) {
				int cntHome = bettingDAO.getHomeNum(getData.home, getData.mdate);
				int cntAway = bettingDAO.getAwayNum(getData.home, getData.mdate);
				// 현재 해당 경기에 배팅한 사람 수
				if (cntHome == 0)
					cntHome = 1;
				if (cntAway == 0)
					cntAway = 1;
				// 0일 시 임의로 1을 준다.
				if (code == getData.bcode) {
					if (getData.result == 1) {
						double ratio = (cntAway / (double) (cntHome + cntAway)) + 1.5; // (배당율 = 상대편 사람 수 / 전체 사람 수) +
																						// 1.5
						totalRatio *= ratio; // 묶을 경우 곱하기를 하여 묶지 않을 경우보다 더 많은 배당율을 주게 함
					} // 홈 팀 승

					else if (getData.result == 2) {
						double ratio = cntHome / (double) (cntHome + cntAway) + 1.5;
						totalRatio *= ratio;
					} // 원정 팀 승
					totalPnt += (int)(bettingPnt * totalRatio);
				} // 같은 묶음
				else {
					code = getData.bcode; // 새로운 코드 갱신
					bettingPnt = getData.pnt; // 새로운 배팅 포인트 갱신

					totalRatio = 1; // 배당율 초기화
					if (getData.result == 1) {
						double ratio = (cntAway / (double) (cntHome + cntAway)) + 1.5;
						totalRatio *= ratio;
					} // 홈 팀 승

					else if (getData.result == 2) {
						double ratio = cntHome / (double) (cntHome + cntAway) + 1.5;
						totalRatio *= ratio;
					} // 원정 팀 승
					totalPnt += (int)(bettingPnt * totalRatio);
				} // 다른 묶음
			} // for
			return totalPnt;
		}// Calculate() 오늘 사용자가 배팅한 리스트를 바탕으로 얻게 되는 전체 포인트를 계산하여 반환
	} // TimeTask class

	// 이너 클래스 - 로그인뷰 리스너
	class LoginViewListener implements ActionListener {

		LoginView loginView;
		MemberDAO memberDAO;

		public LoginViewListener(LoginView loginView, MemberDAO memberDAO) {
			this.loginView = loginView;
			this.memberDAO = memberDAO;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Object obj = e.getSource();

			if (obj == loginView.btnLogin) {

				boolean bLogin = memberDAO.loginMember(loginView.tfId.getText(), loginView.tfPw.getText());
				if (bLogin) {

					mainView.userId = loginView.tfId.getText();
					sceneManager.mainView(loginView.tfId.getText());

				} else {
					loginView.lblMsg.setText("로그인 실패!");
				}
			} else if (obj == loginView.btnSignup) {
				loginView.signupPopup.setVisible(true);

			} else if (obj == loginView.btnSearchId) {
				loginView.searchIdPopup.setVisible(true);

			} else if (obj == loginView.btnSearchPw) {
				loginView.searchPwPopup.setVisible(true);
			}
		}
	}

	// 이너 클래스 - 메인뷰 리스너
	class MainViewListener implements ActionListener {

		MainView mainView;
		MemberDAO memberDAO;
		BettingDAO bettingDAO;

		public MainViewListener(MainView mainView, MemberDAO memberDAO, BettingDAO bettingDAO) {
			this.mainView = mainView;
			this.memberDAO = memberDAO;
			this.bettingDAO = bettingDAO;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Object obj = e.getSource();
			// 메인 화면
			if (obj.equals(mainView.btnMain)) {
				mainView.tab.setVisible(true);
				mainView.myInfoPanel.setVisible(false);
				mainView.userRankPanel.setVisible(false);
			}
			// 마이 페이지
			else if (obj.equals(mainView.btnMyPage)) {
				mainView.myInfoPanel.refreshInfo(mainView.userId);
				mainView.tab.setVisible(false);
				mainView.myInfoPanel.setVisible(true);
				mainView.userRankPanel.setVisible(false);
			}
			// 랭킹
			else if (obj.equals(mainView.btnRanking)) {
				mainView.userRankPanel.refreshRank(mainView.userId);
				mainView.tab.setVisible(false);
				mainView.myInfoPanel.setVisible(false);
				mainView.userRankPanel.setVisible(true);
			}
			// 로그아웃
			else if (obj.equals(mainView.btnLogout)) {
				sceneManager.loginView();

			}
		}
	}

	// 시작 실행부
	public static void main(String[] args) {

		MainController mainController = new MainController(new SceneManager());

	} // main()

} // Program class
