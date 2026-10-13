package chat.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import chat.common.Protocol;

public class RoomManager {

	private static final List<String> DEFAULT_ROOM_NAMES = List.of(
			"Lap-Trinh-Java", "Mang-May-Tinh", "Game-Thu", "Am-Nhac", "Tan-Gau");

	private final List<Room> availableRooms;
	private final Map<String, ClientHandler> registeredClientsByName = new ConcurrentHashMap<>();

	public RoomManager() {
		availableRooms = DEFAULT_ROOM_NAMES.stream().map(Room::new).toList();
	}

	public boolean registerName(String clientName, ClientHandler clientHandler) {
		return registeredClientsByName.putIfAbsent(toNameKey(clientName), clientHandler) == null;
	}

	public void unregisterName(String clientName, ClientHandler clientHandler) {
		registeredClientsByName.remove(toNameKey(clientName), clientHandler);
	}

	public Optional<Room> findRoom(String requestedRoomInput) {
		String trimmedRoomInput = requestedRoomInput.trim();
		if (trimmedRoomInput.isEmpty()) {
			return Optional.empty();
		}
		Optional<Room> roomByNumber = findRoomByNumber(trimmedRoomInput);
		if (roomByNumber.isPresent()) {
			return roomByNumber;
		}
		return availableRooms.stream()
				.filter(candidateRoom -> candidateRoom.getRoomName().equalsIgnoreCase(trimmedRoomInput))
				.findFirst();
	}

	public List<String> buildRoomListLines() {
		List<String> roomListLines = new ArrayList<>();
		roomListLines.add(Protocol.ROOM_LIST_HEADER);
		for (int roomIndex = 0; roomIndex < availableRooms.size(); roomIndex++) {
			Room listedRoom = availableRooms.get(roomIndex);
			roomListLines.add(Protocol.roomListLine(roomIndex + 1, listedRoom.getRoomName(), listedRoom.memberCount()));
		}
		roomListLines.add(Protocol.ROOM_LIST_FOOTER);
		return roomListLines;
	}

	private Optional<Room> findRoomByNumber(String trimmedRoomInput) {
		if (!trimmedRoomInput.chars().allMatch(Character::isDigit) || trimmedRoomInput.length() > 9) {
			return Optional.empty();
		}
		int roomNumber = Integer.parseInt(trimmedRoomInput);
		if (roomNumber < 1 || roomNumber > availableRooms.size()) {
			return Optional.empty();
		}
		return Optional.of(availableRooms.get(roomNumber - 1));
	}

	private static String toNameKey(String clientName) {
		return clientName.toLowerCase(Locale.ROOT);
	}
}
