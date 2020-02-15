package DTO;
import java.util.Calendar;
import java.util.Random;

public class MatchDTO{

	public String type; // 유형
	public String league; // 프로 농구
	public String home; // 홈 팀 
	public String away; // 원정 팀
	public Calendar date; // 경기 날짜, 시간
	public String stadium; //경기장
	public String homeImgUrl; // 홈 팀 이미지 URL
	public String awayImgUrl; // 원정 팀 이미지 URL
	public int homeScore; // 홈팀 스코어
	public int awayScore; // 어웨이팀 스코어
	
} //Match class
