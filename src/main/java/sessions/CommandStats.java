package sessions;

import java.io.File;
import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.requests.RequestFuture;
import unit.IffuUnit;
import unit.IffuUnitRegistry;
import unitCards.IffuUnitProfile;

public class CommandStats extends Command
{
    public CommandStats()
    {
        this.name = "stats";
        this.help = "Return stat card for unit.";
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event)
    {
    	User currUser;
    	List<User> args = event.getMessage().getMentionedUsers();

    	if(args.isEmpty())
    		currUser = event.getAuthor();
    	else
    		currUser = args.get(0);
    	
    	if(!IffuUnitRegistry.isRegistered(currUser.getId()))
    		event.reply(event.getGuild().getMemberById(currUser.getId()).getEffectiveName() + " must register with \"iffu?register\" first.");
    	else
    	{
    		IffuUnit u = IffuUnitRegistry.getMember(currUser.getId());
    		
    		IffuUnitProfile card = new IffuUnitProfile(event.getJDA(), u);
    		card.render();
    		File res = card.saveImage();

            RequestFuture<Message> submitRequest = event.getTextChannel().sendFile(res, u.getUserName() + ".png", null).submit();

            Thread submitRenderAndToss = new Thread(new Runnable() {
                @Override
                public void run() {
                    File toDelete = res;
                    System.out.println("Deleting file \""+toDelete.getAbsolutePath()+"\"");
                    while(!submitRequest.isDone()){};
                    res.delete();
                }
            });

            submitRenderAndToss.start();
    	}   
    }
}
