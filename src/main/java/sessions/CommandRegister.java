package sessions;

import unit.IffuUnit;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import unit.IffuClassRegistry;
import unit.IffuUnitRegistry;

public class CommandRegister extends Command
{
    public CommandRegister()
    {
        this.name = "register";
        this.help = "Register a unit.";
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event)
    {
    	
    	if(IffuUnitRegistry.isRegistered(event.getAuthor().getId()))
    		event.reply("You're already registered!");
    	else
    	{
    		IffuUnit newUnit = new IffuUnit(event.getAuthor().getName(), event.getAuthor().getId(), IffuClassRegistry.getClass("Villager"));
    		if(IffuUnitRegistry.registerMember(newUnit))
    		{
    			event.reply("Registered as \"" + newUnit.getUserName() + '\"');
    		}
    		else
    			event.replyWarning("Could not register.");
    	}
        
    }
}
