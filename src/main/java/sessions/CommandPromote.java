package sessions;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import item.IffuInventoryRegistry;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.requests.RequestFuture;
import unit.IffuUnit;
import unit.IffuClass;
import unit.IffuClassRegistry;
import unit.IffuUnitRegistry;
import unitCards.IffuPromotionCard;

public class CommandPromote extends Command
{
    public CommandPromote()
    {
        this.name = "promote";
        this.help = "Promote a unit.";
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event)
    {
    	
    	if(!IffuUnitRegistry.isRegistered(event.getAuthor().getId()))
    		event.reply("You are not yet registered.");
    	else
    	{
    		if(event.getAuthor().getId().equals(IffuBot.getOwnerId()))
			{
				int indDelimit = event.getMessage().getContentRaw().lastIndexOf('$') + 1;
				String classToPromote = event.getMessage().getContentRaw().substring(indDelimit);

				if(event.getMessage().getMentionedUsers().isEmpty())
				{
					event.reply("Mention a user.");
					return;
				}
				String userToPromoteId = event.getMessage().getMentionedUsers().get(0).getId();

				IffuUnit unitToPromote = IffuUnitRegistry.getMember(userToPromoteId);

				if(unitToPromote == null)
                {
                    event.reply("This user is not registered.");
                    return;
                }

                IffuClass nextClass = IffuClassRegistry.getNullableClass(classToPromote);

				if(nextClass == null)
                {
                    event.reply("\"" + classToPromote + "\" is not a valid class.");
                    return;
                }

                doPromote(unitToPromote, nextClass, true, event);

				return;
			}

    		IffuUnit u = IffuUnitRegistry.getMember(event.getAuthor().getId());
    		ArrayList<IffuClass> promotions = u.getPromotions();
    		String args = event.getArgs();
    		
    		if(args.startsWith("list"))
    		{
    			if(promotions.isEmpty())
                {
                    event.reply("You may not promote at this time.");
                }

    			else
    			{
    				String msg = "You may promote to: \n";
    				
    				for(IffuClass uc : promotions)
    					msg += uc.getClassName() + '\n';
    				
    				msg += "\nTo preview a promotion, use \"iffu?promote preview <class name>\"";
					msg += "\nTo confirm a promotion, use \"iffu?promote promote <class name>\"";
    				
    				event.reply(msg);
    			}
    		}
    			
    		else if(args.startsWith("promote ") || args.startsWith("preview "))
    		{
    			boolean savePromotion = args.startsWith("promote ");
    			args = args.substring(8);
    			IffuClass toPromote = null;
    			
    			toPromote = IffuClassRegistry.getNullableClass(args);
    			if(toPromote == null)
    			{
    				event.reply("Unknown class. Make sure your typing is correct.");
    				return;
    			}
    			
    			if(u.canPromote(toPromote))
				{
				    doPromote(u, toPromote, savePromotion, event);
				}
				else
					event.reply("You may not promote to " + toPromote.getClassName() + " at this time.");

    		}
    		else
    		{
    			event.reply("I don't understand. Use \"iffu?promote list\" to check available promotions.");
    		}
    	}
        
    }

    private void doPromote(IffuUnit u, IffuClass toPromote, boolean savePromotion, CommandEvent event)
    {
        String[] skillsToLearn = toPromote.getBaseSkills();
        String skillLearnedMsg;

        if(savePromotion)
            skillLearnedMsg = "You learned \"";
        else
            skillLearnedMsg = "You will learn \"";

        for(String s : skillsToLearn)
        {
            if(u.canLearn(s))
                event.reply(skillLearnedMsg + s + "\"");
        }

        IffuUnit thePromoted = new IffuUnit(u);
        thePromoted.promoteUnit(toPromote, savePromotion);
        IffuPromotionCard promoteCard = new IffuPromotionCard(thePromoted);

        promoteCard.drawLvlUpCard(u);
        File promoCard = promoteCard.saveImage();

        if(savePromotion)
            IffuUnitRegistry.updateMember(u.getUserID(), thePromoted);

        RequestFuture<Message> submitRequest = event.getTextChannel().sendFile(promoCard, thePromoted.getUserName() + ".png", null).submit();

        Thread submitRenderAndToss = new Thread(new Runnable() {
            @Override
            public void run() {
                File toDelete = promoCard;
                System.out.println("Deleting file \""+toDelete.getAbsolutePath()+"\"");
                while(!submitRequest.isDone()){};
                promoCard.delete();
            }
        });

        submitRenderAndToss.start();
    }
}