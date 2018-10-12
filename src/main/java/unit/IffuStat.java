package unit;

public class IffuStat
{
	public static final int 
	HP = 0,
	Str = 1,
	Mag = 2,
	Skl = 3,
	Spd = 4,
	Lck = 5,
	Def = 6,
	Res = 7,
	Mov = 8;
	
	private byte[] raw;

    public byte[] getRaw()
	{
		return raw;
	}
	
	public IffuStat(byte[] raw)
	{
		this.raw = new byte[9];
		for(int i = 0; i <= 8; i++)
			this.raw[i] = raw[i];
	}
	
	public IffuStat()
	{
		this.raw = new byte[9];
	}
	
	public byte getHP()
	{
		return raw[IffuStat.HP];
	}
	
	public void setHP(byte value)
	{
		raw[IffuStat.HP] = value;
	}
	
	public byte getStr()
	{
		return raw[IffuStat.Str];
	}
	
	public void setStr(byte value)
	{
		raw[IffuStat.Str] = value;
	}
	
	public byte getMag()
	{
		return raw[IffuStat.Mag];
	}
	
	public void setMag(byte value)
	{
		raw[IffuStat.Mag] = value;
	}
	
	public byte getSkl()
	{
		return raw[IffuStat.Skl];
	}
	
	public void setSkl(byte value)
	{
		raw[IffuStat.Skl] = value;
	}
	
	public byte getSpd()
	{
		return raw[IffuStat.Spd];
	}
	
	public void setSpd(byte value)
	{
		raw[IffuStat.Spd] = value;
	}
	
	public byte getLck()
	{
		return raw[IffuStat.Lck];
	}
	
	public void setLck(byte value)
	{
		raw[IffuStat.Lck] = value;
	}
	
	public byte getDef()
	{
		return raw[IffuStat.Def];
	}
	
	public void setDef(byte value)
	{
		raw[IffuStat.Def] = value;
	}

	public byte getRes()
	{
		return raw[IffuStat.Res];
	}
	
	public void setRes(byte value)
	{
		raw[IffuStat.Res] = value;
	}
	
	public byte getMov()
	{
		return raw[IffuStat.Mov];
	}
	
	public void setMov(byte value)
	{
		raw[IffuStat.Mov] = value;
	}

	public static IffuStat add(IffuStat s1, IffuStat s2)
	{
		byte[] ret = new byte[9];
		
		for(int i = 0; i <= 8; i++)
			ret[i] = (byte)(s1.raw[i] + s2.raw[i]);
		
		return new IffuStat(ret);
	}

	public static IffuStat sub(IffuStat s1, IffuStat s2)
	{
		byte[] ret = new byte[9];

		for(int i = 0; i <= 8; i++)
			ret[i] = (byte)(s1.raw[i] - s2.raw[i]);

		return new IffuStat(ret);
	}
	
	public String toString()
	{
		String ret = "[";
		for(int i = 0; i <= 8; i++)
			ret += raw[i] + ", ";
		ret += raw[8] + "]";
		return ret;
	}
}
