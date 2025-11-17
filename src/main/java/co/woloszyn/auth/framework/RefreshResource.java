package co.woloszyn.auth.framework;

import co.woloszyn.auth.dto.LoginDTO;
import co.woloszyn.auth.dto.RefreshRequestDTO;
import co.woloszyn.auth.usecase.JwtUtil;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@Path("refresh")
public class RefreshResource {

    @Inject
    JwtUtil jwtUtil;

    @POST
    public LoginDTO refresh(RefreshRequestDTO req) {
        if (req == null || req.getRefreshToken() == null) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("refreshToken required").build());
        }

        String subject = jwtUtil.validateTokenAndGetSubject(req.getRefreshToken());
        if (subject == null) {
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).entity("invalid refresh token").build());
        }

        // issue a new access token
        String token = jwtUtil.generateToken(subject);
        String refresh = jwtUtil.generateRefreshToken(subject);

        LoginDTO dto = new LoginDTO();
        dto.setToken(token);
        dto.setRefreshToken(refresh);
        dto.setExpiresIn(jwtUtil.getExpirationSeconds());
        return dto;
    }
}

