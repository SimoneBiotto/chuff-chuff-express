package it.zuppa.chuff.domain.carriage;

import com.google.gson.JsonObject;
import it.zuppa.chuff.common.entity.BaseEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SchemaCarriage extends BaseEntity {
  private String code;
  @Embedded private JsonObject schemaJson;

  @OneToMany(mappedBy = "schema")
  private List<Carriage> carriageList;
}
