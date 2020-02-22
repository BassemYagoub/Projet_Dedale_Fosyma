package eu.su.mas.dedaleEtu.mas.behaviours;

import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;

public class ExploPendingBehaviour extends OneShotBehaviour{

	private AgentInformations informations;
	private long period;
	public ExploPendingBehaviour(Agent a, long period ,AgentInformations informations ) {
		super(a);
		this.informations = informations;
		this.period = period;
		// TODO Auto-generated constructor stub
	}


	
	public int onEnd() {
		return informations.state.ordinal();
	}



	@Override
	public void action() {
		this.myAgent.doWait(period);
	}

}
