package sessions;

import java.util.List;

import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import unit.IffuSupport;
import unit.IffuSupportTable;
import unit.IffuUnitRegistry;

public class SupportListener implements EventListener
{

	@Override
	public void onEvent(Event event)
	{
		if(event instanceof MessageReceivedEvent)
		{
			MessageReceivedEvent evt = (MessageReceivedEvent)event;
			
			if(evt.getAuthor().isBot())
				return;
			
			if(IffuUnitRegistry.isRegistered(evt.getAuthor().getId()))
			{
				List<User> mentions = evt.getMessage().getMentionedUsers();
				IffuSupport nextSupport;
				
				for(User u : mentions)
				{
					if(!u.getId().equals(evt.getAuthor().getId()) && IffuUnitRegistry.isRegistered(u.getId()))
					{
						nextSupport = IffuSupportTable.getSupport(u.getId(), evt.getAuthor().getId());
						
						if(nextSupport == null)
						{
							IffuSupportTable.addSupport(evt.getAuthor().getId(), u.getId());
							nextSupport = IffuSupportTable.getSupport(evt.getAuthor().getId(), u.getId());
						}

						nextSupport.increment(evt.getAuthor().getId());
						IffuSupportTable.saveSupport(nextSupport);
					}
				}
			}
			
		}
	}

}
