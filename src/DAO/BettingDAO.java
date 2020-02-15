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
   
   private String sql; //sql ��
   
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
      sql = "insert into betting values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; //���� sql
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
   } //newbetting() ����
   
   public boolean delBetting(String home, Date mdate)
   {
      sql = "delete from betting where home = ? and mdate = ?"; //���� sql
      try {
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, home); //���ڷ� ���� home team �̸����� sql ����
         SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm"); //��¥ ���� ����
         String str=dateFormat.format(mdate).toString();
         pstmt.setString(2, str);
         pstmt.executeUpdate(); 
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } //catch
      return true;
   } //delbetting() ����
   
   public ArrayList<BettingDTO> getUserBettingList(String id, boolean bFinished)
   {
      sql = "select * from betting where id = ? and mstatus = ?";
      ArrayList<BettingDTO> bettingList = new ArrayList<BettingDTO>();
      BettingDTO betting;
      try {
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, id); //id 
         pstmt.setBoolean(2, bFinished); //��� ��, ��� �� ����
         rs = pstmt.executeQuery();
         while(rs.next())
         {
            betting = new BettingDTO();
            betting.bcode = rs.getInt("bcode");
            betting.id = rs.getString("id");
            betting.home = rs.getString("home");
            betting.away = rs.getString("away");
                        
            // sql�� datetime ������ java�� date������ ��ȯ�ϴ� ����
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
            date.setHours(date.getHours() - 9); //�ѱ� �ð����� ����
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
            //���� betting �ű��   
         }
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } //catch
      
      return bettingList;
   } //getbettingList() �ش� id �� ������ ������ ����Ʈ�� ��ȯ
   
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
                        
            // sql�� datetime ������ java�� date������ ��ȯ�ϴ� ����
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
            date.setHours(date.getHours() - 9); //�ѱ� �ð����� ����
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
            //���� betting �ű��   
         }
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } //catch
      
      return bettingList;
   } //getbettingList() �ش� id �� ������ ������ ����Ʈ�� ��ȯ
   
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
            //���� betting �ű��   
         }
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } //catch
      
      return bettingList;
   } //getbettingList() �ش� id �� ���ù����� ������ ����Ʈ�� ��ȯ
   
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
   } //getHomeNum() Ȩ ���� ������ ����� ���� ��ȯ
   
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
   } //getAwayNum() ���� ���� ������ ����� ���� ��ȯ
   
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
   } //getMaxCode() ���� �������� ������ �����ϱ� ���ؼ� bcode�� �����ϱ� ���� ���� bcode�� ������ ���� �ִ밪�� db���� �����´�.
   
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
   } //getUsers() �ش� ��⿡ ������ member���� ����Ʈ�� ��ȯ
   
   public void createDummyBetting(int num, Vector<MatchDTO> matchList)
   {
	     int bcode;
	  	 String id; //����� id
		 String home; //Ȩ ��
	     String away; //���� ��
		 Date mdate; //��� ��¥, �ð�
		 String league; //��ȸ �̸�
		 String type; //��� ����
		 int result; //���� ���, ���� ��� ��� (0 : ���� ��, 1 : Ȩ �¸� , 2 : ���� �¸�)
		 boolean correct; //���� ���� ���� (false : ���� , true : ����)
		 boolean mstatus; //��� ���� (false : ��� �� , true : ��� ��)
		 int pnt; //���õ� �ݾ�
		 String homeURL; //Ȩ �̹��� URL
		 String awayURL; //���� �̹��� URL
		 int homeScore; // Ȩ ����
		 int awayScore; // ����� ����
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
   } //setScore() ���� ��� ����� �ش� ��� ���� ������ �����Ѵ�. 
   
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
		} //Ȩ ��
		else
		{
			pstmt.setBoolean(1, false);
			bCorrect = false;
		} //���� ��
		
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
   } //homeDecision Ȩ ���� ������ �������� ���� ���� ���θ� �����Ѵ�.
   
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
		} //Ȩ ��
		else
		{
			pstmt.setBoolean(1, true);
			bCorrect = true;
		} //���� ��
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
   } //awayDecision() ���� ���� ������ ���� �������� ���� ���θ� �����Ѵ�.
   
   public void setResult(MatchDTO match)
   {
	   sql = "update betting set result = ? where home = ? and mdate = ? and mdate between ? and ?";
	   try {
		   pstmt = conn.prepareStatement(sql);
		if(match.homeScore > match.awayScore)
		{
			pstmt.setInt(1, 1);
		} //Ȩ ��
		else
		{
			pstmt.setInt(1, 2);
		} //���� ��
		
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
   } //setResult() ���� ������� ������ ����� �����Ѵ�
   
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
	   
   } //setMatchFinish ��� ���¸� ���� ���¸� �ٲ۴�.
   
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
   } //getBcode() �������� ������ �������� ó���� ���� ������ ���� �����帣�� code�� ArrayList �� �����´�.
   
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
   } //updateByCode() ������ ������� ���� �ڵ带 ���� ������������ ��� ���� ó���Ѵ�.
   
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
   } //countCorrect() ����ڰ� ���� ���� or ������ ����� ���� ����.
} //bettingDAO class