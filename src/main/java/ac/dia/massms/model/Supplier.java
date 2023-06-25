package ac.dia.massms.model;

import javax.persistence.*;

@Entity
public class Supplier {
	private long supplierId;
	private String supplierName;
	private String contactInfo;
	private String gender;
	public Supplier() {
		
	}
	public Supplier(long supplierId, String supplierName, String contactInfo, String gender) {
		super();
		this.supplierId = supplierId;
		this.supplierName = supplierName;
		this.contactInfo = contactInfo;
		this.gender = gender;
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getContactInfo() {
		return contactInfo;
	}
	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	@Override
	public String toString() {
		return "Supplier [supplierId=" + supplierId + ", supplierName=" + supplierName + ", contactInfo=" + contactInfo
				+ ", Gender=" + gender + "]";
	}

}
