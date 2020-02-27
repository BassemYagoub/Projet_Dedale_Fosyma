package eu.su.mas.dedaleEtu.mas.toolBox;

public enum AgentState {
	Nothing,
	Dispatcher,
	Exploring,
	SendingPing,
	HandlerPingMessage,
	HandlerPongMessage,
	HandlerSynchronizeMessage,
	Blocking,
	Redirect,
	Done
	
}
