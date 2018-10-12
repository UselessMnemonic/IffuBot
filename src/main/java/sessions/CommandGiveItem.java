package sessions;

import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import item.IffuInventoryRegistry;
import item.IffuItem;
import item.IffuItemManager;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import unit.IffuUnitRegistry;

public class CommandGiveItem extends Command
{
    public CommandGiveItem()
    {
        this.name = "giveitem";
        this.help = "Gives an item to a unit.  (Owner only)";
        this.guildOnly = false;
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event)
    {
    	List<User> toWhom = event.getMessage().getMentionedUsers();
		String toParse = event.getMessage().getContentDisplay();
    	toParse = toParse.substring(toParse.indexOf('$')+1);
    	
    	if(toParse.isEmpty())
    	{
    		event.reply("Error in parsing command.");
    		return;
    	}
    	
    	IffuItem itemToGive = IffuItemManager.getInstance(toParse);
    	if(itemToGive == null)
    	{
    		event.reply("The specified item does not exist.");
    		return;
    	}
    	
    	for(User u : toWhom)
    	{
    		Member nextMember = event.getGuild().getMemberById(u.getId());

    		if(nextMember == null)
    		    event.reply(u.getName() + " is not on this server.");
    		else if(!IffuUnitRegistry.isRegistered(u.getId()))
    			event.reply(nextMember.getEffectiveName() + " must register.");
    		else
    		{
    			if(IffuInventoryRegistry.addItem(u.getId(), itemToGive))
    				event.reply("Gave \"" + itemToGive.getItemName() + "\" to " + nextMember.getEffectiveName());
    			else
                    event.reply("Could not give \"" + itemToGive.getItemName() + "\" to " + nextMember.getEffectiveName());
    		}
    	}
    }
}