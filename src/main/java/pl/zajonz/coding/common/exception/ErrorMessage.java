package pl.zajonz.coding.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorMessage {

    // TODO: 06.03.2023 message niech zawiera komunikat błędu, a zamiast details niech będzie LocalDateTime.now();
    private LocalDateTime timestamp;
    private String message;

    public ErrorMessage(String message) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
    }
}
