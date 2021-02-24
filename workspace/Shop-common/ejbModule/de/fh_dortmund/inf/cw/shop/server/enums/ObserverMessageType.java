package de.fh_dortmund.inf.cw.shop.server.enums;

public enum ObserverMessageType {
	CART(0), INVENTORY(1);
	
	private int value;
	
	private ObserverMessageType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static ObserverMessageType getObserverMessageType(int value) {
		ObserverMessageType observerMessageType = null;
		
		for(ObserverMessageType tempObserverMessageType : values()) {
			if(tempObserverMessageType.getValue() == value) {
				observerMessageType = tempObserverMessageType;
			}
		}
		
		return observerMessageType;
	}
}

