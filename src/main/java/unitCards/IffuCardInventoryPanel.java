package unitCards;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import item.IffuInventory;
import item.IffuInventoryRegistry;
import item.IffuItem;
import unit.IffuUnit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class IffuCardInventoryPanel extends JPanel
{

	/**
	 * Create the panel.
	 */
	
	public IffuCardInventoryPanel(IffuUnit u)
	{
		setLayout(null);
		setBackground(new Color(0,0,0,0));
		IffuInventory nullCheck = IffuInventoryRegistry.getInventory(u.getUserID());
		
		if(nullCheck != null)
		{
			ArrayList<IffuItem> inventory = nullCheck.getItemList();
			JLabel nextIconLabel;
			IffuItem nextItem;
			ImageIcon nextIcon;
			JLabel nextTextLabel;
			JLabel nextShadowLabel;

			if(nullCheck.isEquipped())
			{
				JLabel equipLabel = new JLabel("");
				equipLabel.setBounds(16, 10, 5, 7);
				equipLabel.setBackground(new Color(0,0,0,0));
				ImageIcon equipIcon = IffuCardResourceManager.getImageIconResource(IffuCardResourceManager.ImageReference.EquippedIcon);
				equipLabel.setIcon(equipIcon);
				add(equipLabel);
			}
			
			for(int i = 0; i <= inventory.size()-1; i++)
			{
				nextItem = inventory.get(i);
				nextIconLabel = new JLabel("");
				nextIconLabel.setBounds(8, i*19 + 1, 16, 16);
				nextIconLabel.setBackground(new Color(0,0,0,0));
				nextIcon = IffuCardResourceManager.getItemIcon(nextItem.getItemName());
				nextIconLabel.setIcon(nextIcon);
				add(nextIconLabel);

				nextTextLabel= new JLabel();
				nextTextLabel.setBounds(28, i*19 + 1, 131, 16);
				nextTextLabel.setFont(new Font("Calibri", Font.PLAIN, 12));
				nextTextLabel.setVerticalAlignment(SwingConstants.CENTER);
				nextTextLabel.setBackground(new Color(0,0,0,0));


				nextShadowLabel= new JLabel();
				nextShadowLabel.setBounds(29, i*19 + 2, 131, 16);
				nextShadowLabel.setFont(new Font("Calibri", Font.PLAIN, 12));
				nextShadowLabel.setVerticalAlignment(SwingConstants.CENTER);

                if(nextItem.isForged())
                {
                    nextTextLabel.setForeground(new Color(77, 255, 206));
                    nextTextLabel.setText(nextItem.getCustomName());
                    nextShadowLabel.setText(nextItem.getCustomName());
                }
                else
                {
                    nextTextLabel.setForeground(Color.WHITE);
                    nextTextLabel.setText(nextItem.getItemName());
                    nextShadowLabel.setText(nextItem.getItemName());
                }
                if(nextItem.isConsumable())
                {
                    String editedString = nextTextLabel.getText() + " (" + nextItem.getUses() + ")";
                    nextShadowLabel.setText(editedString);
                    nextTextLabel.setText(editedString);
                }

				nextShadowLabel.setBackground(new Color(0,0,0,0));
				nextShadowLabel.setForeground(new Color(30,0,0,128));
				
				add(nextTextLabel);
				add(nextShadowLabel);
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		
		super.paintComponent(g2d);
	}
}
