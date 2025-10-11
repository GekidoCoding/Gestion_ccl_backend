package mg.cnaps.gestion.ccl.project.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.HandlerInterceptor;
import mg.cnaps.gestion.ccl.project.entity.existant.JwtResponse;

import java.util.Objects;


@Component
@Service
public class AutorisationInterceptor implements HandlerInterceptor {

    private String urlAuthPrincipal;

    public AutorisationInterceptor() {
        // TODO Auto-generated constructor stub
    }

    public AutorisationInterceptor(String urlAuthPrincipal) {
        super();
        this.urlAuthPrincipal = urlAuthPrincipal;
    }

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AutorisationInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (request.getMethod().equals(HttpMethod.OPTIONS.toString())) {
            return true;
        }

        String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken = "";
        boolean continueRequest = true;
        if(requestTokenHeader == null || requestTokenHeader.isBlank()) {
            continueRequest = false;
            response.setStatus(401);
            response.getWriter().write("Accès non authorisé ! ");
            return continueRequest;
        } else {
            if (requestTokenHeader.startsWith("Bearer ")) {
                jwtToken = requestTokenHeader.substring(7);
                if(!jwtToken.isBlank()) {
                    try {
                        ResponseEntity<JwtResponse> entity = WebClient.create(urlAuthPrincipal)
                                .get()
                                .uri("/api/auth/access-token")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                                .retrieve()
                                .toEntity(JwtResponse.class)
                                .block();

                        if (entity == null) {
                            response.setStatus(500);
                            response.getWriter().write("Erreur du serveur, veuilllez réessayer");
                            continueRequest = false;
                        } else {
                            if (!entity.getStatusCode().equals(HttpStatus.OK)) {
                                response.setStatus(entity.getStatusCodeValue());
                            }
                            String identifiant = Objects.requireNonNull(entity.getBody()).getUser().getMatricule();
                            request.setAttribute("matricule", identifiant);
                        }

                    } catch (Exception ex) {
                        if (ex.getLocalizedMessage().trim().toLowerCase().contains("unauthorized")) {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.getWriter().write("Accès à la ressource refusée ou session expiré, veuillez vous identifier !");
                            continueRequest = false;
                        } else {
                            ex.printStackTrace();
                            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                            response.getWriter().write(ex.getMessage());
                            continueRequest = false;
                        }

                    }
                } else {
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.getWriter().write("Vous n'avez pas l'autorisation pour cette API!");
                    continueRequest = false;
                }
            } else {
                continueRequest = false;
                response.setStatus(401);
                response.getWriter().write("Accès à la ressource refusée ou session expiré, veuillez vous identifier !");
            }
            return continueRequest;
        }
    }

}
