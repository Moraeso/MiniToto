package Main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import DAO.BettingDAO;
import DAO.MemberDAO;
import DTO.BettingDTO;
import DTO.KBLTeamDTO;
import DTO.MatchDTO;
import Enum.MatchCategory;
import Enum.MatchType;
import Util.JSoupParser;
import View.MatchPanel;
import View.MyInfoPanel;
import View.RecommendPanel;
import View.SelectPanel;
import View.TeamRankPanel;
import View.UserRankPanel;

public class MainView extends JPanel{
   
   public JPanel primaryPanel;
   public JPanel mainScene, teamRankScene, playerRankScene;
   public JPanel schedulePanel, bettingPanel;
   public JPanel bettingListPanel, recommendListPanel;
   public JScrollPane scroll;
   public JTabbedPane tab;
   public JButton btnMain;
   public JButton btnMyPage;
   public JButton btnRanking;
   public JButton btnLogout;
   public JButton btnbettingList, btnRecommend, btnbetting, btnPredict, btnUp, btnDown;
   public JLabel lblUser, lblCal, lblPoint;
   public JButton btnPrev, btnNext;
   public ManagementListener manageL;
   public ArrayList<BettingDTO> bettingList;
   public Vector<MatchDTO> matchList;
   public ArrayList<BettingDTO> userbettingList;
   public JPanel searchPanel;
   public JComboBox monthOptionCb;
   public JComboBox yearOptionCb;
   public JComboBox typeOptionCb;
   public JComboBox leagueOptionCb;
   public JButton searchOptionBtn;
   public JPanel rankSearchPanel;
   public JPanel rankInfoPanel;
   
   public static String[] years= {"2019", "2020"};
   public static String[] months= {"12", "1", "2", "3"};
   public static String[] types= {"��"};
   public String userId;
   public MyInfoPanel myInfoPanel;
   public UserRankPanel userRankPanel;
   public RecommendPanel recommendPanel;
   public TeamRankPanel teamRankPanel;
   
   public int code, nPoint= 50;
   public static String[] leagues= {"���γ�", "NBA", "�������γ�"};
   public static Object[] teamRankTbHeader= {"����", "�ΰ�", "��", "����", "�·�", "��", "��", "����", "����", "AS", "���ٿ��", "��ƿ", "���", "3����", "������", "����������"};
   
   public MemberDAO mDao;
   public BettingDAO bDao;
   public JSoupParser parser;
   
   public Vector<KBLTeamDTO> teamRanks;
   
   public MainView(JSoupParser parser, MemberDAO mDao, BettingDAO bDao)
   {
	  this.parser=parser;
      this.mDao=mDao;
      this.bDao=bDao;
      
      code = bDao.getMaxCode(); //���� db�� �ִ� ������ �� �ִ��� �ڵ� ���� �����´�.
      
      manageL = new ManagementListener();
      
      bettingList = new ArrayList<BettingDTO>(); //���� ���� ����Ʈ
      matchList = new Vector<MatchDTO>(); //��� ����Ʈ

      primaryPanel = new JPanel();
      primaryPanel.setLayout(null);
      primaryPanel.setPreferredSize(new Dimension(1200, 700));
      primaryPanel.setBackground(Color.white);
      this.add(primaryPanel);

      mainScene = new JPanel();
      mainScene.setLayout(null);
      mainScene.setBounds(0, 0, 1180, 580);
      mainScene.setBackground(Color.gray);

      // �� ������ ����ִ� ��
      teamRankPanel = new TeamRankPanel(parser);
      teamRankPanel.setBounds(0, 0, 1180, 580);
      
      //������ ��ȯ
      tab = new JTabbedPane();
      tab.setBounds(10, 100, 1180, 580);
      tab.setBackground(Color.white);
      tab.add("���� ȭ��", mainScene);
      tab.add("�� ����", teamRankPanel);
      primaryPanel.add(tab);
      
      btnMain = new JButton("Main Scene");
      btnMain.setBounds(10, 30, 280, 30);
      primaryPanel.add(btnMain);

      btnMyPage = new JButton("My Info");
      btnMyPage.setBounds(310, 30, 280, 30);
      primaryPanel.add(btnMyPage);

      btnRanking = new JButton("Ranking");
      btnRanking.setBounds(610, 30, 280, 30);
      primaryPanel.add(btnRanking);

      btnLogout = new JButton("Logout");
      btnLogout.setBounds(910, 30, 280, 30);
      primaryPanel.add(btnLogout);
      
      myInfoPanel = new MyInfoPanel(mDao, bDao);
      myInfoPanel.setBounds(10, 100, 1180, 580);
      primaryPanel.add(myInfoPanel);
      
      userRankPanel = new UserRankPanel(mDao);
      userRankPanel.setBounds(10, 100, 1180, 580);
      primaryPanel.add(userRankPanel);
      
      // ���� �г� �ʱ�ȭ
      initMainPanel();

      // ���� �гο� ��� ���� ���
      showMatches(parser.getMatchInfo(MatchType.BASKETBALL, MatchCategory.KBL, 2019, 12), MatchCategory.KBL);      
      
      //mDao.createDummyMember(50);
   } // MainView()
   
