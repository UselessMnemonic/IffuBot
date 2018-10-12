package unitCards;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import unit.IffuUnit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Font;

@SuppressWarnings("serial")
public class IffuCardHPBar extends JPanel {

	private static final Color colorClear = new Color(0, 0, 0, 0),
			shadowColor = new Color(30, 0, 0, 128);
	
	private int currHP,
			maxHP;
	
	/**
	 * Create the panel.
	 */
	public IffuCardHPBar(IffuUnit unit)
	{
		currHP = unit.getCurrentHP();
		maxHP = unit.getVisibleUserStats().getHP();
		String textHP = "" + currHP;
		
		if(currHP > maxHP)
		{
			this.currHP = 0;
			this.maxHP = 0;
			textHP = "??";
		}
		
		setBackground(colorClear);
		setLayout(null);
		setMinimumSize(new Dimension(200, 23));
		setMaximumSize(new Dimension(200, 23));
		
		JLabel hpLabel = new JLabel(textHP);
		//adjustable
		hpLabel.setFont(new Font("Ubuntu Mono", Font.PLAIN, 28));
		hpLabel.setForeground(Color.WHITE);
		hpLabel.setHorizontalAlignment(SwingConstants.CENTER);
		hpLabel.setBounds(0, -2, 41, 23);
		add(hpLabel);
	}

	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		
		super.paintComponent(g2d);

		g.setColor(shadowColor);
		for(int i = 0; i < maxHP && i < 40; i++)
		    g.fillRect((i*4) + 41, 1, 3, 10);

		for(int i = 40; i < maxHP && i < 80; i++)
			g.fillRect(((i%40)*4) + 41, 13, 3, 10);

        g.setColor(Color.WHITE);
        for(int i = 0; i < currHP && i < 40; i++)
            g.fillRect((i*4) + 41, 0, 3, 10);

        for(int i = 40; i < currHP && i < 80; i++)
            g.fillRect(((i%40)*4) + 41, 12, 3, 10);
	}
}
