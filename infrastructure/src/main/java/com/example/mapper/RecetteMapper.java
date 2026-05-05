package com.example.mapper;

import com.example.dtos.RecetteDto;
import com.example.model.Recette;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecetteMapper {

    RecetteDto map(Recette recette);

    List<RecetteDto> map(List<Recette> recettes);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    Recette map(RecetteDto recetteDto);
}
