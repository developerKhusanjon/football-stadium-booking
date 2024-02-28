package org.stadium.adminapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import org.stadium.adminapi.service.dto.StadiumInfoDto;
import org.stadium.corelib.domain.StadiumInfo;

@Mapper(componentModel = "spring")
@Component
public interface StadiumInfoMapper extends EntityMapper<StadiumInfoDto, StadiumInfo>{
}
