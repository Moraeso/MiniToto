package DAO;

import java.sql.Connection;
//import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

import DTO.BettingDTO;
import DTO.MatchDTO;
import DTO.MemberDTO;
import Enum.MatchCategory;

public class BettingDAO {

   private String jdbcDriver = "com.mysql.cj.jdbc.Driver"; //dbdriver
   private String jdbcUrl = "jdbc:mysql://localhost/javadb?serverTimezone = UTC"; //dburl
   private Connection conn;
   
   private PreparedStatement pstmt; //preparedstatement
   private ResultSet rs; //result set
   
   private String sql; //sql 문
   
   public BettingDAO()
   {
      connectDB();
   } //bettingDAO()
   
   public void connectDB()
   {
      try {
         Class.forName(jdbcDriver);
         conn = DriverManager.getConnection(jdbcUrl, "root", "1234");
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } //catch   
   } //connectDB()
   
   public void closeDB()
   {
      try {
         pstmt.close();
         rs.close();
         conn.close();
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } //catch
   } //closeDB()
   
   public boolean newBetting(ArrayList<BettingDTO> bettingList)
   {
      sql = "insert into betting values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; //삽입 sql
      try {
         for(BettingDTO betting : bettingList)
         {
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, betting.bcode);
            pstmt.setString(2, betting.id);
            pstmt.setString(3, betting.home);
            pstmt.setString(4, betting.away);
            
            System.out.println("betting.home : " + betting.home);
            System.out.println("betting.away : " + betting.away);
            
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String str=dateFormat.format(betting.mdate).toString();
            pstmt.setString(5, str);
            pstmt.setString(6, betting.league);
            pstmt.setString(7, betting.type);
            pstmt.setInt(8, betting.result);
            pstmt.setBoolean(9, betting.correct);
            pstmt.setBoolean(10, betting.mstatus);
            pstmt.setInt(11, betting.pnt);
            pstmt.setString(12, betting.homeURL);
            pstmt.setString(13, betting.awayURL);
            pstmt.setInt(14, betting.homeScore);
            pstmt.setInt(15, betting.awayScore);
            pstmt.executeUpdate();   
         }
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      return true;
   } //newbetting() 삽입
   
