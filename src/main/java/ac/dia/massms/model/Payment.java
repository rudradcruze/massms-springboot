package ac.dia.massms.model;

import java.util.Date;

import javax.persistence.*;

@Entity
public class Payment {
	private long paymentId;
	private double amount;
	private Date date;
	private String paymentStatus;
	private String transactionId;
	private String currency;
	private String customerInfo;

	public Payment() {
		
	}
	public Payment(long paymentId, double amount, Date date, String paymentStatus, String transactionId, String currency,
			String customerInfo) {
		super();
		this.paymentId = paymentId;
		this.amount = amount;
		this.date = date;
		this.paymentStatus = paymentStatus;
		this.transactionId = transactionId;
		this.currency = currency;
		this.customerInfo = customerInfo;

	}
	
	@Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCustomerInfo() {
		return customerInfo;
	}
	public void setCustomerInfo(String customerInfo) {
		this.customerInfo = customerInfo;
	}
	
	@Override
	public String toString() {
		return "Payment [paymentId=" + paymentId + ", Amount=" + amount + ", date=" + date + ", paymentStatus="
				+ paymentStatus + ", transactionId=" + transactionId + ", Currency=" + currency + ", customerInfo="
				+ customerInfo + "]";
	}
	
}
