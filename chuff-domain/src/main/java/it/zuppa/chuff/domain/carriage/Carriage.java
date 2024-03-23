package it.zuppa.chuff.domain.carriage;

import it.zuppa.chuff.common.entity.BaseEntity;
import it.zuppa.chuff.domain.trainInstance.InstanceCarriage;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Carriage extends BaseEntity {
    private String code;
    @ManyToOne
    private SchemaCarriage schema;
    @OneToMany(mappedBy = "carriage")
    private List<InstanceCarriage> instanceCarriageList;
}
