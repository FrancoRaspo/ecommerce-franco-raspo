package com.ecommerce.francoraspo.models.authJwtModels;


import com.ecommerce.francoraspo.models.enums.ERolSeguridad;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection = "roles")
@AllArgsConstructor
public class RolSeguridad {
    @Id
    private String id;

    private ERolSeguridad name;

    public RolSeguridad() {

    }

    public RolSeguridad(ERolSeguridad name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ERolSeguridad getName() {
        return name;
    }

    public void setName(ERolSeguridad name) {
        this.name = name;
    }
}
