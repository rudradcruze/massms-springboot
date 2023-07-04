package ac.dia.massms.model;

import lombok.*;
import javax.persistence.*;
import java.util.List;

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
	private String email;
	private String url;

	@OneToMany(mappedBy = "mass")
	private List<MassMember> messMemberList;
}