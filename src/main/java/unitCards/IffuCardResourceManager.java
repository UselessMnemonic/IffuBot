package unitCards;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class IffuCardResourceManager {

    private static BufferedImage unitProfileCardBG;
    private static BufferedImage defaultAvatarImage;
    private static BufferedImage unitStatCardBackground;
    private static BufferedImage brownPointer, bluePointer, redPointer;
    private static BufferedImage[] digitGlyphs;
    private static Font defaultFont, feFont;
    private static BufferedImage statPaneBG;
    private static BufferedImage expCursor, expMax, expBarBG;
    private static BufferedImage expMaxTransperant;
    private static BufferedImage alphaMap;
    private static BufferedImage unknownItemMissingIcon;
    private static BufferedImage equippedIcon;
    private static HashMap<String, BufferedImage> itemIcons;
    private static HashMap<String, BufferedImage> classIcons;
    private static HashMap<String, BufferedImage> skillIcons;

    public enum ImageReference
    {
        UnitCardProfileBackground,
        UnitCardDdefaultAvatarImage,
        UnitStatCardBackground,
        PointerBrown,
        PointerBlue,
        PointerRed,
        ExperienceMaximum,
        ExperienceMaximumTransperant,
        UnitStatPaneBackground,
        ExperienceCurser,
        ExperienceBarBackground,
        AlphaMap,
        MissingIconItem,
        EquippedIcon
    }

    public enum FontReference
    {
        DefaultFont,
        FEFont
    }

    static
    {
        itemIcons = new HashMap<>();
        classIcons = new HashMap<>();
        skillIcons = new HashMap<>();

        try{unitProfileCardBG = ImageIO.read(new File("res/unitProfileCardBG.png"));}
        catch(IOException ioe){unitProfileCardBG = null;}

        try{statPaneBG = ImageIO.read(new File("res/statPaneBG.png"));}
        catch(IOException ioe){statPaneBG = null;}

        defaultFont = new Font("Calibri", Font.BOLD, 13);

        try{feFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/FOT-ChiaroStd-B.otf"));}
        catch(FontFormatException | IOException e){feFont = null;}

        try{defaultAvatarImage = ImageIO.read(new File("res/defaultAvatar.png"));}
        catch(IOException ioe){defaultAvatarImage = null;}

        try{unitStatCardBackground = ImageIO.read(new File("res/StatCard.png"));}
        catch(IOException ioe){unitStatCardBackground = null;}

        try{brownPointer = ImageIO.read(new File("res/brownPointer.png"));}
        catch(IOException ioe){brownPointer = null;}

        try{bluePointer = ImageIO.read(new File("res/bluePointer.png"));}
        catch(IOException ioe){bluePointer = null;}

        try{redPointer = ImageIO.read(new File("res/redPointer.png"));}
        catch(IOException ioe){redPointer = null;}

        try{expCursor = ImageIO.read(new File("res/expCursor.png"));}
        catch(IOException ioe){expCursor = null;}

        try{expMax = ImageIO.read(new File("res/expMax.png"));}
        catch(IOException ioe){expMax = null;}

        try{expMaxTransperant = ImageIO.read(new File("res/expMaxTransperant.png"));}
        catch(IOException ioe){expMaxTransperant = null;}

        try{expBarBG = ImageIO.read(new File("res/expBarBG.png"));}
        catch(IOException ioe){expBarBG = null;}

        try{alphaMap = ImageIO.read(new File("res/alphaMap.png"));}
        catch(IOException ioe){alphaMap = null;}

        try{unknownItemMissingIcon = ImageIO.read(new File("res/itemIcons/unknown.png"));}
        catch(IOException ioe){unknownItemMissingIcon = null;}

        try{equippedIcon = ImageIO.read(new File("res/equippedIcon.png"));}
        catch(IOException ioe){equippedIcon = null;}

        try
        {
            digitGlyphs = new BufferedImage[10];
            BufferedImage glyphImage = ImageIO.read(new File("res/numGlyph.png"));
            for(int i = 0; i < 10; i++)
            {
                digitGlyphs[i] = glyphImage.getSubimage(i*5, 0, 5, 8);
            }
        }
        catch (IOException e) {
            digitGlyphs = null;
        }
    }

    public static ImageIcon getItemIcon(String itemName)
    {
        BufferedImage ret = itemIcons.get(itemName.toLowerCase().replaceAll("\\s",""));

        if(ret == null)
            ret = unknownItemMissingIcon;

        return new ImageIcon(ret);
    }

    public static void registerItemIcon(String itemName, String iconPath)
    {
        try
        {
            File nextIconFile = new File(iconPath);
            BufferedImage nextIcon = ImageIO.read(nextIconFile);
            itemIcons.put(itemName, nextIcon);
            System.out.println("Register sprite \"" + nextIconFile.getAbsolutePath() + "\" for Item \"" + itemName + "\"");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            itemIcons.put(itemName, unknownItemMissingIcon);
        }
    }

    public static ImageIcon getClassIcon(String className)
    {
        BufferedImage ret = classIcons.get(className);

        if(ret == null)
            ret = unknownItemMissingIcon;

        return new ImageIcon(ret);
    }

    public static void registerClassIcon(String className, String iconPath)
    {
        try
        {
            File nextIconFile = new File(iconPath);
            BufferedImage nextIcon = ImageIO.read(nextIconFile);
            classIcons.put(className, nextIcon);
            System.out.println("Register sprite \"" + nextIconFile.getAbsolutePath() + "\" for Class \"" + className + "\"");
        }
        catch(Exception e)
        {
            System.err.println("Could not register sprite for Class \"" + className + "\"");
            e.printStackTrace();
            classIcons.put(className, unknownItemMissingIcon);
        }
    }

    public static ImageIcon getSkillIcon(String skillName)
    {
        BufferedImage ret = skillIcons.get(skillName);

        if(ret == null)
            ret = unknownItemMissingIcon;

        return new ImageIcon(ret);
    }

    public static void registerSkillIcon(String skillName, String iconPath)
    {
        try
        {
            File nextIconFile = new File(iconPath);
            BufferedImage nextIcon = ImageIO.read(nextIconFile);
            skillIcons.put(skillName, nextIcon);
            System.out.println("Register sprite \"" + nextIconFile.getAbsolutePath() + "\" for Skill \"" + skillName + "\"");
        }
        catch(Exception e)
        {
            System.err.println("Could not register sprite for Class \"" + skillName + "\"");
            e.printStackTrace();
            classIcons.put(skillName, unknownItemMissingIcon);
        }
    }

    public static ImageIcon getImageIconResource(ImageReference ir)
    {
        return new ImageIcon(getImageResource(ir));
    }

    public static BufferedImage getImageResource(ImageReference ir)
    {
        switch(ir)
        {
            case UnitCardProfileBackground:
                return unitProfileCardBG;
            case UnitCardDdefaultAvatarImage:
                return defaultAvatarImage;
            case UnitStatCardBackground:
                return unitStatCardBackground;
            case PointerBrown:
                return brownPointer;
            case PointerBlue:
                return bluePointer;
            case PointerRed:
                return redPointer;
            case ExperienceMaximum:
                return expMax;
            case ExperienceMaximumTransperant:
                return expMaxTransperant;
            case UnitStatPaneBackground:
                return statPaneBG;
            case ExperienceCurser:
                return expCursor;
            case ExperienceBarBackground:
                return expBarBG;
            case AlphaMap:
                return alphaMap;
            case MissingIconItem:
                return unknownItemMissingIcon;
            case EquippedIcon:
                return equippedIcon;

                default:
                    return null;
        }
    }

    public static Font getFontResource(FontReference fr)
    {
        switch(fr)
        {
            case DefaultFont:
                return defaultFont;
            case FEFont:
                return feFont;

                default:
                    return null;
        }
    }

    public static BufferedImage getDigitGlyph(int digit)
    {
        if(digit >= 0 || digit <= 9)
            return digitGlyphs[digit];
        else
            return digitGlyphs[0];
    }
}