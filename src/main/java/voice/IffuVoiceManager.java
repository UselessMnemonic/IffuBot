package voice;

import java.io.File;
import java.util.Random;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class IffuVoiceManager
{
	private static final String voiceDir = "res/actors/";
	
	public static String[] listVoiceActors()
	{
		File actorDirectory = new File(voiceDir);
		String[] actors = actorDirectory.list();
		for(int i = 0; i < actors.length; i++)
		{
			actors[i] = (new File(actors[i])).getName();
		}
		return actors;
	}
	
	public static String getVoiceClip(String actorName)
	{
	    String nextPath = "";
        File actorDirectory = new File(voiceDir + actorName);
		if(actorDirectory.exists() && actorDirectory.isDirectory())
		{
			File nextClipFile = null;
			File[] fileList = actorDirectory.listFiles();
			
			int iterations = 0;
			
			while(fileList.length > 0 && nextClipFile == null && iterations <= 10)
			{
				iterations += 1;
				int randIndex = (new Random()).nextInt(fileList.length);
				nextClipFile = fileList[randIndex];
				if(!nextClipFile.exists() || nextClipFile.isDirectory())
					nextClipFile = null;
			}
			
			if(nextClipFile == null)
                nextPath = "";
			else
                nextPath = nextClipFile.getAbsolutePath();
		}

        System.out.println("Accessed file \""+nextPath+"\"");
		return nextPath;
	}
	
}
