package sessions;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import item.IffuInventoryRegistry;
import unit.IffuSupportTable;
import unit.IffuUnit;
import unit.IffuUnitRegistry;

public class CommandDeregister extends Command
{
    public CommandDeregister()
    {
        this.name = "deregister";
        this.help = "Deregister a unit.";
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event)
    {
    	
    	if(!IffuUnitRegistry.isRegistered(event.getAuthor().getId()))
    		event.reply("You are not yet registered.");
    	else
    	{
    		IffuUnit u = IffuUnitRegistry.getMember(event.getAuthor().getId());
    		
    		if(IffuUnitRegistry.deregisterMember(u.getUserID()))
    		{
    			event.reply("Deregistered. Goodbye, \"" + u.getUserName() + '\"');
    			IffuSupportTable.removeAll(u.getUserID());
    			IffuInventoryRegistry.remove(u.getUserID());
    		}
    		else
    			event.replyWarning("Could not deregister " + u.getUserName());
    	}
        
    }
}