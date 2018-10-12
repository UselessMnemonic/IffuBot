package misc;

public class Markup
{
	public static String bold(String s)
	{
		return "**" + s + "**";
	}
	
	public static String italic(String s)
	{
		return '*' + s + '*';
	}
	
	public static String code(String s)
	{
		return "```" + s + "```";
	}
	
	public static String strike(String s)
	{
		return "~~" + s + "~~";
	}
	
	public static String underline(String s)
	{
		return "__" + s + "__";
	}

	public static String nomarkup(String s)
	{
		return '\\' + s;
	}
}
