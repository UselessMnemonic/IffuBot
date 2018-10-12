package sessions;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import item.IffuInventoryRegistry;
import skill.IffuSkillManager;
import unit.IffuClassRegistry;
import unit.IffuSupportTable;
import unit.IffuUnitRegistry;

public class CommandReload extends Command
{
    public CommandReload()
    {
        this.name = "reload";
        this.help = "Realoads the User and Class registries. (Owner only)";
        this.guildOnly = false;
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event)
    {
    	IffuSupportTable.reloadSupports();
    	IffuUnitRegistry.reloadUnits();
    	IffuClassRegistry.reloadClasses();
        IffuSkillManager.reloadSkillDefs();
        IffuInventoryRegistry.reloadInventories();
    	event.reply("Reloaded.");
    }
}