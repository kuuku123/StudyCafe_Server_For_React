package infra.adapter.presenter.tag.response;


public class TagDto {
    private Long id;
    private String title;

    public TagDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
