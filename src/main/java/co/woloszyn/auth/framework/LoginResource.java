package co.woloszyn.auth.framework;

import co.woloszyn.auth.dto.LoginDTO;
import co.woloszyn.auth.dto.LoginRequestDTO;
import co.woloszyn.auth.usecase.Login;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("login")
public class LoginResource {

    @Inject
    Login login;

    @POST
    public LoginDTO login(LoginRequestDTO request) {
        return login.login(request);
    }

}
