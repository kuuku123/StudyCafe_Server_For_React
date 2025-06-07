package infra.adapter.presenter.account.response;

import infra.adapter.presenter.tag.response.TagDto;
import infra.adapter.presenter.zone.response.ZoneDto;
import lombok.Data;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class AccountDto {
    private String nickname;
    private String bio;
    private String url;
    private String occupation;
    private String location;
    private String email;
    private boolean emailVerified;
    private String profileImage;

    /**
     * Use @Builder.Default so that, if the builder doesn’t set these explicitly,
     * they default to empty lists (instead of null).
     */
    @Builder.Default
    private List<TagDto> tags = new ArrayList<>();

    @Builder.Default
    private List<ZoneDto> zones = new ArrayList<>();
}
