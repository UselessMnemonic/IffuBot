package sessions;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import item.IffuInventory;
import item.IffuInventoryRegistry;
import item.IffuItem;
import skill.IffuSkill;
import unit.IffuUnit;
import unit.IffuUnitRegistry;

public class CommandUnequip extends Command
{
    public CommandUnequip()
    {
        this.name = "unequip";
        this.help = "Unequip an item in your inventory or a skill.";
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
            subCommandItem(user, currentInventory, event);
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
            event.reply("I don't understand. Use `iffu?unequip item` or `iffu?unequip skill <skill name>` to unequip an item or skill.");

        IffuInventoryRegistry.saveInventory(event.getAuthor().getId());
        IffuUnitRegistry.saveUnit(event.getAuthor().getId());
    }

    private void subCommandItem(IffuUnit user, IffuInventory currentInventory, CommandEvent event)
    {
        if(currentInventory.numItems() == 0)
    {
        event.reply("You have no items!");
        return;
    }

        if(!currentInventory.isEquipped())
        {
            event.reply("You have no items equipped.");
            return;
        }

        IffuItem theUnequipped = currentInventory.getEquippedItem();
        event.reply("Unequipped \"" + (theUnequipped.isForged() ? theUnequipped.getCustomName() : theUnequipped.getItemName()) + "\"");

        user.unequipItem();
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
            if(!user.hasEquippedSkill(skillName))
            {
                event.reply("You do not have that skill equipped.");
                return;
            }

            user.unequipSkill(skillName);
            event.reply("Unequipped \"" + skillName + "\"");
        }
    }
}