package Util;

import java.io.IOException;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import DTO.MatchDTO;
import DTO.KBLTeamDTO;
import Enum.MatchCategory;
import Enum.MatchType;

public class JSoupParser {

	// �±� ���� �͵��� ���� �м��ؼ� ���ϴ� ���� ��� ��, Ʈ�� ������ ����� �� - �Ľ�,
	// html ���� ���� ���� �� - "ũ�Ѹ�"
	// API, RSS : xml, Ȥ�� json ���·� ��������.
	Document doc = null;

	// ��� ���� �Ľ�
	public Vector<MatchDTO> getMatchInfo(MatchType matchType, MatchCategory matchLeague, int matchYear,
			int matchMonth) {
		doc = null;
		String url = makeURL(matchType, matchLeague, matchYear, matchMonth);
		connectURL(url);

		// �ֿ� ������ ������ �±׸� ã�Ƽ� ���������� �Ѵ�.
		Elements element = doc.select("div.sch_volleyball");

		Vector<MatchDTO> matchLists = new Vector<MatchDTO>();
		MatchDTO matchDTO = null;
		Calendar dateTime = null;
		int month = -1, day = -1; // �ӽ� ���� ����
		String hour, minute; // �ӽ� ���� ����

		for (Element el : element.select("span")) { // ���� ���� ������ for�� ���鼭 ���
			if (el.className().equals("td_date")) {

				// ������: . ( ) ����
				StringTokenizer st = new StringTokenizer(el.text(), ".|(|)| ");
				int i = 0;

				while (st.hasMoreTokens() && i < 3) {
					switch (i) {
					case 0:
						month = Integer.parseInt(st.nextToken()) - 1;
						break;
					case 1:
						day = Integer.parseInt(st.nextToken());
						break;
					}
					i++;
				}

			} else if (el.className().equals("td_hour")) {
				// ���Ϳ� matchDTO�� �������� �Ź� ��ü�� �����ϰ� ������ ���� ������Ʈ�ؾ� ��
				// �ʱ�ȭ
				matchDTO = initMatchDTO(matchType, matchLeague);

				dateTime = Calendar.getInstance();
				dateTime.set(Calendar.YEAR, matchYear);
				dateTime.set(Calendar.MONTH, month);
				dateTime.set(Calendar.DATE, day);

				// ��Ⱑ ������
				if (el.text().equals("-")) {
					matchDTO.date = dateTime;
					matchLists.add(matchDTO);
				}
				// ��Ⱑ ������
				else {
					hour = el.text().substring(0, 2);
					minute = el.text().substring(3, 5);

					dateTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
					dateTime.set(Calendar.MINUTE, Integer.parseInt(minute));

					matchDTO.date = dateTime;
				}
			} else if (el.className().equals("team_lft")) {
				matchDTO.home = el.text();
			} else if (el.className().equals("team_rgt")) {
				matchDTO.away = el.text();
			} else if (el.className().equals("td_stadium")) {
				matchDTO.stadium = el.text();
				matchLists.add(matchDTO);
			}
		}

		// �̹��� Url
		Elements img = element.select("img");

		Element el = null;
		for (int i = 0, j = 0; i < img.size(); i++) {
			el = img.get(i);

			if (!el.className().equals("sch_emblem")
					&& el.absUrl("src").startsWith("https://dthumb-phinf.pstatic.net/")) {

				while (matchLists.get(j).home == null) {
					j++;
				}

				if (matchLists.get(j).home != null) {
					if (matchLists.get(j).homeImgUrl == null) {
						matchLists.get(j).homeImgUrl = el.absUrl("src");
					} else if (matchLists.get(j).awayImgUrl == null) {
						matchLists.get(j).awayImgUrl = el.absUrl("src");
						j++;
					}
				}
			}
		}

		// ��� ��� �Ľ�
		Elements score = element.select("strong.td_score");
		StringTokenizer st = new StringTokenizer(score.text(), " ");
		int scoreInd = 0;
		String[] scoreArr;
		String delimiter = ":";

		while (st.hasMoreTokens()) {
			String str = st.nextToken();
			if (str.contains(":")) {
				scoreArr = str.split(delimiter);
				matchLists.get(scoreInd).homeScore = Integer.parseInt(scoreArr[0]);
				matchLists.get(scoreInd).awayScore = Integer.parseInt(scoreArr[1]);
			}
			scoreInd++;
		}

		return matchLists;
	}

