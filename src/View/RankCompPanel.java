package View;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import DTO.MemberDTO;

public class RankCompPanel extends JPanel {
	public JLabel lblRank, lblId, lblLvl, lblPnt, lblMatchCnt, lblWin, lblLose, lblHitRate;

	public RankCompPanel(MemberDTO user, String userId, int rank) {
		setPreferredSize(new Dimension(430, 60));
		setBackground(Color.lightGray);
		setBorder(BorderFactory.createLineBorder(Color.white));
		setLayout(null);

		lblRank = new JLabel("Ranking");
		lblRank.setBounds(10, 10, 130, 40);
		add(lblRank);

		lblId = new JLabel("ID");
		lblId.setBounds(150, 10, 130, 40);
		add(lblId);

		lblLvl = new JLabel("Level");
		lblLvl.setBounds(290, 10, 130, 40);
		add(lblLvl);

		lblPnt = new JLabel("Points");
		lblPnt.setBounds(450, 10, 130, 40);
		add(lblPnt);

		lblMatchCnt = new JLabel("Total bet");
		lblMatchCnt.setBounds(590, 10, 130, 40);
		add(lblMatchCnt);

		lblWin = new JLabel("Hit");
		lblWin.setBounds(730, 10, 130, 40);
		add(lblWin);

		lblLose = new JLabel("Failure");
		lblLose.setBounds(870, 10, 130, 40);
		add(lblLose);

		lblHitRate = new JLabel("Hit rate");
		lblHitRate.setBounds(1010, 10, 130, 40);
		add(lblHitRate);

		if (user != null) {
			lblRank.setText(Integer.toString(rank));
			lblId.setText(user.id);
			lblLvl.setText(Integer.toString(user.lvl));
			lblPnt.setText(Integer.toString(user.pnt));
			lblMatchCnt.setText(Integer.toString(user.matchCnt));
			lblWin.setText(Integer.toString(user.win));
			lblLose.setText(Integer.toString(user.lose));
			lblHitRate.setText(Float.toString(user.hitRate) + "%");
			
			// 자신의 랭크 패널은 노란색으로 표시
			if (userId.equals(user.id)) {
				setBackground(Color.yellow);
			}
		}
	} // RankCompPanel()
}