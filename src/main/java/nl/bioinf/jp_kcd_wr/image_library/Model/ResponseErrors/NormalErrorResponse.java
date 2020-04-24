package nl.bioinf.jp_kcd_wr.image_library.Model.ResponseErrors;

import org.springframework.http.HttpStatus;

public class NormalErrorResponse {
    private String message;
    private HttpStatus status;

    public NormalErrorResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
