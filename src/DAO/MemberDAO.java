package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import DTO.MemberDTO;

public class MemberDAO {

	String jdbcDriver = "com.mysql.cj.jdbc.Driver";
	String jdbcUrl = "jdbc:mysql://localhost/javadb?serverTimezone=UTC&useSSL=false";

	Connection conn;

	PreparedStatement pstmt;
	ResultSet rs;

	String sql;

	public MemberDAO() {
		connectDB();
	} // MemberDAO()

	public void connectDB() {
		try {
			Class.forName(jdbcDriver);

			conn = DriverManager.getConnection(jdbcUrl, "root", "1234");
			createDummyMember(50);
		} catch (Exception e) {
			e.printStackTrace();
		} // try...catch
	} // connectDB()

	public void closeDB() {
		try {
			pstmt.close();
			rs.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} // try...catch
	} // closeDB()

	// 아이디 중복 검사 메소드
	public boolean isIdOverlap(String id) {

		sql = "select * from member where id = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();
			if (rs.absolute(1)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// Dummy 멤버 생성
	public void createDummyMember(int num) {
		String name, id, pw, sex, phone;
		int age, lvl, pnt, matchCnt, win, lose;
		float hitRate;

		for (int i = 1; i <= num; i++) {
			name = "name" + Integer.toString(i);
			id = "dummy" + Integer.toString(i);
			pw = "111111";
			age = (int)(Math.random() * 60) + 20;
			sex = "여";
			phone = "01012345678";
			pnt = (int)(Math.random() * 100) + 1000;
			lvl = pnt / 1000;
			win = (int)(Math.random() * 100) + 1;
			lose = (int)(Math.random() * 100) + 1;
			matchCnt = win + lose;
			hitRate = Math.round((float) win / (float) (win + lose) * 10000.0) / 100.0f;
			
			// 아이디 중복 시 다음으로 넘어감
			if (isIdOverlap(id)) {
				System.out.println(id + "dummy member 생성 중 아이디 중복");
				continue;
			}
			
			// name, id, pw, age, sex, phone, lvl, pnt, matchCnt, win, lose, hitRate
			sql = "insert into member values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
						

			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, name);
				pstmt.setString(2, id);
				pstmt.setString(3, pw);
				pstmt.setInt(4, age);
				pstmt.setString(5, sex);
				pstmt.setString(6, phone);
				pstmt.setInt(7, lvl);
				pstmt.setInt(8, pnt);
				pstmt.setInt(9, matchCnt);
				pstmt.setInt(10, win);
				pstmt.setInt(11, lose);
				pstmt.setFloat(12, hitRate);

				pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// insertMember(), 입력받은 member 객체를 DB에 추가
	public boolean insertMember(MemberDTO member) {
		int isCreated = 0;

		// 아이디 중복 시 false 반환
		if (isIdOverlap(member.id)) {
			return false;
		}

		sql = "insert into member values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		// name, id, pw, age, sex, phone, lvl, pnt, matchCnt, win, lose, hitRate
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.name);
			pstmt.setString(2, member.id);
			pstmt.setString(3, member.pw);
			pstmt.setInt(4, member.age);
			pstmt.setString(5, member.sex);
			pstmt.setString(6, member.phone);
			pstmt.setInt(7, member.lvl);
			pstmt.setInt(8, member.pnt);
			pstmt.setInt(9, member.matchCnt);
			pstmt.setInt(10, member.win);
			pstmt.setInt(11, member.lose);
			pstmt.setFloat(12, member.hitRate);

			isCreated = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (isCreated == 1) {
			System.out.println("\n- id(" + member.id + ") insert success");
			return true;
		}

		return false;
	} // insertMember()

	// getId(). name과 phone을 입력받아서 일치하는 아이디 반환
	public String getId(String name, String phone) {
		sql = "select * from member WHERE name = ? AND phone = ?";

		String id = "";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, phone);

			rs = pstmt.executeQuery();
			if (!rs.absolute(1)) {
				return null;
			}

			id = rs.getString("id");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return id;
	}

	// getPw(). name과 phone을 입력받아서 일치하는 비밀번호 반환
	public String getPw(String id, String phone) {
		sql = "select * from member WHERE id = ? AND phone = ?";

		String pw = "";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, phone);

			rs = pstmt.executeQuery();
			if (!rs.absolute(1)) {
				return null;
			}

			pw = rs.getString("pw");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pw;
	}

