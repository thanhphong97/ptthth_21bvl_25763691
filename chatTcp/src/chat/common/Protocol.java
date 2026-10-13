package chat.common;

import java.util.regex.Pattern;

public final class Protocol {

	public static final String DEFAULT_HOST = "localhost";
	public static final int DEFAULT_PORT = 5005;

	public static final String COMMAND_PREFIX = "/";
	public static final String COMMAND_QUIT_ROOM = "/quit";
	public static final String COMMAND_EXIT = "/exit";
	public static final String COMMAND_LIST_ROOMS = "/list";

	public static final Pattern VALID_NAME_PATTERN = Pattern.compile("[A-Za-z0-9_-]{1,20}");

	public static final String ERROR_MARKER = "* ";

	public static final String ASK_NAME_MESSAGE = "Nhập tên của bạn:";
	public static final String GOODBYE_MESSAGE = "Tạm biệt!";
	public static final String ROOM_LIST_HEADER = "Danh sách phòng:";
	public static final String ROOM_LIST_FOOTER = "Chọn phòng (số hoặc tên), /list, /exit:";

	public static final String INVALID_NAME_ERROR = ERROR_MARKER + "Tên không hợp lệ (1-20 ký tự A-Z, 0-9, _ hoặc -)";
	public static final String EMPTY_MESSAGE_ERROR = ERROR_MARKER + "Tin nhắn rỗng";
	public static final String NOT_IN_ROOM_ERROR = ERROR_MARKER + "Bạn chưa ở trong phòng nào";

	private Protocol() {
	}

	public static String welcomeMessage(String clientName) {
		return "Chào " + clientName + "!";
	}

	public static String nameTakenError(String requestedName) {
		return ERROR_MARKER + "Tên \"" + requestedName + "\" đã có người dùng";
	}

	public static String roomNotFoundError(String requestedRoomInput) {
		return ERROR_MARKER + "Không tìm thấy phòng \"" + requestedRoomInput + "\"";
	}

	public static String unknownCommandError(String command) {
		return ERROR_MARKER + "Lệnh không hợp lệ: " + command;
	}

	public static String roomListLine(int roomNumber, String roomName, int memberCount) {
		return "  " + roomNumber + ". " + roomName + " (" + memberCount + " người)";
	}

	public static String joinedRoomMessage(String roomName) {
		return "Bạn đã vào phòng " + roomName;
	}

	public static String leftRoomMessage(String roomName) {
		return "Bạn đã rời phòng " + roomName;
	}

	public static String memberJoinedNotice(String clientName) {
		return clientName + " đã vào phòng";
	}

	public static String memberLeftNotice(String clientName) {
		return clientName + " đã rời phòng";
	}

	public static String chatLine(String roomName, String senderName, String messageText) {
		return "[" + roomName + "] " + senderName + ": " + messageText;
	}
}
