package eu.su.mas.dedaleEtu.mas.agents.dummies.dummies;

import java.util.ArrayList;
import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedale.mas.agent.behaviours.startMyBehaviours;

import eu.su.mas.dedaleEtu.mas.behaviours.RandomWalkBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.SayHello;

import jade.core.behaviours.Behaviour;

/**
 * This example class start a Dummy agent that will possess two behaviours :
 * <ol>
 * <li> move randomly and test the API methods {@link RandomWalkBehaviour}.
 * <li> send a meaningless message to two other agents {@link SayHello} 
 * </ol>
 * @author hc
 *
 */
public class DummyMovingAgent extends AbstractDedaleAgent{

	private static final long serialVersionUID = -2991562876411096907L;
	

	/**
	 * This method is automatically called when "agent".start() is executed.
	 * Consider that Agent is launched for the first time. 
	 * 			1) set the agent attributes 
	 *	 		2) add the behaviours
	 *          
	 */
	protected void setup(){
		super.setup();

		//get the parameters given into the object[]
		final Object[] args = getArguments();
		//System.out.println("Arg given by the user to "+this.getLocalName()+": "+args[2]);
		
		//use them as parameters for your behaviours is you want
		
		List<Behaviour> lb=new ArrayList<Behaviour>();
		
		/************************************************
		 * 
		 * ADD the behaviours of the Dummy Moving Agent
		 * 
		 ************************************************/
		lb.add(new RandomWalkBehaviour(this));
		//lb.add(new SayHello(this));
		
		
		/***
		 * MANDATORY TO ALLOW YOUR AGENT TO BE DEPLOYED CORRECTLY
		 */
		
		addBehaviour(new startMyBehaviours(this,lb));

	}




}