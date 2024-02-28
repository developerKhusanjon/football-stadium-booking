package org.stadium.userapi.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.stadium.corelib.domain.StadiumInfo;
import org.stadium.userapi.service.dto.StadiumInfoDto;

@Mapper(componentModel = "spring")
@Component
public interface StadiumInfoMapper extends EntityMapper<StadiumInfoDto, StadiumInfo>{
}
