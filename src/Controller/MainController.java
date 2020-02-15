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
		timer.schedule(task, date, 86400000); // schedule(�ð��� �Ǹ� TimeTask�� run()�޼ҵ� ȣ��, �߻��� �ð�, �ݺ��Ǵ� �ð�)
		// ���α׷� ���� ��¥�� 23:59:59 ���� 24�ð� ���� task�� �����
	}

	// ������ ����
	public void bindListener() {
		loginViewListener = new LoginViewListener(this.sceneManager.loginView, memberDAO);
		this.loginView.addLoginViewListener(loginViewListener);

		mainViewListener = new MainViewListener(this.sceneManager.mainView, memberDAO, bettingDAO);
		this.mainView.addMainViewListener(mainViewListener);

	}

	// �̳� Ŭ���� - TimerTask
	public class TimeTask extends TimerTask {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("main view run....");
			ArrayList<MemberDTO> memberList = memberDAO.getUsersByDesc(); // ��ü ���� ����Ʈ
			Iterator it = mainView.matchList.iterator();

			while (it.hasNext()) {
				MatchDTO match = (MatchDTO) it.next();
				bettingDAO.setScore(match); // ���ھ� ����
				bettingDAO.homeDecision(match); // Ȩ ���� ���� ���� ���
				bettingDAO.awayDecision(match); // ���� ���� ���� ���� ���
				bettingDAO.setMatchFinish(match); // ��� ���� ����
			}

			it = memberList.listIterator();
			while (it.hasNext()) {
				MemberDTO member = (MemberDTO) it.next();
				memberDAO.updatePnt(member.id, (memberDAO.getPnt(member.id)) + CalculateID(member.id)); // ����Ʈ ����
				for (int i = 0; i < bettingDAO.countCorrect(member.id, true); i++) {
					memberDAO.increaseWin(member.id); // ���� ����
				}
				for (int i = 0; i < bettingDAO.countCorrect(member.id, false); i++) {
					memberDAO.increaseLose(member.id); // ���� ����
				}
			}
			bettingDAO.updateByCode(bettingDAO.getBcode()); // �����ִ� ������ ���� ���� �簻��
			it = mainView.matchList.iterator();

			while (it.hasNext()) {
				MatchDTO match = (MatchDTO) it.next();
				bettingDAO.setResult(match); // ���� ��� ����� ����
			}
		} // run()

		public int CalculateID(String id) {
			int bettingPnt; // ������� ���ñݾ�
			int totalPnt = 0; // ȹ���� �ݾ�
			double totalRatio = 1; // ����
			int code;
			ArrayList<BettingDTO> userCurrentList = bettingDAO.getTodayBettingList(id); // �ش� id�� ���� ������ ����Ʈ
			ArrayList<Integer> pointList = new ArrayList<Integer>();

			if (userCurrentList.isEmpty()) {
				return 0;
			} // ���� ���
			code = userCurrentList.get(0).bcode; // �ڵ� �ʱ�ȭ
			bettingPnt = userCurrentList.get(0).pnt; // ����ڰ� ������ ����Ʈ

			for (BettingDTO getData : userCurrentList) {
				int cntHome = bettingDAO.getHomeNum(getData.home, getData.mdate);
				int cntAway = bettingDAO.getAwayNum(getData.home, getData.mdate);
				// ���� �ش� ��⿡ ������ ��� ��
				if (cntHome == 0)
					cntHome = 1;
				if (cntAway == 0)
					cntAway = 1;
				// 0�� �� ���Ƿ� 1�� �ش�.
				if (code == getData.bcode) {
					if (getData.result == 1) {
						double ratio = (cntAway / (double) (cntHome + cntAway)) + 1.5; // (����� = ����� ��� �� / ��ü ��� ��) +
																						// 1.5
						totalRatio *= ratio; // ���� ��� ���ϱ⸦ �Ͽ� ���� ���� ��캸�� �� ���� ������� �ְ� ��
					} // Ȩ �� ��

					else if (getData.result == 2) {
						double ratio = cntHome / (double) (cntHome + cntAway) + 1.5;
						totalRatio *= ratio;
					} // ���� �� ��
					totalPnt += (int)(bettingPnt * totalRatio);
				} // ���� ����
				else {
					code = getData.bcode; // ���ο� �ڵ� ����
					bettingPnt = getData.pnt; // ���ο� ���� ����Ʈ ����

					totalRatio = 1; // ����� �ʱ�ȭ
					if (getData.result == 1) {
						double ratio = (cntAway / (double) (cntHome + cntAway)) + 1.5;
						totalRatio *= ratio;
					} // Ȩ �� ��

					else if (getData.result == 2) {
						double ratio = cntHome / (double) (cntHome + cntAway) + 1.5;
						totalRatio *= ratio;
					} // ���� �� ��
					totalPnt += (int)(bettingPnt * totalRatio);
				} // �ٸ� ����
			} // for
			return totalPnt;
		}// Calculate() ���� ����ڰ� ������ ����Ʈ�� �������� ��� �Ǵ� ��ü ����Ʈ�� ����Ͽ� ��ȯ
	} // TimeTask class

	// �̳� Ŭ���� - �α��κ� ������
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
					loginView.lblMsg.setText("�α��� ����!");
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

	// �̳� Ŭ���� - ���κ� ������
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
			// ���� ȭ��
			if (obj.equals(mainView.btnMain)) {
				mainView.tab.setVisible(true);
				mainView.myInfoPanel.setVisible(false);
				mainView.userRankPanel.setVisible(false);
			}
			// ���� ������
			else if (obj.equals(mainView.btnMyPage)) {
				mainView.myInfoPanel.refreshInfo(mainView.userId);
				mainView.tab.setVisible(false);
				mainView.myInfoPanel.setVisible(true);
				mainView.userRankPanel.setVisible(false);
			}
			// ��ŷ
			else if (obj.equals(mainView.btnRanking)) {
				mainView.userRankPanel.refreshRank(mainView.userId);
				mainView.tab.setVisible(false);
				mainView.myInfoPanel.setVisible(false);
				mainView.userRankPanel.setVisible(true);
			}
			// �α׾ƿ�
			else if (obj.equals(mainView.btnLogout)) {
				sceneManager.loginView();

			}
		}
	}

	// ���� �����
	public static void main(String[] args) {

		MainController mainController = new MainController(new SceneManager());

	} // main()

} // Program class
