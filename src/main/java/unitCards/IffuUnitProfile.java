package unitCards;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Dimension;

import net.dv8tion.jda.core.JDA;
import unit.IffuUnit;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class IffuUnitProfile extends JLabel
{
	private BufferedImage render;
	private static final Color colorClear = new Color(0, 0, 0, 0),
			shadowColor = new Color(30, 0, 0, 128);
	
	private static final Font nameFont = new Font("Franklin Gothic",
		Font.PLAIN, 16),
			classFont = new Font("High Tower Text", Font.ITALIC, 16);
	private IffuUnit unit;
	
	public IffuUnitProfile(JDA jda, IffuUnit unit)
	{
		this.unit = unit;
		setSize(new Dimension(320, 240));
		setMinimumSize(new Dimension(320, 240));
		setMaximumSize(new Dimension(320, 240));
		setIcon(IffuCardResourceManager.getImageIconResource(IffuCardResourceManager.ImageReference.UnitCardProfileBackground));
		setBackground(Color.BLACK);
		setLayout(null);

			BufferedImage pfp = null;
			try
			{
				pfp = getUserImage(jda, unit);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				try
				{
					pfp = IffuCardResourceManager.getImageResource(IffuCardResourceManager.ImageReference.UnitCardDdefaultAvatarImage);
				}
				catch (Exception e1)
				{
					System.err.println("Could not fetch default user profile image.");
				}
			}
		
		JLabel nameLabel = new JLabel(unit.getUserName());
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nameLabel.setFont(nameFont);
		nameLabel.setBounds(126, 8, 175, 18);
		add(nameLabel);
		
		JLabel classLabel = new JLabel(unit.getUserClass().getClassName());
		classLabel.setForeground(Color.WHITE);
		classLabel.setFont(classFont);
		classLabel.setHorizontalAlignment(SwingConstants.CENTER);
		classLabel.setBounds(126, 29, 175, 18);
		add(classLabel);
		
		JLabel nameShadowLabel = new JLabel(unit.getUserName());
		nameShadowLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nameShadowLabel.setForeground(shadowColor);
		nameShadowLabel.setFont(nameFont);
		nameShadowLabel.setBounds(127, 9, 175, 18);
		add(nameShadowLabel);
		
		JLabel classShadowLabel = new JLabel(unit.getUserClass().getClassName());
		classShadowLabel.setFont(classFont);
		classShadowLabel.setForeground(shadowColor);
		classShadowLabel.setHorizontalAlignment(SwingConstants.CENTER);
		classShadowLabel.setBounds(127, 30, 175, 18);
		add(classShadowLabel);
		
		IffuCardHPBar hpBar = new IffuCardHPBar(unit);
		hpBar.setBounds(110, 87, 200, 29);
		add(hpBar);
		
		IffuCardExpLvlBar expLvl = new IffuCardExpLvlBar(unit);
		expLvl.setBounds(101, 222, 210, 12);
		add(expLvl);
		
		IffuCardInventoryPanel invPane = new IffuCardInventoryPanel(unit);
		invPane.setBounds(174, 121, 136, 94);
		add(invPane);
		
		IffuCardStatPanel stats = new IffuCardStatPanel(unit);
		stats.setBounds(0, 119, 57, 114);
		add(stats);

        JLabel classIcon;
        ImageIcon imageIcon = IffuCardResourceManager.getClassIcon(unit.getUserClass().getClassName());
        classIcon = new JLabel(imageIcon);
        classIcon.setBorder(null);
        //classIcon.setBounds(110-imageIcon.getIconWidth(), 110-imageIcon.getIconHeight(), imageIcon.getIconWidth(), imageIcon.getIconHeight());
        classIcon.setBounds(212-(imageIcon.getIconWidth()/2), 65-(imageIcon.getIconHeight()/2), imageIcon.getIconWidth(), imageIcon.getIconHeight());
		add(classIcon);
		
		IffuCardProfilePicture profilePicturePanel;
		profilePicturePanel = new IffuCardProfilePicture(pfp);
		profilePicturePanel.setBorder(null);
		profilePicturePanel.setBackground(colorClear);
		profilePicturePanel.setBounds(10, 10, 100, 100);
		add(profilePicturePanel);

		//TODO move to a new class
        JLabel nextSkillLabel;
        ImageIcon nextSkillIcon;
        JLabel nextSkillNameLabel;

		ArrayList<String> skills = unit.getEquippedSkills();
		for(int i = 0; i < skills.size(); i++)
        {
            nextSkillIcon = IffuCardResourceManager.getSkillIcon(skills.get(i));
            nextSkillLabel = new JLabel(nextSkillIcon);
            nextSkillLabel.setBorder(null);
            nextSkillLabel.setBounds(69, 121+(i*24), nextSkillIcon.getIconWidth(), nextSkillIcon.getIconHeight());
            add(nextSkillLabel);

            nextSkillNameLabel= new JLabel("<html>"+ skills.get(i) +"</html>");
            nextSkillNameLabel.setBounds(92, 119+(i*24), 71, 24);
            nextSkillNameLabel.setFont(new Font("Calibri", Font.BOLD, 10));
            nextSkillNameLabel.setForeground(Color.WHITE);
            nextSkillNameLabel.setVerticalAlignment(SwingConstants.CENTER);
            nextSkillNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(nextSkillNameLabel);
        }
	}

	private static BufferedImage getUserImage(JDA jda, IffuUnit unit) throws IOException
	{
		BufferedImage ret = null;
		URL url = new URL(jda.getUserById(unit.getUserID()).getAvatarUrl());
		URLConnection connection = url.openConnection();
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
		ret = ImageIO.read(connection.getInputStream());
		return ret;
	}

	public void render()
	{
		render = new BufferedImage(
			      320,
			      240,
			      BufferedImage.TYPE_INT_ARGB
			      );
			    // call the Component's paint method, using
			    // the Graphics object of the image.
			    paint( render.getGraphics() ); // alternately use .printAll(..)
	}

	public File saveImage()
	{
		File dest = new File("renders/" + unit.getUserID() + ".png");
		try
		{
			ImageIO.write(render, "PNG", dest);
			return dest;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
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
