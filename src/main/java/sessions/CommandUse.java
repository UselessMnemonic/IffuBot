package sessions;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import item.IffuInventory;
import item.IffuInventoryRegistry;
import item.IffuItem;
import unit.IffuUnit;
import unit.IffuUnitRegistry;

public class CommandUse extends Command
{
    public CommandUse()
    {
        this.name = "use";
        this.help = "Use an item in your inventory.";
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

        if(itemPlace.equals(""))
        {
            if(!currentInventory.isEquipped())
            {
                event.reply("You have no item equipped. Try equipping one, or select using a number.");
                return;
            }

            IffuItem item = currentInventory.grabItem(1);

            if(!item.isConsumable())
            {
                event.reply("You can't use this item yet.");
                return;
            }

            user.useItem(1);
            event.reply("Used \"" + (item.isForged() ? item.getCustomName() : item.getItemName()) + "\"");
        }

        else
        {
            try
            {
                Integer placeValue = Integer.valueOf(itemPlace);
                IffuItem item = currentInventory.grabItem(placeValue);

                if(item == null)
                {
                    event.reply("No item in that place. Try again!");
                    return;
                }

                if(!item.isConsumable())
                {
                    event.reply("You can't use this item yet.");
                    return;
                }

                user.useItem(placeValue);
                event.reply("Used \"" + (item.isForged() ? item.getCustomName() : item.getItemName()) + "\"");
            }
            catch(Exception e)
            {
                event.reply("I don't understand. Use `iffu?use` to use your equipped item or `iffu?use <place>` to use an item in that place.");
            }
        }

        IffuInventoryRegistry.saveInventory(event.getAuthor().getId());
        IffuUnitRegistry.saveUnit(event.getAuthor().getId());
    }
}