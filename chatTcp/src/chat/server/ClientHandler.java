package chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import chat.common.Protocol;

public class ClientHandler implements Runnable {

	private enum SessionState {
		NHAP_TEN, SANH_CHO, TRONG_PHONG
	}

	private final Socket clientSocket;
	private final RoomManager roomManager;
	private final String remoteAddress;

	private volatile PrintWriter clientWriter;
	private SessionState sessionState = SessionState.NHAP_TEN;
	private String clientName;
	private Room currentRoom;

	public ClientHandler(Socket clientSocket, RoomManager roomManager) {
		this.clientSocket = clientSocket;
		this.roomManager = roomManager;
		this.remoteAddress = String.valueOf(clientSocket.getRemoteSocketAddress());
	}

	@Override
	public void run() {
		try (clientSocket;
				BufferedReader clientReader = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
				PrintWriter socketWriter = new PrintWriter(
						new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true)) {
			clientWriter = socketWriter;
			System.out.println("Connected: " + remoteAddress);
			send(Protocol.ASK_NAME_MESSAGE);
			String receivedLine;
			while ((receivedLine = clientReader.readLine()) != null) {
				String trimmedInput = receivedLine.strip();
				if (isCommand(trimmedInput) && firstWordOf(trimmedInput).equalsIgnoreCase(Protocol.COMMAND_EXIT)) {
					releaseNameAndLeaveRoom();
					send(Protocol.GOODBYE_MESSAGE);
					return;
				}
				switch (sessionState) {
				case NHAP_TEN -> handleNameInput(trimmedInput);
				case SANH_CHO -> handleLobbyInput(trimmedInput);
				case TRONG_PHONG -> handleRoomInput(trimmedInput);
				}
			}
		} catch (IOException exception) {
			System.err.println("Lỗi phiên client " + remoteAddress + ": " + exception.getMessage());
		} finally {
			releaseNameAndLeaveRoom();
			System.out.println("Disconnected: " + remoteAddress
					+ (clientName != null ? " (" + clientName + ")" : ""));
		}
	}

	public void send(String message) {
		PrintWriter currentWriter = clientWriter;
		if (currentWriter != null) {
			currentWriter.println(message);
		}
	}

	private void handleNameInput(String requestedName) {
		if (!Protocol.VALID_NAME_PATTERN.matcher(requestedName).matches()) {
			send(Protocol.INVALID_NAME_ERROR);
			return;
		}
		if (!roomManager.registerName(requestedName, this)) {
			send(Protocol.nameTakenError(requestedName));
			return;
		}
		clientName = requestedName;
		sessionState = SessionState.SANH_CHO;
		send(Protocol.welcomeMessage(clientName));
		sendRoomList();
	}

	private void handleLobbyInput(String lobbyInput) {
		if (isCommand(lobbyInput)) {
			String command = firstWordOf(lobbyInput);
			if (command.equalsIgnoreCase(Protocol.COMMAND_LIST_ROOMS)) {
				sendRoomList();
			} else if (command.equalsIgnoreCase(Protocol.COMMAND_QUIT_ROOM)) {
				send(Protocol.NOT_IN_ROOM_ERROR);
			} else {
				send(Protocol.unknownCommandError(command));
			}
			return;
		}
		Optional<Room> requestedRoom = roomManager.findRoom(lobbyInput);
		if (requestedRoom.isEmpty()) {
			send(Protocol.roomNotFoundError(lobbyInput));
			return;
		}
		joinRoom(requestedRoom.get());
	}

	private void handleRoomInput(String roomInput) {
		if (isCommand(roomInput)) {
			String command = firstWordOf(roomInput);
			if (command.equalsIgnoreCase(Protocol.COMMAND_QUIT_ROOM)) {
				String leftRoomName = currentRoom.getRoomName();
				leaveCurrentRoom();
				send(Protocol.leftRoomMessage(leftRoomName));
				sendRoomList();
			} else {
				send(Protocol.unknownCommandError(command));
			}
			return;
		}
		if (roomInput.isEmpty()) {
			send(Protocol.EMPTY_MESSAGE_ERROR);
			return;
		}
		currentRoom.broadcastToOthers(Protocol.chatLine(currentRoom.getRoomName(), clientName, roomInput), this);
	}

	private void joinRoom(Room targetRoom) {
		currentRoom = targetRoom;
		sessionState = SessionState.TRONG_PHONG;
		send(Protocol.joinedRoomMessage(targetRoom.getRoomName()));
		targetRoom.join(this);
		targetRoom.broadcastToOthers(Protocol.memberJoinedNotice(clientName), this);
	}

	private void releaseNameAndLeaveRoom() {
		if (clientName != null) {
			roomManager.unregisterName(clientName, this);
		}
		leaveCurrentRoom();
	}

	private void leaveCurrentRoom() {
		if (currentRoom == null) {
			return;
		}
		Room roomBeingLeft = currentRoom;
		currentRoom = null;
		sessionState = SessionState.SANH_CHO;
		roomBeingLeft.leave(this);
		roomBeingLeft.broadcastToOthers(Protocol.memberLeftNotice(clientName), this);
	}

	private void sendRoomList() {
		for (String roomListLine : roomManager.buildRoomListLines()) {
			send(roomListLine);
		}
	}

	private static boolean isCommand(String userInput) {
		return userInput.startsWith(Protocol.COMMAND_PREFIX);
	}

	private static String firstWordOf(String userInput) {
		return userInput.split("\\s+", 2)[0];
	}
}
