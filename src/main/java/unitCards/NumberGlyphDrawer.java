package unitCards;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class NumberGlyphDrawer
{
	public static void drawNumber(Graphics g, int num, Color color, int x, int y, int charWidth)
	{
		int[] digits = new int[charWidth];
		int i;
		
		for(i = 1; i <= charWidth; i ++)
		{
			digits[charWidth - i] = num % 10;
			num /= 10;
		}
		
		i = 0;
		
		while(digits[i] == 0 && i < charWidth - 1)
			i++;
		
		BufferedImage nextGlyph;
		for(; i < charWidth; i++)
		{
			nextGlyph = IffuCardResourceManager.getDigitGlyph(digits[i]);
			nextGlyph = replace(nextGlyph, Color.WHITE.getRGB(), color.getRGB());
			g.drawImage(nextGlyph, x + (6*i), y, null);
		}
	}
	
	public static BufferedImage replace(BufferedImage image, int target, int preferred)
	{
	    int width = image.getWidth();
	    int height = image.getHeight();
	    BufferedImage newImage = new BufferedImage(width, height, image.getType());
	    int color;

	    for (int i = 0; i < width; i++) {
	        for (int j = 0; j < height; j++) {
	            color = image.getRGB(i, j);
	            if (color == target)
	            {
	                newImage.setRGB(i, j, preferred);
	            }
	            else
	            {
	                newImage.setRGB(i, j, color);
	            }
	        }
	    }

	    return newImage;
	}
}
