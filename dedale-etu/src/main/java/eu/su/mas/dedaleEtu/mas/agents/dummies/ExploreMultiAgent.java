package eu.su.mas.dedaleEtu.mas.agents.dummies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedale.mas.agent.behaviours.startMyBehaviours;
import eu.su.mas.dedaleEtu.mas.behaviours.CoalitionBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.ExploCoalitionBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.ExploDispatchBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.ExploMultiBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.ExploSoloBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.RedirectBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.SayHello;
import eu.su.mas.dedaleEtu.mas.behaviours.SendingEndConversationBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.SendingPingMessageBehaviour;
import eu.su.mas.dedaleEtu.mas.handlers.AcceptCoalitionMessage;
import eu.su.mas.dedaleEtu.mas.handlers.CoalitionInvitationMessage;
import eu.su.mas.dedaleEtu.mas.handlers.ConversationFinishedMessage;
import eu.su.mas.dedaleEtu.mas.handlers.DispatcherMessage;
import eu.su.mas.dedaleEtu.mas.handlers.IsBusyMessage;
import eu.su.mas.dedaleEtu.mas.handlers.PingMessage;
import eu.su.mas.dedaleEtu.mas.handlers.PongMessage;
import eu.su.mas.dedaleEtu.mas.handlers.RequestJoinCoalitionMessage;
import eu.su.mas.dedaleEtu.mas.handlers.SynchronizeMessage;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import eu.su.mas.dedaleEtu.mas.toolBox.AgentState;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;


public class ExploreMultiAgent extends AbstractDedaleAgent {

	private static final long serialVersionUID = -6431752665590433727L;
	private AgentInformations informations;

