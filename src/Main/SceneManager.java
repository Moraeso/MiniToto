package Main;
import javax.swing.JFrame;

import DAO.BettingDAO;
import DAO.MemberDAO;
import Util.JSoupParser;

// view
// 프로그램 전체 JFrame에 JPanel 화면 전환을 위한 view 클래스
public class SceneManager {

	JFrame frame=null;
	public LoginView loginView;
	public MainView mainView;
	public JSoupParser jSoupParser;
	
	// 프로그램 화면 UI 시작
	public void startUI(JSoupParser jSoupParser, MemberDAO mDao, BettingDAO bDao) {
		frame=new JFrame("경기 결과 배팅 게임");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(1200,700);
		frame.setVisible(true);
		
		if(loginView==null)
			loginView=new LoginView();
		if(mainView==null)
			mainView=new MainView(jSoupParser, mDao, bDao);
		// 로그인 화면 시작
		loginView();
	}
	
	public void loginView()
	{
		frame.setContentPane(loginView);
//		frame.revalidate(); // frame.pack() if you want to resize()
		frame.pack();
	}

	public void mainView(String userId){
		
		mainView.refreshMainView(userId);
		mainView.recommendPanel.refreshRecommend();
		// 메인 화면 초기화
		mainView.tab.setVisible(true);
		mainView.myInfoPanel.setVisible(false);
		mainView.userRankPanel.setVisible(false);
		
		frame.setContentPane(mainView);
//		frame.revalidate(); // frame.pack() if you want to resize()
		frame.pack();
	}

}
