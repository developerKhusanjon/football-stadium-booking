package org.stadium.adminapi.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.stadium.adminapi.service.dto.BookingDto;
import org.stadium.corelib.domain.Booking;

@Mapper(componentModel = "spring", uses = {UserMapper.class, StadiumMapper.class})
@Component
public interface BookingMapper extends EntityMapper<BookingDto, Booking> {
}
