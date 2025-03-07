package pl.lodz.coordinationsystem.app.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

    @RequestMapping(value = "/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object servletName = request.getAttribute(RequestDispatcher.ERROR_SERVLET_NAME);
        Object requestUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute("statusCode", statusCode);

            String errorMessage;

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorMessage = "The page you are looking for is not available.";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorMessage = "An unexpected server error occurred.";
            } else {
                errorMessage = "An error occurred. Please try again later.";
            }

            model.addAttribute("errorMessage", errorMessage);

            // Log detailed error information
            logger.error("Error occurred with status code: {}", statusCode);
            if (message != null) {
                logger.error("Error message: {}", message);
            }
            if (exception != null) {
                logger.error("Exception: {}", exception);
            }
            if (servletName != null) {
                logger.error("Servlet name: {}", servletName);
            }
            if (requestUri != null) {
                logger.error("Request URI: {}", requestUri);
            }
        }

        return "error";
    }
}
