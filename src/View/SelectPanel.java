package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import DTO.BettingDTO;
import Main.MainView;

public class SelectPanel extends JPanel{
	
	public JLabel Team;
	public JButton btnRemove;
	public BettingDTO betting;
	public MainView view;
	
	public SelectPanel(BettingDTO betting, MainView view)
	{
		this.betting = betting;
		this.view = view;
		
		Team = new JLabel(betting.home + "    vs    " + betting.away);
		Team.setHorizontalAlignment(SwingConstants.CENTER);
		btnRemove = new JButton("Delete");
		btnRemove.addActionListener(new selectListener());
		setBorder(BorderFactory.createLineBorder(Color.darkGray));
		setLayout(new BorderLayout(6,1));
		setBackground(Color.pink);
		add(Team);
		add(btnRemove, BorderLayout.LINE_END);
	} //SelectPanel()
	
	public class selectListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event) {
			// TODO Auto-generated method stub
			Object obj = event.getSource();
			if(obj == btnRemove)
			{ 
				view.removebetting(betting);
				
				for(int i=0;i<view.matchList.size();i++)
				{
					MatchPanel panel = (MatchPanel)view.schedulePanel.getComponent(i);
					SimpleDateFormat dateFormat=new SimpleDateFormat("MM / dd HH:mm");
					String str=dateFormat.format(betting.mdate);
					if(betting.home.equals(panel.btnHome.getText()) && str.equals(panel.lblTime.getText()))
					{
						panel.btnHome.setEnabled(true);
						panel.btnAway.setEnabled(true);
						panel.btnHome.setBackground(new JButton().getBackground());
						panel.btnAway.setBackground(new JButton().getBackground());
					}
				}
				repaint();
			} //삭제 버튼
		} //actionPerformed()
		
	}//selectListener class
	
}//selectPanel class