package eu.su.mas.dedaleEtu.mas.behaviours;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.AgentInformations;
import jade.core.behaviours.OneShotBehaviour;

public class RedirectBehaviour extends OneShotBehaviour {

	private AgentInformations informations;


	public RedirectBehaviour(final AbstractDedaleAgent myagent ,AgentInformations informations ) {
		super(myagent);
		this.informations = informations;
	}

	
	@Override
	public void action() {
		// TODO Auto-generated method stub
	}
	
	public int onEnd() {
		return informations.state.ordinal();
	}

}
