package com.example.mapper;

import com.example.dtos.UserDto;
import com.example.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    UserDto map(UserEntity entity);
}
