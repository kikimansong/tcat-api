package isol.tcat_api.domain.region.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

@Getter
@ToString
@NoArgsConstructor
@DynamicInsert
@SequenceGenerator(name = "seq_region_idx_generator", sequenceName = "seq_region_idx", initialValue = 1, allocationSize = 1)
@Entity
@Table(name = "region")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_region_idx_generator")
    @Column(name = "region_idx", nullable = false)
    private long regionIdx;

    @Column(name = "region_name", nullable = false, length = 100)
    private String regionName;

}
