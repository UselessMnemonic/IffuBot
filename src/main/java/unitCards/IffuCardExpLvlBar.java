package unitCards;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

import unit.IffuUnit;

@SuppressWarnings("serial")
public class IffuCardExpLvlBar extends JPanel
{
	private int exp;
	private IffuUnit unit;
	/**
	 * Create the panel.
	 */
	public IffuCardExpLvlBar(IffuUnit unit)
	{
		this.unit = unit;
		exp = unit.getExp();
		setSize(210, 12);
		setBackground(new Color(0,0,0,0));
		setBorder(null);
		setLayout(null);
		
		JLabel lvlLabel = new JLabel(""+unit.getLevel());
		lvlLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		//adjustable
		lvlLabel.setFont(new Font("Ubuntu Mono", Font.PLAIN, 15));
		lvlLabel.setForeground(Color.white);
		lvlLabel.setBounds(33, -5, 20, 20);
		add(lvlLabel);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		
		super.paintComponent(g2d);
		
		g.setColor(Color.WHITE);
		g.drawImage(IffuCardResourceManager.getImageResource(IffuCardResourceManager.ImageReference.ExperienceBarBackground), 0, 0, null);
		if(unit.isMaxLvl())
			g.drawImage(IffuCardResourceManager.getImageResource(IffuCardResourceManager.ImageReference.ExperienceMaximumTransperant), 134, 1, null);
		else
		{
			g.fillRect(93, 6, exp, 5);
			g.drawImage(IffuCardResourceManager.getImageResource(IffuCardResourceManager.ImageReference.ExperienceCurser), 89+exp, 1, null);
			NumberGlyphDrawer.drawNumber(g, exp, new Color(0,0,0,200), 94+exp, 2, 2);
		}
	}
}
