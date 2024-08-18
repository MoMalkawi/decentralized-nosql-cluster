package malkawi.logging.database.entities;

import lombok.*;

import java.time.LocalDate;

@RequiredArgsConstructor @Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LogInfo {

    private @Setter int id = -1;

    private @NonNull String message;

    private @NonNull LocalDate date;

}