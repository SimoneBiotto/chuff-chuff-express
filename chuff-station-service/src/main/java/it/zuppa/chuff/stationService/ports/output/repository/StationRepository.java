package it.zuppa.chuff.stationService.ports.output.repository;

import it.zuppa.chuff.domain.station.Station;
import it.zuppa.chuff.stationService.dto.station.SearchStationDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StationRepository {
    Station createStation(Station station);

    Optional<Station> findById(UUID uuid);

    Optional<Station> findByCode(String code);

    Optional<Station> updateStation(Station station);

    void deleteStation(Station station);

    Optional<Station> searchById(UUID id);

    Optional<Station> searchByCode(String code);

    List<Station> searchAllBySearchStationDto(SearchStationDto name);

}
