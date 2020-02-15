package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import DTO.BettingDTO;

public class UserBetPanel extends JPanel {
   public JPanel pnlHome, pnlAway;
   public JLabel lblTime, lblHome, lblAway, lblHomeIcon, lblAwayIcon, lblHomeScr, lblAwayScr;
   public ImageIcon homeImage, awayImage;
   public BettingDTO betting;

   public UserBetPanel(BettingDTO betting, int type) {
      setPreferredSize(new Dimension(430, 60));
      setBackground(Color.darkGray);
      if (type == 2)
         setBackground(Color.orange);
      setBorder(BorderFactory.createLineBorder(Color.white));
      setLayout(null);

      this.betting = betting;

      // Home 팀 패널 정의
      pnlHome = new JPanel();
      pnlHome.setBackground(Color.white);
      // pnlHome.setLayout(new BoxLayout(pnlHome, BoxLayout.Y_AXIS));
      pnlHome.setBounds(10, 10, 100, 40);
      add(pnlHome);

      lblHome = new JLabel(betting.home);
      lblHome.setHorizontalAlignment(SwingConstants.CENTER);
      //lblHome.setVerticalAlignment(SwingConstants.TOP);
      pnlHome.add(lblHome);

      if (type == 2) {
         lblHomeScr = new JLabel(Integer.toString(betting.homeScore));
         lblHomeScr.setFont(new Font("굴림", Font.BOLD, 12));
         lblHomeScr.setBounds(170,30,30,20);
         add(lblHomeScr);
      }

      homeImage = new ImageIcon(getUrlImage(betting.homeURL));
      lblHomeIcon = new JLabel(homeImage);
      lblHomeIcon.setBounds(115, 10, 40, 40);
      add(lblHomeIcon);

      // Away 팀 패널 정의1
      pnlAway = new JPanel();
      pnlAway.setBackground(Color.white);
      pnlAway.setBounds(300, 10, 100, 40);
      add(pnlAway);

      lblAway = new JLabel(betting.away);
      lblAway.setHorizontalAlignment(SwingConstants.CENTER);
      //lblAway.setVerticalAlignment(SwingConstants.TOP);
      pnlAway.add(lblAway);

      if (type == 2) {
         lblAwayScr = new JLabel(Integer.toString(betting.awayScore));
         lblAwayScr.setFont(new Font("굴림", Font.BOLD, 12));
         lblAwayScr.setBounds(225,30,30,20);
         add(lblAwayScr);
      }

      awayImage = new ImageIcon(getUrlImage(betting.awayURL));
      lblAwayIcon = new JLabel(awayImage);
      lblAwayIcon.setBounds(250, 10, 40, 40);
      add(lblAwayIcon);

      // 유저 예측에 따라 패널 색 바꿔줌
      if (type == 1) { // 현재 배팅 패널 설정
         if (betting.result == 1) {
            pnlHome.setBackground(Color.yellow);
         } else {
            pnlAway.setBackground(Color.yellow);
         }
      } else if (type == 2) { // 전체 전적 패널 설정
         if (betting.result == 1 && betting.correct == true) {
            // 홈 팀이 이기고, 맞췄을 때
            pnlHome.setBackground(Color.green);
         } else if (betting.result == 1 && betting.correct == false) {
            // 홈 팀이 이기고, 틀렸을 때
            pnlHome.setBackground(Color.red);
         } else if (betting.result == 2 && betting.correct == true) {
            // 원정 팀이 이기고, 맞췄을 때
            pnlAway.setBackground(Color.green);
         } else if (betting.result == 2 && betting.correct == false) {
            // 원정 팀이 이기고, 틀렸을 때
            pnlAway.setBackground(Color.red);
         }
      }

      // Date 관련
      lblTime = new JLabel();

      String date = new SimpleDateFormat("MM / dd HH:mm").format(betting.mdate.getTime());
      lblTime.setText(date);
      lblTime.setForeground(Color.cyan);
      if (type == 2)
         lblTime.setForeground(Color.blue);
      lblTime.setBounds(160, 10, 150, 20);
      add(lblTime);

      JLabel lblVS = new JLabel("VS");
      lblVS.setForeground(Color.red);
      lblVS.setBounds(200, 30, 30, 20);
      add(lblVS);

   } // MatchPanel()

   // 해당 Url의 이미지 파일을 불러와서 BufferedImage로 반환
   public BufferedImage getUrlImage(String urlStr) {
      URL url;
      try {
         url = new URL(urlStr);
//         url = new URL("http://www.nlotto.co.kr/img/common_new/ball_" + num + ".png");
         BufferedImage img = null;
         try {
            img = ImageIO.read(url);
         } catch (IOException e) {
            System.out.println("E: Image Load failed!");
         }
         return img;
      } catch (MalformedURLException e) {
         System.out.println("E: Connection Error!");
         return null;
      }
   }
}