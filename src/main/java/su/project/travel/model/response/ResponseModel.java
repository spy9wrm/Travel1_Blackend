package su.project.travel.model.response;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseModel<T> {
    private int code;
    private String message;
    private T data;
}
