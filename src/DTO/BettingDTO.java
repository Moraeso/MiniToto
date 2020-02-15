package DTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BettingDTO {
	public int bcode;
	public String id; //사용자 id
	public String home; //홈 팀
	public String away; //원정 팀
	public Date mdate; //경기 날짜, 시간
	public String league; //대회 이름
	public String type; //경기 종목
	public int result; //예측 결과, 실제 경기 결과 (0 : 예상 전, 1 : 홈 승리 , 2 : 원정 승리)
	public boolean correct; //예측 성공 여부 (false : 실패 , true : 성공)
	public boolean mstatus; //경기 상태 (false : 경기 전 , true : 경기 후)
	public int pnt; //배팅된 금액
	public String homeURL; //홈 이미지 URL
	public String awayURL; //원정 이미지 URL
	public int homeScore; // 홈 점수
	public int awayScore; // 어웨이 점수
	
	public BettingDTO()
	{
		bcode = 1;
		id = "user";
		home = "TeamA";
		away = "TeamB";
		try {
			mdate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2018-12-29 12:12");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		type = "basketball";
		league = "friendly";
		result = 0;
		correct = false;
		mstatus = false;
		pnt = 0;
		homeURL = "URL";
		awayURL = "URL";
		homeScore = 0;
		awayScore = 0;
	} //Batting()
	
} //Batting class
