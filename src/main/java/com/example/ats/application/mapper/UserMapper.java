package com.example.ats.application.mapper;

import com.example.ats.application.dto.response.UserResponse;
import com.example.ats.domain.model.User;
import com.example.ats.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);

//    @Mapping(source = "fullName", target = "fullname")
    User toEntity(UserEntity entity);
}
