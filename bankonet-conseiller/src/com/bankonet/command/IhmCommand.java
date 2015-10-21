package com.bankonet.command;

public abstract class IhmCommand {
	public abstract void execute();
	public abstract Integer getId();
	public abstract String getLibelle();
	
	public int compareTo(IhmCommand command){
		// A coder
		return 0;
	}
}
