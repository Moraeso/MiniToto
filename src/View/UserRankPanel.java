package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import DAO.MemberDAO;
import DTO.MemberDTO;

public class UserRankPanel extends JPanel {

   private String userId;
   private MemberDAO mDAO;

   private JLabel lblRank;
   private JPanel rankPanel;

   private ArrayList<MemberDTO> userList;

   public UserRankPanel(MemberDAO mDAO) {
      setLayout(null);
      setSize(1180, 580);
      setBackground(Color.gray);

      this.mDAO = mDAO;

      userList = new ArrayList<MemberDTO>();

      lblRank = new JLabel("Ranking");
      lblRank.setBounds(20, 15, 300, 40);
      lblRank.setFont(new Font("나눔고딕", Font.BOLD, 32));
      add(lblRank);

      rankPanel = new JPanel();
      rankPanel.setLayout(new BoxLayout(rankPanel, BoxLayout.Y_AXIS));
      rankPanel.setBackground(Color.white);
      JScrollPane scroll = new JScrollPane(rankPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      scroll.setBounds(10, 130, 1160, 440);
      add(scroll);
      
      // 랭킹 목록의 구분 행
      RankCompPanel pnlRankSchema = new RankCompPanel(null, userId, 0);
      pnlRankSchema.setBounds(10,70,1160,60);
      add(pnlRankSchema);
   }

   // refreshRank(), 랭킹을 DB의 있는 데이터로 업데이트한다.
   public void refreshRank(String id) {
      int rank = 1, size;
      
      this.userId = id;

      rankPanel.removeAll();

      // 내림차순으로 받아온 userList 순차적으로 rankPanel에 add
      userList = mDAO.getUsersByDesc();
      for (MemberDTO user : userList) {
         RankCompPanel pnlRankComp = new RankCompPanel(user, userId, rank++);
         rankPanel.add(pnlRankComp);
      }

      // BoxLayout으로 인해 발생하는 컴포넌트 이슈 보정(사이즈)
      size = userList.size();
      while (size != 0 && size < 7) {
         JPanel pnlEmpty = new JPanel();
         pnlEmpty.setPreferredSize(new Dimension(430, 60));
         pnlEmpty.setBackground(Color.gray);
         pnlEmpty.setBorder(BorderFactory.createLineBorder(Color.white));
         // pnlEmpty.setBorder(new TitledBorder(new LineBorder(Color.black), "1"));
         rankPanel.add(pnlEmpty);
         size++;
      }
   }
}