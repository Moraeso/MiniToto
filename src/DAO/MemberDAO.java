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

	// ���̵� �ߺ� �˻� �޼ҵ�
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

	// Dummy ��� ����
	public void createDummyMember(int num) {
		String name, id, pw, sex, phone;
		int age, lvl, pnt, matchCnt, win, lose;
		float hitRate;

		for (int i = 1; i <= num; i++) {
			name = "name" + Integer.toString(i);
			id = "dummy" + Integer.toString(i);
			pw = "111111";
			age = (int)(Math.random() * 60) + 20;
			sex = "��";
			phone = "01012345678";
			pnt = (int)(Math.random() * 100) + 1000;
			lvl = pnt / 1000;
			win = (int)(Math.random() * 100) + 1;
			lose = (int)(Math.random() * 100) + 1;
			matchCnt = win + lose;
			hitRate = Math.round((float) win / (float) (win + lose) * 10000.0) / 100.0f;
			
			// ���̵� �ߺ� �� �������� �Ѿ
			if (isIdOverlap(id)) {
				System.out.println(id + "dummy member ���� �� ���̵� �ߺ�");
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
	
	// insertMember(), �Է¹��� member ��ü�� DB�� �߰�
	public boolean insertMember(MemberDTO member) {
		int isCreated = 0;

		// ���̵� �ߺ� �� false ��ȯ
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

	// getId(). name�� phone�� �Է¹޾Ƽ� ��ġ�ϴ� ���̵� ��ȯ
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

	// getPw(). name�� phone�� �Է¹޾Ƽ� ��ġ�ϴ� ��й�ȣ ��ȯ
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

	// getUser(). �Է¹��� ID�� ���� ��� ��ü ��ȯ
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

	// getPnt(), id ������ ����Ʈ�� ��ȯ�Ѵ�.
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

	// updatePnt(), id ������ ����Ʈ�� ������ ������Ʈ
	public void updatePnt(String id, int pnt) {
		// �Է¹��� point�� ���� id ������ ����Ʈ ����
		sql = "update member set pnt = ? where id = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pnt);
			pstmt.setString(2, id);

			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ����Ʈ�� ���ϸ� �� ���� ���� ���� ����(1 level == 1000 point)
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

	// increaseMatchCnt(), id ������ �� ���� ���� 1 ����
	public int increaseMatchCnt(String id) {
		// DB���� id ������ matchCnt ���� �޾ƿ�
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

		// matchCnt�� 1 ������Ű�� DB�� �ݿ�
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

	// increaseeWin(), id ������ win�� 1 ����
	public int increaseWin(String id) {
		// DB���� id ������ win ���� �޾ƿ�
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

		// win�� 1 ������Ű�� DB�� �ݿ�
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

		// ����� win�� ���� HitRate�� matchCnt(�� ��� ��) ������Ʈ
		updateHitRate(id);
		increaseMatchCnt(id);

		return win;
	}

	// increaseLose(), id ������ lose�� 1 ����
	public int increaseLose(String id) {
		// DB���� id ������ lose ���� �޾ƿ�
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

		// lose�� 1 ������Ű�� DB�� �ݿ�
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

		// ����� lose�� ���� HitRate�� matchCnt(�� ��� ��) ������Ʈ
		updateHitRate(id);
		increaseMatchCnt(id);

		return lose;
	}

	// updateHitRate(),
	public float updateHitRate(String id) {
		// ������ win�� lose�� �޾ƿ�
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

		// win�� lose�� ���缭 hitRate ������Ʈ
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