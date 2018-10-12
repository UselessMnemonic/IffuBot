package sessions;

import java.util.ArrayList;
import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import unit.*;

public class CommandSupports extends Command
{
    public CommandSupports()
    {
        this.name = "supports";
        this.help = "Look at support levels.";
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event)
    {
    	if(event.getAuthor().isBot())
    		return;

    	if(!IffuUnitRegistry.isRegistered(event.getAuthor().getId()))
    		event.reply("You must register with \"iffu?register\" first.");
    	else
    	{
    		String subcommand = event.getArgs();

    		if(subcommand.startsWith("list"))
			{

				String msg;

				String currUser = event.getAuthor().getId();

				ArrayList<IffuSupport> unitSupports = IffuSupportTable.getSupports(currUser);
				ArrayList<IffuSupport> unitAvailableSupports = new ArrayList<IffuSupport>();

				for(IffuSupport is : unitSupports)
				{
					Member support = event.getGuild().getMemberById(is.getSupport(currUser));
					if(support != null)
						unitAvailableSupports.add(is);
				}

				if(unitAvailableSupports.isEmpty())
				{
					msg = "You have no mutual supports here! Try mentioning other Units.";
				}
				else
				{
					msg = "Your current supports are:\n";
					for(IffuSupport is : unitAvailableSupports)
					{
						IffuUnit support = IffuUnitRegistry.getMember(is.getSupport(currUser));
						msg += support.getUserName();
						msg += " with Support Rank " + is.getSupportRank() + "\n";
						msg += "   " + event.getJDA().getUserById(support.getUserID())/*.getAsMention()*/ + "\n";
					}
				}

				event.reply(msg);
			}
			else if(subcommand.startsWith("marry"))
			{
			    IffuSupport currentPartner = IffuSupportTable.getPartner(event.getAuthor().getId());

			    if(currentPartner != null)
                {
                    event.reply("You're currently married to " + event.getJDA().getUserById(currentPartner.getSupport(event.getAuthor().getId())).getName() + " If you want a divorce, use `iffu?support divorce`");
                    return;
                }

				if(event.getMessage().getMentionedUsers().isEmpty())
				{
					event.reply("Please specify a user by mentioning them.");
					return;
				}

				Member mentionedUser = event.getGuild().getMemberById(event.getMessage().getMentionedUsers().get(0).getId());
				IffuUnit unitToMarry = IffuUnitRegistry.getMember(mentionedUser.getUser().getId());

				if(mentionedUser == null)
				{
					event.reply("This person is not in this server.");
					return;
				}

				if(unitToMarry == null)
				{
					event.reply(mentionedUser.getEffectiveName() + " has not registered.");
				}

				IffuSupport mutualSupport = IffuSupportTable.getSupport(event.getAuthor().getId(), mentionedUser.getUser().getId());

				if(mutualSupport.getSupportRank() != "A+")
                {
                    event.reply("You must have support rank A+ to marry this person.");
                    return;
                }

				boolean nowMarried = mutualSupport.makePartners(event.getAuthor().getId());

				if(nowMarried)
                {
                    event.reply("Congrats to the newly-weds! Your support rank is now S!");
                }
                else
                {
                    event.reply("Your fiance must now enact the same command with you.");
                }
			}
			else if(subcommand.startsWith("divorce"))
            {
                IffuSupport currentPartner = IffuSupportTable.getPartner(event.getAuthor().getId());

                if(currentPartner == null)
                    event.reply("You aren't married to anybody.");

                currentPartner.dropPartner(event.getAuthor().getId());
                event.reply("You and " + event.getJDA().getUserById(currentPartner.getSupport(event.getAuthor().getId())).getName() + " have divorced. Your support rank has returned to A+");
            }
			else
			{
				event.reply("I don't understand. Use `iffu?supports list` to see your supports, `iffu?supports marry` to marry a unit, or `iffu?support divorce` to divorce.");
			}
    	}
    }
}