	// getUser(). 입력받은 ID에 따른 멤버 객체 반환
	public MemberDTO getUser(String id) {
		sql = "select * from member WHERE id = ?";

		MemberDTO m = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();
			if (!rs.absolute(1)) {
				System.out.println("Errror[getUser]!!");
				return null;
			}

			m = new MemberDTO();
			m.name = rs.getString("name");
			m.id = rs.getString("id");
			m.pw = rs.getString("pw");
			m.age = rs.getInt("age");
			m.sex = rs.getString("sex");
			m.phone = rs.getString("phone");
			m.lvl = rs.getInt("lvl");
			m.pnt = rs.getInt("pnt");
			m.matchCnt = rs.getInt("matchCnt");
			m.win = rs.getInt("win");
			m.lose = rs.getInt("lose");
			m.hitRate = rs.getFloat("hitRate");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return m;
	}

	// loginMember() 
	public boolean loginMember(String id, String pw) {
		sql = "select * from member WHERE id = ? AND pw = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);

			rs = pstmt.executeQuery();
			if (!rs.absolute(1)) {
				return false;
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// getPnt(), id 유저의 포인트를 반환한다.
	public int getPnt(String id) {
		sql = "select * from member WHERE id = ?";

		int pnt = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				pnt = rs.getInt("pnt");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pnt;
	}

	// updatePnt(), id 유저의 포인트와 레벨을 업데이트
	public void updatePnt(String id, int pnt) {
		// 입력받은 point에 따라 id 유저의 포인트 변경
		sql = "update member set pnt = ? where id = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pnt);
			pstmt.setString(2, id);

			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 포인트가 변하면 그 값에 따라 레벨 변경(1 level == 1000 point)
		int lvl = pnt / 1000;
		if (lvl < 1)
			lvl = 1;
		sql = "update member set lvl = ? where id = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, lvl);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// increaseMatchCnt(), id 유저의 총 배팅 수를 1 증가
	public int increaseMatchCnt(String id) {
		// DB에서 id 유저의 matchCnt 값을 받아옴
		sql = "select * from member WHERE id = ?";
		int matchCnt = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				matchCnt = rs.getInt("matchCnt");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// matchCnt를 1 증가시키고 DB에 반영
		matchCnt++;
		sql = "update member set matchCnt = ? WHERE id = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, matchCnt);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return matchCnt;
	}

	// increaseeWin(), id 유저의 win을 1 증가
	public int increaseWin(String id) {
		// DB에서 id 유저의 win 값을 받아옴
		sql = "select * from member WHERE id = ?";
		int win = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				win = rs.getInt("win");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// win을 1 증가시키고 DB에 반영
		win++;
		sql = "update member set win = ? WHERE id = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, win);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 변경된 win에 따라서 HitRate와 matchCnt(총 경기 수) 업데이트
		updateHitRate(id);
		increaseMatchCnt(id);

		return win;
	}

	// increaseLose(), id 유저의 lose를 1 증가
	public int increaseLose(String id) {
		// DB에서 id 유저의 lose 값을 받아옴
		sql = "select * from member WHERE id = ?";
		int lose = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				lose = rs.getInt("lose");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// lose를 1 증가시키고 DB에 반영
		lose++;
		sql = "update member set lose = ? WHERE id = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, lose);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 변경된 lose에 따라서 HitRate와 matchCnt(총 경기 수) 업데이트
		updateHitRate(id);
		increaseMatchCnt(id);

		return lose;
	}

	// updateHitRate(),
	public float updateHitRate(String id) {
		// 유저의 win과 lose를 받아옴
		sql = "select * from member WHERE id = ?";
		int win = 0, lose = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				win = rs.getInt("win");
				lose = rs.getInt("lose");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// win과 lose에 맞춰서 hitRate 업데이트
		float hitRate;

		if (win == 0 && lose == 0) {
			hitRate = 0.0f;
		} else if (win == 0) {
			hitRate = 0.0f;
		} else if (lose == 0) {
			hitRate = 100.0f;
		} else {
			hitRate = Math.round((float) win / (float) (win + lose) * 10000.0) / 100.0f;
		}
		sql = "update member set hitRate = ? WHERE id = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setFloat(1, hitRate);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (float) hitRate;
	}

	public ArrayList<MemberDTO> getUsersByDesc() {
		sql = "select * from member order by pnt DESC";

		ArrayList<MemberDTO> userList = new ArrayList<MemberDTO>();
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				MemberDTO m = new MemberDTO();

				m = new MemberDTO();
				m.name = rs.getString("name");
				m.id = rs.getString("id");
				m.pw = rs.getString("pw");
				m.age = rs.getInt("age");
				m.sex = rs.getString("sex");
				m.phone = rs.getString("phone");
				m.lvl = rs.getInt("lvl");
				m.pnt = rs.getInt("pnt");
				m.matchCnt = rs.getInt("matchCnt");
				m.win = rs.getInt("win");
				m.lose = rs.getInt("lose");
				m.hitRate = rs.getFloat("hitRate");

				userList.add(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return userList;
	}

} // MemberDAO class