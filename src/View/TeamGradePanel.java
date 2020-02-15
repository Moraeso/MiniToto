package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import DTO.KBLTeamDTO;

public class TeamGradePanel extends JPanel {
	public JLabel rankLb;
	public JLabel teamLb;
	public JLabel gameCntLb;
	public JLabel winRateLb;
	public JLabel winLb;
	public JLabel loseLb;
	public JLabel winDiffLb;
	public JLabel scoreAvgLb;
	public JLabel assistAvgLb;
	public JLabel reboundAvgLb;
	public JLabel stealAvgLb;
	public JLabel blockAvgLb;
	public JLabel point3AvgLb;
	public JLabel freeThrowAvgLb;
	public JLabel freeThrowRateLb;
	public JLabel lblLogoIcon;
	public ImageIcon imgLogo;
	
	// 내 정보에서 볼 수 잇는 게임 결과 패널들.
	public TeamGradePanel(KBLTeamDTO teamDTO) {
		setPreferredSize(new Dimension(1180, 35));
		setBackground(Color.lightGray);
		setBorder(BorderFactory.createLineBorder(Color.white));
		setLayout(null);
		
		rankLb = new JLabel("Rank");
		rankLb.setBounds(10, 0, 70, 40);
		add(rankLb);
		
		imgLogo = new ImageIcon();
		lblLogoIcon = new JLabel();
		lblLogoIcon.setBounds(40, 0, 40, 40);
		
		teamLb = new JLabel("Team");
		teamLb.setBounds(70, 0, 100, 40);
		add(teamLb);
		
		gameCntLb = new JLabel("Total match");
		gameCntLb.setBounds(170, 0, 70, 40);
		add(gameCntLb);
		
		winRateLb = new JLabel("Win rate");
		winRateLb.setBounds(235, 0, 70, 40);
		add(winRateLb);
		
		winLb = new JLabel("Win");
		winLb.setBounds(300, 0, 70, 40);
		add(winLb);
		
		loseLb = new JLabel("Lose");
		loseLb.setBounds(370, 0, 70, 40);
		add(loseLb);
		
		winDiffLb = new JLabel("Difference");
		winDiffLb.setBounds(440, 0, 70, 40);
		add(winDiffLb);
		
		scoreAvgLb = new JLabel("Goals");
		scoreAvgLb.setBounds(510, 0, 70, 40);
		add(scoreAvgLb);
		
		assistAvgLb = new JLabel("AS");
		assistAvgLb.setBounds(580, 0, 70, 40);
		add(assistAvgLb);
		
		reboundAvgLb = new JLabel("Rebound");
		reboundAvgLb.setBounds(635, 0, 70, 40);
		add(reboundAvgLb);
		
		stealAvgLb = new JLabel("Steal");
		stealAvgLb.setBounds(720, 0, 70, 40);
		add(stealAvgLb);
		
		blockAvgLb = new JLabel("Block");
		blockAvgLb.setBounds(790, 0, 70, 40);
		add(blockAvgLb);
		
		point3AvgLb = new JLabel("3point shot");
		point3AvgLb.setBounds(860, 0, 70, 40);
		add(point3AvgLb);
		
		freeThrowAvgLb = new JLabel("Free throw");
		freeThrowAvgLb.setBounds(930, 0, 70, 40);
		add(freeThrowAvgLb);

		freeThrowRateLb = new JLabel("Free throw success");
		freeThrowRateLb.setBounds(1000, 0, 70, 40);
		add(freeThrowRateLb);
		
		if (teamDTO != null) {
			rankLb.setText(teamDTO.rank);
			teamLb.setText(teamDTO.team);
			gameCntLb.setText(teamDTO.gameCnt);
			winRateLb.setText(teamDTO.winRate);
			winLb.setText(teamDTO.win);
			loseLb.setText(teamDTO.lose);
			winDiffLb.setText(teamDTO.winDiff);
			scoreAvgLb.setText(teamDTO.scoreAvg);
			assistAvgLb.setText(teamDTO.assistAvg);
			reboundAvgLb.setText(teamDTO.reboundAvg);
			stealAvgLb.setText(teamDTO.reboundAvg);
			blockAvgLb.setText(teamDTO.blockAvg);
			point3AvgLb.setText(teamDTO.point3Avg);
			freeThrowAvgLb.setText(teamDTO.freeThrowAvg);
			freeThrowRateLb.setText(teamDTO.freeThrowRate);
		
			gameCntLb.setBounds(185, 0, 70, 40);
			reboundAvgLb.setBounds(650, 0, 70, 40);
			
			imgLogo.setImage(getUrlImage(teamDTO.imgUrl));
			lblLogoIcon.setIcon(imgLogo);
			add(lblLogoIcon);
		}
	}
	
	public BufferedImage getUrlImage(String urlStr) {
		URL url;
		try {
			url = new URL(urlStr);
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
