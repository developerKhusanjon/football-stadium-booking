package org.stadium.adminapi.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.stadium.adminapi.controller.error.BadRequestAlertException;
import org.stadium.adminapi.mapper.StadiumInfoMapper;
import org.stadium.adminapi.mapper.StadiumMapper;
import org.stadium.adminapi.service.StadiumManageService;
import org.stadium.adminapi.service.dto.StadiumDto;
import org.stadium.adminapi.service.dto.StadiumInfoDto;
import org.stadium.adminapi.service.dto.StadiumRequestDto;
import org.stadium.corelib.domain.Stadium;
import org.stadium.corelib.domain.StadiumImage;
import org.stadium.corelib.domain.StadiumInfo;
import org.stadium.corelib.repo.StadiumImageRepository;
import org.stadium.corelib.repo.StadiumInfoRepository;
import org.stadium.corelib.repo.StadiumRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@Log4j2
public class StadiumManageServiceImpl implements StadiumManageService {

    private final StadiumRepository stadiumRepository;
    private final StadiumInfoRepository stadiumInfoRepository;
    private final StadiumImageRepository stadiumImageRepository;

    private final StadiumInfoMapper stadiumInfoMapper;

    private final StadiumMapper stadiumMapper;

    private final MediaFileService mediaFileService;

    public StadiumManageServiceImpl(StadiumRepository stadiumRepository, StadiumInfoRepository stadiumInfoRepository,
                                    StadiumImageRepository stadiumImageRepository, StadiumInfoMapper stadiumInfoMapper, StadiumMapper stadiumMapper, MediaFileService mediaFileService) {
        this.stadiumRepository = stadiumRepository;
        this.stadiumInfoRepository = stadiumInfoRepository;
        this.stadiumImageRepository = stadiumImageRepository;
        this.stadiumInfoMapper = stadiumInfoMapper;
        this.stadiumMapper = stadiumMapper;
        this.mediaFileService = mediaFileService;
    }


    @Override
    public Page<StadiumInfoDto> findAll(Pageable pageable) throws BadRequestAlertException {
        return stadiumInfoRepository.findAll(pageable).map(stadiumInfoMapper::toDto);
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

    @Transactional
    @Override
    public StadiumDto save(StadiumRequestDto requestDto) throws BadRequestAlertException {
        Stadium stadium = Stadium.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .address(requestDto.getAddress())
                .email(requestDto.getEmail())
                .phone1(requestDto.getPhone1())
                .phone2(requestDto.getPhone2())
                .price(requestDto.getPrice()).build();

        StadiumImage stadiumImage = uploadAndSaveImage(requestDto.getImageFile(), requestDto.getCompressImageFile());
        stadium.setImage(stadiumImage);

        stadium = stadiumRepository.save(stadium);

        StadiumInfo stadiumInfo = StadiumInfo.builder()
                .stadiumId(stadium.getId())
                .price(requestDto.getPrice())
                .address(requestDto.getShortAddress())
                .imageId(stadium.getImage().getId())
                .name(requestDto.getName())
                .lon(requestDto.getLon()).lat(requestDto.getLat())
                .build();

        stadiumInfoRepository.save(stadiumInfo);

        return stadiumMapper.toDto(stadium);
    }

    @Transactional
    @Override
    public StadiumDto update(Long id, StadiumRequestDto requestDto) throws BadRequestAlertException {
        Optional<Stadium> stadium = stadiumRepository.findById(id);

        if (stadium.isEmpty()) throw new BadRequestAlertException("stadium not found", "Stadium", "id");

        StadiumInfo stadiumInfo = StadiumInfo.builder()
                .stadiumId(stadium.get().getId())
                .price(requestDto.getPrice())
                .address(requestDto.getShortAddress())
                .name(requestDto.getName())
                .lon(requestDto.getLon()).lat(requestDto.getLat())
                .build();


        if (requestDto.getImageFile() != null) {
            StadiumImage image = stadium.get().getImage();

            if (image != null) {
                if (!image.getUrl().isEmpty()) mediaFileService.deleteFile(image.getUrl());
                if (!image.getCompressUrl().isEmpty()) mediaFileService.deleteFile(image.getCompressUrl());
                stadiumInfoRepository.deleteById(image.getId());
            }

            StadiumImage stadiumImage = uploadAndSaveImage(requestDto.getImageFile(), requestDto.getCompressImageFile());

            stadium.get().setImage(stadiumImage);
        }


        stadium.get().setName(requestDto.getName());
        stadium.get().setDescription(requestDto.getDescription());
        stadium.get().setAddress(requestDto.getAddress());
        stadium.get().setEmail(requestDto.getEmail());
        stadium.get().setPhone1(requestDto.getPhone1());
        stadium.get().setPhone2(requestDto.getPhone2());
        stadium.get().setPrice(requestDto.getPrice());

        Stadium updateStadium = stadiumRepository.save(stadium.get());

        stadiumInfoRepository.deleteAllByStadiumId(updateStadium.getId());

        stadiumInfo.setStadiumId(updateStadium.getImage().getId());
        stadiumInfoRepository.save(stadiumInfo);

        return stadiumMapper.toDto(updateStadium);
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

    public void deleteStadiumById(Long id) {
        stadiumRepository.findById(id).ifPresent(stadium -> {
            deleteImageById(stadium.getImage().getId());
            stadiumInfoRepository.deleteAllByStadiumId(stadium.getId());
            stadiumRepository.deleteById(id);
        });
    }

    private StadiumImage uploadAndSaveImage(MultipartFile file, MultipartFile compressFile) throws BadRequestAlertException {

        if (file == null || compressFile == null)
            throw new BadRequestAlertException("Image file or compress image file not provided", "image", "file/compressFile", HttpStatus.BAD_REQUEST);

        String path = "/articles/images/";
        String imageUrl = uploadImageFile(file, path);
        String compressImgUrl = uploadImageFile(compressFile, path + "compress/");


        StadiumImage image = new StadiumImage();
        image.setUrl(imageUrl);
        image.setCompressUrl(compressImgUrl);
        image.setContentType(file.getContentType());
        image.setContentType(file.getContentType());

        image = stadiumImageRepository.save(image);

        return image;
    }

    private String uploadImageFile(MultipartFile file, String path) {
        String fileName = mediaFileService.uploadFile(file, path);
        return path + fileName;
    }

    public void deleteImageById(Long id) {
        stadiumImageRepository.findById(id).ifPresent(image -> {
            boolean successDeletion = mediaFileService.deleteFile(image.getUrl());
            boolean successCompressDeletion = mediaFileService.deleteFile(image.getCompressUrl());
            if (!successDeletion || !successCompressDeletion)
                log.error("Error while deleting image file on server, Image," + id + ", " + image.getUrl() + "   |   " + image.getCompressUrl());
            stadiumImageRepository.deleteById(id);
        });
    }
}
