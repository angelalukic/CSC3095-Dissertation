package com.bot.filter.beans;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageJudgement {

	private List<String> blacklist;
	private List<String> greylist;
	private List<String> whitelist;
	private JudgementLevel judgement;
	
	public MessageJudgement() {
	}
	
	public MessageJudgement(MessageJudgement messageJudgement) {
		this.blacklist = messageJudgement.getBlacklist();
		this.greylist = messageJudgement.getGreylist();
		this.whitelist = messageJudgement.getWhitelist();
		this.judgement = messageJudgement.getJudgement();
	}
}
