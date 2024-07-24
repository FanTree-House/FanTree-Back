package com.example.fantreehouse.domain.artistgroup.service;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.domain.artist.entity.Artist;
import com.example.fantreehouse.domain.artist.repository.ArtistRepository;
import com.example.fantreehouse.domain.artistgroup.dto.ArtistGroupRequestDto;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import com.example.fantreehouse.domain.entertainment.repository.EntertainmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArtistGroupService {

    private final ArtistGroupRepository artistGroupRepository;
    private final EntertainmentRepository entertainmentRepository;
    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistGroupService(ArtistGroupRepository artistGroupRepository, EntertainmentRepository entertainmentRepository, ArtistRepository artistRepository) {
        this.artistGroupRepository = artistGroupRepository;
        this.entertainmentRepository = entertainmentRepository;
        this.artistRepository = artistRepository;
    }

    @Transactional
    public ArtistGroup createArtistGroup(String entername, ArtistGroupRequestDto request) {
        Entertainment entertainment = entertainmentRepository.findByEntername(entername)
                .orElseThrow(() -> new CustomException(ErrorType.ENTERTAINMENT_NOT_FOUND));

        ArtistGroup artistGroup = new ArtistGroup(request.getGroupName(), request.getArtistProfilePicture(), entertainment);

        for (Long artistId : request.getArtistIds()) {
            Artist artist = artistRepository.findById(artistId)
                    .orElseThrow(() -> new CustomException(ErrorType.ARTIST_NOT_FOUND));
            artistGroup.addArtist(artist);
        }

        return artistGroupRepository.save(artistGroup);
    }

    public ArtistGroup getArtistGroup(String entername, String groupName) {
        return artistGroupRepository.findByEntertainmentEnternameAndGroupName(entername, groupName)
                .orElseThrow(() -> new CustomException(ErrorType.ARTIST_GROUP_NOT_FOUND));
    }

    public List<ArtistGroup> getAllArtistGroups(String entername) {
        return artistGroupRepository.findAllByEntertainmentEntername(entername);
    }

    @Transactional
    public ArtistGroup updateArtistGroup(String entername, String groupName, ArtistGroupRequestDto request) {
        ArtistGroup artistGroup = getArtistGroup(entername, groupName);
        artistGroup.setGroupName(request.getGroupName());
        artistGroup.setArtistProfilePicture(request.getArtistProfilePicture());

        artistGroup.clearArtists();
        for (Long artistId : request.getArtistIds()) {
            Artist artist = artistRepository.findById(artistId)
                    .orElseThrow(() -> new CustomException(ErrorType.ARTIST_NOT_FOUND));
            artistGroup.addArtist(artist);
        }

        return artistGroupRepository.save(artistGroup);
    }

    @Transactional
    public void deleteArtistGroup(String entername, String groupName) {
        ArtistGroup artistGroup = getArtistGroup(entername, groupName);
        artistGroupRepository.delete(artistGroup);
    }
}