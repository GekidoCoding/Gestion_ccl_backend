package mg.cnaps.gestion.ccl.project.exception.gestion;

import mg.cnaps.gestion.ccl.framework.core.exception.GestionGlobaleException;
import mg.cnaps.gestion.ccl.framework.error.ErrorResponse;
import mg.cnaps.gestion.ccl.project.exception.EtatNotFoundException;
import mg.cnaps.gestion.ccl.project.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GestionError extends GestionGlobaleException {
    @ExceptionHandler(EtatNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEtatNotFoundException( EtatNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException( UnauthorizedException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }
}
