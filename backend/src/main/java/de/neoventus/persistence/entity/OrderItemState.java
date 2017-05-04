package de.neoventus.persistence.entity;

/**
 * states for OrderItems
 *
 * @author dominik
 * @version 0.0.1
 */
public enum OrderItemState {

	// if the object OrderItem is initialized the state is null, which means the state is new
	NEW,
	CANCELED,
	FINISHED
}
