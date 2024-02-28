package org.stadium.userapi.service.impl;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.stadium.corelib.domain.Stadium;
import org.stadium.corelib.domain.StadiumImage;
import org.stadium.corelib.domain.StadiumInfo;
import org.stadium.corelib.repo.StadiumImageRepository;
import org.stadium.corelib.repo.StadiumInfoRepository;
import org.stadium.corelib.repo.StadiumRepository;
import org.stadium.userapi.controller.errors.BadRequestAlertException;
import org.stadium.userapi.mapper.StadiumInfoMapper;
import org.stadium.userapi.mapper.StadiumMapper;
import org.stadium.userapi.service.StadiumManageService;
import org.stadium.userapi.service.dto.StadiumDto;
import org.stadium.userapi.service.dto.StadiumInfoDto;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class StadiumManageServiceImpl implements StadiumManageService {

    private final StadiumMapper stadiumMapper;
    private final StadiumInfoMapper stadiumInfoMapper;

    private final StadiumRepository stadiumRepository;
    private final StadiumInfoRepository stadiumInfoRepository;
    private final StadiumImageRepository stadiumImageRepository;

    private final MediaFileService mediaFileService;

    public StadiumManageServiceImpl(StadiumMapper stadiumMapper, StadiumInfoMapper stadiumInfoMapper,
                                    StadiumRepository stadiumRepository, StadiumInfoRepository stadiumInfoRepository, StadiumImageRepository stadiumImageRepository, MediaFileService mediaFileService) {
        this.stadiumMapper = stadiumMapper;
        this.stadiumInfoMapper = stadiumInfoMapper;
        this.stadiumRepository = stadiumRepository;
        this.stadiumInfoRepository = stadiumInfoRepository;
        this.stadiumImageRepository = stadiumImageRepository;
        this.mediaFileService = mediaFileService;
    }

    @Override
    public Page<StadiumInfoDto> findAll(Pageable pageable) {
        return stadiumInfoRepository.findAll(pageable).map(stadiumInfoMapper::toDto);
    }

    @Override
    public Page<StadiumInfoDto> findNearestAvailableByTimeRange(Pageable pageable, Double lon, Double lat, LocalDateTime from, LocalDateTime to) {
        return stadiumInfoRepository.findNearestByTimeRangeOrderByDistance(pageable, lon, lat, from, to).map(stadiumInfoMapper::toDto);
    }

    @Override
    public StadiumInfoDto findInfoById(Long id) throws BadRequestAlertException {
        Optional<StadiumInfo> stadiumInfo = stadiumInfoRepository.findById(id);
        if (stadiumInfo.isEmpty())
            throw new BadRequestAlertException("Stadium Info not found", "Stadium", "id");

        return stadiumInfoMapper.toDto(stadiumInfo.get());
    }

    @Override
    public StadiumDto findById(Long id) throws BadRequestAlertException {
        Optional<Stadium> stadium = stadiumRepository.findById(id);
        if (stadium.isEmpty())
            throw new BadRequestAlertException("Stadium not found", "Stadium", "id");

        return stadiumMapper.toDto(stadium.get());
    }

    @Override
    public Resource downloadImage(Long imageId) throws BadRequestAlertException {
        Optional<StadiumImage> image = stadiumImageRepository.findById(imageId);
        if (image.isPresent()) {
            String imgPath = image.get().getUrl();
            return mediaFileService.getFile(imgPath);
        }
        throw new BadRequestAlertException("File not found", "image", "id", HttpStatus.NOT_FOUND);
    }

    @Override
    public Resource downloadCompressImage(Long imageId) throws BadRequestAlertException {
        Optional<StadiumImage> image = stadiumImageRepository.findById(imageId);
        if (image.isPresent()) {
            String imgPath = image.get().getCompressUrl();
            return mediaFileService.getFile(imgPath);
        }
        throw new BadRequestAlertException("File not found", "image", "id", HttpStatus.NOT_FOUND);
    }
}
