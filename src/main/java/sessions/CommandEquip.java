package sessions;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import item.IffuInventory;
import item.IffuInventoryRegistry;
import item.IffuItem;
import skill.IffuSkill;
import unit.IffuUnit;
import unit.IffuUnitRegistry;

import java.util.Queue;

public class CommandEquip extends Command
{
    public CommandEquip()
    {
        this.name = "equip";
        this.help = "Equip an item in your inventory or a skill.";
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

        if(event.getArgs().startsWith("item"))
        {
            String subArgs = event.getArgs();
            if(subArgs.length() > 5)
                subArgs = subArgs.substring(5);
            else
                subArgs = "";

            subCommandItem(user, currentInventory, event, subArgs);
        }
        else if(event.getArgs().startsWith("skill"))
        {
            String subArgs = event.getArgs();
            if(subArgs.length() > 6)
                subArgs = subArgs.substring(6);
            else
                subArgs = "";

            subCommandSkill(user, currentInventory, event, subArgs);
        }
        else
            event.reply("I don't understand. Use `iffu?equip item <place>` or `iffu?equip skill <skill name>` to equip an item or skill.");

        IffuInventoryRegistry.saveInventory(event.getAuthor().getId());
        IffuUnitRegistry.saveUnit(event.getAuthor().getId());
    }

    private void subCommandItem(IffuUnit user, IffuInventory currentInventory, CommandEvent event, String itemPlace)
    {
        if(currentInventory.numItems() == 0)
        {
            event.reply("You have no items!");
            return;
        }

        if(itemPlace.equals(""))
        {
            user.equipItem(1);
            IffuItem item = currentInventory.getEquippedItem();
            event.reply("Equipped \"" + (item.isForged() ? item.getCustomName() : item.getItemName()) + "\"");
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

                user.equipItem(placeValue);
                event.reply("Equipped \"" + (item.isForged() ? item.getCustomName() : item.getItemName()) + "\"");
            }
            catch(Exception e)
            {
                event.reply("I don't understand. Use `iffu?equip` to equip the first item or `iffu?equip <place> to equip an item in that place.");
            }
        }
    }

    private void subCommandSkill(IffuUnit user, IffuInventory currentInventory, CommandEvent event, String skillName)
    {
        if(currentInventory.numSkills() == 0)
        {
            event.reply("You have no skills!");
            return;
        }

        if(skillName.equals(""))
        {
            event.reply("You must specify a skill.");
        }

        else
        {
            if(!currentInventory.hasSkill(skillName))
            {
                event.reply("You may not use that skill.");
                return;
            }

            if(!user.canEquipSkill())
            {
                event.reply("You cannot equip any more skills.");
                return;
            }

            user.equipSkill(skillName);
            event.reply("Equipped \"" + skillName + "\"");
        }
    }
}