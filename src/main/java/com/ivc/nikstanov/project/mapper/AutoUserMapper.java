package com.ivc.nikstanov.project.mapper;

import com.ivc.nikstanov.project.dto.UserDto;
import com.ivc.nikstanov.project.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AutoUserMapper {

    UserDto mapToUserDto(User user);

    User mapToUser(UserDto userDto);
}
