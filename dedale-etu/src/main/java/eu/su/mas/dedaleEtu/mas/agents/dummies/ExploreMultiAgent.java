package eu.su.mas.dedaleEtu.mas.agents.dummies;

import java.util.ArrayList;
import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedale.mas.agent.behaviours.startMyBehaviours;
import eu.su.mas.dedaleEtu.mas.behaviours.ExploMultiBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.ExploPendingBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.ExploSoloBehaviour;
import eu.su.mas.dedaleEtu.mas.handlers.PingMessage;
import eu.su.mas.dedaleEtu.mas.handlers.PongMessage;
import eu.su.mas.dedaleEtu.mas.handlers.SynchronizeMessage;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import eu.su.mas.dedaleEtu.mas.toolBox.AgentState;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;


public class ExploreMultiAgent extends AbstractDedaleAgent {

	private static final long serialVersionUID = -6431752665590433727L;
	private AgentInformations informations;

	protected void setup(){

		super.setup();
		

		List<Behaviour> lb=new ArrayList<Behaviour>();
		
		final Object[] args = getArguments();
        final ArrayList<String> receivers = new ArrayList<String> ();

        for(Object arg : args) {
            // very hard pop,refactor this after ( ignore 2 first parametre) 
            if (arg == args[0] || arg == args[1]) continue;
            receivers.add((String) arg);
        }
        
        informations = new AgentInformations(receivers);

		//lb.add(new ExploSoloBehaviour(this,informations.myMap));
		FSMBehaviour stateMachine = new FSMBehaviour(this);
		
		stateMachine.registerFirstState(new ExploMultiBehaviour(this,informations), "A");
		//refactor after
		stateMachine.registerState(new ExploPendingBehaviour(this,1,informations), "B");

		stateMachine.registerTransition("A","A",AgentState.Working.ordinal()); // 0 for staying A  <-> A
		stateMachine.registerTransition("A","B",AgentState.Pending.ordinal()); // 1 for staying A  -> B
		
		stateMachine.registerTransition("B","A",AgentState.Pending.ordinal()); // 1 for staying B  -> A
		stateMachine.registerTransition("B","A",AgentState.Working.ordinal()); // 1 for staying B  -> A

		lb.add(stateMachine);
		
		lb.add(new PingMessage(this,informations));
		lb.add(new PongMessage(this,informations));
		lb.add(new SynchronizeMessage(this,informations));
		
		addBehaviour(new startMyBehaviours(this,lb));
		System.out.println("the  agent "+this.getLocalName()+ " is started");

	}
	
	
	
}
