package eu.su.mas.dedaleEtu.mas.protocol;

import jade.util.leap.Serializable;
//POJO
public class PingMessage implements Serializable{
		private String key;
		private Integer coalitionId;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}
		
		public 	Integer getCoalitionId() {
			return coalitionId;
		}
		
		public void setCoalitionId(Integer coalitionId) {
			this.coalitionId = coalitionId;
		}

}
