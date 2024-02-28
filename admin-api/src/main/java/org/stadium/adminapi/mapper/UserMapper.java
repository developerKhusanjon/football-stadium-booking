package org.stadium.adminapi.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.stadium.adminapi.service.dto.UserDto;
import org.stadium.corelib.domain.User;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper extends EntityMapper<UserDto, User> {
}
