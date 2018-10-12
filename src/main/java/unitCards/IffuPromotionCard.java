package unitCards;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import unit.IffuUnit;

public class IffuPromotionCard
{
    private BufferedImage card;
    protected static final Point lvlTxt = new Point(171, 42);
	protected static final Point classTxt = new Point(18, 42);
	protected static final Rectangle nameTxt = new Rectangle(42, 5, 117, 16);
	protected static final Rectangle statBox = new Rectangle(19, 10);
	protected static final Point maxExpLoc = new Point(106, 235);
	protected static final Point expBarLoc = new Point(54, 236);
	
	protected static final Color defaultColor = new Color(66, 8, 0),
			blueColor = new Color(0, 121, 253),
			redColor = new Color(176, 44, 20),
			shadowColor = new Color(0,0,0,50);
	
	protected IffuUnit unitPtr;

	public IffuPromotionCard(IffuUnit u)
	{
		unitPtr = u;
	}

    private BufferedImage getBlankCard()
    {
        BufferedImage source = IffuCardResourceManager.getImageResource(IffuCardResourceManager.ImageReference.UnitStatCardBackground);
        BufferedImage blankCard = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = blankCard.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return blankCard;
    }
	
	public void drawStatCard()
	{
	    card = getBlankCard();
		Graphics2D canvas = card.createGraphics();
		
		canvas.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));
		
		canvas.setColor(defaultColor);
		canvas.setFont(IffuCardResourceManager.getFontResource(IffuCardResourceManager.FontReference.DefaultFont));
		
		drawNameLvlClass(canvas);
		drawStatBars(canvas, unitPtr);
		drawExpBar(canvas, IffuCardResourceManager.getImageResource(IffuCardResourceManager.ImageReference.PointerBrown), defaultColor);
		
		canvas.drawImage(card, 0, 0, null);
		canvas.dispose();
	}
	
	private void drawNameLvlClass(Graphics2D g)
	{
		//draw level and class
		g.drawString(((Integer)unitPtr.getLevel()).toString(), (int)(lvlTxt.getX()), (int)(lvlTxt.getY()));
		g.drawString(unitPtr.getUserClass().getClassName(), (int)(classTxt.getX()), (int)(classTxt.getY()));
		
		//draw name
		g.setColor(Color.WHITE);
		drawCenteredString(g, unitPtr.getUserName(), nameTxt);
	}
	
	private void drawStatBars(Graphics2D g, IffuUnit oldUnit)
	{
		int diff;
		int x = 0, y = 0;
		
		//draw stat bars
		for(int i = 0; i <= 8; i++)
		{
			y = 54 + (i*18);
			
			g.setColor(shadowColor);
			g.fillRect(91, y+1, unitPtr.getUserBaseStats().getRaw()[i], 8);
			
			g.setColor(defaultColor);
			g.fillRect(90, y, unitPtr.getUserBaseStats().getRaw()[i], 8);
			
			statBox.setLocation(59, 53 + (i*18));
			drawCenteredString(g, ((Byte)(unitPtr.getUserBaseStats().getRaw()[i])).toString(), statBox);
			
			diff = unitPtr.getUserBaseStats().getRaw()[i] - oldUnit.getUserBaseStats().getRaw()[i];
			
			if(diff == 0)
				continue;
			
			if(diff > 0)
			{
				x = 90 + unitPtr.getUserBaseStats().getRaw()[i];
				g.setColor(shadowColor);
				g.fillRect(x + 1, y + 1, 1, 8);
				g.setColor(blueColor);
				g.fillRect(x - diff, y, diff + 1, 8);
				drawPointer(g, IffuCardResourceManager.getImageResource(IffuCardResourceManager.ImageReference.PointerBlue), x + 3, y - 3, diff);
			}
			else
			{
				x = 90 + oldUnit.getUserBaseStats().getRaw()[i];
				g.setColor(shadowColor);
				g.fillRect(x + 1, y + 1, 1, 8);
				g.setColor(redColor);
				g.fillRect(x + diff, y, 1 - diff, 8);
				drawPointer(g, IffuCardResourceManager.getImageResource(IffuCardResourceManager.ImageReference.PointerRed), x + 3, y - 3, -diff);
			}
		}
	}
	
	private void drawExpBar(Graphics2D g, BufferedImage ptr, Color color)
	{
		//draw exp bar
		g.setColor(color);
		if(unitPtr.isMaxLvl())
		{
			g.drawImage(IffuCardResourceManager.getImageResource(IffuCardResourceManager.ImageReference.ExperienceMaximum), maxExpLoc.x, maxExpLoc.y, null);
		}
		else
		{
			if(unitPtr.getExp() > 0)
			{
				g.setColor(shadowColor);
				g.fillRect(expBarLoc.x+1, expBarLoc.y+1, unitPtr.getExp(), 8);
				g.setColor(color);
				g.fillRect(expBarLoc.x, expBarLoc.y, unitPtr.getExp(), 8);
			}
			drawPointer(g, ptr, expBarLoc.x + unitPtr.getExp() + 2, expBarLoc.y - 3, unitPtr.getExp());
		}
	}

	public void drawLvlUpCard(IffuUnit oldUnit)
	{
        card = getBlankCard();
		Graphics2D canvas = card.createGraphics();
		canvas.setFont(IffuCardResourceManager.getFontResource(IffuCardResourceManager.FontReference.DefaultFont));
		canvas.setColor(defaultColor);

		drawNameLvlClass(canvas);
		drawStatBars(canvas, oldUnit);
		drawExpBar(canvas, IffuCardResourceManager.getImageResource(IffuCardResourceManager.ImageReference.PointerBlue), blueColor);

		canvas.drawImage(card, 0, 0, null);
		canvas.dispose();
	}

	private void drawPointer(Graphics2D g, BufferedImage pointer, int x, int y, int num)
	{
		g.drawImage(pointer, x, y, null);
		drawNumGlyph(g, num, x + 9, y + 3);
		
	}
	
	private void drawNumGlyph(Graphics2D g, int num, int x, int y)
	{
		int ones = num%10;
		num /= 10;
		int tens = num;
		if(tens != 0)
			g.drawImage(IffuCardResourceManager.getDigitGlyph(tens), x, y, null);
		x += 6;
		g.drawImage(IffuCardResourceManager.getDigitGlyph(ones), x, y, null);
	}

	public File saveImage()
	{
		try
		{
			File ret = new File("renders/" + unitPtr.getUserID() + ".png");
			ImageIO.write(card, "PNG", ret);
			return ret;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public void drawCenteredString(Graphics2D g, String text, Rectangle rect)
	{
	    // Get the FontMetrics
	    FontMetrics metrics = g.getFontMetrics(g.getFont());
	    // Determine the X coordinate for the text
	    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
	    // Draw the String
	    g.drawString(text, x, y);
	}
}
