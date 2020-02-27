package eu.su.mas.dedaleEtu.mas.agents.dummies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedale.mas.agent.behaviours.startMyBehaviours;
import eu.su.mas.dedaleEtu.mas.behaviours.ExploMultiBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.ExploSoloBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.RedirectBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.SendingPingMessageBehaviour;
import eu.su.mas.dedaleEtu.mas.handlers.DispatcherMessage;
import eu.su.mas.dedaleEtu.mas.handlers.PingMessage;
import eu.su.mas.dedaleEtu.mas.handlers.PongMessage;
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
		FSMBehaviour stateMachine = new FSMBehaviour(this);
		
		stateMachine.registerFirstState(new ExploMultiBehaviour(this,informations), "Exploring");
		//refactor after
		stateMachine.registerState(new SendingPingMessageBehaviour(this,informations), "SendingPing");
		stateMachine.registerState(new DispatcherMessage(this,informations), "Dispatcher");
		stateMachine.registerState(new RedirectBehaviour(this,informations), "Redirect");
		stateMachine.registerState(new PingMessage(this,informations), "HandlerPingMessage");
		stateMachine.registerState(new PongMessage(this,informations), "HandlerPongMessage");
		stateMachine.registerState(new SynchronizeMessage(this,informations), "HandlerSynchronizeMessage");

		
		stateMachine.registerTransition("Exploring","SendingPing",AgentState.SendingPing.ordinal()); 
		stateMachine.registerTransition("SendingPing","Dispatcher",AgentState.Dispatcher.ordinal()); 
		
		stateMachine.registerTransition("Dispatcher","Exploring",AgentState.Exploring.ordinal()); 
		stateMachine.registerTransition("Dispatcher","HandlerPingMessage",AgentState.HandlerPingMessage.ordinal()); 
		stateMachine.registerTransition("Dispatcher","HandlerPongMessage",AgentState.HandlerPongMessage.ordinal()); 
		stateMachine.registerTransition("Dispatcher","HandlerSynchronizeMessage",AgentState.HandlerSynchronizeMessage.ordinal()); 

		
		stateMachine.registerTransition("HandlerPingMessage","Redirect",AgentState.Redirect.ordinal()); 		
		stateMachine.registerTransition("HandlerPongMessage","Redirect",AgentState.Redirect.ordinal()); 		
		stateMachine.registerTransition("HandlerSynchronizeMessage","Redirect",AgentState.Redirect.ordinal()); 		

		
		stateMachine.registerTransition("Redirect","Dispatcher",AgentState.Dispatcher.ordinal()); 		
		stateMachine.registerTransition("Redirect","Exploring",AgentState.Exploring.ordinal()); 		




		lb.add(stateMachine);

		
		addBehaviour(new startMyBehaviours(this,lb));
		System.out.println("the  agent "+this.getLocalName()+ " is started");

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
