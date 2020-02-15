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
      lblRank.setFont(new Font("�������", Font.BOLD, 32));
      add(lblRank);

      rankPanel = new JPanel();
      rankPanel.setLayout(new BoxLayout(rankPanel, BoxLayout.Y_AXIS));
      rankPanel.setBackground(Color.white);
      JScrollPane scroll = new JScrollPane(rankPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      scroll.setBounds(10, 130, 1160, 440);
      add(scroll);
      
      // ��ŷ ����� ���� ��
      RankCompPanel pnlRankSchema = new RankCompPanel(null, userId, 0);
      pnlRankSchema.setBounds(10,70,1160,60);
      add(pnlRankSchema);
   }

   // refreshRank(), ��ŷ�� DB�� �ִ� �����ͷ� ������Ʈ�Ѵ�.
   public void refreshRank(String id) {
      int rank = 1, size;
      
      this.userId = id;

      rankPanel.removeAll();

      // ������������ �޾ƿ� userList ���������� rankPanel�� add
      userList = mDAO.getUsersByDesc();
      for (MemberDTO user : userList) {
         RankCompPanel pnlRankComp = new RankCompPanel(user, userId, rank++);
         rankPanel.add(pnlRankComp);
      }

      // BoxLayout���� ���� �߻��ϴ� ������Ʈ �̽� ����(������)
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