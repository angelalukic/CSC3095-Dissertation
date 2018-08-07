package com.bot.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javacord.api.entity.message.Message;

import com.bot.command.commands.ChannelChanger;
import com.bot.command.commands.WordAdder;

public class AdminCommand extends AbstractCommand {
	
	public AdminCommand(Message message) {
		super(message);
	}

	public void execute() throws IOException {

		String command = retrieveCommand();
		
		if(command.startsWith("adminchannel") || command.startsWith("reportschannel")) {
			changeChannel(command);
		}
		
		else if(command.startsWith("addhighurgencywords") || command.startsWith("addmediumurgencywords") ||
				command.startsWith("addlowurgencywords") || command.startsWith("addwhitelistwords")) {
			changeWordFilter(command);
		}
	}
	
	private void changeChannel(String command) throws IOException {
		
		try {
			String channelId = getMessage().getMentionedChannels().get(0).getIdAsString();
			
			Map<String, String> channelData = new HashMap<String, String>();
			Map<String, Object> serverData = new HashMap<String, Object>();
			
			ChannelChanger channelChanger = new ChannelChanger(getMessage(), channelData, serverData);
			
			if(command.startsWith("adminchannel")) {
				channelChanger.changeAdminChannel(channelId);
			}
			
			else if(command.startsWith("reportschannel")) {
				channelChanger.changeReportsChannel(channelId);
			}
			
		} catch (IndexOutOfBoundsException e) {
			getMessage().getChannel().sendMessage("You need to provide a valid channel.");
		}
	}
	
	private void changeWordFilter(String command) throws IOException {
		
		String words = command.replaceFirst("(addhighurgencywords)?(addmediumurgencywords)?(addlowurgencywords)?(addwhitelistwords)?\\s?", "");
		
		Map<String, List<String>> wordData = new HashMap<String, List<String>>();
		Map<String, Object> serverData = new HashMap<String, Object>();
		
		WordAdder wordAdder = new WordAdder(getMessage(), wordData, serverData);
		
		if(command.startsWith("addhighurgencywords") && words.length() != 0) {
			wordAdder.addHighUrgencyWords(words);
		}
		
		else if(command.startsWith("addmediumurgencywords") && words.length() != 0) {
			wordAdder.addMediumUrgencyWords(words);
		}
		
		else if(command.startsWith("addlowurgencywords") && words.length() != 0) {
			wordAdder.addLowUrgencyWords(words);
		}
		
		else if (command.startsWith("addwhitelistwords") && words.length() != 0) {
			wordAdder.addWhitelistWords(words);
		}
		
		else {
			getMessage().getChannel().sendMessage("You need to provide words.");
		}
	}
}
