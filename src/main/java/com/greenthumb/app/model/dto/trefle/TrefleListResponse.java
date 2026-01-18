package com.greenthumb.app.model.dto.trefle;

import lombok.Data;
import java.util.List;

@Data
public class TrefleListResponse {
    private List<TreflePlantDto> data;
}
