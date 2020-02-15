package Main;
import javax.swing.JFrame;

import DAO.BettingDAO;
import DAO.MemberDAO;
import Util.JSoupParser;

// view
// ���α׷� ��ü JFrame�� JPanel ȭ�� ��ȯ�� ���� view Ŭ����
public class SceneManager {

	JFrame frame=null;
	public LoginView loginView;
	public MainView mainView;
	public JSoupParser jSoupParser;
	
	// ���α׷� ȭ�� UI ����
	public void startUI(JSoupParser jSoupParser, MemberDAO mDao, BettingDAO bDao) {
		frame=new JFrame("��� ��� ���� ����");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(1200,700);
		frame.setVisible(true);
		
		if(loginView==null)
			loginView=new LoginView();
		if(mainView==null)
			mainView=new MainView(jSoupParser, mDao, bDao);
		// �α��� ȭ�� ����
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
		// ���� ȭ�� �ʱ�ȭ
		mainView.tab.setVisible(true);
		mainView.myInfoPanel.setVisible(false);
		mainView.userRankPanel.setVisible(false);
		
		frame.setContentPane(mainView);
//		frame.revalidate(); // frame.pack() if you want to resize()
		frame.pack();
	}

}
