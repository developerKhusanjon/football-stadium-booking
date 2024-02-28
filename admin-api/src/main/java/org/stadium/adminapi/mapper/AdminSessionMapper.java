package org.stadium.adminapi.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.stadium.adminapi.service.dto.AdminSessionDto;
import org.stadium.corelib.domain.AdminSession;


@Mapper(componentModel = "spring")
@Component
public interface AdminSessionMapper extends EntityMapper<AdminSessionDto, AdminSession> {
}
