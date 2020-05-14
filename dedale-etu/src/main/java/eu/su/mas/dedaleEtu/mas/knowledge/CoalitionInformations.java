package eu.su.mas.dedaleEtu.mas.knowledge;

import java.util.ArrayList;
import java.util.HashMap;

import eu.su.mas.dedaleEtu.mas.toolBox.CoalitionType;
import javafx.util.Pair;

public class CoalitionInformations {
	
	// liste qui décris chaque groupe [ groupeId  , Membres : (nom , type ( bosse ou bien membre ) ) ]
	private  HashMap<Integer,ArrayList<Pair<String,CoalitionType>>> maps;
	private static Object lock = new Object();
	public CoalitionInformations() {
		maps = new HashMap<Integer,ArrayList<Pair<String,CoalitionType>>>();
		
	}
	
	public void addCoalition(Integer coalitionId , String agentName) {
		synchronized(lock) {
			Pair<String,CoalitionType> information = new Pair<String,CoalitionType>(agentName,CoalitionType.Boss);
			maps.put(coalitionId,new ArrayList<Pair<String,CoalitionType>>());
			maps.get(coalitionId).add(information);
		}
	
	}
	
	public void addMember(Integer coalitionId , String agentName) {
		synchronized(lock) {
			Pair<String,CoalitionType> information = new Pair<String,CoalitionType>(agentName,CoalitionType.Member);
			maps.get(coalitionId).add(information);
		}
	}
	
	public ArrayList<Pair<String,CoalitionType>> get(Integer coalitionId){
		ArrayList<Pair<String,CoalitionType>>  obj = null;
		synchronized(lock) {
			obj =  maps.get(coalitionId);
		}
		return obj;
	}
	
}
