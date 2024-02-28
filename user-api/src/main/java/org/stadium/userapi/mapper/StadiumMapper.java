package org.stadium.userapi.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.stadium.corelib.domain.Stadium;
import org.stadium.userapi.service.dto.StadiumDto;


@Mapper(componentModel = "spring")
@Component
public interface StadiumMapper extends EntityMapper<StadiumDto, Stadium> {
}
