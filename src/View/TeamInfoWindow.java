package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import DTO.KBLTeamDTO;
import DTO.NBATeamDTO;

public class TeamInfoWindow extends JFrame {

	public TeamInfoWindow(KBLTeamDTO teamDTO) {
		setTitle("Team Info : " + teamDTO.team);
		setBackground(Color.lightGray);
		setLayout(new BorderLayout());

		JPanel titlePanel = new JPanel(new FlowLayout());
		titlePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		titlePanel.setBackground(Color.darkGray);

		// ¼øÀ§
		JLabel titleRankLabel = new JLabel("Rank");
		titleRankLabel.setForeground(Color.white);
		titleRankLabel.setFont(new Font("Courier", Font.BOLD, 15));
		titlePanel.add(titleRankLabel);

		JLabel rankLabel = new JLabel(teamDTO.rank);
		rankLabel.setForeground(Color.YELLOW);
		rankLabel.setFont(new Font("Courier", Font.BOLD, 19));
		titlePanel.add(rankLabel);

		// ÆÀ ·Î°í
		ImageIcon imgLogo = new ImageIcon();
		JLabel lblLogoIcon = new JLabel();
		imgLogo.setImage(getUrlImage(teamDTO.imgUrl));
		lblLogoIcon.setIcon(imgLogo);

		Border border = lblLogoIcon.getBorder();
		Border margin = new EmptyBorder(0, 10, 0, 0);
		lblLogoIcon.setBorder(new CompoundBorder(border, margin));
		titlePanel.add(lblLogoIcon);

		// ÆÀ ÀÌ¸§
		JLabel teamLabel = new JLabel(teamDTO.team);
		teamLabel.setForeground(Color.white);
		titlePanel.add(teamLabel);

		// ÆÀ Á¤º¸ ÆÐ³Î
		JPanel teamInfoPanel = new JPanel(new GridLayout(2, 5));
		teamInfoPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		teamInfoPanel.add(new JLabel("Win rate", SwingConstants.CENTER));
		teamInfoPanel.add(new JLabel("Total Match", SwingConstants.CENTER));
		teamInfoPanel.add(new JLabel("Win", SwingConstants.CENTER));
		teamInfoPanel.add(new JLabel("Lose", SwingConstants.CENTER));
		teamInfoPanel.add(new JLabel("Difference", SwingConstants.CENTER));

		teamInfoPanel.add(new JLabel(teamDTO.winRate, SwingConstants.CENTER));
		teamInfoPanel.add(new JLabel(teamDTO.gameCnt, SwingConstants.CENTER));
		teamInfoPanel.add(new JLabel(teamDTO.win, SwingConstants.CENTER));
		teamInfoPanel.add(new JLabel(teamDTO.lose, SwingConstants.CENTER));
		teamInfoPanel.add(new JLabel(teamDTO.winDiff, SwingConstants.CENTER));

		add(titlePanel, BorderLayout.NORTH);
		add(teamInfoPanel, BorderLayout.CENTER);

		setSize(500, 300);
		setResizable(false);
		setVisible(true);
	}

	public TeamInfoWindow(NBATeamDTO teamDTO) {
		setTitle("ÆÀ Á¤º¸ :: " + teamDTO.team);
		setBackground(Color.lightGray);
		setLayout(new BorderLayout());

		JPanel titlePanel = new JPanel(new FlowLayout());
		titlePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		titlePanel.setBackground(Color.darkGray);

		// ¼øÀ§
		JLabel titleRankLabel = new JLabel("Rank");
		titleRankLabel.setForeground(Color.white);
		titleRankLabel.setFont(new Font("Courier", Font.BOLD, 15));
		titlePanel.add(titleRankLabel);

		JLabel rankLabel = new JLabel(teamDTO.rank);
		rankLabel.setForeground(Color.YELLOW);
		rankLabel.setFont(new Font("Courier", Font.BOLD, 19));
		titlePanel.add(rankLabel);

		// ÆÀ ·Î°í
		ImageIcon imgLogo = new ImageIcon();
		JLabel lblLogoIcon = new JLabel();
		imgLogo.setImage(getUrlImage(teamDTO.imgUrl));
		lblLogoIcon.setIcon(imgLogo);

		Border border = lblLogoIcon.getBorder();
		Border margin = new EmptyBorder(0, 10, 0, 0);
		lblLogoIcon.setBorder(new CompoundBorder(border, margin));
		titlePanel.add(lblLogoIcon);

		// ÆÀ ÀÌ¸§
		JLabel teamLabel = new JLabel(teamDTO.team);
		teamLabel.setForeground(Color.white);
		titlePanel.add(teamLabel);

		// ÆÀ Á¤º¸ ÆÐ³Î
		JPanel teamInfoPanel = new JPanel(new GridLayout(2, 5));
		teamInfoPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		teamInfoPanel.add(new JLabel("Win rate", SwingConstants.CENTER));
		teamInfoPanel.add(new JLabel("Total match", SwingConstants.CENTER));
		teamInfoPanel.add(new JLabel("Win", SwingConstants.CENTER));
		teamInfoPanel.add(new JLabel("Lose", SwingConstants.CENTER));
		teamInfoPanel.add(new JLabel("Difference", SwingConstants.CENTER));

		teamInfoPanel.add(new JLabel(teamDTO.winRate, SwingConstants.CENTER));
		teamInfoPanel.add(new JLabel(teamDTO.gameCnt, SwingConstants.CENTER));
		teamInfoPanel.add(new JLabel(teamDTO.win, SwingConstants.CENTER));
		teamInfoPanel.add(new JLabel(teamDTO.lose, SwingConstants.CENTER));
		teamInfoPanel.add(new JLabel(teamDTO.winDiff, SwingConstants.CENTER));

		add(titlePanel, BorderLayout.NORTH);
		add(teamInfoPanel, BorderLayout.CENTER);

		setSize(500, 300);
		setResizable(false);
		setVisible(true);
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
