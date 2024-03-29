package ac.dia.massms.model;

import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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

	public int calculateTotalMeal(String url) {
		List<MemberMeal> list = getMemberMealList();
		int count = 0;
		for (MemberMeal memberMeal : list) {
			if (Objects.equals(memberMeal.getMealDate().getMass().getUrl(), url))
				count += memberMeal.getQuantity();
		}
		return count;
	}

	public double calculateAmount(String url, boolean paidOrDue) {
		List<MemberMeal> list = getMemberMealList();
		double amountPaid = 0, amountDue = 0;
		for (MemberMeal memberMeal : list) {
			if (Objects.equals(memberMeal.getMealDate().getMass().getUrl(), url)) {
				if (paidOrDue && memberMeal.isPayment())
					amountPaid += (memberMeal.getQuantity() * memberMeal.getMealDate().getMeal().getPrice());
				else if (!memberMeal.isPayment())
					amountDue += (memberMeal.getQuantity() * memberMeal.getMealDate().getMeal().getPrice());
			}
		}
		if (paidOrDue)
			return amountPaid;
		else
			return amountDue;
	}
}
