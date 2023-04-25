/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package annotations;

import play.mvc.With;

import java.lang.annotation.*;

/**
 * @author D. Esposito
 * @brief Catch an possible error of a web requests, shows a message of error, and finally logs the error
 */
@With(bErrorCatcherAction.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Documented
public @interface bErrorCatcher {

}
