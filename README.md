# decentralized-nosql-cluster
A Custom Load-Balanced Decentralized NoSQL Cluster using Vanilla Sockets.

Date: March 2023

## Sample Screenshot of Usecase within Cluster
![image](https://github.com/user-attachments/assets/ed95e981-044d-4233-9d7e-d85ae7be9cac)
![image](https://github.com/user-attachments/assets/dc207ef7-3fb1-4136-8908-0eb611cf0547)


## Overview

The project consists of three primary components:
- **Bootstrap Server (Bootstrap)**
- **NoSQL Server (Server)**
- **Logging Application (User)**

The most critical part of the project is the Logging Application/User, despite its relatively lower complexity compared to the other components. This is because the databases are designed to serve users, and in this project, the Logging Application is the user.

## The Logging Application

The Logging Application is a straightforward website for log dumping, management, and retrieval, built using Spring Boot for efficient request and session management.

### Key Features
- Users receive crucial information (e.g., connection details, credentials) upon logging in or registering.
- User actions trigger requests to NoSQL custom servers, which are organized in a decentralized cluster.

## NoSQL Custom Servers

These servers manage database data structures, access, and manipulation. They operate in a decentralized cluster formation, where any action on one server is broadcasted to others, maintaining uniformity and load distribution.

## Bootstrap Server

The Bootstrap Server is responsible for forming the cluster and managing connection details. It provides credentials and load-balanced server details to users, supporting both new and returning sessions.

# Functionality Showcase

## Logging Application Interface

1. **Login Page**
   - Users authenticate or request credentials.

2. **Authentication**
   - Successful authentication redirects to the “logs” database. Incorrect attempts lead to connection termination after three failed tries.

3. **Registration**
   - New users register via the `/register` page, which interacts with the Bootstrap Node.

4. **Dashboard**
   - Once logged in, users access a dashboard for database manipulation, with logs automatically created for each user.

5. **Operation Feedback**
   - Actions such as inserting, updating, or deleting log messages result in a unified success page.

6. **Log Viewing**
   - Users can view all or specific logs using Thymeleaf for XML namespace handling.

## Database File System

1. **Directory Structure**
   - Contains directories for database nodes and bootstrap files, each independent of the others.

2. **Server Directories**
   - Each server directory includes database indexes and metadata, with synchronization across nodes.

3. **Collection and Document Storage**
   - Collections and documents are indexed, with schemas providing property details. Data is managed and updated automatically.

# Code Summary

## Logging Application

### Code Structure
- **Network**: Manages client architecture, including pooling, security, and response framework.
- **Web**: Contains Spring controllers for web access, authorization, and log manipulation.
- **Database Client**: Handles database instance creation and interaction.
- **External Services / Bootstrap**: Connects to the Bootstrap server for user credentials and server information.
- **Resources**: Contains frontend files and Thymeleaf integration.

### Web Section
- **Controllers**: Handle access, authorization, and log requests.
- **Session Management**: Includes session handling, cleaning, and caching.

### Database Section
- **LogInfoDAO**: Manages data retrieval, updates, and insertion.
- **Request Handling**: Uses threads for data requests and responses.

### Network Section
- **Client Implementation**: Includes database and bootstrap clients, managing connections and responses.

## NoSQL Database Code

### Structure
- **Database**: Manages database objects, collections, and documents.
- **Network**: Facilitates communication between NoSQL nodes, user applications, and the bootstrap server.
- **Services**: Connects to the Bootstrap server for nodal information.

### Database Components
- **Database Objects**: Managed by Database Manager, including collections and documents.
- **Collections and Documents**: Managed by Collections Manager with schemas and indexed properties.

### Network Architectures
- **Client**: Includes database and bootstrap client implementations.
- **Server**: Handles user authorization, database operations, and API responses.
- **Cluster Session**: Manages NoSQL cluster information and load balancing.

## Bootstrap Server

### Network Section
- **Node Client**: Sends new user broadcasts.
- **Bootstrap Server**: Responds to credential requests and node setup requests.
- **Cluster**: Manages user load balancing and caching.

# Focused Segments

## Data Structures
- **Maps**: Used for indexing with O(1) access times.
- **Concurrent HashMap**: Used for thread-safe operations.
- **HashSet**: Used for fast object availability checks.
- **ArrayLists**: Used for iterative operations.
- **LinkedList Queues**: Used in threaded services.

## Server-Client Methodology
- **Server to Client**: Involves socket handling, polling, and security.
- **Client to Server**: Includes socket management, polling, and response handling.

## Database Implementation
- **NoSQL System**: Managed by a System Manager with indexed databases, collections, and documents.
- **Observable Classes**: Update the file system and cluster via broadcasting.

## Multi-threading, Locks, & Volatility
- **Optimistic Locking**: Implemented in Collections Manager.
- **Synchronization**: Applied to frequently accessed methods.
- **Volatile and Atomic Variables**: Used for consistency and uniqueness.

## Design Patterns
- **Singleton**: Applied to various components.
- **Factory**: Used for generating responses.
- **Builder**: Used for packet creation and console messages.
- **Updater**: Used for automatic updates.
- **Adapter**: Used for wrapping property values.

## Security Issues
- **AES Encryption**: Salt-less and password protection.
- **Clear JSON Files**: Stored without encryption.
- **TLS**: Recommended for improved security.

## Communication Protocols
- **TCP Protocol**: Uses a 3-way handshake with a ping test.

## Load Balancing
- **NoSQL Servers**: Balances document assignment using a formula.
- **Bootstrap**: Balances server assignment using an iterative approach.

## Data Consistency Issues
- **Synchronization**: Ensured with fail safes but lacked complete implementation.

## Clean Code Defense & S.O.L.I.D Principles
- **Naming Conventions**: Followed Java standards with meaningful names.
- **Encapsulation**: Applied using Lombok.
- **Single Responsibility Principle**: Maintained cohesion in classes and methods.
- **Interface Segregation Principle**: Not applicable.
- **Open Closed Principle**: Created extendable abstractions.
- **Dependency Inversion Principle**: Depended on abstractions.
- **Favor Composition over Inheritance**: Applied in cluster management.
- **Thread Management**: Used cached and daemon threads.


