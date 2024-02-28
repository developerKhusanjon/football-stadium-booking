package org.stadium.adminapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import org.stadium.adminapi.service.dto.StadiumDto;
import org.stadium.corelib.domain.Stadium;


@Mapper(componentModel = "spring")
@Component
public interface StadiumMapper extends EntityMapper<StadiumDto, Stadium>{
}