	// ��� ���� �ʱ�ȭ
	private MatchDTO initMatchDTO(MatchType matchType, MatchCategory matchLeague) {
		MatchDTO matchDTO = new MatchDTO();

		if (matchType == MatchType.BASKETBALL) {
			matchDTO.type = "basketball";
		}

		if (matchLeague == MatchCategory.KBL) {
			matchDTO.league = "kbl";
		} else if (matchLeague == MatchCategory.NBA) {
			matchDTO.league = "nba";
		} else if (matchLeague == MatchCategory.WKBL) {
			matchDTO.league = "wkbl";
		}

		return matchDTO;
	}

	// URL ����
	private String makeURL(MatchType matchType, MatchCategory matchLeague, int matchYear, int matchMonth) {
		String url = "https://sports.news.naver.com/";

		// matchType
		if (matchType == MatchType.BASKETBALL) {
			url += "basketball/";
		} else if (matchType == MatchType.VOLLEYBALL) {
			url += "volleyball/";
		} else if (matchType == MatchType.WFOOTBALL) {
			url += "wfootball/";
		}

		url += "schedule/index.nhn?";

		// matchCategory
		if (matchLeague == MatchCategory.KBL) {
			url += "category=kbl";
		} else if (matchLeague == MatchCategory.NBA) {
			url += "category=nba";
		} else if (matchLeague == MatchCategory.WKBL) {
			url += "category=wkbl";
		} else if (matchLeague == MatchCategory.EPL) {
			url += "category=epl";
		}

		// matchYear, matchMonth
		url += "&year=" + matchYear + "&month=" + matchMonth;

		return url;
	}

	// URL ����
	private void connectURL(String url) {
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// �� ��ŷ ���� ������ �Ľ�
	public Vector<KBLTeamDTO> getTeamRankTableData(MatchCategory matchLeague, String year) {

		String url = "https://sports.news.naver.com/basketball/record/index.nhn?category=";

		if (matchLeague == MatchCategory.KBL) {
			url += "kbl";
		} else if (matchLeague == MatchCategory.NBA) {
			url += "nba";
		} else if (matchLeague == MatchCategory.WKBL) {
			url += "wkbl";
		}
		url += "&year=" + year;

		Elements intro = null;
		Element teamRankTable = null;
		KBLTeamDTO team = null;
		Elements e = null;
		Elements element = null;
		Vector<KBLTeamDTO> teamInfo = new Vector<KBLTeamDTO>();
//		https://sports.news.naver.com/basketball/record/index.nhn?category=nba&year=2019&conference=EAST

		doc = null;
		connectURL(url);

		element = doc.select("div.sch_volleyball");
		intro = element.select("div.tbl_box");
		teamRankTable = intro.select("tbody").get(0);

		team = null;
		e = teamRankTable.getElementsByTag("td");

		for (int i = 0, j = 0, k = 0; k < e.size(); j++, k++) {
			if (e.get(k).className().equals("tm")) {
				team = new KBLTeamDTO();
				team.team = e.get(k).text();
				team.rank = String.valueOf(i + 1);
			} else {
				switch (j) {
				case 1:
					team.gameCnt = e.get(k).text();
					break;
				case 2:
					team.winRate = e.get(k).text();
					break;
				case 3:
					team.win = e.get(k).text();
					break;
				case 4:
					team.lose = e.get(k).text();
					break;
				case 5:
					team.winDiff = e.get(k).text();
					break;
				case 6:
					team.scoreAvg = e.get(k).text();
					break;
				case 7:
					team.assistAvg = e.get(k).text();
					break;
				case 8:
					team.reboundAvg = e.get(k).text();
					break;
				case 9:
					team.stealAvg = e.get(k).text();
					break;
				case 10:
					team.blockAvg = e.get(k).text();
					break;
				case 11:
					team.point3Avg = e.get(k).text();
					break;
				case 12:
					team.freeThrowAvg = e.get(k).text();
					break;
				case 13:
					team.freeThrowRate = e.get(k).text();
					teamInfo.add(team);
					i++;
					j = -1;
					break;
				}
			}
		}

		// �̹��� Url
		Elements teamRankImg = teamRankTable.select("img");

		Element smallImg = null;
		for (int j = 0; j < teamRankImg.size(); j++) {
			smallImg = teamRankImg.get(j);

			if (smallImg.absUrl("src").startsWith("https://dthumb-phinf.pstatic.net/")) {
				teamInfo.get(j).imgUrl = smallImg.absUrl("src");
			}
		}
		
		return teamInfo;
	}

}
