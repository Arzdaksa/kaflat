const net = require('net');
const readline = require('readline');

const socket = net.createConnection(2323, 'localhost');

socket.on('connect', () => {
    console.log('Connected to server at localhost:2323');

    const rl = readline.createInterface({ input: process.stdin });

    rl.on('line', (line) => {
        socket.write(line + '\n');
    });

    rl.on('close', () => {
        socket.end();
    });
});

socket.on('data', (data) => {
    console.log('Server: ' + data.toString().trim());
});

socket.on('close', () => {
    console.log('Connection closed');
    process.exit(0);
});

socket.on('error', (err) => {
    console.error('Connection error: ' + err.message);
    process.exit(1);
});
