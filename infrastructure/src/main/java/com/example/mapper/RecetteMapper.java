package com.example.mapper;

import com.example.dtos.RecetteDto;
import com.example.model.Recette;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecetteMapper {

//    @Mapping(target = "name",source = "")
    RecetteDto map(Recette recette);

    List<RecetteDto> map(List<Recette> recettes);

//    @InheritInverseConfiguration
    @Mapping(target = "id", ignore = true)
    Recette map(RecetteDto recetteDto);

}
