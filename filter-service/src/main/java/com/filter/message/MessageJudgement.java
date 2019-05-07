package com.filter.message;

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
	
	public MessageJudgement(List<String> blacklist, List<String> greylist, List<String> whitelist, JudgementLevel judgement) {
		this.blacklist = blacklist;
		this.greylist = greylist;
		this.whitelist = whitelist;
		this.judgement = judgement;
	}
}
