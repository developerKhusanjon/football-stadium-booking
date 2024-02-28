package org.stadium.userapi.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.stadium.corelib.domain.User;
import org.stadium.userapi.service.dto.UserDto;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper extends EntityMapper<UserDto, User> {
}
