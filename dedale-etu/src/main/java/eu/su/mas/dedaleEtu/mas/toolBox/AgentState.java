package eu.su.mas.dedaleEtu.mas.toolBox;

public enum AgentState {
	Nothing,
	Dispatcher,
	Exploring,
	SendingPing,
	HandlerPingMessage,
	HandlerPongMessage,
	HandlerSynchronizeMessage,
	HandlerIsBusyMessage,
	Blocking,
	Redirect,
	Done
	
}