   public boolean delBetting(String home, Date mdate)
   {
      sql = "delete from betting where home = ? and mdate = ?"; //삭제 sql
      try {
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, home); //인자로 받은 home team 이름으로 sql 설정
         SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm"); //날짜 형식 설정
         String str=dateFormat.format(mdate).toString();
         pstmt.setString(2, str);
         pstmt.executeUpdate(); 
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } //catch
      return true;
   } //delbetting() 삭제
   
   public ArrayList<BettingDTO> getUserBettingList(String id, boolean bFinished)
   {
      sql = "select * from betting where id = ? and mstatus = ?";
      ArrayList<BettingDTO> bettingList = new ArrayList<BettingDTO>();
      BettingDTO betting;
      try {
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, id); //id 
         pstmt.setBoolean(2, bFinished); //경기 전, 경기 후 구분
         rs = pstmt.executeQuery();
         while(rs.next())
         {
            betting = new BettingDTO();
            betting.bcode = rs.getInt("bcode");
            betting.id = rs.getString("id");
            betting.home = rs.getString("home");
            betting.away = rs.getString("away");
                        
            // sql의 datetime 변수를 java의 date형으로 변환하는 과정
            Timestamp myDateTime = rs.getTimestamp("mdate");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            TimeZone tz = TimeZone.getTimeZone("Greenwich");
            sdf.setTimeZone(tz);
            String str = sdf.format(myDateTime);
            
            Date date = new Date();
            try {
                //betting.mdate
                date = sdf.parse(str);
         } catch (ParseException e) {
            e.printStackTrace();
         }
            date.setHours(date.getHours() - 9); //한국 시간으로 조정
            betting.mdate = date;
            
            betting.league = rs.getString("league");
            betting.type = rs.getString("mtype");
            betting.result = rs.getInt("result");
            betting.correct = rs.getBoolean("correct");
            betting.mstatus = rs.getBoolean("mstatus");
            betting.pnt = rs.getInt("pnt");
            betting.homeURL = rs.getString("homeURL");
            betting.awayURL = rs.getString("awayURL");
            betting.homeScore = rs.getInt("homeScore");
            betting.awayScore = rs.getInt("awayScore");
            
            bettingList.add(betting);
            //얻은 betting 옮기기   
         }
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } //catch
      
      return bettingList;
   } //getbettingList() 해당 id 가 배팅한 내역을 리스트로 반환
   
   public ArrayList<BettingDTO> getUserBettingList(String id)
   {
      sql = "select * from betting where id = ?";
      ArrayList<BettingDTO> bettingList = new ArrayList<BettingDTO>();
      BettingDTO betting;
      try {
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, id); //id 
         rs = pstmt.executeQuery();
         while(rs.next())
         {
            betting = new BettingDTO();
            betting.bcode = rs.getInt("bcode");
            betting.id = rs.getString("id");
            betting.home = rs.getString("home");
            betting.away = rs.getString("away");
                        
            // sql의 datetime 변수를 java의 date형으로 변환하는 과정
            Timestamp myDateTime = rs.getTimestamp("mdate");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            TimeZone tz = TimeZone.getTimeZone("Greenwich");
            sdf.setTimeZone(tz);
            String str = sdf.format(myDateTime);
            
            Date date = new Date();
            try {
                //betting.mdate
                date = sdf.parse(str);
         } catch (ParseException e) {
            e.printStackTrace();
         }
            date.setHours(date.getHours() - 9); //한국 시간으로 조정
            betting.mdate = date;
            
            betting.league = rs.getString("league");
            betting.type = rs.getString("mtype");
            betting.result = rs.getInt("result");
            betting.correct = rs.getBoolean("correct");
            betting.mstatus = rs.getBoolean("mstatus");
            betting.pnt = rs.getInt("pnt");
            betting.homeURL = rs.getString("homeURL");
            betting.awayURL = rs.getString("awayURL");
            betting.homeScore = rs.getInt("homeScore");
            betting.awayScore = rs.getInt("awayScore");
            
            bettingList.add(betting);
            //얻은 betting 옮기기   
         }
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } //catch
      
      return bettingList;
   } //getbettingList() 해당 id 가 배팅한 내역을 리스트로 반환
   
   public ArrayList<BettingDTO> getTodayBettingList(String id)
   {
      sql = "select * from betting where correct = ? and id = ?  and mdate between ? and ?";
      ArrayList<BettingDTO> bettingList = new ArrayList<BettingDTO>();
      try {
         pstmt = conn.prepareStatement(sql);
         SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
         
         Date date=new Date();
         date.setHours(0);
         date.setMinutes(0);
         date.setSeconds(0);
         String str1=dateFormat.format(date).toString();
         
         date.setHours(23);
         date.setMinutes(59);
         date.setSeconds(59);
         String str2=dateFormat.format(date).toString();
        
         pstmt.setBoolean(1, true);
         pstmt.setString(2, id);
         pstmt.setString(3, str1);
         pstmt.setString(4, str2);
         rs = pstmt.executeQuery();
         while(rs.next())
         {
            BettingDTO betting = new BettingDTO();
            betting.bcode = rs.getInt("bcode");
            betting.id = rs.getString("id");
            betting.home = rs.getString("home");
            betting.away = rs.getString("away");
            betting.mdate = rs.getDate("mdate");
            betting.league = rs.getString("league");
            betting.type = rs.getString("mtype");
            betting.result = rs.getInt("result");
            betting.correct = rs.getBoolean("correct");
            betting.mstatus = rs.getBoolean("mstatus");
            betting.pnt = rs.getInt("pnt");
            betting.homeURL = rs.getString("homeURL");
            betting.awayURL = rs.getString("awayURL");
            betting.homeScore = rs.getInt("homeScore");
            betting.awayScore = rs.getInt("awayScore");
            
            bettingList.add(betting);
            //얻은 betting 옮기기   
         }
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } //catch
      
      return bettingList;
   } //getbettingList() 해당 id 가 오늘배팅한 내역을 리스트로 반환
   
   public ArrayList<BettingDTO> getCorrectList()
   {
	   ArrayList<BettingDTO> bettingList = new ArrayList<BettingDTO>();
	     
	   
	   return bettingList;
   }
   
   public int getHomeNum(String home, Date mdate)
   {
      int count = 0;
      
      sql = "select * from betting where home = ? and mdate = ? and result = ?";
      try {
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, home);
         SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
         String str=dateFormat.format(mdate).toString();
         pstmt.setString(2, str);
         pstmt.setInt(3, 1);
         rs = pstmt.executeQuery();
         
         while(rs.next())
         {
            count++;
         }
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      return count;
   } //getHomeNum() 홈 팀에 배팅한 사람의 수를 반환
   
   public int getAwayNum(String home, Date mdate)
   {
      int count = 0;
      
      sql = "select * from betting where home = ? and mdate = ? and result = ?";
      try {
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, home);
         SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
         String str=dateFormat.format(mdate).toString();
         pstmt.setString(2, str);
         pstmt.setInt(3, 2);
         rs = pstmt.executeQuery();
         
         while(rs.next())
         {
            count++;
         }
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      return count;
   } //getAwayNum() 원정 팀에 배팅한 사람의 수를 반환
   
   public int getMaxCode()
   {
      int max = 0;
      sql = "select max(bcode) from betting";
      try {
         pstmt = conn.prepareStatement(sql);
         rs = pstmt.executeQuery();
         if(rs.next())
         {
            max = rs.getInt(1);
         }
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      return max;
   } //getMaxCode() 배팅 정보들의 묶음을 구분하기 위해서 bcode를 설정하기 위해 현재 bcode로 설정된 값의 최대값을 db에서 가져온다.
   
   public ArrayList<String> getUsers(BettingDTO betting)
   {
      ArrayList<String> userList = new ArrayList<String>();
      sql = "select * from betting where home = ? and mdate = ? and mstatus = ?";
      try {
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, betting.home);
         SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
         String str=dateFormat.format(betting.mdate).toString();
         pstmt.setString(2, str);
         pstmt.setBoolean(3, false);
         rs = pstmt.executeQuery();
         while(rs.next())
         {
            userList.add(rs.getString("id"));
         }
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return userList;
   } //getUsers() 해당 경기에 배팅한 member들의 리스트를 반환
   
   public void createDummyBetting(int num, Vector<MatchDTO> matchList)
   {
	     int bcode;
	  	 String id; //사용자 id
		 String home; //홈 팀
	     String away; //원정 팀
		 Date mdate; //경기 날짜, 시간
		 String league; //대회 이름
		 String type; //경기 종목
		 int result; //예측 결과, 실제 경기 결과 (0 : 예상 전, 1 : 홈 승리 , 2 : 원정 승리)
		 boolean correct; //예측 성공 여부 (false : 실패 , true : 성공)
		 boolean mstatus; //경기 상태 (false : 경기 전 , true : 경기 후)
		 int pnt; //배팅된 금액
		 String homeURL; //홈 이미지 URL
		 String awayURL; //원정 이미지 URL
		 int homeScore; // 홈 점수
		 int awayScore; // 어웨이 점수
		 for(int i =0; i<num;i++)
		 {		 			                                                                       
			 int random = (int)(Math.random()*matchList.size());
			 bcode = i+1;
			 id = "dummy" + Integer.toString(i);
			 home = matchList.get(random).home;
			 away = matchList.get(random).away;
			 mdate = matchList.get(random).date.getTime();
			 league = "KBL";
			 type = "basketball";
			 result = (int)(Math.random()*2) + 1;
			 correct = false;
			 mstatus = false;
			 pnt = (int)(Math.random() * 50000) + 1000;
			 homeURL = "https://dthumb-phinf.pstatic.net/?src=https://imgsports.pstatic.net/images/emblem/new/kbl/default/35.png&type=f25_25&refresh=1";
			 awayURL = "https://dthumb-phinf.pstatic.net/?src=https://imgsports.pstatic.net/images/emblem/new/kbl/default/35.png&type=f25_25&refresh=1";
			 homeScore = 0;
			 awayScore = 0;
			 
			 sql = "insert into betting values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			 
			 try {
				 pstmt = conn.prepareStatement(sql);
				 pstmt.setInt(1, bcode);
				 pstmt.setString(2, id);
				 pstmt.setString(3, home);
				 pstmt.setString(4, away);
			     SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		         String str=dateFormat.format(mdate).toString();
		         pstmt.setString(5, str);
		         pstmt.setString(6, league);
		         pstmt.setString(7, type);
		         pstmt.setInt(8, result);
		         pstmt.setBoolean(9, correct);
		         pstmt.setBoolean(10, mstatus);
		         pstmt.setInt(11, pnt);
		         pstmt.setString(12, homeURL);
		         pstmt.setString(13, awayURL);
		         pstmt.setInt(14, homeScore);
		         pstmt.setInt(15, awayScore);
		         pstmt.executeUpdate();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		 }
   } //createDummy()
   
   public void setScore(MatchDTO match)
   {
	   sql = "update betting set homeScore = ?, awayScore = ? where home = ? and mdate = ?";
	   try {
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, match.homeScore);
		pstmt.setInt(2, match.awayScore);
		pstmt.setString(3, match.home);
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String str=dateFormat.format(match.date.getTime()).toString();
        pstmt.setString(4, str);
        pstmt.executeUpdate();
		
         
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   } //setScore() 실제 경기 결과를 해당 경기 배팅 정보에 삽입한다. 
   
   public boolean homeDecision(MatchDTO match)
   {
	   boolean bCorrect = false;
	   sql = "update betting set correct = ? where home = ? and mdate = ? and result = ? and mdate between ? and ?";
	   try {
		   pstmt = conn.prepareStatement(sql);
		if(match.homeScore > match.awayScore)
		{
			pstmt.setBoolean(1, true);
			bCorrect = true;
		} //홈 승
		else
		{
			pstmt.setBoolean(1, false);
			bCorrect = false;
		} //원정 승
		
		pstmt.setString(2, match.home);
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String str=dateFormat.format(match.date.getTime()).toString();
		pstmt.setString(3, str);
		pstmt.setInt(4, 1);
        
        Date date=new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        String str1=dateFormat.format(date).toString();
        
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        String str2=dateFormat.format(date).toString();
        pstmt.setString(5, str1);
        pstmt.setString(6, str2);
		pstmt.executeUpdate();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   return bCorrect;
   } //homeDecision 홈 팀에 배팅한 정보들의 배팅 성공 여부를 갱신한다.
   
   public boolean awayDecision(MatchDTO match)
   {
	   boolean bCorrect = false;
	   sql = "update betting set correct = ? where home = ? and mdate = ? and result = ? and mdate between ? and ?";
	   try {
		   pstmt = conn.prepareStatement(sql);
		if(match.homeScore > match.awayScore)
		{
			pstmt.setBoolean(1, false);
			bCorrect = false;
		} //홈 승
		else
		{
			pstmt.setBoolean(1, true);
			bCorrect = true;
		} //원정 승
		pstmt.setString(2, match.home);
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String str=dateFormat.format(match.date.getTime()).toString();
		pstmt.setString(3, str);
		pstmt.setInt(4, 2);
		Date date=new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        String str1=dateFormat.format(date).toString();
        
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        String str2=dateFormat.format(date).toString();
        pstmt.setString(5, str1);
        pstmt.setString(6, str2);
		pstmt.executeUpdate();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   return bCorrect;
   } //awayDecision() 원정 팀에 배팅한 배팅 정보들의 성공 여부를 갱신한다.
   
   public void setResult(MatchDTO match)
   {
	   sql = "update betting set result = ? where home = ? and mdate = ? and mdate between ? and ?";
	   try {
		   pstmt = conn.prepareStatement(sql);
		if(match.homeScore > match.awayScore)
		{
			pstmt.setInt(1, 1);
		} //홈 승
		else
		{
			pstmt.setInt(1, 2);
		} //원정 승
		
		pstmt.setString(2, match.home);
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String str=dateFormat.format(match.date.getTime()).toString();
		pstmt.setString(3, str);
		Date date=new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        String str1=dateFormat.format(date).toString();
        
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        String str2=dateFormat.format(date).toString();
        pstmt.setString(4, str1);
        pstmt.setString(5, str2);
		pstmt.executeUpdate();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   } //setResult() 예상 결과에서 실제로 결과로 변경한다
   
   public void setMatchFinish(MatchDTO match)
   {
	   sql = "update betting set mstatus = ? where home = ? and mdate = ? and mdate between ? and ?";
	   try {
		pstmt = conn.prepareStatement(sql);
		pstmt.setBoolean(1, true);
		pstmt.setString(2, match.home);
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String str=dateFormat.format(match.date.getTime()).toString();
		pstmt.setString(3, str);
		Date date=new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        String str1=dateFormat.format(date).toString();
        
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        String str2=dateFormat.format(date).toString();
        pstmt.setString(4, str1);
        pstmt.setString(5, str2);
		pstmt.executeUpdate();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   
   } //setMatchFinish 경기 상태를 종료 상태를 바꾼다.
   
   public ArrayList<Integer> getBcode()
   {
	   ArrayList<Integer> codeList = new ArrayList<Integer>();
	   sql="select * from betting where correct = ? and mdate between ? and ?";
	   try {
		pstmt = conn.prepareStatement(sql);
		pstmt.setBoolean(1, false);
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		Date date=new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        String str1=dateFormat.format(date).toString();
        
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        String str2=dateFormat.format(date).toString();
        pstmt.setString(2, str1);
        pstmt.setString(3, str2);
		rs = pstmt.executeQuery();
		
		while(rs.next())
		{
			codeList.add(rs.getInt(1));
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   
	   return codeList;
   } //getBcode() 묶음으로 배팅한 정보들의 처리를 위해 실패한 배팅 정보드르이 code를 ArrayList 로 가져온다.
   
   public void updateByCode(ArrayList<Integer> codeList)
   {
	   sql = "update betting set correct = ? where bcode = ?";
	   
	   for(int code : codeList)
	   {
		   try {
			   pstmt = conn.prepareStatement(sql);
			   pstmt.setBoolean(1, false);
			   pstmt.setInt(2, code);
			   pstmt.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	   }
   } //updateByCode() 실패한 정보들과 같은 코드를 가진 배팅정보들을 모두 실패 처리한다.
   
   public int countCorrect(String id, boolean bCorrect)
   {
	   int count = 0;
	   sql = "select * from betting where id = ? and correct = ? and mdate between ? and ?";
	   try {
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, id);
		pstmt.setBoolean(2, bCorrect);
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date=new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        String str1=dateFormat.format(date).toString();
        
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        String str2=dateFormat.format(date).toString();
        pstmt.setString(3, str1);
        pstmt.setString(4, str2);
		rs = pstmt.executeQuery();
		
		while(rs.next())
		{
			count++;
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  return count;
   } //countCorrect() 사용자가 오늘 적중 or 실패한 경기의 수를 센다.
} //bettingDAO class