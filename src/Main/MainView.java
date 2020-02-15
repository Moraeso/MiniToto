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
   public static String[] types= {"농구"};
   public String userId;
   public MyInfoPanel myInfoPanel;
   public UserRankPanel userRankPanel;
   public RecommendPanel recommendPanel;
   public TeamRankPanel teamRankPanel;
   
   public int code, nPoint= 50;
   public static String[] leagues= {"프로농구", "NBA", "여자프로농구"};
   public static Object[] teamRankTbHeader= {"순위", "로고", "팀", "경기수", "승률", "승", "패", "승차", "득점", "AS", "리바운드", "스틸", "블록", "3점슛", "자유투", "자유투성공"};
   
   public MemberDAO mDao;
   public BettingDAO bDao;
   public JSoupParser parser;
   
   public Vector<KBLTeamDTO> teamRanks;
   
   public MainView(JSoupParser parser, MemberDAO mDao, BettingDAO bDao)
   {
	  this.parser=parser;
      this.mDao=mDao;
      this.bDao=bDao;
      
      code = bDao.getMaxCode(); //현재 db에 있는 정보들 중 최대의 코드 값을 가져온다.
      
      manageL = new ManagementListener();
      
      bettingList = new ArrayList<BettingDTO>(); //현재 배팅 리스트
      matchList = new Vector<MatchDTO>(); //경기 리스트

      primaryPanel = new JPanel();
      primaryPanel.setLayout(null);
      primaryPanel.setPreferredSize(new Dimension(1200, 700));
      primaryPanel.setBackground(Color.white);
      this.add(primaryPanel);

      mainScene = new JPanel();
      mainScene.setLayout(null);
      mainScene.setBounds(0, 0, 1180, 580);
      mainScene.setBackground(Color.gray);

      // 팀 순위를 띄어주는 씬
      teamRankPanel = new TeamRankPanel(parser);
      teamRankPanel.setBounds(0, 0, 1180, 580);
      
      //탭으로 전환
      tab = new JTabbedPane();
      tab.setBounds(10, 100, 1180, 580);
      tab.setBackground(Color.white);
      tab.add("메인 화면", mainScene);
      tab.add("팀 순위", teamRankPanel);
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
      
      // 메인 패널 초기화
      initMainPanel();

      // 메인 패널에 경기 정보 출력
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
         // 경기가 있는 날에만 출력
         if (match.home != null)
            addMatch(match, teamRanks, MatchCategory.KBL);
      }
      
      schedulePanel.repaint();
      schedulePanel.revalidate();
      
      myInfoPanel.refreshInfo(userId);
      userRankPanel.refreshRank(userId);
   } //refreshMainVIew() 로그인 할 때 해당 id 의 정보로 업데이트하기 위한 메소드
   
   public void addMainViewListener(ActionListener btnL)
   {
      btnMain.addActionListener(btnL);
      btnMyPage.addActionListener(btnL);
      btnRanking.addActionListener(btnL);
      btnLogout.addActionListener(btnL);
   } //addMainViewListener()
   
   // 메인 화면 UI 컴포넌트 초기화
   public void initMainPanel() {
      manageL = new ManagementListener();

      schedulePanel = new JPanel();
      schedulePanel.setLayout(new BoxLayout(schedulePanel, BoxLayout.Y_AXIS));

      scroll = new JScrollPane(schedulePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      scroll.setBounds(10, 60, 600, 470);
      mainScene.add(scroll);
      
      // 검색 옵션 패널
      searchPanel=new JPanel(new FlowLayout());
      searchPanel.setBounds(10,10,600,40);
      searchPanel.setBackground(Color.DARK_GRAY);
      
      // year 옵션
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

      // 추천 패널
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

   // 경기 일정 출력 + 해당 MatchCategory에 대한 팀 랭킹 정보 가져오기
   public void showMatches(Vector<MatchDTO> matchLists, MatchCategory matchCategory) {

	  // 해당 리그의 2019년 경기 순위에 따른 팀 정보 반환
	  teamRanks=parser.getTeamRankTableData(matchCategory, "2020");
	  
      MatchDTO match = null;
      Calendar cal=Calendar.getInstance();

      schedulePanel.removeAll();
      
      for (int i = 0; i < matchLists.size(); i++) {
         match = matchLists.get(i);
         
         // 경기가 있는 날에만 출력
         if (match.home != null)
         {
            addMatch(match, teamRanks, matchCategory);
         }
      }
      
      schedulePanel.repaint();
      schedulePanel.revalidate();
   } // showMatches() 경기 리스트에 있는 경기들을 보여줌

   public void addMatch(MatchDTO match, Vector<KBLTeamDTO> teamRanks, MatchCategory category) {
	  // 팀 랭킹 정보 받아오기
      MatchPanel matchPanel = new MatchPanel(match, this, teamRanks, category);
      matchList.add(match);
      schedulePanel.add(matchPanel);
   } // addMatch() 경기 추가

   public void addbetting(BettingDTO betting)
   {
      bettingList.add(betting);
      btnbetting.setEnabled(true);
      SelectPanel newPanel = new SelectPanel(betting, this);
      bettingListPanel.add(newPanel);
      repaint();
   } //addbetting() 현재 배팅 리스트에 추가
   
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
   } //removebetting() 삭제 버튼 클릭시 해당 배팅 정보를 삭제
   
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
   } //removeAllbetting() 최종 배팅 클릭시 화면 초기화
   
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
            } //홈 팀 승
            
            else if(getData.result == 2)
            {
               double ratio = cntHome / (double)(cntHome+cntAway) + 1.5;
               totalRatio *= ratio;
            } //원정 팀 승
         } //for
         totalPnt = (int)(BettingPnt * totalRatio);
         return totalPnt;
      }//calculate() 현재 배팅 리스트를 바탕으로 예상 획득 가능 포인트를 보여주기 위한 메소드
      
   
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
               code++; //묶음 구분
               for(BettingDTO getData : bettingList)
               {
                  getData.bcode = code;
                  getData.pnt = nPoint;
                
               }
               bDao.newBetting(bettingList); //배팅 추가
               mDao.updatePnt(userId, (mDao.getPnt(userId)) - nPoint); //포인트 차감
               removeAllbetting(); //현재 배팅 리스트 초기화
            } //최종 배팅
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
            }//UP 버튼
            
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
            }//DOWN 버튼
            
            else if(obj == btnPredict)
            {
               int total = calculate(nPoint);
               lblCal.setText(" Points Earned Upon Success : " + total);
               //현재 상황에서 적중했을 경우의 점수를 보여준다.
            } //예측 버튼
            
            else if(obj == btnbettingList)
            {
               bettingPanel.setVisible(true);
               recommendPanel.setVisible(false);
            } // 배팅 버튼
            else if (obj == btnRecommend) {
               bettingPanel.setVisible(false);
               recommendPanel.setVisible(true);
            } // 추천 버튼
         else if(obj==searchOptionBtn)
         {
            matchList.clear(); //경기 리스트 초기화
            
            int year=Integer.parseInt(yearOptionCb.getSelectedItem().toString());
            int month=Integer.parseInt(monthOptionCb.getSelectedItem().toString());
            MatchType type=null; // 경기 종목
            MatchCategory category=null; // 리그 종류 
            
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
            
            // 해당 리그의 팀 순위 정보 받아오기
            teamRanks=parser.getTeamRankTableData(category, String.valueOf(year));
            
            Vector<MatchDTO> matchLists = parser.getMatchInfo(type, category, year, month);
            showMatches(matchLists, category);
            
         } // 검색 옵션 버튼
         
      } // actionPerformed()

   } // ManagementListener class
   
} // MainView class