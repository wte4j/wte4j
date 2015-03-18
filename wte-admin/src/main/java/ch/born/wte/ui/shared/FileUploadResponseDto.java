package ch.born.wte.ui.shared;


public class FileUploadResponseDto implements FileUploadResponse {

	private boolean done;
	private String message;

	public FileUploadResponseDto() {

	}

	public FileUploadResponseDto(boolean done, String message) {
		super();
		this.done = done;
		this.message = message;
	}

	public boolean getDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String toJson() {
		return "{\"done\":"+done+",\"message\":\""+message+"\"}";
	}
}
