package com.services.dog.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.services.dog.validation.BreedConstraints;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for {@link com.services.dog.entity.Dogs}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DogsVo implements Serializable {
    @NotNull
    @NotEmpty
    @BreedConstraints
    private String breed;
    @NotNull
    @NotEmpty
    private List<String> imageUrls = new ArrayList<>();
}