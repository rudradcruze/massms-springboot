package ac.dia.massms.model;

import lombok.*;

import java.util.List;
import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Meal {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	private String name;
	private String servingSize;
	private String allergens;
	private double price;
	private String description;
	private String time;

	@OneToMany(mappedBy = "meal", cascade = CascadeType.ALL)
    private List<MealDate> dailyMeals;

	@ManyToOne(optional = false)
	@JoinColumn(name = "serve_time_id",
				nullable = false,
				referencedColumnName = "serve_time_id")
	private ServeTime serveTime;
}