# Extra Lib For Sockets
from Crypto.Cipher import AES
from Crypto.Hash import SHA256
import os
import socket
import time
import random
import threading

class Crypto:
    def __init__(Self):
        Self.BLOCK_SIZE = 16
        Self.FILLER = '~'

    def KeyShuffler(Self, Key):
        print()

    def SHA256_KeyGen(Self, plain_text):
        return SHA256.new(plain_text.encode('utf-8')).digest()

    def AES_Encrypt(Self, msg):
        FixString = lambda text : text + (Self.BLOCK_SIZE - (len(text) % Self.BLOCK_SIZE)) * Self.FILLER
        Key = Self.SHA256_KeyGen(msg)
        Cipher = AES.new(Key, AES.MODE_ECB)
        return Cipher.encrypt(FixString(msg).encode('utf-8')), Key

    def AES_Decrypt(Self, msg, key):
        Decipher = AES.new(key, AES.MODE_ECB)
        Decrypt = Decipher.decrypt(msg).decode('utf-8')
        RemoveFillers = Decrypt.find(Self.FILLER)
        return Decrypt[:RemoveFillers]

class Server:
    def __init__(Self, SERVER):
        Self.Clients = {} # Collect Clients Connected
        Self.Alias = {} # Collect Aliases
        Self.Limits = 2024 # Byte Limit to Send
        Self.Crypto = Crypto()
        Self.SERVER = SERVER
        Self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        Self.s.bind(SERVER)

    def LaunchServer(Self): # ONLY CALL THIS
        Self.s.listen()
        print(f'Server is Listening on {Self.SERVER}')
        while True:
            client, addr = Self.s.accept()
            T = threading.Thread(target=Self.HandleClient, args=(client, addr))
            T.start()

    def HandleClient(Self, client, addr):
        try:
            print(f'{addr} connected to the server')
            time.sleep(0.5)
            Self.SendAtClientEntry(client)
            Self.Clients[client] = addr # Add To Clients
            Connected = True
            while Connected:
                msg = Self.ReceiveMessage(client) # Receive Message
                if msg == '<*Server_Discon*>':
                    Self.Clients.pop(client) # Remove To Clients
                    print(f'{addr} Disconnected')
                    Self.SendBack(addr, msg)
                    Connected = False
                else:
                    print(f'{addr}: {msg}')
                    Self.SendBack(addr, msg)
        except:
            print(f'{addr} [X] Disconnected')
            Self.Clients.pop(client) # Remove To Clients

    def FixNSendMessage(Self, client, msg): # ENCRYPT MESSAGE #
        Data, Key = Self.Crypto.AES_Encrypt(msg)
        # Send Back To All
        client.sendall(Key) # Send Key First
        client.sendall(Data) # Send Data After Key

    def ReceiveMessage(Self, client): # DECRYPT MESSAGE #
        Key = client.recv(32) # Get RAW bytes [Get Key]
        Data = client.recv(Self.Limits) # Get RAW bytes [Get Cipher]
        return Self.Crypto.AES_Decrypt(Data, Key) # Return Deciphered Message

    def SendAtClientEntry(Self, client):
        msgToSend = ['Welcome To The Server,', 'Change your name using $<NewName>$,', 'See How Many Are Connected Using <$Len$>,', 'And Disconnect Using <*Server_Discon*>']
        for msgs in msgToSend:
            Self.FixNSendMessage(client, msgs)
            time.sleep(0.25)
        notes = '<Don\'t use \'[Shift] + [`]\' character>'
        Self.FixNSendMessage(client, notes)

    def SendBack(Self, addr, msg):
        for client in Self.Clients.keys():
            # Check For Change Name
            if '$<NewName>$' in msg:
                ip, port = addr
                nName = msg.replace('$<NewName>$', '')
                Self.Alias[ip] = nName
                msgToSend = f'{ip} transformed to {nName}'
                Self.FixNSendMessage(client, msgToSend)
            # Check For Disconnection Request
            elif '<*Server_Discon*>' in msg:
                msgToSend = f'{addr} Disconnected From The Server'
                Self.FixNSendMessage(client, msgToSend)
            # Check For Length Request
            elif '<$Len$>' in msg:
                msgToSend = f'{addr} There are {len(Self.Clients)} Clients Connected'
                Self.FixNSendMessage(client, msgToSend)
            # Normal Message
            else:
                ip, port = addr
                # Check For Aliases
                if Self.Alias.get(ip) is not None:
                    ip = Self.Alias.get(ip)
                # Finalize Message
                msgToSend = str(ip) + ": " + msg
                Self.FixNSendMessage(client, msgToSend)

class Client:
    def __init__(Self, Host, Port):
        Self.Limits = 2024 # Byte Limit to Send
        Self.Crypto = Crypto()
        Self.Host = Host
        Self.Port = Port
        Self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    def ConnectToServer(Self):
        try:
            Self.s.connect((Self.Host, Self.Port))
            return True
        except:
            print('Connection Failed, Server is Down')
            time.sleep(1.5)
            os._exit(0)

    def SendToServer(Self, Message): # ENCRYPT MESSAGE #
        Message.strip()
        if len(Message) != 0:
            Data, Key = Self.Crypto.AES_Encrypt(Message)
            # Send Back To All
            Self.s.send(Key) # Send Key First
            Self.s.send(Data) # Send Data After Key

    def ClientUpdate(Self): # DECRYPT MESSAGE #
        Key = Self.s.recv(32) # Get RAW bytes [Get Key]
        Data = Self.s.recv(Self.Limits) # Get RAW bytes [Get Data]
        return Self.Crypto.AES_Decrypt(Data, Key) # Return Deciphered Message

if __name__ == '__main__':
    print('Use this as a library or Debug Here')
    x = input()
    if x == 'SERVER':
        ADDR = (socket.gethostbyname(socket.gethostname()), 65432)
        S = Server(ADDR)
        S.LaunchServer()
    elif x == 'CLIENT':
        C = Client('192.168.1.14', 65432)
        Stats = C.ConnectToServer()
        while Stats:
            y = input('Message: ')
            C.SendToServer(y)
            C.ClientUpdate()
    elif x == 'CRYPTO':
        y = input()
        Cr = Crypto()
        Cipher, Key = Cr.AES_Encrypt(y)
        print(Cipher, Key)
        print(Cr.AES_Decrypt(Cipher, Key))
        #os._exit(0)


