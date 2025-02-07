
# Tic-Tac-Toe Engine

This project is a laboratory assignment for the Programming Languages course at Wrocław University of Science and Technology. It represents a console-based client-server application implementing a Tic-Tac-Toe game engine. The project leverages hybrid network communication by utilizing both RMI (Remote Method Invocation) and TCP/IP sockets. Maven is used for application building.

## Table of Contents

- [Project Description](#project-description)
- [Functionality](#functionality)
- [Installation and Running the Application](#installation-and-running-the-application)
- [Contact](#contact)

## Project Description

The Tic-Tac-Toe Engine is a console-based client-server application that allows users to play or observe the Tic-Tac-Toe game. The game operates on a client-server architecture, providing seamless communication and management of gameplay sessions.


## Functionality

- [Client Functionalities](#client-functionalities)
- [Rmi Mechanism](#rmi-mechanism)
- [Sockets Mechanism](#sockets-mechanism)
- [Thread Management](#thread-management)

### Client Functionalities
1. **Player:**
- Players can join game rooms, play turns, and interact with the game board.
- They can request:
  - Information about other players in the game.
  - The current state of the game board (map).
  - The token of the player whose turn it is.
  - The result of a move or a winning combination.
- Game logic, such as determining winning combinations, is handled on the server and returned as responses to client requests.

2. **Observer:**

- Can view the list of available game rooms.
- Can connect to a game room to watch the ongoing game between two players.


### Rmi Mechanism
The application uses RMI (Remote Method Invocation) to enable the client to access server-side functionalities, specifically the game logic, through the `PlayerFeaturesInterface`. This mechanism allows clients to perform operations such as retrieving player details, managing game states, and interacting with the game board.

#### How RMI Works in the Application
1. **Remote Object and Interface:**
 The server defines a remote interface, `PlayerFeaturesInterface`, which declares the methods that clients can invoke remotely. This interface acts as the contract between the client and the server, ensuring the client knows what operations are available.

2. **Binding the Remote Object to the RMI Registry**:

- The server creates an instance of the PlayerFeaturesImplementation class and exports it as a remote object using UnicastRemoteObject. Exporting the object makes it available to accept incoming remote method calls:

```java 
public class Player extends UnicastRemoteObject implements PlayerFeaturesInterface {

    private static final HashMap<String, GameRoom> gameRooms= new HashMap<>();
    // UnicastRemoteObject is used, because objects from this class could be transfer to client using RMI server
    public Player () throws RemoteException
    {
        super();
    }
...
}    
```
- Then the server binds this remote object to the RMI registry under a specific name ("Player"). The RMI registry runs on a known port, allowing clients to locate and communicate with the remote object:
```java
public Registry registry;
public static int rmiPort = 9090;

Player player = new Player();
server.registry = LocateRegistry.createRegistry(Server.rmiPort);
server.registry.rebind("Player",player);
```
3. **Client Interaction with RMI:**
- The client uses the LocateRegistry class to connect to the RMI registry on the server's IP address and known port.
- By looking up the remote object name (e.g., Player), the client obtains a stub, which is a proxy for the remote object.
```java
// Getting access to Remote object PlayerFeaturesInterface
Registry registry = LocateRegistry.getRegistry("localhost",9090);
PlayerFeaturesInterface player = (PlayerFeaturesInterface) registry.lookup("Player");
```
- The client invokes methods on the stub, and these calls are forwarded to the server over the network.

4. **Remote Method Invocation:**

When the client calls a method on the stub, the RMI runtime handles the communication between the client and the server:
- Client-Side: The stub serializes the method name, parameters, and metadata, and sends them to the server.
- Server-Side: The RMI runtime on the server deserializes the request and invokes the corresponding method on the remote object (`Player`).
- Response: The method's result is serialized and sent back to the client, where the stub deserializes it and returns it to the client application.

### Sockets Mechanism
Sockets enable real-time communication between the server and clients. The server creates a port and listens for incoming connections from clients. When a client connects, the server assigns a dedicated thread (ClientHandler) to handle communication with that client. This ensures that multiple clients can be handled concurrently without blocking each other.

Communication between the client and server is facilitated using `BufferedReader` and `BufferedWriter`, which work with character streams to efficiently read and write text. These classes use underlying `InputStreamReader` and `OutputStreamWriter` to convert bytes from `InputStream` and `OutputStream` into characters. `BufferedReader` reads characters, lines, or chunks of text, while `BufferedWriter` writes text efficiently, buffering data before it's sent out, reducing the number of actual I/O operations.

- Server: The server listens on a specific port for incoming client connections. Once a connection is made, it creates a new thread to handle communication with the client.
- Client: The client connects to the server via the designated port. After establishing the connection, it can send and receive messages through the input and output streams.
- BufferedReader/BufferedWriter: These classes are used to efficiently handle the reading and writing of data. BufferedReader reads the input stream and converts it to text, while BufferedWriter writes text data to the output stream.




### Thread Management
Each client connection is managed in a separate thread (`ClientHandler`), enabling concurrent interactions with multiple clients. This ensures:

- Independent processing of each client's requests.
- Stability and responsiveness, as one client's operations do not block others.


## Installation and Running the Application

**Step 1: Clone the repository**

Start by cloning the Tic-Tac-Toe Engine repository to your local machine. Open a terminal and execute:

```
git clone https://github.com/makszdanowicz/Tic-Tac-Toe-Engine.git
```

**Step 2: Build the application**

Navigate to the project directory and use Maven to build the application. This will generate two JAR files for the server and client in the target directory.:

```
cd tic-tac-toe-engine
mvn clean package
```

**Step 3: Run the server**

To start the server, navigate to the directory containing the server.jar file and run the following command:

```java -jar server-server.jar```

The server will start listening for connections on the 9091 port.

**Step 4: Run the client**

To start the client, navigate to the directory containing the client.jar file and run:

```java -jar client-client.jar```

The application will ask you to enter nickname and select role.
Enter p to join a game as a player.
Or enter w to observe an existing sessions.



## Contact

If you have any questions, please contact me at:

- email: zdanowiczmm@gmail.com
- LinkedIn: http://www.linkedin.com/in/maksim-zdanovich
