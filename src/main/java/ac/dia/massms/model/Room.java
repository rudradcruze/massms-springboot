package ac.dia.massms.model;

import java.util.List;

import javax.persistence.*;

@Entity
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	private String type;
	private int capacity;
	private int rent;
	private String description;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
	private List<Seat> seats;
	
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public int getRent() {
		return rent;
	}
	public void setRent(int rent) {
		this.rent = rent;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Room(Long id, String type, int capacity, int rent, String description) {
		super();
		Id = id;
		this.type = type;
		this.capacity = capacity;
		this.rent = rent;
		this.description = description;
	}
	public Room() {
		super();
	}
	@Override
	public String toString() {
		return "Room [Id=" + Id + ", type=" + type + ", capacity=" + capacity + ", rent=" + rent + ", description="
				+ description + "]";
	}
	
	
}
