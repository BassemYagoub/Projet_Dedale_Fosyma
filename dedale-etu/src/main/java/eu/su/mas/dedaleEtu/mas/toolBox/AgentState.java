package eu.su.mas.dedaleEtu.mas.toolBox;

public enum AgentState {
	Nothing,
	Dispatcher,
	Exploring,
	ExploringDispatch,
	ExploringCoalition,
	SendingPing,
	SendingEndConversation,
	HandlerPingMessage,
	HandlerPongMessage,
	HandlerSynchronizeMessage,
	HandlerIsBusyMessage,
	HandlerAcceptCoalitionMessage,
	HandlerCoalitionInvitationMessage,
	HandlerConversationFinishedMessage,
	HandlerRequestJoinCoalitionMessage,
	HandlerCanMoveNearMessage,
	HandlerCanMoveMessage,
	HandlerSynchronizeGroupMessage,
	Coalition,
	Blocking,
	Redirect,
	Done
	
}
