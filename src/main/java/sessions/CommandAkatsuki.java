package sessions;

import java.util.concurrent.ExecutionException;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.examples.command.ShutdownCommand;

import item.IffuInventory;
import item.IffuInventoryRegistry;
import unit.IffuSupportTable;
import unit.IffuUnitRegistry;

public class CommandAkatsuki extends ShutdownCommand
{
    public CommandAkatsuki()
    {
    	super();
        this.name = "Akatsuki";
        this.help = "Shut down Bot (Owner only)";
        this.guildOnly = false;
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event)
    {
    	IffuUnitRegistry.saveUnits();
        IffuInventoryRegistry.saveInventories();
        IffuSupportTable.saveSupports();

    	try
    	{
    		event.getChannel().sendMessage("hitori...").complete();
			event.getAuthor().openPrivateChannel().submit().get().sendMessage("I have been shut down.").complete();
		}
    	catch (InterruptedException | ExecutionException e)
    	{
		}
    	event.getJDA().shutdown();
    }
}
