package ac.dia.massms.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Mass {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private String address;
	private String contact;

	@ManyToOne
	@JoinColumn(name="users_id")
	private User manager;

	private String email;
	private String url;
}