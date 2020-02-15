package DTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BettingDTO {
	public int bcode;
	public String id; //����� id
	public String home; //Ȩ ��
	public String away; //���� ��
	public Date mdate; //��� ��¥, �ð�
	public String league; //��ȸ �̸�
	public String type; //��� ����
	public int result; //���� ���, ���� ��� ��� (0 : ���� ��, 1 : Ȩ �¸� , 2 : ���� �¸�)
	public boolean correct; //���� ���� ���� (false : ���� , true : ����)
	public boolean mstatus; //��� ���� (false : ��� �� , true : ��� ��)
	public int pnt; //���õ� �ݾ�
	public String homeURL; //Ȩ �̹��� URL
	public String awayURL; //���� �̹��� URL
	public int homeScore; // Ȩ ����
	public int awayScore; // ����� ����
	
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
