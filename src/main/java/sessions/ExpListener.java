package sessions;

import item.IffuInventory;
import item.IffuInventoryRegistry;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.requests.RequestFuture;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import unit.IffuClass;
import unit.IffuUnit;
import unit.IffuUnitRegistry;
import unitCards.IffuPromotionCard;

import java.io.File;
import java.util.ArrayList;

public class ExpListener implements EventListener
{

	@Override
	public void onEvent(Event event)
	{
		IffuUnit u;
		
		if(event instanceof MessageReceivedEvent)
		{
			u = IffuUnitRegistry.getMember( ((MessageReceivedEvent)event).getAuthor().getId() );
			
			if(u != null && !u.isMaxLvl())
			{
				String toCheck = ((MessageReceivedEvent) event).getMessage().getContentDisplay();
				
				if(toCheck != null && !toCheck.toLowerCase().startsWith("iffu?"))
				{
					int expToAdd = (int)(2.0f*toCheck.length()/u.getInternalLevel() + 1.0f);
					if(expToAdd > 100)
						expToAdd = 100;
					
					lvlUp((MessageReceivedEvent)event, u, expToAdd);
				}
			}
		}
	}

	private void lvlUp(MessageReceivedEvent event, IffuUnit oldUnit, int expGain)
	{
	    IffuUnit newUnit = new IffuUnit(oldUnit);

        ArrayList<String> oldSkillList = new ArrayList<>();
        oldSkillList.addAll(IffuInventoryRegistry.getInventory(oldUnit.getUserID()).getUseableSkills());

		if(!newUnit.addExpAndLvlUp(expGain))
		    return;

		IffuUnitRegistry.updateMember(newUnit.getUserID(), newUnit);

        ArrayList<String> learnedSkills = new ArrayList<>();
        learnedSkills.addAll(IffuInventoryRegistry.getInventory(newUnit.getUserID()).getUseableSkills());

        learnedSkills.removeIf(s -> oldSkillList.contains(s));

		IffuPromotionCard lvlUpCard = new IffuPromotionCard(newUnit);
		lvlUpCard.drawLvlUpCard(oldUnit);
		File res = lvlUpCard.saveImage();

		for(String s : learnedSkills)
		{
		    event.getTextChannel().sendMessage("You learned \"" + s + "\"" ).submit();
		}

		RequestFuture<Message> submitRequest = event.getTextChannel().sendFile(res, newUnit.getUserName() + ".png", null).submit();
		Thread submitRenderAndToss = new Thread(new Runnable() {
            @Override
            public void run() {
                File toDelete = res;
                while(!submitRequest.isDone()){};
                System.out.println("Deleting file \""+res.getAbsolutePath()+"\"");
                toDelete.delete();
            }
        });
        submitRenderAndToss.start();
	}

}
