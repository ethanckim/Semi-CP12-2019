#Accord Chat

This program only works when the server port and address is provided. This program has only been tested in a local network environment (Semiahmoo Secondary School)

This program uses the following protocols for communication:

The protocol has 4-letter, capitalized command names, for simplicity.
NOTE: square brackets [] must be filled with context. Angled brackets <> are part of the protocol. 

The program sends...

JOIN: [username]
After opening the socket
SPEK: [message]
After you have been WELComed to the channel, but not yet PARTed or been KICKed
PART: [message]
When you are done and are ready to close the socket.


The program can receive...

WELC: [usernames],[usernames],[usernames]...
You are WELComed to the channel. You are told the other users that are currently active in the channel.
FAIL: [reason]
Your join FAILs and you are rejected from the channel
SPOK: [message]
The server acknowledges your message, and it is SPOKen to the rest of the channel
MESG: <username> [message]
Someone else sent a SPEaK command.
ENTR: [username]
Someone else ENTeRs the channel
LEAV: <username> [message]
Someone else LEAVes the channel (PART, Socket goes dead… IOException, network glitch, etc.)
GBYE: [message]
You have PARTed and the server is saying GoodBYE.
PUNT: <username> [message]
Someone else got KICKed out
KICK: [reason]
You got KICKed out
