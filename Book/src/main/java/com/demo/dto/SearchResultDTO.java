import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchResultDTO {
    private String version;
    private String title;
    private String link;
    private String pubDate;
    private String imageUrl;
    private int totalResults;
    private int startIndex;
    private int itemsPerPage;
    private String query;
    private int searchCategoryId;
    private String searchCategoryName;

    @JsonProperty("item") // JSON의 'item' 필드를 매핑
    private List<ItemDTO> items; // DTO에서 'items' 필드로 매핑
}

@Getter
@Setter
class ItemDTO {
    private String title;
    private String author;
    private String description;
    private String cover;
}
