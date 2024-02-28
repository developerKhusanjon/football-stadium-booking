package org.stadium.adminapi.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.stadium.adminapi.service.dto.RoleDto;
import org.stadium.corelib.domain.Role;

import java.util.Set;


@Mapper(componentModel = "spring")
@Component
public interface RoleMapper {
    Set<Role> toEntity(Set<RoleDto> roleDtos);
}
