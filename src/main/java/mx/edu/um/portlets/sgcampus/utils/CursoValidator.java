package mx.edu.um.portlets.sgcampus.utils;

import mx.edu.um.portlets.sgcampus.model.Curso;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Valida que el curso tenga los campos requeridos
 * 
 * @author jdmr
 */
@Component
public class CursoValidator implements Validator {
    
    private static final Logger log = LoggerFactory.getLogger(CursoValidator.class);

    @Override
    public boolean supports(Class<?> aClass) {
        return Curso.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.debug("Validando curso");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "codigo", "curso.codigo.requerido");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nombre", "curso.nombre.requerido");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "comunidadId", "curso.comunidad.requerida");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "telefono", "curso.telefono.requerido");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "estado", "curso.estado.requerido");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pais", "curso.pais.requerido");
    }

}
