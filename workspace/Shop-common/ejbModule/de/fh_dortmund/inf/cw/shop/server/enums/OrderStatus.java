package de.fh_dortmund.inf.cw.shop.server.enums;

import java.io.Serializable;

public enum OrderStatus implements Serializable {
	SUBMITTED, PACKAGED, WAITING_FOR_AVAILABILITY, SHIPPED
}
