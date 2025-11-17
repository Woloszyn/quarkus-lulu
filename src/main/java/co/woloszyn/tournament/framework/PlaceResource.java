package co.woloszyn.tournament.framework;

import co.woloszyn.tournament.dto.CreatePlaceRequestDTO;
import co.woloszyn.tournament.dto.PlaceDTO;
import co.woloszyn.tournament.usecase.CreatePlace;
import co.woloszyn.tournament.usecase.GetPlace;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.jboss.resteasy.reactive.RestHeader;
import org.jboss.resteasy.reactive.RestQuery;

import java.util.List;

@Path("place")
public class PlaceResource {

    @Inject
    CreatePlace createPlace;

    @Inject
    GetPlace getPlace;

    @POST
    public PlaceDTO createPlace(CreatePlaceRequestDTO requestDTO, @RestHeader ("X-Customer-Id") Long customerId) {
        return createPlace.createPlace(requestDTO, customerId);
    }

    @GET
    @Path("{id}")
    public PlaceDTO getPlaceById(Long id) {
        return getPlace.getById(id);
    }

    @GET
    public List<PlaceDTO> getPlacesByCustomerId(@RestHeader ("X-Customer-Id") Long customerId) {
        return getPlace.getPlacesByCustomerId(customerId);
    }

    @GET
    @Path("online")
    public List<PlaceDTO> getPlacesByCustomerId(@RestHeader ("X-Customer-Id") Long customerId, @RestQuery("online") String online) {
        boolean onlineBool = "YES".equals(online);
        return getPlace.getPlacesByCustomerIdAndOnline(customerId, onlineBool);
    }

}
