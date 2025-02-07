package isol.tcat_api.global.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonPropertyOrder({"code", "message", "data"})
public class CommonResponse<T> {
    private int code;
    private String message;
    private T data;
}
