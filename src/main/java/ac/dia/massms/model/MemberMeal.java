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

	@ManyToOne
	@JoinColumn(name="meal_date_id")
	private MealDate meal;

	@ManyToOne
	@JoinColumn(name="users_id")
	private User user;
}