package sessions;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import item.IffuInventory;
import item.IffuInventoryRegistry;
import item.IffuItem;
import unit.IffuUnit;
import unit.IffuUnitRegistry;

public class CommandToss extends Command
{
    public CommandToss()
    {
        this.name = "toss";
        this.help = "Toss an item in your inventory.";
        this.guildOnly = true;
        this.ownerCommand = false;
    }

    @Override
    protected void execute(CommandEvent event)
    {
        if(!IffuUnitRegistry.isRegistered(event.getAuthor().getId()))
        {
            event.reply("You are not registered!");
            return;
        }

        IffuUnit user = IffuUnitRegistry.getMember(event.getAuthor().getId());
        IffuInventory currentInventory = IffuInventoryRegistry.getInventory(event.getAuthor().getId());

        if(currentInventory.numItems() == 0)
        {
            event.reply("You have no items!");
            return;
        }

        String itemPlace = event.getArgs();

        try
        {
            Integer placeValue = Integer.valueOf(itemPlace);
            IffuItem item = currentInventory.grabItem(placeValue);

            if(item == null)
            {
                event.reply("No item in that place. Try again!");
                return;
            }

            user.removeItem(placeValue);
            event.reply("Tossed \"" + (item.isForged() ? item.getCustomName() : item.getItemName()) + "\"");
        }
        catch(Exception e)
        {
            event.reply("I don't understand. Use `iffu?toss <place>` to toss the item in that place.");
        }

        IffuInventoryRegistry.saveInventory(event.getAuthor().getId());
        IffuUnitRegistry.saveUnit(event.getAuthor().getId());
    }
}