package ac.dia.massms.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberMeal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int quantity;
	private boolean payment;

	@ManyToOne
	@JoinColumn(name="meal_date_id")
	private MealDate mealDate;

	@ManyToOne
	@JoinColumn(name="users_id")
	private User user;
}