package pl.zajonz.coding.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorMessage {

    // TODO: 06.03.2023 message niech zawiera komunikat błędu, a zamiast details niech będzie LocalDateTime.now();

    private String message;
    private LocalDateTime dateTime;

    public ErrorMessage(String message) {
        this.message = message;
        this.dateTime = LocalDateTime.now();
    }
}
