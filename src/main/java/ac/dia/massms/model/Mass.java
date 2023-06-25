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
	private long massId;
	private String massName;
	private String address;
	private String contact;
	private String managerInfo;
	private String email;
	private String url;
}