package ac.dia.massms.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "serve_time")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ServeTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "serve_time_id")
    private Long id;
    @Column(unique = true)
    private String identifier;
    private String startTime;
    private String endTime;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Meal> mealList;
}