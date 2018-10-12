package sessions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.security.auth.login.LoginException;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import java.util.List;

import item.IffuInventoryRegistry;
import item.IffuItemManager;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ShutdownEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import skill.IffuSkillManager;
import unit.IffuClassRegistry;
import unit.IffuSupportTable;
import unit.IffuUnitRegistry;

public class IffuBot
{
    public static String ownerId;

	public static void main(String[] args) throws IOException, LoginException, IllegalArgumentException
    {
    	IffuClassRegistry.loadClasses("classDefs.json");
    	IffuItemManager.loadItemDefs("itemDefs.json");
    	IffuSupportTable.loadSupports("supportstore");
    	IffuUnitRegistry.loadUnits("unitstore");
    	IffuInventoryRegistry.loadInventories("inventories/equiped");
        IffuSkillManager.loadSkillDefs("skillDefs.json");
    	
        List<String> list = Files.readAllLines(Paths.get("config.txt"));
        String token = list.get(0);
        ownerId = list.get(1);

        /*EventListener shutdownListener = new EventListener() {
            @Override
            public void onEvent(Event event) {
                if(event instanceof ShutdownEvent)
                {
                    IffuClassRegistry.saveClasses();
                    IffuInventoryRegistry.saveInventories();
                    IffuSupportTable.saveSupports();
                    System.out.println("Oh OK");
                }
            }
        };*/

        EventWaiter waiter = new EventWaiter();
        CommandClientBuilder client = new CommandClientBuilder();
        client.setOwnerId(ownerId);
        client.setPrefix("iffu?");
        client.addCommands
        (

                new CommandRegister(),

                new CommandStats(),

                new CommandAkatsuki(),
                
                new CommandRename(),
                
                new CommandDeregister(),
                
                new CommandPromote(),
                
                new CommandReload(),
                
                new CommandSupports(),
                
                new CommandListUnits(),
                
                new CommandListItemsOrSkills(),
                
                new CommandGiveItem(),

                new CommandGiveSkill(),
                
                new CommandTrigger(),

                new CommandHonk(),

                new CommandUse(),

                new CommandEquip(),

                new CommandUnequip(),

                new CommandToss()
        );

        ExpListener expListener = new ExpListener();
        SupportListener supportListener = new SupportListener();
        
		new JDABuilder(AccountType.BOT)
                .setToken(token)
                .addEventListener(supportListener)
                .addEventListener(expListener)
                .addEventListener(waiter)
                .addEventListener(client.build())
                //.addEventListener(shutdownListener)
                .buildAsync();
		
		IffuClassRegistry.printClasses();
    	IffuItemManager.printItems();
    }

    public static String getOwnerId() {
	    return ownerId;
    }
}