   public void refreshMainView(String id)
   {
      userId = id;
      
      Vector<MatchDTO> matchLists = parser.getMatchInfo(MatchType.BASKETBALL, MatchCategory.KBL, 2019, 12);
      MatchDTO match = null;

      schedulePanel.removeAll();
      matchList.clear();
      for (int i = 0; i < matchLists.size(); i++) {
         match = matchLists.get(i);
         // ��Ⱑ �ִ� ������ ���
         if (match.home != null)
            addMatch(match, teamRanks, MatchCategory.KBL);
      }
      
      schedulePanel.repaint();
      schedulePanel.revalidate();
      
      myInfoPanel.refreshInfo(userId);
      userRankPanel.refreshRank(userId);
   } //refreshMainVIew() �α��� �� �� �ش� id �� ������ ������Ʈ�ϱ� ���� �޼ҵ�
   
   public void addMainViewListener(ActionListener btnL)
   {
      btnMain.addActionListener(btnL);
      btnMyPage.addActionListener(btnL);
      btnRanking.addActionListener(btnL);
      btnLogout.addActionListener(btnL);
   } //addMainViewListener()
   
   // ���� ȭ�� UI ������Ʈ �ʱ�ȭ
   public void initMainPanel() {
      manageL = new ManagementListener();

      schedulePanel = new JPanel();
      schedulePanel.setLayout(new BoxLayout(schedulePanel, BoxLayout.Y_AXIS));

      scroll = new JScrollPane(schedulePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      scroll.setBounds(10, 60, 600, 470);
      mainScene.add(scroll);
      
      // �˻� �ɼ� �г�
      searchPanel=new JPanel(new FlowLayout());
      searchPanel.setBounds(10,10,600,40);
      searchPanel.setBackground(Color.DARK_GRAY);
      
      // year �ɼ�
      yearOptionCb=new JComboBox(years);
      yearOptionCb.setSelectedItem("2019");
      monthOptionCb=new JComboBox(months);
      monthOptionCb.setSelectedItem("12");
      typeOptionCb=new JComboBox(types);
      leagueOptionCb=new JComboBox(leagues);
      leagueOptionCb.setSelectedItem("Basketball");
      searchOptionBtn=new JButton("Search");
      searchOptionBtn.addActionListener(manageL);
      
      searchPanel.add(typeOptionCb);
      searchPanel.add(leagueOptionCb);
      searchPanel.add(yearOptionCb);
      searchPanel.add(monthOptionCb);
      searchPanel.add(searchOptionBtn);

      mainScene.add(searchPanel);

      bettingPanel = new JPanel();
      bettingPanel.setBounds(620, 60, 540, 470);
      bettingPanel.setBackground(Color.white);
      mainScene.add(bettingPanel);

      btnbettingList = new JButton("Batting");
      btnbettingList.setBounds(620, 10, 260, 40);
      btnbettingList.addActionListener(manageL);
      mainScene.add(btnbettingList);

      btnRecommend = new JButton("Recommendation");
      btnRecommend.setBounds(900, 10, 260, 40);
      btnRecommend.addActionListener(manageL);
      mainScene.add(btnRecommend);

      bettingListPanel = new JPanel();
      bettingListPanel.setLayout(new GridLayout(6, 1));
      bettingListPanel.setBackground(Color.pink);
      bettingListPanel.setPreferredSize(new Dimension(505, 350));
      bettingPanel.add(bettingListPanel);

      JLabel lblHomeAway;
      lblHomeAway = new JLabel("Home                              Away");
      lblHomeAway.setHorizontalAlignment(SwingConstants.CENTER);
      lblHomeAway.setBorder(BorderFactory.createLineBorder(Color.black,2));
      bettingListPanel.add(lblHomeAway);
      
      btnUp = new JButton("UP");
      btnDown = new JButton("DOWN");
      btnDown.setEnabled(false);
      
      btnUp.addActionListener(manageL);
      btnDown.addActionListener(manageL);
      
      lblPoint = new JLabel("Bat Amount : " + nPoint);
      bettingPanel.add(lblPoint);
      
      bettingPanel.add(btnUp);
      bettingPanel.add(btnDown);
      
      lblCal = new JLabel(" Points Earned Upon Success : ");
      lblCal.setHorizontalAlignment(SwingConstants.CENTER);
      bettingPanel.add(lblCal);
      
      btnPredict = new JButton("Prediction");
      btnPredict.setEnabled(false);
      btnPredict.addActionListener(manageL);
      bettingPanel.add(btnPredict);
      
      btnbetting = new JButton("Final Bet");
      btnbetting.setPreferredSize(new Dimension(505, 50));
      btnbetting.addActionListener(manageL);
      bettingPanel.add(btnbetting);

      // ��õ �г�
      recommendPanel = new RecommendPanel(mDao, bDao);
      recommendPanel.setBounds(620, 60, 540, 470);
      recommendPanel.setVisible(false);
      mainScene.add(recommendPanel);

      btnPrev = new JButton("Prev");
      btnPrev.setPreferredSize(new Dimension(60, 20));
      btnNext = new JButton("Next");
      btnNext.setPreferredSize(new Dimension(60, 20));
      lblUser = new JLabel("User 1");

   } //initMainPanel()

   // ��� ���� ��� + �ش� MatchCategory�� ���� �� ��ŷ ���� ��������
   public void showMatches(Vector<MatchDTO> matchLists, MatchCategory matchCategory) {

	  // �ش� ������ 2019�� ��� ������ ���� �� ���� ��ȯ
	  teamRanks=parser.getTeamRankTableData(matchCategory, "2020");
	  
      MatchDTO match = null;
      Calendar cal=Calendar.getInstance();

      schedulePanel.removeAll();
      
      for (int i = 0; i < matchLists.size(); i++) {
         match = matchLists.get(i);
         
         // ��Ⱑ �ִ� ������ ���
         if (match.home != null)
         {
            addMatch(match, teamRanks, matchCategory);
         }
      }
      
      schedulePanel.repaint();
      schedulePanel.revalidate();
   } // showMatches() ��� ����Ʈ�� �ִ� ������ ������

   public void addMatch(MatchDTO match, Vector<KBLTeamDTO> teamRanks, MatchCategory category) {
	  // �� ��ŷ ���� �޾ƿ���
      MatchPanel matchPanel = new MatchPanel(match, this, teamRanks, category);
      matchList.add(match);
      schedulePanel.add(matchPanel);
   } // addMatch() ��� �߰�

   public void addbetting(BettingDTO betting)
   {
      bettingList.add(betting);
      btnbetting.setEnabled(true);
      SelectPanel newPanel = new SelectPanel(betting, this);
      bettingListPanel.add(newPanel);
      repaint();
   } //addbetting() ���� ���� ����Ʈ�� �߰�
   
   public void removebetting(BettingDTO betting)
   {
      bettingListPanel.removeAll();
      JLabel lblHomeAway;
      lblHomeAway = new JLabel("Home                          Away");
      lblHomeAway.setHorizontalAlignment(SwingConstants.CENTER);
      lblHomeAway.setBorder(BorderFactory.createLineBorder(Color.black,2));
      bettingListPanel.add(lblHomeAway);
      repaint();
      Iterator<BettingDTO> it = bettingList.iterator();
      while(it.hasNext())
      {
         BettingDTO getData = it.next();
         if(getData.home == betting.home && getData.mdate == betting.mdate)
         {
            it.remove();
            break;
         }
      }
      if(bettingList.isEmpty())
      {
         btnPredict.setEnabled(false);
         btnbetting.setEnabled(false);
         lblCal.setText(" Points Earned Upon Success : ");
      }
      for(BettingDTO getData : bettingList)
      {
         SelectPanel newPanel = new SelectPanel(getData, this);
         bettingListPanel.add(newPanel);
      }
   } //removebetting() ���� ��ư Ŭ���� �ش� ���� ������ ����
   
   public void removeAllbetting()
   {
      bettingListPanel.removeAll();
      bettingList.clear();
      btnPredict.setEnabled(false);
      btnbetting.setEnabled(false);
      lblCal.setText(" Points Earned Upon Success : ");
      JLabel lblHomeAway;
      lblHomeAway = new JLabel("Home                          Away");
      lblHomeAway.setHorizontalAlignment(SwingConstants.CENTER);
      lblHomeAway.setBorder(BorderFactory.createLineBorder(Color.black,2));
      bettingListPanel.add(lblHomeAway);
      repaint();
   } //removeAllbetting() ���� ���� Ŭ���� ȭ�� �ʱ�ȭ
   
   public int calculate(int point)
      {
         int BettingPnt = point;
         int totalPnt = 0;
         double totalRatio = 1;
         for(BettingDTO getData : bettingList)
         {
            int cntHome = bDao.getHomeNum(getData.home, getData.mdate);
            int cntAway = bDao.getAwayNum(getData.home, getData.mdate);
            if(cntHome == 0)
               cntHome = 1;
            if(cntAway == 0)
               cntAway = 1;
            if(getData.result == 1)
            {
               double ratio = (cntAway / (double)(cntHome+cntAway)) + 1.5; 
               totalRatio *= ratio;
            } //Ȩ �� ��
            
            else if(getData.result == 2)
            {
               double ratio = cntHome / (double)(cntHome+cntAway) + 1.5;
               totalRatio *= ratio;
            } //���� �� ��
         } //for
         totalPnt = (int)(BettingPnt * totalRatio);
         return totalPnt;
      }//calculate() ���� ���� ����Ʈ�� �������� ���� ȹ�� ���� ����Ʈ�� �����ֱ� ���� �޼ҵ�
      
   
   public void startView()
   {
      this.setVisible(true);
   } //startView()
   
   public class ManagementListener implements ActionListener{
      
      JSoupParser parser=new JSoupParser();
      
      @Override
      public void actionPerformed(ActionEvent event) {
         Object obj = event.getSource();

          if(obj == btnbetting)
            {   
               code++; //���� ����
               for(BettingDTO getData : bettingList)
               {
                  getData.bcode = code;
                  getData.pnt = nPoint;
                
               }
               bDao.newBetting(bettingList); //���� �߰�
               mDao.updatePnt(userId, (mDao.getPnt(userId)) - nPoint); //����Ʈ ����
               removeAllbetting(); //���� ���� ����Ʈ �ʱ�ȭ
            } //���� ����
            else if(obj == btnUp)
            {
               int maxLimit = 5000;
               if(nPoint < maxLimit)
               {
                  if(nPoint + 50 == maxLimit)
                  {
                     btnUp.setEnabled(false);
                  }
                  nPoint += 50;
               }
                lblPoint.setText("Bet Amount : " + nPoint);
                btnDown.setEnabled(true);
            }//UP ��ư
            
            else if(obj == btnDown)
            {
               int minLimit = 50;
               if(nPoint > minLimit)
               {
                  if(nPoint - 50 == minLimit)
                  {
                     btnDown.setEnabled(false);
                  }
                  nPoint -= 50;
               }
               lblPoint.setText("Bet Amount : " + nPoint);
               btnUp.setEnabled(true);
            }//DOWN ��ư
            
            else if(obj == btnPredict)
            {
               int total = calculate(nPoint);
               lblCal.setText(" Points Earned Upon Success : " + total);
               //���� ��Ȳ���� �������� ����� ������ �����ش�.
            } //���� ��ư
            
            else if(obj == btnbettingList)
            {
               bettingPanel.setVisible(true);
               recommendPanel.setVisible(false);
            } // ���� ��ư
            else if (obj == btnRecommend) {
               bettingPanel.setVisible(false);
               recommendPanel.setVisible(true);
            } // ��õ ��ư
         else if(obj==searchOptionBtn)
         {
            matchList.clear(); //��� ����Ʈ �ʱ�ȭ
            
            int year=Integer.parseInt(yearOptionCb.getSelectedItem().toString());
            int month=Integer.parseInt(monthOptionCb.getSelectedItem().toString());
            MatchType type=null; // ��� ����
            MatchCategory category=null; // ���� ���� 
            
            if(typeOptionCb.getSelectedItem().toString().equals("Basketball"))
            {
               type=MatchType.BASKETBALL;
            }
            
            if(leagueOptionCb.getSelectedItem().toString().equals("Korea Basketball"))
            {
               category=MatchCategory.KBL;
            }else if(leagueOptionCb.getSelectedItem().toString().equals("NBA"))
            {
               category=MatchCategory.NBA;
            }else if(leagueOptionCb.getSelectedItem().toString().equals("Female Basketball"))
            {
               category=MatchCategory.WKBL;
            }
            
            // �ش� ������ �� ���� ���� �޾ƿ���
            teamRanks=parser.getTeamRankTableData(category, String.valueOf(year));
            
            Vector<MatchDTO> matchLists = parser.getMatchInfo(type, category, year, month);
            showMatches(matchLists, category);
            
         } // �˻� �ɼ� ��ư
         
      } // actionPerformed()

   } // ManagementListener class
   
} // MainView class