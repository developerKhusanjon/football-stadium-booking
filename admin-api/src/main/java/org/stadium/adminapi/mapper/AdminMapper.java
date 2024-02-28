package org.stadium.adminapi.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.stadium.adminapi.service.dto.AdminDto;
import org.stadium.corelib.domain.Admin;


@Mapper(componentModel = "spring", uses = {RoleMapper.class})
@Component
public interface AdminMapper extends EntityMapper<AdminDto, Admin> {
}
