package team.guest.tgbotty.dto;

public class RequestDto {

    private Long requestId;
    private String requestNumber;
    private String text;

    public RequestDto(Long requestId, String requestNumber, String text) {
        this.requestId = requestId;
        this.requestNumber = requestNumber;
        this.text = text;
    }

    public RequestDto() {
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(String requestNumber) {
        this.requestNumber = requestNumber;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
