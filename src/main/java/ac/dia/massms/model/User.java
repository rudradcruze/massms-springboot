package ac.dia.massms.model;

import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Size(min = 2, max = 15, message = "First name should have 3-15 characters")
	private String firstName;
	@Size(min = 2, max = 15, message = "Last name should have 3-15 characters")
	private String lastName;
	private String username;
	private String password;
	private String confirmPassword;
	private boolean enabled;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> roles = new HashSet<>();

	@OneToMany(mappedBy = "user")
	private List<MemberMeal> memberMealList;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<Mass> massList;
}
