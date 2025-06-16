package infra.adapter.presenter.account.response;

import infra.adapter.presenter.tag.response.TagDto;
import infra.adapter.presenter.zone.response.ZoneDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private Set<TagDto> tags = new HashSet<>();

    @Builder.Default
    private Set<ZoneDto> zones = new HashSet<>();
}
