package chat.server;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Room {

	private final String roomName;
	private final Set<ClientHandler> roomMembers = ConcurrentHashMap.newKeySet();

	public Room(String roomName) {
		this.roomName = roomName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void join(ClientHandler memberHandler) {
		roomMembers.add(memberHandler);
	}

	public void leave(ClientHandler memberHandler) {
		roomMembers.remove(memberHandler);
	}

	public int memberCount() {
		return roomMembers.size();
	}

	public void broadcastToOthers(String message, ClientHandler senderHandler) {
		for (ClientHandler memberHandler : roomMembers) {
			if (memberHandler != senderHandler) {
				memberHandler.send(message);
			}
		}
	}
}
