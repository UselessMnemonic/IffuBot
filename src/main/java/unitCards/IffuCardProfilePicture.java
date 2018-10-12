package unitCards;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class IffuCardProfilePicture extends JLabel
{

	/**
	 * Create the panel.
	 */
	
	public IffuCardProfilePicture(BufferedImage imageToDisplay)
	{
			imageToDisplay = toBufferedImage(imageToDisplay.getScaledInstance(100, 100, BufferedImage.SCALE_SMOOTH));

			BufferedImage alphaMask = IffuCardResourceManager.getImageResource(IffuCardResourceManager.ImageReference.AlphaMap);

			final int width = imageToDisplay.getWidth();
			int[] imgData = new int[width];
			int[] maskData = new int[width];

			for (int y = 0; y < imageToDisplay.getHeight(); y++)
			{
			    // fetch a line of data from each image
				imageToDisplay.getRGB(0, y, width, 1, imgData, 0, 1);
                alphaMask.getRGB(0, y, width, 1, maskData, 0, 1);
			    // apply the mask
			    for (int x = 0; x < width; x++)
			    {
			        int color = (imgData[x] & 0x00FFFFFF); //mask away alpha
			        int maskColor = (maskData[x] & 0xFF000000); //set alpha from mask
			        color |= maskColor;
			        imgData[x] = color;
			    }
			    // replace the data
			    imageToDisplay.setRGB(0, y, width, 1, imgData, 0, 1);
			}
			
			this.setIcon(new ImageIcon(imageToDisplay));
		
		setBackground(new Color(0,0,0,0));
		setBorder(null);
	}
	
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}

}
