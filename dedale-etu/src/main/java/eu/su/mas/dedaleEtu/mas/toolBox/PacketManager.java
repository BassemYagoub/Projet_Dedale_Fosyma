package eu.su.mas.dedaleEtu.mas.toolBox;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import org.graphstream.ui.javafx.util.AttributeUtils.Tuple;

import eu.su.mas.dedaleEtu.mas.protocol.PongMessage;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import javafx.util.Pair;

public class PacketManager {
	
	public static void Send(Agent sender , String receiver , Serializable message) {
		PacketManager.Send(sender,  new	ArrayList<String>(Arrays.asList(receiver)), message);
	}
	public static void Send(Agent sender , ArrayList<String> receivers , Serializable message) {

		final ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setSender(sender.getAID());
		String className = message.getClass().getSimpleName();
		msg.setProtocol(className);
		for(String receiver  : receivers) {
			msg.addReceiver(new AID(receiver, AID.ISLOCALNAME));  
		}
			
		try {
			msg.setContentObject(message);
			sender.send(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static<T> Pair<T,ACLMessage> BlockingReceiveByClassName(String className , Agent myAgent , long timeOut) {
	
		MessageTemplate msgTemplate = MessageTemplate.MatchProtocol(className);
		ACLMessage msg = myAgent.blockingReceive(msgTemplate,timeOut);
		
		if(msg == null) {
			return null;
		}else {
			try {
				return new Pair<T,ACLMessage>((T)msg.getContentObject(),msg);
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public static<T> Pair<T,ACLMessage> BlockingReceiveByClassName(String className , Agent myAgent) {
		MessageTemplate msgTemplate = MessageTemplate.MatchProtocol(className);
		ACLMessage msg = myAgent.blockingReceive(msgTemplate);
		if(msg == null) {
			return null;
		}else {
			try {
				return new Pair<T,ACLMessage>((T)msg.getContentObject(),msg);
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public static<T> Pair<T,ACLMessage> ReceiveByClassName(String className , Agent myAgent) {
		MessageTemplate msgTemplate = MessageTemplate.MatchProtocol(className);
		ACLMessage msg = myAgent.receive(msgTemplate);
		if(msg == null) {
			return null;
		}else {
			try {
				return new Pair<T,ACLMessage>((T)msg.getContentObject(),msg);
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
	}
}
