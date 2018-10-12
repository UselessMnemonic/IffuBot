package misc;

import unit.IffuUnit;

public class UnitPrinter
{
	public static String statsPrint(IffuUnit u)
	{
		String r = "HP: " + u.getVisibleUserStats().getHP() + '\n';
			  r += "Str: " + u.getVisibleUserStats().getStr() + '\n';
			  r += "Mag: " + u.getVisibleUserStats().getMag() + '\n';
			  r += "Skill: " + u.getVisibleUserStats().getSkl() + '\n';
			  r += "Spd: " + u.getVisibleUserStats().getSpd() + '\n';
			  r += "Lck: " + u.getVisibleUserStats().getLck() + '\n';
			  r += "Def: " + u.getVisibleUserStats().getDef() + '\n';
			  r += "Res: " + u.getVisibleUserStats().getRes() + '\n';
			  r += "Mov: " + u.getVisibleUserStats().getMov() + '\n';
			  r += "Exp: " + (u.isMaxLvl() ? "Max Exp" : u.getExp()) + '\n';
			  r += "Lvl: " + u.getLevel() + '\n';
	   return r;
	}
}
