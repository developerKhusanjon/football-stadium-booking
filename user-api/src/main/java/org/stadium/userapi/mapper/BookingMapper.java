package org.stadium.userapi.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.stadium.corelib.domain.Booking;
import org.stadium.userapi.service.dto.BookingDto;

@Mapper(componentModel = "spring", uses = {UserMapper.class, StadiumMapper.class})
@Component
public interface BookingMapper extends EntityMapper<BookingDto, Booking> {
}