	protected void setup(){

		super.setup();
		

		List<Behaviour> lb=new ArrayList<Behaviour>();
		
		final Object[] args = getArguments();
        final ArrayList<String> receivers = new ArrayList<String> ();

        /*
         * old version for get receivers
         * for(Object arg : args) {
            // very hard pop,refactor this after ( ignore 2 first parametre) 
            if (arg == args[0] || arg == args[1]) continue;
            receivers.add((String) arg);
        }*/
        
        this.addServiceAgent("exploration");
        informations = new AgentInformations(receivers);

		//lb.add(new ExploSoloBehaviour(this,informations.myMap));	
		FSMBehaviour exploration = new FSMBehaviour(this);
		
		exploration.registerFirstState(new ExploDispatchBehaviour(this,informations), "ExploringDispatch");
		exploration.registerState(new ExploMultiBehaviour(this,informations), "Exploring");
		exploration.registerState(new ExploCoalitionBehaviour(this,informations), "ExploringCoalition");
		
		//refactor after
		exploration.registerState(new SendingPingMessageBehaviour(this,informations), "SendingPing");
		exploration.registerState(new DispatcherMessage(this,informations), "Dispatcher");
		exploration.registerState(new RedirectBehaviour(this,informations), "Redirect");
		exploration.registerState(new PingMessage(this,informations), "HandlerPingMessage");
		exploration.registerState(new PongMessage(this,informations), "HandlerPongMessage");
		exploration.registerState(new SynchronizeMessage(this,informations), "HandlerSynchronizeMessage");
		exploration.registerState(new IsBusyMessage(this,informations), "HandlerIsBusyMessage");
		//
		exploration.registerState(new CoalitionBehaviour(this,informations), "Coalition");
		exploration.registerState(new SendingEndConversationBehaviour(this,informations), "SendingEndConversation");
		
		exploration.registerState(new AcceptCoalitionMessage(this,informations), "HandlerAcceptCoalitionMessage");
		exploration.registerState(new CoalitionInvitationMessage(this,informations), "HandlerCoalitionInvitationMessage");
		exploration.registerState(new ConversationFinishedMessage(this,informations), "HandlerConversationFinishedMessage");
		exploration.registerState(new RequestJoinCoalitionMessage(this,informations), "HandlerRequestJoinCoalitionMessage");

		
		exploration.registerTransition("ExploringDispatch","Exploring",AgentState.Exploring.ordinal()); 
		exploration.registerTransition("ExploringDispatch","ExploringCoalition",AgentState.ExploringCoalition.ordinal()); 

		exploration.registerTransition("Exploring","SendingPing",AgentState.SendingPing.ordinal()); 
		exploration.registerTransition("SendingPing","Dispatcher",AgentState.Dispatcher.ordinal()); 
		exploration.registerTransition("SendingEndConversation","Dispatcher",AgentState.Dispatcher.ordinal()); 	
		exploration.registerTransition("ExploringCoalition","Dispatcher",AgentState.Dispatcher.ordinal()); 

		
		exploration.registerTransition("Dispatcher","Dispatcher",AgentState.Dispatcher.ordinal()); 
		exploration.registerTransition("Dispatcher","ExploringDispatch",AgentState.ExploringDispatch.ordinal()); 
		exploration.registerTransition("Dispatcher","HandlerPingMessage",AgentState.HandlerPingMessage.ordinal()); 
		exploration.registerTransition("Dispatcher","HandlerPongMessage",AgentState.HandlerPongMessage.ordinal()); 
		exploration.registerTransition("Dispatcher","HandlerSynchronizeMessage",AgentState.HandlerSynchronizeMessage.ordinal()); 
		exploration.registerTransition("Dispatcher","HandlerIsBusyMessage",AgentState.HandlerIsBusyMessage.ordinal()); 
		
		exploration.registerTransition("Dispatcher","HandlerAcceptCoalitionMessage",AgentState.HandlerAcceptCoalitionMessage.ordinal()); 
		exploration.registerTransition("Dispatcher","HandlerCoalitionInvitationMessage",AgentState.HandlerCoalitionInvitationMessage.ordinal()); 
		exploration.registerTransition("Dispatcher","HandlerConversationFinishedMessage",AgentState.HandlerConversationFinishedMessage.ordinal()); 
		exploration.registerTransition("Dispatcher","HandlerRequestJoinCoalitionMessage",AgentState.HandlerRequestJoinCoalitionMessage.ordinal()); 

		
		exploration.registerTransition("HandlerPingMessage","Redirect",AgentState.Redirect.ordinal()); 		
		exploration.registerTransition("HandlerPongMessage","Redirect",AgentState.Redirect.ordinal()); 		
		exploration.registerTransition("HandlerSynchronizeMessage","Redirect",AgentState.Redirect.ordinal()); 		
		exploration.registerTransition("HandlerIsBusyMessage","Redirect",AgentState.Redirect.ordinal()); 
		
		exploration.registerTransition("HandlerAcceptCoalitionMessage","Redirect",AgentState.Redirect.ordinal()); 		
		exploration.registerTransition("HandlerCoalitionInvitationMessage","Redirect",AgentState.Redirect.ordinal()); 		
		exploration.registerTransition("HandlerConversationFinishedMessage","Redirect",AgentState.Redirect.ordinal()); 		
		exploration.registerTransition("HandlerRequestJoinCoalitionMessage","Redirect",AgentState.Redirect.ordinal()); 		

		
		exploration.registerTransition("Redirect","Dispatcher",AgentState.Dispatcher.ordinal()); 		
		exploration.registerTransition("Redirect","ExploringDispatch",AgentState.ExploringDispatch.ordinal()); 	
		exploration.registerTransition("Redirect","Coalition",AgentState.Coalition.ordinal()); 	
		exploration.registerTransition("Redirect","SendingEndConversation",AgentState.SendingEndConversation.ordinal()); 	

		exploration.registerTransition("Coalition","Redirect",AgentState.Redirect.ordinal()); 	

		informations.statsMachine.put("Exploration", exploration);
		informations.currentBehaviour = informations.statsMachine.get("Exploration");
/*		
		FSMBehaviour hunter = new FSMBehaviour(this);
		hunter.registerFirstState(new SayHello(this), "hello");
		hunter.registerDefaultTransition("hello","hello");
		
		informations.statsMachine.put("Hunter",hunter);
		informations.currentBehaviour = informations.statsMachine.get("Exploration");
*/
		addBehaviour(new startMyBehaviours(this,List.of(informations.currentBehaviour)));


		//System.out.println("the  agent "+this.getLocalName()+ " is started");

	}
	
    @Override
	public void addBehaviour(Behaviour b) {
    	super.addBehaviour(b);
	}
    
	protected void addServiceAgent(String type) {
	     DFAgentDescription dfdescr = new DFAgentDescription();
	        dfdescr.setName(getAID());
	        
	        ServiceDescription sd = new ServiceDescription();
	        sd.setType(type);
	        sd.setName(getLocalName());
	        dfdescr.addServices(sd);
	        try {
	            DFService.register(this, dfdescr);
	        }catch (FIPAException e) {
	            System.err.println("FIPA ERROR ExploreMultiAgent");
	        }
	}
	

}
