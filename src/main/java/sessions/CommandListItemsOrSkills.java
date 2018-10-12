package sessions;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import item.IffuInventory;
import item.IffuInventoryRegistry;
import item.IffuItem;
import item.IffuItemManager;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import unit.IffuUnit;
import unit.IffuUnitRegistry;

import java.util.ArrayList;

public class CommandListItemsOrSkills extends Command
{
    public CommandListItemsOrSkills()
    {
        this.name = "list";
        this.help = "Lists your items or skills.";
        this.guildOnly = false;
        this.ownerCommand = false;
    }

    @Override
    protected void execute(CommandEvent event)
    {
        User potentialUser = event.getAuthor();

        if(!event.getMessage().getMentionedUsers().isEmpty())
            potentialUser = event.getMessage().getMentionedUsers().get(0);

        Member potentialMember = event.getGuild().getMemberById(potentialUser.getId());

        if(potentialMember == null)
        {
            event.reply(potentialUser.getName() + " is not on this server.");
            return;
        }

    	if(!IffuUnitRegistry.isRegistered(potentialUser.getId()))
        {
            event.reply(potentialMember.getEffectiveName() + " must register first! Use `iffu?register`");
        }

        IffuInventory theirInventory = IffuInventoryRegistry.getInventory(potentialUser.getId());
    	IffuUnit theirUnit = IffuUnitRegistry.getMember(potentialUser.getId());
    	String reply = "";

        if(event.getArgs().startsWith("skills"))
        {
            ArrayList<String> skills = theirInventory.getUseableSkills();
            if(skills.isEmpty())
            {
                event.reply("No skills learned.");
                return;
            }

            reply = "Available skills:\n";
            for(String s : skills)
            {
                if(theirUnit.hasEquippedSkill(s))
                    reply += "**" + s + "**\n";
                else
                    reply += s + "\n";
            }

            event.reply(reply);
        }
        else if(event.getArgs().startsWith("items"))
        {
            ArrayList<IffuItem> items = theirInventory.getItemList();
            if(items.isEmpty())
            {
                event.reply("No items in inventory.");
                return;
            }

            reply = "Available items:\n";
            IffuItem currentItem = theirInventory.grabItem(1);

            if(theirInventory.isEquipped())
            {
                if(currentItem.isForged())
                    reply += "***" + currentItem.getCustomName() + "***\n";
                else
                    reply += "**" + currentItem.getCustomName() + "**\n";
            }
            else if(currentItem.isForged())
                reply += "*" + currentItem.getCustomName() + "*\n";

            else
                reply += currentItem.getCustomName() + "\n";

            for(int place = 2; place <= items.size(); place++)
            {
                currentItem = theirInventory.grabItem(place);

                if(currentItem.isForged())
                    reply += "*" + currentItem.getCustomName() + "*\n";

                else
                    reply += currentItem.getCustomName() + "\n";
            }
            event.reply(reply);
        }
    }
}