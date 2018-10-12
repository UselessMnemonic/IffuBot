package unitCards;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import unit.IffuUnit;

public class IffuCardStatPanel extends JLabel {

	private byte[] visibleStats;
	private byte[] baseStats;
	/**
	 * Create the panel.
	 */
	public IffuCardStatPanel(IffuUnit unit)
	{
		visibleStats = unit.getVisibleUserStats().getRaw();
		baseStats = unit.getUserBaseStats().getRaw();

		setBackground(new Color(0,0,0,0));
		setBorder(null);
		setIcon(IffuCardResourceManager.getImageIconResource(IffuCardResourceManager.ImageReference.UnitStatPaneBackground));
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Color colorToUse;

		for(int i = 0; i < visibleStats.length; i++)
		{
		    if(visibleStats[i] < baseStats[i])
		        colorToUse = new Color(255, 67, 75);
		    else if(visibleStats[i] == baseStats[i])
		        colorToUse = Color.WHITE;
		    else
		        colorToUse = new Color(91, 255, 100);

			NumberGlyphDrawer.drawNumber(g, visibleStats[i], colorToUse, 38, i*13 + 1, 3);
		}
	}

}
