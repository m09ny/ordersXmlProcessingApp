package sacom.orders.entities;

import java.util.List;

import org.joda.time.DateTime;

public class Order implements Comparable<Order> {
	private DateTime created;
	private List<Product> product;

	public Order() {
		super();
	}

	public Order(DateTime created, List<Product> product) {
		super();
		this.created = created;
		this.product = product;
	}

	public DateTime getCreated() {
		return created;
	}

	public void setCreated(DateTime created) {
		this.created = created;
	}

	public List<Product> getProduct() {
		return product;
	}

	public void setProduct(List<Product> product) {
		this.product = product;
	}

	@Override
	public int compareTo(Order o) {
		return getCreated().compareTo(o.getCreated());
	}

	@Override
	public String toString() {
		return "Order [created=" + created + ", product=" + product + "]";
	}

}
