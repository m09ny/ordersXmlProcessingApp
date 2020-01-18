package sacom.orders.entities;

public class Product {

	private String description;
	private String gtin;
	private double price;
	private String currency;
	private String orderid;

	public Product() {
		super();
	}

	public Product(String description, String gtin, double price, String currency, String orderid) {
		super();
		this.description = description;
		this.gtin = gtin;
		this.price = price;
		this.currency = currency;
		this.orderid = orderid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGtin() {
		return gtin;
	}

	public void setGtin(String gtin) {
		this.gtin = gtin;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	@Override
	public String toString() {
		return "Product [description=" + description + ", gtin=" + gtin + ", price=" + price
				+ ", currency=" + currency + ", orderid=" + orderid + "]";
	}

}
