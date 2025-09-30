import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

export class WebSocketService {
    constructor() {
        this.stompClient = null;
        this.connected = false;
        this.subscriptions = new Map();
    }

    connect() {
        return new Promise((resolve, reject) => {
            try {
                const socket = new SockJS('/ws');
                this.stompClient = Stomp.over(socket);

                this.stompClient.debug = null;

                this.stompClient.connect({}, (frame) => {
                    console.log('WebSocket connected:', frame);
                    this.connected = true;
                    resolve();
                }, (error) => {
                    console.error('WebSocket connection error:', error);
                    this.connected = false;
                    reject(error);
                });
            } catch (error) {
                console.error('Failed to initialize WebSocket:', error);
                reject(error);
            }
        });
    }

    disconnect() {
        if (this.stompClient && this.connected) {
            this.stompClient.disconnect(() => {
                console.log('WebSocket disconnected');
                this.connected = false;
            });
        }
    }

    subscribe(destination, callback) {
        if (!this.connected || !this.stompClient) {
            console.error('WebSocket not connected');
            return null;
        }

        const subscription = this.stompClient.subscribe(destination, (message) => {
            try {
                const data = JSON.parse(message.body);
                callback(data);
            } catch (error) {
                console.error('Error parsing WebSocket message:', error);
            }
        });

        this.subscriptions.set(destination, subscription);
        return subscription;
    }

    unsubscribe(destination) {
        const subscription = this.subscriptions.get(destination);
        if (subscription) {
            subscription.unsubscribe();
            this.subscriptions.delete(destination);
        }
    }

    send(destination, message) {
        if (!this.connected || !this.stompClient) {
            console.error('WebSocket not connected');
            return;
        }

        this.stompClient.send(destination, {}, JSON.stringify(message));
    }

    isConnected() {
        return this.connected;
    }
}

export const websocketService = new WebSocketService();