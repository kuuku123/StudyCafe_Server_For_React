package infra.adapter.presenter.zone.response;

public class ZoneDto {
    private Long id;
    private String city;
    private String localNameOfCity;
    private String province;

    public ZoneDto(Long id, String city, String localNameOfCity, String province) {
        this.id = id;
        this.city = city;
        this.localNameOfCity = localNameOfCity;
        this.province = province;
    }
}
